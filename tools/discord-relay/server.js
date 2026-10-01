'use strict';

const http = require('http');
const https = require('https');

const port = Number.parseInt(process.env.PORT || '3000', 10);
const discordWebhookUrl = (process.env.DISCORD_WEBHOOK_URL || '').trim();
const relayKeys = new Set(
  (process.env.SOLOSCAPE_RELAY_KEYS || '')
    .split(',')
    .map((value) => value.trim())
    .filter(Boolean)
);

const maxBodyBytes = 16 * 1024;
const rateLimitWindowMs = 60 * 1000;
const rateLimitEventsPerWindow = 30;
const keyUsage = new Map();

if (!discordWebhookUrl) {
  console.error('Missing DISCORD_WEBHOOK_URL.');
  process.exit(1);
}
if (relayKeys.size === 0) {
  console.error('Missing SOLOSCAPE_RELAY_KEYS.');
  process.exit(1);
}

function sendJson(response, statusCode, body) {
  const payload = JSON.stringify(body);
  response.writeHead(statusCode, {
    'Content-Type': 'application/json; charset=utf-8',
    'Content-Length': Buffer.byteLength(payload),
    'Cache-Control': 'no-store'
  });
  response.end(payload);
}

function readBody(request) {
  return new Promise((resolve, reject) => {
    let size = 0;
    const chunks = [];

    request.on('data', (chunk) => {
      size += chunk.length;
      if (size > maxBodyBytes) {
        reject(new Error('body_too_large'));
        request.destroy();
        return;
      }
      chunks.push(chunk);
    });

    request.on('end', () => resolve(Buffer.concat(chunks).toString('utf8')));
    request.on('error', reject);
  });
}

function getBearerKey(request) {
  const value = request.headers.authorization || '';
  const match = /^Bearer\s+(.+)$/i.exec(value);
  return match ? match[1].trim() : '';
}

function allowRequest(key) {
  const now = Date.now();
  const current = keyUsage.get(key);
  if (!current || now - current.startedAt >= rateLimitWindowMs) {
    keyUsage.set(key, { startedAt: now, count: 1 });
    return true;
  }
  if (current.count >= rateLimitEventsPerWindow) {
    return false;
  }
  current.count += 1;
  return true;
}

function cleanText(value, maxLength) {
  if (typeof value !== 'string') {
    return '';
  }
  return value.replace(/[\r\n\t]+/g, ' ').trim().slice(0, maxLength);
}

function escapeDiscord(value) {
  return value.replace(/([\\`*_{}\[\]()<>#+\-.!|>~])/g, '\\$1');
}

function formatMessage(event) {
  const player = escapeDiscord(cleanText(event.player, 32));
  const displayName = escapeDiscord(cleanText(event.server, 64));
  if (!player) {
    return null;
  }

  const actor = displayName || player;
  const account = displayName ? ` on **${player}**` : '';

  if (event.type === 'level_up') {
    const skill = escapeDiscord(cleanText(event.skill, 32));
    const level = Number(event.level);
    if (!skill || !Number.isInteger(level) || level < 1 || level > 255) {
      return null;
    }
    return `🎉 **${actor}** reached **${level} ${skill}**${account}!`;
  }

  if (event.type === 'boss_kill') {
    const boss = escapeDiscord(cleanText(event.boss, 64));
    const npcId = Number(event.npcId);
    if (!boss || !Number.isInteger(npcId) || npcId < 0) {
      return null;
    }
    return `☠️ **${actor}** defeated **${boss}**${account}!`;
  }

  return null;
}

function postToDiscord(content) {
  return new Promise((resolve, reject) => {
    let target;
    try {
      target = new URL(discordWebhookUrl);
    } catch (error) {
      reject(new Error('invalid_discord_webhook_url'));
      return;
    }

    if (target.protocol !== 'https:') {
      reject(new Error('discord_webhook_must_use_https'));
      return;
    }

    const payload = JSON.stringify({
      content,
      allowed_mentions: { parse: [] }
    });

    const request = https.request({
      protocol: target.protocol,
      hostname: target.hostname,
      port: target.port || 443,
      path: `${target.pathname}${target.search}`,
      method: 'POST',
      headers: {
        'Content-Type': 'application/json; charset=utf-8',
        'Content-Length': Buffer.byteLength(payload),
        'User-Agent': 'SoloScape-Discord-Relay/1.0'
      },
      timeout: 5000
    }, (response) => {
      response.resume();
      response.on('end', () => {
        if (response.statusCode >= 200 && response.statusCode < 300) {
          resolve();
        } else {
          reject(new Error(`discord_http_${response.statusCode}`));
        }
      });
    });

    request.on('timeout', () => request.destroy(new Error('discord_timeout')));
    request.on('error', reject);
    request.end(payload);
  });
}

const server = http.createServer(async (request, response) => {
  if (request.method === 'GET' && request.url === '/health') {
    sendJson(response, 200, { ok: true });
    return;
  }

  if (request.method !== 'POST' || request.url !== '/event') {
    sendJson(response, 404, { error: 'not_found' });
    return;
  }

  const key = getBearerKey(request);
  if (!key || !relayKeys.has(key)) {
    sendJson(response, 401, { error: 'unauthorized' });
    return;
  }
  if (!allowRequest(key)) {
    sendJson(response, 429, { error: 'rate_limited' });
    return;
  }

  let rawBody;
  try {
    rawBody = await readBody(request);
  } catch (error) {
    sendJson(response, error.message === 'body_too_large' ? 413 : 400, { error: 'invalid_body' });
    return;
  }

  let event;
  try {
    event = JSON.parse(rawBody);
  } catch (error) {
    sendJson(response, 400, { error: 'invalid_json' });
    return;
  }

  const content = formatMessage(event);
  if (!content) {
    sendJson(response, 400, { error: 'invalid_event' });
    return;
  }

  try {
    await postToDiscord(content);
    sendJson(response, 202, { ok: true });
  } catch (error) {
    console.error('Discord webhook failed:', error.message);
    sendJson(response, 502, { error: 'discord_unavailable' });
  }
});

server.listen(port, '0.0.0.0', () => {
  console.log(`SoloScape Discord relay listening on port ${port}`);
});

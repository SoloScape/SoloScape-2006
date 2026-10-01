'use strict';

function cleanText(value, maxLength) {
  if (typeof value !== 'string') {
    return '';
  }
  return value.replace(/[\r\n\t]+/g, ' ').trim().slice(0, maxLength);
}

function escapeDiscord(value) {
  return value.replace(/([\\`*_{}\[\]()<>#+\-.!|>~])/g, '\\$1');
}

function createFormatter(rawEmojiMap = '') {
  const config = rawEmojiMap.trim() ? JSON.parse(rawEmojiMap) : {};
  if (!config || typeof config !== 'object' || Array.isArray(config)) {
    throw new Error('Emoji mapping must be a JSON object.');
  }
  const maps = {};
  for (const category of Object.keys(config)) {
    if (category !== 'skills' && category !== 'bosses') {
      throw new Error(`Unknown emoji category: ${category}`);
    }
    const entries = config[category];
    if (!entries || typeof entries !== 'object' || Array.isArray(entries)) {
      throw new Error(`${category} must be a JSON object.`);
    }
    maps[category] = new Map();
    for (const [name, emoji] of Object.entries(entries)) {
      if (!name.trim() || typeof emoji !== 'string'
          || !/^<a?:[A-Za-z0-9_]{2,32}:[0-9]{17,20}>$/.test(emoji)) {
        throw new Error(`Invalid custom emoji mapping in ${category} for ${name}.`);
      }
      maps[category].set(name.trim().toLowerCase(), emoji);
    }
  }

  function icon(category, keys, fallback) {
    const map = maps[category];
    if (map) {
      for (const key of keys) {
        const emoji = map.get(String(key).trim().toLowerCase());
        if (emoji) return emoji;
      }
    }
    return fallback;
  }

  return function formatMessage(event) {
    if (!event || typeof event !== 'object' || Array.isArray(event)) return null;
    const player = escapeDiscord(cleanText(event.player, 32));
    const displayName = escapeDiscord(cleanText(event.server, 64));
    if (!player) return null;

    const actor = displayName || player;
    const account = displayName ? ` on **${player}**` : '';

    if (event.type === 'level_up') {
      const skillName = cleanText(event.skill, 32);
      const skill = escapeDiscord(skillName);
      const level = Number(event.level);
      if (!skill || !Number.isInteger(level) || level < 1 || level > 255) return null;
      return `${icon('skills', [skillName], '🎉')} **${actor}** reached **${level} ${skill}**${account}!`;
    }

    if (event.type === 'boss_kill') {
      const bossName = cleanText(event.boss, 64);
      const boss = escapeDiscord(bossName);
      const npcId = Number(event.npcId);
      if (!boss || !Number.isInteger(npcId) || npcId < 0) return null;
      return `${icon('bosses', [npcId, bossName], '☠️')} **${actor}** defeated **${boss}**${account}!`;
    }

    return null;
  };
}

module.exports = { createFormatter };

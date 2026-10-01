# SoloScape Discord relay

This tiny Node service keeps the shared Discord webhook secret while allowing friends to run SoloScape entirely on localhost.

## How it works

Each local SoloScape server sends a small HTTPS event to this relay. The relay validates that server's private key, rate-limits it, formats the message, and then posts to the shared Discord webhook.

The real Discord webhook URL exists only on the relay host. Do not put it in the game server, a client, or GitHub.

## Relay environment variables

- `DISCORD_WEBHOOK_URL` - the real Discord channel webhook URL.
- `SOLOSCAPE_RELAY_KEYS` - comma-separated private keys. Give each friend a different random key so an individual key can be revoked later.
- `PORT` - optional HTTP port; defaults to `3000`.

Example:

```text
DISCORD_WEBHOOK_URL=https://discord.com/api/webhooks/...
SOLOSCAPE_RELAY_KEYS=callum-very-long-random-key,friend2-another-random-key
PORT=3000
```

Run it with Node 18+:

```bash
cd tools/discord-relay
npm start
```

`GET /health` returns `{ "ok": true }` when the relay is running.

For internet use, put the relay behind HTTPS (for example a normal HTTPS reverse proxy or a hosting service that supplies HTTPS). Do not expose it as plain HTTP over the public internet.

## Each friend's SoloScape environment variables

Each friend's local server needs:

```text
SOLOSCAPE_DISCORD_RELAY_URL=https://your-relay.example/event
SOLOSCAPE_DISCORD_RELAY_KEY=their-own-private-key
SOLOSCAPE_DISCORD_SERVER_NAME=Callum's SoloScape
```

Optional boss configuration:

```text
SOLOSCAPE_DISCORD_BOSS_IDS=50,1158,1160,2745,3200
SOLOSCAPE_DISCORD_BOSS_NAMES=Custom Boss,Another Boss
```

`SOLOSCAPE_DISCORD_BOSS_IDS` and `SOLOSCAPE_DISCORD_BOSS_NAMES` extend the built-in common boss-name list. They are useful for custom bosses or caches where IDs/names differ.

If either `SOLOSCAPE_DISCORD_RELAY_URL` or `SOLOSCAPE_DISCORD_RELAY_KEY` is missing, Discord event sending is simply disabled.

## Events

Initial event types are:

- Level ups, including levels gained from quest XP.
- Boss kills for configured/common bosses.

The game sends the HTTP request from a daemon worker thread, so a slow or unavailable Discord/relay connection does not block the game tick.

## Security notes

- Never commit the Discord webhook URL or friend relay keys.
- Use a different relay key for each friend.
- If one key is leaked, remove only that key from `SOLOSCAPE_RELAY_KEYS` and issue that friend a new one.
- The relay disables Discord mentions in webhook messages, so a player/boss name cannot trigger `@everyone` or role mentions.
- The relay rate-limits each key to 30 events per minute in memory.

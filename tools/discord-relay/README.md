# SoloScape Discord relay

This tiny Node service keeps the shared Discord webhook secret while allowing friends to run SoloScape entirely on localhost.

## How it works

Each local SoloScape server sends a small HTTPS event to this relay. The relay validates that server's private key, rate-limits it, formats the message, and then posts to the shared Discord webhook.

The real Discord webhook URL exists only on the relay host. Do not put it in the game server, a client, or GitHub.

## Relay environment variables

These are configured only on the relay host (for example Render):

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

## Each friend's SoloScape config file

The easiest setup for each friend is a local properties file. `Server/Run.bat` creates `Server/discord-relay.properties` from the included example on first run if the file does not already exist.

Edit `Server/discord-relay.properties`:

```properties
relay.url=https://your-relay.example/event
relay.key=their-own-private-key
server.name=Callum

# Optional: extend the built-in boss list.
boss.ids=50,1158,1160,2745,3200
boss.names=Custom Boss,Another Boss
```

Only `relay.url`, `relay.key`, and `server.name` are normally needed. `server.name` is the person's Discord/display name. The in-game username is read automatically from the logged-in player, so a level-up appears like `Callum reached 2 Woodcutting on Tester!` when the Discord name is Callum and the in-game username is Tester. `boss.ids` and `boss.names` extend the built-in common boss-name list and are useful for custom bosses or caches where IDs/names differ.

The real `Server/discord-relay.properties` file is ignored by Git so a friend's private relay key is not accidentally committed. `Server/discord-relay.properties.example` is the safe template that stays in the repository.

If either `relay.url` or `relay.key` is missing, Discord event sending is disabled.

### Optional environment-variable overrides

Environment variables are still supported and take priority over matching values in the properties file:

```text
SOLOSCAPE_DISCORD_RELAY_URL=https://your-relay.example/event
SOLOSCAPE_DISCORD_RELAY_KEY=their-own-private-key
SOLOSCAPE_DISCORD_SERVER_NAME=Callum
SOLOSCAPE_DISCORD_BOSS_IDS=50,1158,1160,2745,3200
SOLOSCAPE_DISCORD_BOSS_NAMES=Custom Boss,Another Boss
```

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
- A friend who controls their own local server and relay key can forge events using that key. The relay protects the Discord webhook and supports revocation/rate limits, but it is not an anti-cheat authority.

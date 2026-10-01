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
- `SOLOSCAPE_DISCORD_EMOJIS` - optional JSON mapping of skill names and boss names/NPC IDs to custom Discord emoji codes (see below).

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

The easiest setup for each friend is a local properties file. `Server/Start-Server.bat` creates `Server/discord-relay.properties` from the included example on first run if the file does not already exist.

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

### Inline game icons

Upload PNGs of the game's skill sprites and boss images as custom emojis in the Discord server receiving the webhook. In Discord, send an emoji with a backslash before it (for example, `\:woodcutting:`) to obtain its full code, such as `<:woodcutting:123456789012345678>`.

Set `SOLOSCAPE_DISCORD_EMOJIS` on the relay host to a JSON object like this, replacing the example IDs with your actual emoji IDs:

```json
{"skills":{"woodcutting":"<:woodcutting:123456789012345678>","fishing":"<:fishing:123456789012345679>"},"bosses":{"50":"<:kbd:123456789012345680>","kalphite queen":"<:kalphite_queen:123456789012345681>"}}
```

Names are matched without regard to case or surrounding spaces. Boss NPC IDs take priority over boss names. Animated emoji codes (`<a:name:id>`) are also accepted. Restart/redeploy the relay after changing the environment variable; friends' game configs require no changes.

The mapped emoji replaces the leading party/skull emoji and keeps the existing one-line message layout. Missing mappings retain the current default emoji. Invalid JSON or malformed emoji codes stop the relay at startup with a configuration error; a valid code referencing a deleted or inaccessible Discord emoji cannot be detected locally, so keep the mappings up to date.

Item drop notifications are not currently sent by the game. Adding inline item icons would first require an item drop event carrying the item ID.

Run `npm test` in this directory to check icon selection, fallbacks, and message escaping.

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

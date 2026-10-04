# 2006Scape Singleplayer Server

This is the game server for the 2006Scape single-player project. It provides the
world simulation, networking, player persistence, NPCs, skills, quests,
minigames, trading, and configurable bot populations used by the matching Java
client. It includes a desktop control panel for starting, stopping, configuring,
and monitoring the server.

The server listens on port `43594` by default. Player and hiscore data can be
stored in the embedded SQLite database at `data/server.db`, so an external
database server is not needed.

**Revision 443 migration:** This branch defaults to a 443 cache path, login, and
update handshake. The static region packet now uses the 443 layout and XTEA
keys from `cache/xteas.json`. The matching client loads 443 JS5 assets when
started with `Play-Client.bat`; packet and interface behavior still needs graphical
validation.

## Requirements

- Windows (the included build and run scripts are batch files)
- A Java 8 JDK, any update (for example 8u101 or 8u491)
- The batch scripts detect Java 8 automatically. For a custom installation folder, set `JAVA_HOME` to that folder. Java 11+ and JRE-only installations are skipped.
- If none is found, the scripts automatically download and install a free Temurin Java 8 JDK under `%LOCALAPPDATA%\SoloScape\java8`, with no administrator permissions required.
- The Client is `2006sp client`
- About 1 GB of free RAM

No dependency download or package manager is required. All Java libraries are
included in `lib/`:

- Apache Commons Compress 1.0
- Joda-Time 2.3
- SQLite JDBC 3.53.4.0
- SLF4J API and no-operation provider 2.0.17
- `javac++.jar`, a bundled project runtime dependency

The revision 443 JS5 cache and map XTEA keys are in `cache/`. Server content
data is in `data/`.

## Build

From File Explorer, double-click `Build-Server.bat`. From Command Prompt, run:

```bat
cd /d "location of server"
Build-Server.bat
```

`Start-Server.bat` appears in this folder after a successful build. Keep
`Start-Server.bat.template` in place so the build can create it.

The script compiles every Java file under `src/main/java`, uses all JARs in
`lib/` as the classpath, and creates:

```text
dist/server.jar
```

The dependencies remain in `lib/`; keep that directory next to `dist/` when
running the server.

For a build that exits without waiting for a keypress, run `Build-Server.bat --no-pause`.
This is useful for terminals and automation; it returns a nonzero exit code when
compilation or packaging fails.


## Run

Start the compiled server control panel with:

```bat
cd /d "location of server"
Start-Server.bat
```

When the control panel opens:

1. Review the connection and gameplay settings if needed.
2. Click **Start Server** and wait for the status to show that it is online.
3. Start the matching client with its `Play-Client.bat` file.
4. Log in with the username and password you want to use. Local player data is
   created and saved by the server.

To diagnose a click that does nothing, enable **Debug Mode** in the launcher
before starting the server. Click packet details and interaction handling
messages will appear in the server terminal with the `[packet-debug]` prefix.
The same trace is saved to `qa-output/gameplay-trace.log`.

Inventory clicks also include an `[item-debug]` marker and a structured outcome.
`received` confirms the packet decoded, `rejected` explains validation failures
such as a stale slot or unmapped widget, and `unhandled` identifies an item option
that reached the server but has no gameplay handler. Each line includes the item
ID and name, option, slot, interface, player position, and rejection reason.
Administrators can alternatively enter `::debug` in-game to toggle these
diagnostics for only their player; the terminal prints an `[interaction-debug]`
confirmation when the toggle changes.

Enter `::item` to open the developer item picker in the matching client. Type
part of an item's name and click a result to add one to your inventory for free.
The GE-style search includes coins, untradeable items, members items and noted
items, which are labelled `(noted)`. It stays open for repeated selections;
press Escape or click its close button to exit. A full inventory uses the usual
no-space message. Like the existing test cheats, this command is available to
local players without an administrator rank.

Keep the project directory structure intact. `Start-Server.bat` uses the project root as
the working directory so the relative paths to `config/`, `data/`, `cache/`, and
`lib/` resolve correctly.

## Interaction coverage and revision 443 routing

Hold Ctrl and left-click the game view to teleport to the picked ground tile,
including beneath scenery. The client uses the existing `::tele` command and
then `::pos` to print the destination coordinates in chat.

### 2006 Christmas rescue

Speak to Shanty Claws on the larger northern Musa Point jetty at `2904,3186,0`.
The client marks his location with the original wrapped-present minimap icon.
Continue through his introduction and instructions to start the rescue;
walking away before finishing the introduction does not start the event.
Make snowballs south of Karamja Volcano, beside the cave entrance at
`2843,3141`, and wield them. The 2x2 cave replaces the tree there and has a dungeon minimap icon.
Pelt a gublinch three times, pick up its
shards, and use one shard on each of Shanty's ten cages. Talk to Shanty
after filling all ten to receive the reindeer hat (`10507`) and, on a
members world with membership, the Wintumber tree (`10508`). Operate the
worn hat to perform its emote. Diango in Draynor replaces missing rewards;
items already in the inventory, equipment or bank are not duplicated.

The four Make-ball-of snow piles are at `2841,3147`, `2844,3147`, `2840,3150`
and `2842,3153`, all on plane 0.

Sixteen Gublinch wander throughout both dungeon loops and the northern furnace
chamber. After freezing, they respawn at their original anchors and resume wandering.

The client preserves the revision-443 cache's original snow south of Karamja
Volcano; it does not repaint the eastern terrain. While standing on those snowy
tiles, chunky screen-space snowballs sweep diagonally from the top-right of the
game viewport toward the bottom-left, matching the direction visible in the 2006
footage. There is no camera-following transform or pale screen wash. The effect
stops inside caves, on upper planes and in instances. Restart the rebuilt client
and reload the map to remove the previous eastern ground override. The Karamja
weather layer is a recreation based on the 2006 footage; the separate legacy
Trollweiss overlay remains interface 11877.

Shanty's conversations use the supplied 2006 transcript, including the ring of
Charos greeting, questions, progress reminders, item-use replies, and post-event
dialogue. His five overhead shanties repeat in order. Search the cave entrance
to read his notice. The hat and members tree are handed over separately: with
one free slot, take the hat and return with space for the tree. Players who
received the hat on a free world can collect the tree later on a members world.

The ten required cages are at `2907,3183..3185`, `2904,3188..3191`, and
`2907,3188..3190`, all on plane 0. All ten cages count toward completion.
The existing jetty
terrain is used without extending the map.

The rescue is permanently available with the revision 443 cache, independently
of seasonal rare-item drops. Cage progress and completion use saved event slot
5, including offline character rewrites. Children and filled-cage appearances
reflect each player's progress. The tree is awarded and reclaimable; planting
awaits a player-owned-house system.

Historical mechanics reference: [2006 Christmas event guide](https://runescape.salmoneus.net/tips/christmas-2006-event.html).
The event's cache-backed regression entry point is
`src/test/java/GublinchChristmasEventChecks.java`.
Client visual checks are `Client/tests/ChristmasSnowChecks.java` and
`Client/tests/ChristmasMinimapChecks.java`, run from `Server` with the client and
server classes plus `Server/lib/*` on the classpath.

Run `powershell -ExecutionPolicy Bypass -File tools\interaction-audit.ps1 -Check`
from the repository root to compile an isolated audit build, inventory the loaded
cache, and run the interaction regression checks. The output is
`qa-output/interaction-audit/interactions.csv` and `summary.md`.

The report includes NPC actions, object actions, inventory and ground-item actions,
native widgets, and interface-group bridge targets. It includes unused cache
content. `semantic` means an action has a named gameplay owner, not that every
NPC, item or quest has been play-tested. `review-legacy` and
`review-native-or-unmapped` need inspection; they are not automatically broken.
`missing-*` records missing bindings or data. The client's generated Grand
Exchange groups are listed separately from cache interface groups.

Revision 443 NPC clicks now resolve their loaded action names instead of assuming
that the old server's numeric menu slots still match. Talk-to retains quest and
dialogue dispatch; bank, trade, pickpocket, fishing, supported teleports, tanning,
shearing, healing and the Rewards Guardian use their existing gameplay services.
Registered regional fishing variants resolve paired Net/Bait, Lure/Bait,
Cage/Harpoon and Net/Harpoon actions. Specialised NPC actions keep their legacy
handlers pending an audited mapping. Object routing also identifies bank-booth
operations and supported tree/rock gathering. Inventory routing identifies common
consumption, equipment and disposal actions; other item options retain their
legacy handlers.

Runtime interaction traces record the selected action and route, and record
missing NPC data and unmapped widget actions as `unhandled`. Add regression checks
when binding additional actions; preserve movement, cancellation, ownership,
membership and quest requirements. This audit provides a backlog for completing
the migration, rather than claiming all game content is implemented.

## Configuration and data

- `config/server.cfg` controls membership, XP rates, bots, shops, drops,
  gameplay options, LAN access, and other server behaviour.
- `data/settings.dat` stores settings managed by the control panel.
- `data/server.db` is the generated SQLite player/hiscore database.
- `data/characters/` contains generated character data when file-based storage
  is used.
- `cache/` contains the revision 443 JS5 cache.
- `cache/xteas.json` contains the validated map XTEA keys.

The client defaults to `127.0.0.1:43594`. Leave LAN support disabled for a
local-only game. If you enable LAN connections, ensure your firewall and network
settings allow the selected server port.

Tree respawns use `[EFFECTIVE_WORLD_POPULATION];1250` in `config/server.cfg`.
Set 0..2000 to emulate a world population, or -1 to use connected players
(including bots). Restart the server after changing it. Larger populations
make oak, willow, maple, yew and magic trees respawn faster. At the default
1250, elapsed delays are approximately 11.4s, 11.4s, 50.4s, 82.8s and 162s.
Other tree types retain their existing delays; NPCs and mining are unaffected.

The curves reconstruct the linear timing table supplied for 750..2000 players,
with linear extrapolation below 750 and population clamped to 0..2000. They are
not a verified revision 443 formula. Delays round up to 600ms ticks and account
for ObjectManager restoring one tick after its countdown reaches zero.
Jagex confirmed the former population dependency in
[Boss Pets and Spawn Rates](https://secure.runescape.com/m=news/boss-pets-and-spawn-rates?oldschool=1).

Woodcutting rolls Tree Spirits only on a validated initial tree interaction
(1/2001), before scheduling chopping. Axe-head loss rolls on that initial hit
(1/2001) and less often on continued four-tick chopping cycles (1/20001).
Either event interrupts chopping before a log can be awarded. Existing Ent
interactions use their own axe-breaking path rather than these initial rolls.
The initial-attempt description comes from the
[Tree Spirit history](https://runescape.fandom.com/wiki/Tree_spirit), and the
initial-hit bias for axe separation comes from the
[period Woodcutting guide](https://2007rshelp.com/skill/11/woodcutting).
These sources do not establish exact odds; the chosen rates are approximations.
Ents retain the existing two scheduled warning checks before axe breakage;
the exact historical delay remains unverified.

## Project layout

```text
src/main/java/       Server, networking, gameplay, bots, and control-panel source
lib/                 Bundled runtime dependencies
config/              Editable server configuration
cache/               Revision 443 JS5 game cache
data/                Content, settings, saves, logs, and SQLite data
Build-Server.bat             Compiles and packages the server
Start-Server.bat               Opens the server control panel
dist/server.jar       Generated executable JAR
```

## Third-party notices

### RS Mod pathfinder

Parts of the route-reach semantics and large-entity clipping approach in this
repository are adapted from the RS Mod pathfinder project.

Copyright (c) 2020 RS Mod

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION
OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN
CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.

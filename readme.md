# SoloScape

From the repository root, use Windows PowerShell with a JDK (`java`, `javac`,
and `jar`) on PATH:

```powershell
.\scape.ps1 build          # Build both
.\scape.ps1 build client   # Build only the client
.\scape.ps1 build server   # Build only the server
.\scape.ps1 run            # Build and launch both; automatically start the server
.\scape.ps1 dev            # Launch both with live recompilation
.\scape.ps1 clean          # Remove generated build files
.\scape.ps1 update         # Pull latest commits (fast-forward only)
```

Double-click `scape.bat` for the same commands as menu options 1-7, with `0`
to exit. It also accepts commands, such as `scape.bat build client`, and uses
PowerShell with execution-policy bypass for that invocation.

`run` and `dev` open separate client and server consoles. Development mode uses
the existing HotSwap watchers: saving Java sources recompiles changes, and
structural edits trigger application restarts. Both applications need the
bundled `Server/cache` folder. Close the applications to stop them; stop both
development watchers before building or cleaning.

`clean` removes `Client/build`, `Server/build`, `Server/build-release`, and
`Server/dist`. Cache files, libraries, configuration, and player saves are kept.

`update` requires Git on PATH and a branch with an upstream remote. It preserves
local work and reports conflicting changes or diverged history for you to resolve.

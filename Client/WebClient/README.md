# 06 WebClient

This is a thin mobile/browser view of the real `06-Client` Java client.

It does **not** reimplement the RuneScape UI, cache, packets, widgets, models, scene, login protocol, ISAAC, RSA, or gameplay in JavaScript. Those remain inside the existing revision-443 Java client.

## How it works

`06-Client` still does all normal work:

- revision 443 JS5/cache loading
- login and RSA/ISAAC
- server packets
- players/NPCs
- scene/model rendering
- interfaces/widgets
- inventory/chat/minimap
- mouse/keyboard handling

A small `WebClientBridge` was added to the Java client. Every native `JImage.draw(...)` blit is copied into a 765x503 composite framebuffer. At the end of a normal Java render pass that framebuffer is encoded as PNG and streamed to Safari over WebSocket.

Browser input is converted back to AWT mouse/key events and dispatched to the existing Java `Canvas`, so the same Java input handlers run.

## Start

1. Start `06-Server` normally on port `43594`.
2. Run:

```bat
Client\WebClient\Play-In-Browser-Or-Phone.bat
```

This rebuilds and starts the real Java renderer with its native window hidden, then automatically opens the game in your default browser at `http://127.0.0.1:8081/`. The browser is a streamed view of the Java client, not a JavaScript reimplementation: the Java JAR must run locally to render frames and handle the RuneScape protocol.

The browser displays the game at its original **765×503** pixel size on desktop, centred rather than stretched across the window. On smaller windows and phones, it scales down proportionally to fit while keeping clicks aligned with the game.

The console prints the exact phone URL, for example:

```text
[webclient] Phone: http://192.168.0.129:8081/
```

Keep the command window open while you use your browser or phone. Closing that window stops the Java renderer and its music; you do not need to keep a separate visible Java game window open.

## Original Windows EXE

For the original Jagex `runescape.exe` with its URL changed to
`http://127.0.0.1:8081/`, run `Enable-Original-Client.bat` once, then close and
reopen the EXE. This enables IE11 rendering for that executable for the current
Windows user. The page automatically selects an ES5 desktop view for its embedded
browser, with the same framebuffer, mouse and keyboard connection to Java.
Its game area stays at 765x503 pixels at 1:1 scale. The patched original EXE has
a fixed window with resizing and maximize disabled. The game remains focusable
for keyboard input without drawing a dotted focus outline.

Start the game server and run `Start-Original-Client.bat` before opening the EXE.
This starts the Java renderer with an off-screen utility window, **attached to
its command window**, so closing that console stops the renderer and music.
After the original EXE has connected, closing its window also shuts down the
renderer once its last browser/WebSocket connection closes (after a brief grace
period). Leave the console open while using the EXE. If a bridge already owns
port 8081, close its launcher first rather than silently starting another
renderer on a different port. Rebuild the client after pulling this change.

The game server on port 43594 does not serve the webpage on port 8081; the Java
client bridge provides that page. Modern browsers keep using the same stream.

## Phone controls

- Tap = normal left click.
- Long press (about 0.45 sec) = right click / context menu.
- Drag = normal left-button drag.
- The small keyboard button opens the iPhone keyboard; characters are sent through the Java client's existing key handler.
- Landscape orientation is recommended because the native client framebuffer is 765x503.

## Current transport

Frames are lossless PNG at up to 50 FPS, matching the Java client loop. Encoding
uses a fast Sub filter and level-1 compression to reduce latency. Slow decoding
keeps the newest waiting frame rather than accumulating a playback backlog.

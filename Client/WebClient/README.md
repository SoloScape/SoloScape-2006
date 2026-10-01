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

This rebuilds and starts the real `06-Client` with the bridge enabled.

The console prints the exact phone URL, for example:

```text
[webclient] Phone: http://192.168.0.129:8081/
```

Keep both the command window and Java client window open.

## Phone controls

- Tap = normal left click.
- Long press (about 0.45 sec) = right click / context menu.
- Drag = normal left-button drag.
- The small keyboard button opens the iPhone keyboard; characters are sent through the Java client's existing key handler.
- Landscape orientation is recommended because the native client framebuffer is 765x503.

## Current transport

Frames are lossless PNG at up to 10 FPS. This prioritises correctness/fidelity over bandwidth for the first working version. The next optimisation can change the transport to dirty rectangles or a faster codec without changing any game/client behaviour.

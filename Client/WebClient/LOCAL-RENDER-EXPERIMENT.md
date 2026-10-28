# Browser-local rendering experiment (CheerpJ 4.3)

This is **experimental**, not a completed playable mobile client. It tries to
run the existing revision-443 Java renderer **inside the phone's browser** using
CheerpJ, instead of streaming finished PNG frames from the Linux machine.
The existing `Play-In-Browser-Or-Phone.sh` is unchanged and remains the working
remote-rendered fallback.

## Architecture

- Linux: run the existing SoloScape **game server** on TCP 43594.
- Phone: download the Java client JAR and the game cache once, then execute
  the Java client locally via CheerpJ (Java 8 on WebAssembly/JavaScript).
- The Java client's ordinary RSPS socket must connect back to the Linux game
  server. CheerpJ requires a separate mechanism for raw TCP; this spike uses
  its **Tailscale integration**. The existing `WebGateway.java` WebSocket/TCP
  relay cannot be used automatically with Java's `Socket` API.
- The static HTTP server serves the JAR/cache only. No hidden Linux renderer,
  per-frame PNG encoding, or gameplay logic is moved to the static server.

## How to try it

1. Install/run Tailscale on the **Linux server** and confirm it can reach the
   SoloScape game server on TCP 43594 at its **Tailscale IP**. Keep its firewall
   and VPN access restricted to your devices.
2. From the repository root, start the normal SoloScape game server.
3. In a **separate terminal on Linux**, run:

   ```bash
   bash Client/WebClient/Try-Local-Render.sh
   ```

   This builds `Client/build/client.jar`, stages a minimal set of cache
   archives and serves them with Python on port **8082**. It does not run a
   Java renderer on Linux. Files are staged in the ignored
   `Client/WebClient/build/local-render/` directory.
4. On the phone, connected to the same trusted LAN or private VPN, open
   `http://<linux-lan-ip>:8082/local-render.html`.
5. Enter the Linux server's **Tailscale IP** and port **43594**. Tap
   **Try local rendering**. When the Tailscale login link appears, sign in via
   that link and return to the game tab. Never place an auth key in the page.
6. Check the browser console / Java output for cache import, client startup,
   AWT rendering and network errors. The game may not yet launch successfully.

The page loads CheerpJ 4.3 from its official CDN, so Internet access is
required the first time. Its runtime and licensing are third-party concerns:
https://cheerpj.com/docs/licensing

**Do not port-forward or expose port 8082 to the public Internet.** The staging
server has no authentication and deliberately serves the Java JAR and game cache
to devices on the reachable network. Use a trusted LAN or properly secured VPN,
and use HTTPS when deploying beyond a local experiment.

## Why the cache bootstrap exists

`Signlink` opens the `main_file_cache.dat2` and `main_file_cache.idx*`
files using `RandomAccessFile("rw")`. CheerpJ's `/app` mount points at
read-only HTTP content, so `BrowserClientBootstrap` first imports the
necessary files to `/files/soloscape-cache` (browser-persistent storage).
The UID file is **not** copied from Linux. It remains per-browser.

To avoid repeated copies, the spike compares file lengths. A changed cache
file with an identical length will **not** be replaced; clear the site's
browser storage before testing a new same-length cache. The first import may
be expensive on phones and the client may exceed mobile memory limits.

## What has *not* been validated

- CheerpJ compatibility with this particular decompiled Java/AWT software
  renderer, its native event handling and its audio code.
- A complete game login or render pass on mobile Chrome/Safari.
- Tailscale browser login and raw Java TCP socket routing end-to-end.
- The full cache import and storage budget on phone.
- Touch controls, zoom, performance, latency, data usage, battery, and memory.
- Browser foreground/background lifecycle behaviour.

If startup fails, keep using the existing streamed client on port 8081.
Do not claim this experiment has shipped a usable local renderer until a real
phone has logged in, entered the world, moved, and completed a play session.

## After the first successful startup

Measure total cold start, cache transferred, frame rate, memory, phone battery
drain per hour, and network bytes/minute. If the Java-in-browser approach
cannot meet acceptable performance, the longer-term solution is a dedicated
WebGL/WebAssembly or Canvas port of the client renderer, cache parser and
revision-443 networking, with the existing Linux game server unchanged.

## Relevant sources

- CheerpJ launch API: https://cheerpj.com/docs/reference/cheerpjRunMain
- CheerpJ virtual filesystem: https://cheerpj.com/docs/guides/filesystem
- CheerpJ networking/Tailscale: https://cheerpj.com/docs/guides/Networking.html
- Java game code: `Client/src/unpackaged/Client.java` and `Signlink.java`
- Browser cache bootstrap: `Client/src/unpackaged/BrowserClientBootstrap.java`
- Experimental browser page: `Client/WebClient/web/local-render.html`

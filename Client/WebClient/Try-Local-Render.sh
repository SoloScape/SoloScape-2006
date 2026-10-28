#!/usr/bin/env bash
# EXPERIMENTAL: attempt to run the real Java client locally in the phone browser.
# Unlike Play-In-Browser-Or-Phone.sh, this does NOT start a Java renderer on Linux.
set -euo pipefail

web="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd -P)"
client="$(cd "$web/.." && pwd -P)"
server="$(cd "$web/../../Server" && pwd -P)"
stage="$web/build/local-render"
cache="$server/cache"
port="${LOCAL_RENDER_HTTP_PORT:-8082}"

if [[ ! -f "$cache/main_file_cache.dat2" ]]; then
    echo "Missing game cache: $cache/main_file_cache.dat2" >&2
    exit 1
fi
for index in $(seq 0 13) 255; do
    if [[ ! -f "$cache/main_file_cache.idx$index" ]]; then
        echo "Missing game cache index: $cache/main_file_cache.idx$index" >&2
        exit 1
    fi
done
if ! command -v python3 >/dev/null 2>&1; then
    echo "python3 is required to serve the experimental browser files." >&2
    exit 1
fi

# Existing build process uses Java 8 and includes BrowserClientBootstrap.
bash "$client/Build-Client.sh"
mkdir -p "$stage/cache"
cp "$client/build/client.jar" "$stage/client.jar"
cp "$web/web/local-render.html" "$stage/local-render.html"
cp "$cache/main_file_cache.dat2" "$stage/cache/main_file_cache.dat2"
for index in $(seq 0 13) 255; do
    cp "$cache/main_file_cache.idx$index" "$stage/cache/main_file_cache.idx$index"
done

echo
echo "EXPERIMENTAL browser-local client (not the existing PNG stream)"
echo "From your phone, open: http://<linux-lan-ip>:$port/local-render.html"
echo "Enter the Linux server's Tailscale IP (not 127.0.0.1) in the page."
echo "Keep the SoloScape game server running on port 43594."
echo "This HTTP launcher is for trusted LAN or private-VPN testing only."
echo "Do not port-forward $port or expose these files directly to the Internet."
echo "Press Ctrl+C to stop serving browser files."
echo

# One-way cache/JAR delivery only: game rendering will happen in CheerpJ on phone.
exec python3 -m http.server "$port" --bind 0.0.0.0 --directory "$stage"

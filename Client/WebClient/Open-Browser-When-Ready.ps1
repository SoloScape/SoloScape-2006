# Started alongside the console-attached Java renderer by Play-In-Browser-Or-Phone.bat.
# Do not open a stale browser page before WebClientBridge is listening.
$ErrorActionPreference = 'SilentlyContinue'
$url = 'http://127.0.0.1:8081/'
for ($attempt = 0; $attempt -lt 120; $attempt++) {
    try {
        $response = Invoke-WebRequest -Uri $url -UseBasicParsing -TimeoutSec 2
        if ($response.StatusCode -eq 200 -and $response.Content -match '/windows-client.js') {
            Start-Process $url
            exit 0
        }
    } catch {
        # Java may still be initializing the web bridge.
    }
    Start-Sleep -Milliseconds 250
}
exit 1

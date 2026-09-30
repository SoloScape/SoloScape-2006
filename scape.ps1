param(
    [ValidateSet('build', 'run', 'dev', 'clean', 'update')]
    [string]$Command = 'run',
    [ValidateSet('client', 'server')]
    [string]$Target
)

$ErrorActionPreference = 'Stop'
$Root = $PSScriptRoot

function Invoke-Build([string]$Component) {
    $script = Join-Path $Root "$Component\build.bat"
    Write-Host "Building $Component..." -ForegroundColor Cyan
    & $script --no-pause
    if ($LASTEXITCODE -ne 0) {
        throw "$Component build failed (exit code $LASTEXITCODE)."
    }
}

function Start-Component([string]$Component, [string]$Script, [string]$Arguments = '') {
    $directory = Join-Path $Root $Component
    # Each component keeps its own interactive console for logs and shutdown.
    $commandLine = '/d /k ""{0}" {1}"' -f (Join-Path $directory $Script), $Arguments
    Start-Process -FilePath $env:ComSpec -ArgumentList $commandLine -WorkingDirectory $directory | Out-Null
}

try {
    if ($Target -and $Command -ne 'build') {
        throw 'Only build accepts a client or server target.'
    }

    switch ($Command) {
        'build' {
            if ($Target) {
                Invoke-Build $Target
            } else {
                Invoke-Build 'Server'
                Invoke-Build 'Client'
            }
        }
        'run' {
            Invoke-Build 'Server'
            Invoke-Build 'Client'
            Start-Component 'Server' 'Run.bat' '--auto-start'
            Start-Component 'Client' 'run.bat'
            Write-Host 'Server and client launched in separate consoles.'
        }
        'dev' {
            Start-Component 'Server' 'dev.bat'
            Start-Component 'Client' 'dev.bat'
            Write-Host 'Live recompilation started in separate consoles. Close each application to stop its watcher.'
        }
        'clean' {
            # Only these generated outputs are removed; caches and player data stay intact.
            foreach ($relative in @('Client\build', 'Server\build', 'Server\build-release', 'Server\dist')) {
                $path = [System.IO.Path]::GetFullPath((Join-Path $Root $relative))
                $prefix = [System.IO.Path]::GetFullPath($Root).TrimEnd('\') + '\'
                if (-not $path.StartsWith($prefix, [System.StringComparison]::OrdinalIgnoreCase)) {
                    throw "Refusing to clean outside the repository: $path"
                }
                if (Test-Path -LiteralPath $path) {
                    Remove-Item -LiteralPath $path -Recurse -Force
                    Write-Host "Removed $relative"
                }
            }
            Write-Host 'Clean complete.'
        }
        'update' {
            & git -C $Root pull --ff-only
            if ($LASTEXITCODE -ne 0) {
                throw 'Git pull failed. Resolve the reported Git issue and try again.'
            }
        }
    }
    exit 0
} catch {
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

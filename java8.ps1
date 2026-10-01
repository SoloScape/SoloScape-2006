$ErrorActionPreference = 'Stop'

function Test-Java8Jdk([string]$JdkDirectory) {
    # Windows PowerShell treats java -version's stderr as a non-terminating error.
    $ErrorActionPreference = 'Continue'
    foreach ($tool in @('java', 'javac', 'jar')) {
        if (-not (Test-Path -LiteralPath (Join-Path $JdkDirectory "bin\$tool.exe") -PathType Leaf)) {
            return $false
        }
    }
    # Check the executables themselves: folder names and registry entries can be stale.
    foreach ($tool in @('java', 'javac')) {
        $output = & (Join-Path $JdkDirectory "bin\$tool.exe") -version 2>&1
        if ($LASTEXITCODE -ne 0 -or ($output -join "`n") -notmatch '(?m)^(?:java|openjdk|javac) (?:version )?"?1\.8\.0(?:[_\s"-]|$)') {
            return $false
        }
    }
    return $true
}

$candidates = [System.Collections.Generic.List[string]]::new()
if ($env:JAVA_HOME) { $candidates.Add($env:JAVA_HOME.Trim('"')) }

# Reuse the per-user fallback on subsequent launches, including while offline.
$managedRoot = Join-Path $env:LOCALAPPDATA 'SoloScape\java8'
foreach ($installation in @(Get-ChildItem -LiteralPath $managedRoot -Directory -ErrorAction SilentlyContinue)) {
    $candidates.Add($installation.FullName)
    foreach ($jdk in @(Get-ChildItem -LiteralPath $installation.FullName -Directory -ErrorAction SilentlyContinue)) {
        $candidates.Add($jdk.FullName)
    }
}

foreach ($command in @(Get-Command javac.exe -All -ErrorAction SilentlyContinue)) {
    $candidates.Add((Split-Path (Split-Path $command.Source -Parent) -Parent))
}

foreach ($registryRoot in @(
    'HKLM:\SOFTWARE\JavaSoft',
    'HKLM:\SOFTWARE\WOW6432Node\JavaSoft',
    'HKLM:\SOFTWARE\Eclipse Adoptium',
    'HKLM:\SOFTWARE\AdoptOpenJDK',
    'HKCU:\SOFTWARE\JavaSoft'
)) {
    foreach ($key in @(Get-ChildItem -LiteralPath $registryRoot -Recurse -ErrorAction SilentlyContinue)) {
        $properties = Get-ItemProperty -LiteralPath $key.PSPath -ErrorAction SilentlyContinue
        foreach ($name in @('JavaHome', 'Path')) {
            if ($properties.$name) { $candidates.Add([string]$properties.$name) }
        }
    }
}

foreach ($base in @($env:ProgramFiles, ${env:ProgramFiles(x86)}, "$env:LOCALAPPDATA\Programs", $env:USERPROFILE)) {
    if (-not $base) { continue }
    foreach ($vendor in @('Java', 'Eclipse Adoptium', 'Eclipse Foundation', 'AdoptOpenJDK', 'Microsoft', 'Amazon Corretto', 'BellSoft', 'Zulu', 'Semeru', '.jdks')) {
        $directory = Join-Path $base $vendor
        if (Test-Path -LiteralPath $directory -PathType Container) {
            $candidates.Add($directory)
            foreach ($installation in @(Get-ChildItem -LiteralPath $directory -Directory -ErrorAction SilentlyContinue)) {
                $candidates.Add($installation.FullName)
            }
        }
    }
}

foreach ($candidate in @($candidates | Select-Object -Unique)) {
    try {
        if (Test-Java8Jdk $candidate) {
            Write-Output ([System.IO.Path]::GetFullPath($candidate))
            exit 0
        }
    } catch {
        # A broken installation must not prevent trying another candidate.
    }
}

function Install-Java8Jdk([string]$InstallRoot) {
    [Console]::Error.WriteLine('No Java 8 JDK found. Downloading and installing free Eclipse Temurin Java 8...')
    [Net.ServicePointManager]::SecurityProtocol = [Net.ServicePointManager]::SecurityProtocol -bor [Net.SecurityProtocolType]::Tls12
    $architecture = if ([Environment]::Is64BitOperatingSystem) { 'x64' } else { 'x86' }
    $api = "https://api.adoptium.net/v3/assets/latest/8/hotspot?architecture=$architecture&image_type=jdk&os=windows&vendor=eclipse"
    $assets = @(Invoke-RestMethod -Uri $api -TimeoutSec 60)
    $package = $assets[0].binary.package
    if (-not $package -or $package.checksum -notmatch '^[a-fA-F0-9]{64}$' -or
        $package.link -notlike 'https://github.com/adoptium/temurin8-binaries/releases/download/*.zip') {
        throw 'Adoptium did not return a valid Windows Java 8 JDK archive.'
    }

    $installationId = [guid]::NewGuid().ToString('N')
    $archive = Join-Path ([System.IO.Path]::GetTempPath()) "soloscape-java8-$installationId.zip"
    $destination = Join-Path $InstallRoot "temurin-$installationId"
    try {
        # Keep all progress off stdout: java8.bat reads stdout as the selected path.
        $ProgressPreference = 'SilentlyContinue'
        Invoke-WebRequest -UseBasicParsing -Uri $package.link -OutFile $archive -TimeoutSec 600
        if ((Get-FileHash -LiteralPath $archive -Algorithm SHA256).Hash -ne $package.checksum) {
            throw 'Java 8 download failed its SHA-256 checksum check.'
        }
        Expand-Archive -LiteralPath $archive -DestinationPath $destination
        foreach ($jdk in @(Get-ChildItem -LiteralPath $destination -Directory)) {
            if (Test-Java8Jdk $jdk.FullName) {
                [Console]::Error.WriteLine('Java 8 installed successfully. Continuing SoloScape...')
                return $jdk.FullName
            }
        }
        throw 'The downloaded archive did not contain a working Java 8 JDK.'
    } finally {
        if (Test-Path -LiteralPath $archive) { Remove-Item -LiteralPath $archive -Force }
    }
}

try {
    Write-Output (Install-Java8Jdk $managedRoot)
    exit 0
} catch {
    [Console]::Error.WriteLine("ERROR: Java 8 setup failed: $($_.Exception.Message)")
    [Console]::Error.WriteLine('Check your internet connection and retry, or install a Java 8 JDK manually and set JAVA_HOME to its folder.')
    exit 1
}

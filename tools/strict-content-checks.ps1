param([switch]$SelfTest)
$ErrorActionPreference = 'Stop'
$strictRoot = Split-Path -Parent $PSScriptRoot
$strictServer = Join-Path $strictRoot 'Server'
$strictOutput = Join-Path $strictRoot 'qa-output\strict-content-checks'
$strictClasses = Join-Path $strictOutput 'classes'
New-Item -ItemType Directory -Force -Path $strictClasses | Out-Null
$strictSources = @('Definitions', 'Js5CacheStore', 'Js5Container', 'Js5Group', 'Js5ReferenceTable') | ForEach-Object {
    Get-Item -LiteralPath (Join-Path $strictServer "src\main\java\com\rs2\cache\js5\$_.java")
}
$strictSources += Get-Item -LiteralPath (Join-Path $strictServer 'src\test\java\StrictContentChecks.java')
$strictList = Join-Path $strictOutput 'sources.txt'
[System.IO.File]::WriteAllLines($strictList, [string[]]($strictSources | ForEach-Object { '"' + ($_.FullName -replace '\\', '/') + '"' }), [System.Text.UTF8Encoding]::new($false))
$strictLibraries = Join-Path $strictServer 'lib\commons-compress-1.0.jar'
$strictJdk = Split-Path (Split-Path (Get-Command javac).Source -Parent) -Parent
$strictTools = Join-Path $strictJdk 'lib\tools.jar'
$strictRuntime = "$strictClasses;$strictLibraries"
if (Test-Path -LiteralPath $strictTools) {
    $strictRuntime += ";$strictTools"
    $strictLibraries += ";$strictTools"
}
& javac -encoding UTF-8 -classpath $strictLibraries -d $strictClasses "@$strictList"
if ($LASTEXITCODE -ne 0) { throw 'Strict content checks compilation failed.' }
Push-Location $strictServer
try {
    if ($SelfTest) { & java -cp $strictRuntime StrictContentChecks --self-test }
    else { & java -cp $strictRuntime StrictContentChecks }
    if ($LASTEXITCODE -ne 0) { throw 'Strict content checks failed. See qa-output/strict-content-checks/report.tsv.' }
} finally { Pop-Location }

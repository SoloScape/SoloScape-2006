param([switch]$Check)
$ErrorActionPreference = 'Stop'
$interactionRoot = Split-Path -Parent $PSScriptRoot
$interactionServer = Join-Path $interactionRoot 'Server'
$interactionOutput = Join-Path $interactionRoot 'qa-output\interaction-audit'
$interactionClasses = Join-Path $interactionOutput 'classes'
New-Item -ItemType Directory -Force -Path $interactionClasses | Out-Null
$interactionChecks = @('InteractionCoverageAudit', 'InteractionRoutingChecks', 'ShopkeeperTradeChecks')
$interactionSources = @(Get-ChildItem -LiteralPath (Join-Path $interactionServer 'src\main\java') -Recurse -Filter '*.java')
foreach ($interactionCheck in $interactionChecks) {
    $interactionSources += Get-Item -LiteralPath (Join-Path $interactionServer "src\test\java\$interactionCheck.java")
}
$interactionSourceList = Join-Path $interactionOutput 'sources.txt'
$interactionSourceLines = $interactionSources | ForEach-Object { '"' + ($_.FullName -replace '\\', '/') + '"' }
[System.IO.File]::WriteAllLines($interactionSourceList, [string[]]$interactionSourceLines,
    [System.Text.UTF8Encoding]::new($false))
$interactionLibraries = Join-Path $interactionServer 'lib\*'
# Use the available JDK; the normal project launch scripts select Java 8.
& javac -encoding UTF-8 -classpath $interactionLibraries -d $interactionClasses "@$interactionSourceList"
if ($LASTEXITCODE -ne 0) { throw 'Interaction audit compilation failed.' }
Push-Location $interactionServer
try {
    $interactionClasspath = "$interactionClasses;$interactionLibraries"
    & java -cp $interactionClasspath InteractionCoverageAudit $interactionOutput
    if ($LASTEXITCODE -ne 0) { throw 'Interaction inventory failed.' }
    if ($Check) {
        foreach ($interactionCheck in @('InteractionRoutingChecks', 'ShopkeeperTradeChecks')) {
            & java -cp $interactionClasspath $interactionCheck
            if ($LASTEXITCODE -ne 0) { throw "$interactionCheck failed." }
        }
    }
} finally { Pop-Location }

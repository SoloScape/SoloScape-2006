$ErrorActionPreference = 'Stop'
$mcpRoot = Split-Path -Parent $PSScriptRoot
. (Join-Path $PSScriptRoot 'java8.ps1')
$mcpOutput = Join-Path $mcpRoot 'qa-output\mcp'
$mcpClasses = Join-Path $mcpOutput 'classes'
New-Item -ItemType Directory -Force -Path $mcpClasses | Out-Null
$mcpSources = @(Get-ChildItem -LiteralPath (Join-Path $mcpRoot 'Client\src') -Recurse -Filter '*.java')
$mcpSources += Get-Item -LiteralPath (Join-Path $mcpRoot 'Client\tests\DeveloperToolsChecks.java')
$mcpSourceList = Join-Path $mcpOutput 'sources.txt'
[IO.File]::WriteAllLines($mcpSourceList, [string[]]($mcpSources | ForEach-Object {
    '"' + $_.FullName.Replace('\', '/') + '"'
}), [Text.UTF8Encoding]::new($false))
& "$env:JAVA_HOME\bin\javac.exe" -encoding UTF-8 -d $mcpClasses "@$mcpSourceList"
if ($LASTEXITCODE -ne 0) { throw 'Developer tools compilation failed.' }
& "$env:JAVA_HOME\bin\java.exe" '-Djava.awt.headless=true' '-Dsun.net.http.allowRestrictedHeaders=true' -cp "$mcpClasses;$(Join-Path $mcpRoot 'Client\src')" unpackaged.DeveloperToolsChecks
if ($LASTEXITCODE -ne 0) { throw 'Developer tools checks failed.' }

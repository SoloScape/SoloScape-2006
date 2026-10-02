$ErrorActionPreference = 'Stop'
$examineRoot = Split-Path -Parent $PSScriptRoot
$examineServer = Join-Path $examineRoot 'Server'
$examineOutput = Join-Path $examineRoot 'qa-output\examine-checks'
$examineClasses = Join-Path $examineOutput 'classes'
New-Item -ItemType Directory -Force -Path $examineClasses | Out-Null
$examineSources = @(Get-ChildItem -LiteralPath (Join-Path $examineServer 'src\main\java') -Recurse -Filter '*.java')
$examineSources += Get-Item -LiteralPath (Join-Path $examineServer 'src\test\java\ExamineTextChecks.java')
$examineList = Join-Path $examineOutput 'sources.txt'
[System.IO.File]::WriteAllLines($examineList, [string[]]($examineSources | ForEach-Object { '"' + ($_.FullName -replace '\\', '/') + '"' }), [System.Text.UTF8Encoding]::new($false))
$examineLibraries = Join-Path $examineServer 'lib\*'
& javac -encoding UTF-8 -classpath $examineLibraries -d $examineClasses "@$examineList"
if ($LASTEXITCODE -ne 0) { throw 'Examine checks compilation failed.' }
Push-Location $examineServer
try {
    & java -cp "$examineClasses;$examineLibraries" ExamineTextChecks
    if ($LASTEXITCODE -ne 0) { throw 'Examine checks failed.' }
} finally { Pop-Location }

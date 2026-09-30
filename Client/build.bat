@echo off
setlocal EnableDelayedExpansion
cd /d "%~dp0"

where javac >nul 2>&1
if errorlevel 1 (
    echo Error: javac was not found. Install a JDK and add its bin directory to PATH.
    exit /b 1
)

if not exist "build\classes" mkdir "build\classes"

echo Collecting Java sources...
>"build\sources.txt" (
    for /r "src" %%F in (*.java) do (
        set "source=%%F"
        echo "!source:\=/!"
    )
)

echo Compiling client...
javac -encoding UTF-8 -d "build\classes" @"build\sources.txt"
if errorlevel 1 exit /b 1

echo Creating build\client.jar...
jar cfe "build\client.jar" unpackaged.Client -C "build\classes" .
if errorlevel 1 exit /b 1

echo Build complete: build\client.jar
exit /b 0

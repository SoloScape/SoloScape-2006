@echo off
setlocal EnableDelayedExpansion
set "JAVA_HOME=C:\Program Files\Java\jdk1.8.0_101"
set "PATH=%JAVA_HOME%\bin;%PATH%"
for %%T in (java javac jar) do (
    if not exist "%JAVA_HOME%\bin\%%T.exe" (
        echo ERROR: Required Java 8 JDK tool is missing: "%JAVA_HOME%\bin\%%T.exe"
        echo Install JDK 8 at "%JAVA_HOME%" before building or running SoloScape.
        exit /b 1
    )
)
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
"%JAVA_HOME%\bin\javac.exe" -encoding UTF-8 -d "build\classes" @"build\sources.txt"
if errorlevel 1 exit /b 1

echo Creating build\client.jar...
"%JAVA_HOME%\bin\jar.exe" cfe "build\client.jar" unpackaged.Client -C "build\classes" .
if errorlevel 1 exit /b 1

echo Build complete: build\client.jar
exit /b 0

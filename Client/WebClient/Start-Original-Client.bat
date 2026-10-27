@echo off
setlocal
rem Only one bridge may own the original runescape.exe URL on port 8081.
powershell.exe -NoProfile -Command "try { $reply=Invoke-WebRequest 'http://127.0.0.1:8081/' -UseBasicParsing -TimeoutSec 2; if ($reply.StatusCode -eq 200 -and $reply.Content -match '/windows-client.js') { exit 0 }; exit 1 } catch { exit 1 }" >nul 2>&1
if not errorlevel 1 (
    echo A SoloScape browser renderer is already running at http://127.0.0.1:8081/
    echo Close its launcher before starting Original Client mode, so it can stop on exit.
    exit /b 1
)
call "%~dp0..\..\Setup-Java-8.bat"
if errorlevel 1 exit /b 1
call "%~dp0Enable-Original-Client.bat"
if errorlevel 1 exit /b 1
cd /d "%~dp0.."
if not exist "build\client.jar" (
    call Build-Client.bat
    if errorlevel 1 exit /b 1
)
rem Keep Java attached to this console: closing it ends the renderer.
rem Closing the original EXE also ends Java after the last browser connection leaves.
echo Original client renderer active at http://127.0.0.1:8081/
echo Open runescape.exe. Close it or this console to stop the renderer and music.
"%JAVA_HOME%\bin\java.exe" -Dclient.background=true -Dweb.bridge.exitOnLastClient=true -Dweb.bridge.strictPort=true -Dweb.bridge.port=8081 -Dweb.bridge.root=WebClient/web -Dclient.cache=../Server/cache -Dclient.host=127.0.0.1 -Dclient.port=43594 -jar build/client.jar 1 local live highmem members english
exit /b %ERRORLEVEL%

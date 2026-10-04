@echo off
setlocal
powershell.exe -NoProfile -Command "try { $reply=Invoke-WebRequest 'http://127.0.0.1:8081/' -UseBasicParsing -TimeoutSec 2; if ($reply.StatusCode -eq 200 -and $reply.Content -match '/windows-client.js') { exit 0 }; exit 1 } catch { exit 1 }" >nul 2>&1
if not errorlevel 1 (
    echo SoloScape browser service is already running at http://127.0.0.1:8081/
    exit /b 0
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
rem Leave the rendering client running after the original EXE closes.
start "" /b "%JAVA_HOME%\bin\javaw.exe" -Dclient.background=true -Dweb.bridge.port=8081 -Dweb.bridge.root=WebClient/web -Dclient.cache=../Server/cache -Dclient.host=127.0.0.1 -Dclient.port=43594 -jar build/client.jar 1 local live highmem members english
echo SoloScape browser service started in the background at http://127.0.0.1:8081/
exit /b 0

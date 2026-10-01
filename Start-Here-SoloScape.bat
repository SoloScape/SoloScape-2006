@echo off
setlocal
echo Checking Java 8 - it will be installed automatically if needed...
call "%~dp0Setup-Java-8.bat"
if errorlevel 1 (
    echo Java 8 setup failed. See the error above, then try again.
    pause
    exit /b 1
)
cd /d "%~dp0"

rem Also accept commands directly, for example: Start-Here-SoloScape.bat build client
if not "%~1"=="" goto :direct

:menu
cls
echo SoloScape - Start Here
echo.
echo 1. First-time setup - build both, create play launchers, and close
echo 2. Prepare client files - rebuild the game window
echo 3. Prepare server files - rebuild the game world
echo 4. Play SoloScape - prepare files and start server + game
echo 5. Start developer mode - rebuild automatically when code changes
echo 6. Delete generated build files - keeps your saved players
echo 7. Download latest project changes from Git
echo 0. Exit
echo.
echo New player? Choose 1, then open Start-Server.bat and Play-Client.bat here.
echo.
choice /c 12345670 /n /m "Select an option: "
set "selection=%ERRORLEVEL%"
if "%selection%"=="8" exit /b 0
if "%selection%"=="7" set "command=update"
if "%selection%"=="6" set "command=clean"
if "%selection%"=="5" set "command=dev"
if "%selection%"=="4" set "command=run"
if "%selection%"=="3" set "command=build server"
if "%selection%"=="2" set "command=build client"
if "%selection%"=="1" set "command=build"
goto :execute

:execute
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0tools\scape.ps1" %command%
if errorlevel 1 goto :command_failed
if "%selection%"=="1" exit /b 0
pause
goto :menu

:command_failed
echo Command failed. See the error above.
pause
goto :menu

:direct
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0tools\scape.ps1" %*
exit /b %ERRORLEVEL%

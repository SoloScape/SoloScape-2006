@echo off
setlocal
call "%~dp0java8.bat"
if errorlevel 1 exit /b 1
cd /d "%~dp0"

rem Also accept commands directly, for example: scape.bat build client
if not "%~1"=="" goto :direct

:menu
cls
echo SoloScape
echo.
echo 1. Build both
echo 2. Build client
echo 3. Build server
echo 4. Build and launch both
echo 5. Launch both with live recompilation
echo 6. Clean generated build files
echo 7. Update from Git
echo 0. Exit
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
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0scape.ps1" %command%
if errorlevel 1 echo Command failed. See the error above.
pause
goto :menu

:direct
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0scape.ps1" %*
exit /b %ERRORLEVEL%

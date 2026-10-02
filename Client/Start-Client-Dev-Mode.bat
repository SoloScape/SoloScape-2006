@echo off
setlocal
call "%~dp0..\Setup-Java-8.bat"
if errorlevel 1 exit /b 1
cd /d "%~dp0"
rem Dev mode includes live HotSwap, the developer tools dashboard and MCP endpoint.
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0dev.ps1" %*
exit /b %ERRORLEVEL%

@echo off
setlocal
call "%~dp0..\java8.bat"
if errorlevel 1 exit /b 1
cd /d "%~dp0"
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0dev.ps1" %*
exit /b %ERRORLEVEL%

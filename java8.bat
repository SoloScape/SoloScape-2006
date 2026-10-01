@echo off
rem Set the caller's environment so PowerShell dev scripts inherit the same JDK.
set "SOLOSCAPE_JAVA8_HOME="
for /f "usebackq delims=" %%J in (`powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0java8.ps1"`) do set "SOLOSCAPE_JAVA8_HOME=%%J"
if not defined SOLOSCAPE_JAVA8_HOME exit /b 1
set "JAVA_HOME=%SOLOSCAPE_JAVA8_HOME%"
set "PATH=%JAVA_HOME%\bin;%PATH%"
set "SOLOSCAPE_JAVA8_HOME="
exit /b 0

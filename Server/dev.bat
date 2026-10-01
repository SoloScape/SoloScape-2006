@echo off
setlocal
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
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0dev.ps1" %*
exit /b %ERRORLEVEL%

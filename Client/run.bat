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

set "CLIENT_CACHE_ROOT=%~dp0..\Server\cache"

if not exist "%CLIENT_CACHE_ROOT%\main_file_cache.dat2" (
    echo Error: sibling server cache was not found at "%CLIENT_CACHE_ROOT%".
    echo Expected the Server\cache folder next to Client.
    exit /b 1
)

if not exist "%CLIENT_CACHE_ROOT%\map-fallback" (
    echo Error: map fallbacks were not found at "%CLIENT_CACHE_ROOT%\map-fallback".
    exit /b 1
)

if not exist "build\client.jar" (
    call build.bat
    if errorlevel 1 exit /b 1
)

set "CLIENT_LOG=client.log"
>"%CLIENT_LOG%" echo Client started at %DATE% %TIME%
echo Client output will be saved to %CLIENT_LOG%.

if "%~1"=="" (
    "%JAVA_HOME%\bin\java.exe" -XX:ErrorFile=client-hs-err.log -Dclient.cache="%CLIENT_CACHE_ROOT%" -Dclient.host=127.0.0.1 -Dclient.port=43594 -jar "build\client.jar" 1 local live highmem members english >>"%CLIENT_LOG%" 2>&1
) else (
    "%JAVA_HOME%\bin\java.exe" -XX:ErrorFile=client-hs-err.log -Dclient.cache="%CLIENT_CACHE_ROOT%" -Dclient.host=127.0.0.1 -Dclient.port=43594 -jar "build\client.jar" %* >>"%CLIENT_LOG%" 2>&1
)

set "EXIT_CODE=%ERRORLEVEL%"
echo Client exited with code %EXIT_CODE%.
if exist "%CLIENT_LOG%" type "%CLIENT_LOG%"
pause
exit /b %EXIT_CODE%

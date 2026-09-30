@echo off
setlocal
cd /d "%~dp0"

set "CLIENT_CACHE_ROOT=%~dp0..\06-server\cache"

if not exist "%CLIENT_CACHE_ROOT%\main_file_cache.dat2" (
    echo Error: sibling server cache was not found at "%CLIENT_CACHE_ROOT%".
    echo Expected 06-client and 06-server to be next to each other.
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
    java -XX:ErrorFile=client-hs-err.log -Dclient.cache="%CLIENT_CACHE_ROOT%" -Dclient.host=127.0.0.1 -Dclient.port=43594 -jar "build\client.jar" 1 local live highmem members english >>"%CLIENT_LOG%" 2>&1
) else (
    java -XX:ErrorFile=client-hs-err.log -Dclient.cache="%CLIENT_CACHE_ROOT%" -Dclient.host=127.0.0.1 -Dclient.port=43594 -jar "build\client.jar" %* >>"%CLIENT_LOG%" 2>&1
)

set "EXIT_CODE=%ERRORLEVEL%"
echo Client exited with code %EXIT_CODE%.
if exist "%CLIENT_LOG%" type "%CLIENT_LOG%"
pause
exit /b %EXIT_CODE%

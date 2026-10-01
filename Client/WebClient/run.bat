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

set "CLIENT_ROOT=%~dp0.."
set "SERVER_ROOT=%~dp0..\..\Server"
set "CLIENT_CACHE_ROOT=%SERVER_ROOT%\cache"
set "WEB_ROOT=%~dp0web"

if not exist "%CLIENT_ROOT%\build.bat" (
  echo Error: Client was not found at "%CLIENT_ROOT%".
  pause
  exit /b 1
)

if not exist "%CLIENT_CACHE_ROOT%\main_file_cache.dat2" (
  echo Error: server cache not found at "%CLIENT_CACHE_ROOT%".
  pause
  exit /b 1
)

echo Building the real Java client with phone bridge support...
call "%CLIENT_ROOT%\build.bat"
if errorlevel 1 (
  echo.
  echo Client build failed.
  pause
  exit /b 1
)

echo.
echo Starting the REAL 443 Java client.
echo Keep this window and the Java client window open while using your phone.
echo The phone page will print below when the bridge starts.
echo.

pushd "%CLIENT_ROOT%"
"%JAVA_HOME%\bin\java.exe" -XX:ErrorFile=client-web-hs-err.log -Dweb.bridge.port=8081 -Dweb.bridge.root="%WEB_ROOT%" -Dclient.cache="%CLIENT_CACHE_ROOT%" -Dclient.host=127.0.0.1 -Dclient.port=43594 -jar "build\client.jar" 1 local live highmem members english
set "EXIT_CODE=%ERRORLEVEL%"
popd

echo.
echo Web client stopped with code %EXIT_CODE%.
pause
exit /b %EXIT_CODE%

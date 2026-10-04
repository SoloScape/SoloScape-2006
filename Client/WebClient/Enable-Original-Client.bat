@echo off
rem Enable IE11 rendering for the original runescape.exe, for this Windows user.
reg add "HKCU\Software\Microsoft\Internet Explorer\Main\FeatureControl\FEATURE_BROWSER_EMULATION" /v runescape.exe /t REG_DWORD /d 11001 /f
if errorlevel 1 exit /b 1
echo Original RuneScape client browser mode enabled. Close and reopen runescape.exe.

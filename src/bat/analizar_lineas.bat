@echo off
setlocal enabledelayedexpansion
set "archivo=%~1"
set "contador=0"

for /f "usebackq delims=" %%a in ("%archivo%") do (
    set /a contador+=1
)

echo !contador!
endlocal
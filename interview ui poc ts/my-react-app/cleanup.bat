@echo off
REM Complete cleanup script for fixing the /100 cache issue
REM Run this in the my-react-app directory

echo.
echo ===============================================
echo  Cleaning up Vite cache and old files
echo ===============================================
echo.

REM Delete Vite cache
if exist "node_modules\.vite" (
    echo Removing Vite cache...
    rmdir /s /q "node_modules\.vite"
    echo Vite cache deleted
) else (
    echo Vite cache not found (already clean)
)

REM Delete dist folder (old builds)
if exist "dist" (
    echo Removing old build...
    rmdir /s /q "dist"
    echo Old build deleted
) else (
    echo No old build found
)

echo.
echo ===============================================
echo  Cleanup complete!
echo ===============================================
echo.
echo Next steps:
echo 1. Delete these files manually (optional but recommended):
echo    - src\index.jsx (duplicate entry point)
echo    - src\App.css (old styling)
echo    - src\styles\evaluation.css (old styling)
echo.
echo 2. Run: npm run dev
echo.
echo 3. Open http://localhost:5173 in your browser
echo.
echo 4. Hard refresh: Ctrl+Shift+R (Windows) or Cmd+Shift+R (Mac)
echo.
pause


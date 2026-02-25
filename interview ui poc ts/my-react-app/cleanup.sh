#!/bin/bash
# Complete cleanup script for fixing the /100 cache issue
# Run this in the my-react-app directory

echo ""
echo "==============================================="
echo "  Cleaning up Vite cache and old files"
echo "==============================================="
echo ""

# Delete Vite cache
if [ -d "node_modules/.vite" ]; then
    echo "Removing Vite cache..."
    rm -rf "node_modules/.vite"
    echo "Vite cache deleted"
else
    echo "Vite cache not found (already clean)"
fi

# Delete dist folder (old builds)
if [ -d "dist" ]; then
    echo "Removing old build..."
    rm -rf "dist"
    echo "Old build deleted"
else
    echo "No old build found"
fi

echo ""
echo "==============================================="
echo "  Cleanup complete!"
echo "==============================================="
echo ""
echo "Next steps:"
echo "1. Delete these files manually (optional but recommended):"
echo "   - src/index.jsx (duplicate entry point)"
echo "   - src/App.css (old styling)"
echo "   - src/styles/evaluation.css (old styling)"
echo ""
echo "2. Run: npm run dev"
echo ""
echo "3. Open http://localhost:5173 in your browser"
echo ""
echo "4. Hard refresh: Cmd+Shift+R (Mac) or Ctrl+Shift+R (Windows)"
echo ""


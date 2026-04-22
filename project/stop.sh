#!/bin/bash

# ============================================
# TaskSketch — STOP SCRIPT
# Shuts down Tomcat. MySQL keeps running.
# ============================================

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
BOLD='\033[1m'
NC='\033[0m'

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
TOMCAT_DIR="$SCRIPT_DIR/.tomcat"

echo ""
echo -e "${CYAN}${BOLD}📝 TaskSketch — Stopping...${NC}"
echo ""

# Stop Tomcat
if [ -f "$TOMCAT_DIR/bin/shutdown.sh" ]; then
    "$TOMCAT_DIR/bin/shutdown.sh" 2>/dev/null
    echo -e "  ${GREEN}✓ Tomcat shutdown initiated.${NC}"
else
    echo -e "  ${YELLOW}⚠ Tomcat directory not found.${NC}"
fi

# Kill any remaining process on port 8080
sleep 2
PID_ON_8080=$(lsof -ti:8080 2>/dev/null)
if [ -n "$PID_ON_8080" ]; then
    kill -9 $PID_ON_8080 2>/dev/null
    echo -e "  ${GREEN}✓ Killed remaining process on port 8080.${NC}"
fi

echo ""
echo -e "  ${GREEN}✓ TaskSketch stopped.${NC}"
echo -e "  ${YELLOW}Note: MySQL is still running (use 'brew services stop mysql' to stop it).${NC}"
echo -e "  ${YELLOW}To restart the app: ./run.sh${NC}"
echo ""

#!/bin/bash

# ============================================
# TaskSketch — RUN SCRIPT
# Builds the project, deploys to Tomcat, and
# opens the app in your browser.
# Run this every time you want to start the app.
# ============================================

# --- Colors ---
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
BOLD='\033[1m'
NC='\033[0m'

# --- Paths ---
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
TOMCAT_DIR="$SCRIPT_DIR/.tomcat"
WAR_FILE="$SCRIPT_DIR/target/tasksketch.war"

# Load environment variables
if [ -f "$SCRIPT_DIR/.env" ]; then
    set -a
    source "$SCRIPT_DIR/.env"
    set +a
fi

DB_USER=${DB_USER:-"tasksketch"}
if [ -z "$DB_PASSWORD" ]; then
    echo -e "${RED}Error: DB_PASSWORD environment variable is not set.${NC}"
    echo -e "${YELLOW}Please create a .env file with DB_USER and DB_PASSWORD or export them before running.${NC}"
    exit 1
fi

echo ""
echo -e "${CYAN}${BOLD}╔══════════════════════════════════════════╗${NC}"
echo -e "${CYAN}${BOLD}║     📝 TaskSketch — Starting App         ║${NC}"
echo -e "${CYAN}${BOLD}╚══════════════════════════════════════════╝${NC}"
echo ""

# ═══════════════════════════════════════════
# Pre-flight checks
# ═══════════════════════════════════════════
echo -e "${BLUE}${BOLD}[0/5] Checking prerequisites...${NC}"

# Check Tomcat exists
if [ ! -f "$TOMCAT_DIR/bin/catalina.sh" ]; then
    echo -e "  ${RED}✗ Tomcat not found at .tomcat/${NC}"
    echo -e "  ${YELLOW}Run ./setup.sh first!${NC}"
    exit 1
fi
echo -e "  ${GREEN}✓ Tomcat found${NC}"

# Check Maven
if ! command -v mvn &>/dev/null; then
    echo -e "  ${RED}✗ Maven not found.${NC}"
    echo -e "  ${YELLOW}Run ./setup.sh first!${NC}"
    exit 1
fi
echo -e "  ${GREEN}✓ Maven found${NC}"

# Check MySQL
if ! command -v mysql &>/dev/null; then
    echo -e "  ${RED}✗ MySQL not found.${NC}"
    echo -e "  ${YELLOW}Run ./setup.sh first!${NC}"
    exit 1
fi
echo -e "  ${GREEN}✓ MySQL found${NC}"

# Set JAVA_HOME if not set (macOS specific)
if [ -z "$JAVA_HOME" ] && [ -x /usr/libexec/java_home ]; then
    export JAVA_HOME=$(/usr/libexec/java_home 2>/dev/null)
fi
echo ""

# ═══════════════════════════════════════════
# STEP 1: Ensure MySQL is running
# ═══════════════════════════════════════════
echo -e "${BLUE}${BOLD}[1/5] 🐬 Ensuring MySQL is running...${NC}"
brew services start mysql 2>/dev/null

# Wait for MySQL to actually be connectable
RETRIES=10
while [ $RETRIES -gt 0 ]; do
    if mysql -u "${DB_USER}" -p"${DB_PASSWORD}" -e "SELECT 1;" &>/dev/null; then
        echo -e "  ${GREEN}✓ MySQL is running and accessible.${NC}"
        break
    fi
    RETRIES=$((RETRIES - 1))
    if [ $RETRIES -eq 0 ]; then
        echo -e "  ${RED}✗ Cannot connect to MySQL.${NC}"
        echo -e "  ${YELLOW}Try: brew services restart mysql${NC}"
        exit 1
    fi
    sleep 1
done

# Ensure database and tables exist (idempotent)
mysql -u root -e "CREATE DATABASE IF NOT EXISTS tasksketch_db;" 2>/dev/null
mysql -u "${DB_USER}" -p"${DB_PASSWORD}" tasksketch_db < "$SCRIPT_DIR/sql/schema.sql" 2>/dev/null
echo -e "  ${GREEN}✓ Database ready.${NC}"
echo ""

# ═══════════════════════════════════════════
# STEP 2: Build the project with Maven
# ═══════════════════════════════════════════
echo -e "${BLUE}${BOLD}[2/5] 🔨 Building project with Maven...${NC}"
cd "$SCRIPT_DIR"
mvn clean package -q 2>&1 | tail -5

if [ -f "$WAR_FILE" ]; then
    WAR_SIZE=$(du -h "$WAR_FILE" | awk '{print $1}')
    echo -e "  ${GREEN}✓ Build successful! (tasksketch.war — ${WAR_SIZE})${NC}"
else
    echo -e "  ${RED}✗ Build failed!${NC}"
    echo -e "  ${YELLOW}Run 'mvn clean package' to see full error output.${NC}"
    exit 1
fi
echo ""

# ═══════════════════════════════════════════
# STEP 3: Stop existing Tomcat (if running)
# ═══════════════════════════════════════════
echo -e "${BLUE}${BOLD}[3/5] 🛑 Stopping old Tomcat (if running)...${NC}"
"$TOMCAT_DIR/bin/shutdown.sh" 2>/dev/null
sleep 2

# Also kill any process on port 8080 (just in case)
PID_ON_8080=$(lsof -ti:8080 2>/dev/null)
if [ -n "$PID_ON_8080" ]; then
    echo -e "  ${YELLOW}→ Killing process on port 8080 (PID: $PID_ON_8080)${NC}"
    kill -9 $PID_ON_8080 2>/dev/null
    sleep 1
fi
echo -e "  ${GREEN}✓ Port 8080 is free.${NC}"
echo ""

# ═══════════════════════════════════════════
# STEP 4: Deploy WAR to Tomcat
# ═══════════════════════════════════════════
echo -e "${BLUE}${BOLD}[4/5] 🚀 Deploying to Tomcat...${NC}"

# Remove old deployment
rm -rf "$TOMCAT_DIR/webapps/tasksketch" 2>/dev/null
rm -f "$TOMCAT_DIR/webapps/tasksketch.war" 2>/dev/null

# Copy fresh WAR
cp "$WAR_FILE" "$TOMCAT_DIR/webapps/"
echo -e "  ${GREEN}✓ WAR deployed to Tomcat webapps.${NC}"
echo ""

# ═══════════════════════════════════════════
# STEP 5: Start Tomcat
# ═══════════════════════════════════════════
echo -e "${BLUE}${BOLD}[5/5] 🐱 Starting Tomcat...${NC}"

# Set JVM options for Java 17+ compatibility
export CATALINA_OPTS="--add-opens java.base/java.lang=ALL-UNNAMED \
--add-opens java.base/java.io=ALL-UNNAMED \
--add-opens java.base/java.util=ALL-UNNAMED \
--add-opens java.base/java.util.concurrent=ALL-UNNAMED \
--add-opens java.rmi/sun.rmi.transport=ALL-UNNAMED"

"$TOMCAT_DIR/bin/startup.sh" >/dev/null 2>&1

# Wait for Tomcat to be ready
echo -e "  ${YELLOW}→ Waiting for Tomcat to start...${NC}"
RETRIES=30
while [ $RETRIES -gt 0 ]; do
    # Check if the app responds
    if curl -s -o /dev/null -w "%{http_code}" "http://localhost:8080/tasksketch/login" 2>/dev/null | grep -qE "200|302"; then
        break
    fi
    RETRIES=$((RETRIES - 1))
    sleep 1
    printf "."
done
echo ""

if [ $RETRIES -eq 0 ]; then
    echo -e "  ${YELLOW}⚠ Tomcat may still be starting up. Check in a few seconds.${NC}"
else
    echo -e "  ${GREEN}✓ Tomcat is running!${NC}"
fi
echo ""

# ═══════════════════════════════════════════
# All done! Print information
# ═══════════════════════════════════════════
echo -e "${GREEN}${BOLD}╔══════════════════════════════════════════════════╗${NC}"
echo -e "${GREEN}${BOLD}║          ✅ TaskSketch is LIVE!                  ║${NC}"
echo -e "${GREEN}${BOLD}╚══════════════════════════════════════════════════╝${NC}"
echo ""
echo -e "  ${BOLD}🌐 App URL:${NC}"
echo -e "     ${CYAN}${BOLD}http://localhost:8080/tasksketch/${NC}"
echo ""
echo -e "  ${BOLD}🔑 Demo Account:${NC}"
echo -e "     Email:    ${CYAN}demo@example.com${NC}"
echo -e "     Password: ${CYAN}demo123${NC}"
echo ""
echo -e "  ${BOLD}🐬 Database Check Commands:${NC}"
echo -e "     ${YELLOW}View all users:${NC}"
echo -e "     mysql -u ${DB_USER} -p -e 'USE tasksketch_db; SELECT id, name, email FROM users;'"
echo ""
echo -e "     ${YELLOW}View all tasks:${NC}"
echo -e "     mysql -u ${DB_USER} -p -e 'USE tasksketch_db; SELECT t.id, u.name AS user_name, t.title, t.status, t.created_at FROM tasks t JOIN users u ON t.user_id = u.id;'"
echo ""
echo -e "  ${BOLD}🛑 To stop:${NC}  ./stop.sh"
echo ""

# Open in browser
echo -e "  ${YELLOW}→ Opening in browser...${NC}"
sleep 1
open "http://localhost:8080/tasksketch/" 2>/dev/null

echo -e "  ${GREEN}Done! Enjoy TaskSketch! ✎${NC}"
echo ""

#!/bin/bash

# ============================================
# TaskSketch — ONE-TIME SETUP SCRIPT
# Installs: Homebrew, Maven, MySQL, Tomcat 9
# Run this ONCE, then use ./run.sh to start
# ============================================

# --- Colors for pretty output ---
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
BOLD='\033[1m'
NC='\033[0m' # No Color

# --- Get the directory where this script lives ---
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
TOMCAT_DIR="$SCRIPT_DIR/.tomcat"

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
echo -e "${CYAN}${BOLD}║     📝 TaskSketch — Setup Script         ║${NC}"
echo -e "${CYAN}${BOLD}║     Installing all dependencies...       ║${NC}"
echo -e "${CYAN}${BOLD}╚══════════════════════════════════════════╝${NC}"
echo ""

# Track what we installed
INSTALLED=""

# ═══════════════════════════════════════════
# STEP 1: Check Java
# ═══════════════════════════════════════════
echo -e "${BLUE}${BOLD}[1/5] ☕ Checking Java...${NC}"
if command -v java &>/dev/null; then
    JAVA_VER=$(java -version 2>&1 | head -1)
    echo -e "  ${GREEN}✓ Found: $JAVA_VER${NC}"

    # Set JAVA_HOME if not set
    if [ -z "$JAVA_HOME" ]; then
        if [ -x /usr/libexec/java_home ]; then
            export JAVA_HOME=$(/usr/libexec/java_home 2>/dev/null)
            echo -e "  ${GREEN}✓ JAVA_HOME set to: $JAVA_HOME${NC}"
        fi
    fi
else
    echo -e "  ${RED}✗ Java not found!${NC}"
    echo -e "  ${YELLOW}Please install JDK 17+ from https://jdk.java.net${NC}"
    exit 1
fi
echo ""

# ═══════════════════════════════════════════
# STEP 2: Check / Install Homebrew
# ═══════════════════════════════════════════
echo -e "${BLUE}${BOLD}[2/5] 🍺 Checking Homebrew...${NC}"
if command -v brew &>/dev/null; then
    echo -e "  ${GREEN}✓ Homebrew is already installed.${NC}"
else
    echo -e "  ${YELLOW}→ Homebrew not found. Installing...${NC}"
    echo -e "  ${YELLOW}  (This may ask for your Mac password)${NC}"
    /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

    # Add brew to PATH for Apple Silicon Macs
    if [ -f /opt/homebrew/bin/brew ]; then
        eval "$(/opt/homebrew/bin/brew shellenv)"
    fi

    if command -v brew &>/dev/null; then
        echo -e "  ${GREEN}✓ Homebrew installed successfully!${NC}"
        INSTALLED="$INSTALLED Homebrew"
    else
        echo -e "  ${RED}✗ Homebrew installation failed.${NC}"
        echo -e "  ${YELLOW}Visit https://brew.sh for manual install.${NC}"
        exit 1
    fi
fi
echo ""

# ═══════════════════════════════════════════
# STEP 3: Check / Install Maven
# ═══════════════════════════════════════════
echo -e "${BLUE}${BOLD}[3/5] 📦 Checking Maven...${NC}"
if command -v mvn &>/dev/null; then
    MVN_VER=$(mvn -version 2>&1 | head -1)
    echo -e "  ${GREEN}✓ Found: $MVN_VER${NC}"
else
    echo -e "  ${YELLOW}→ Maven not found. Installing via Homebrew...${NC}"
    brew install maven
    if command -v mvn &>/dev/null; then
        echo -e "  ${GREEN}✓ Maven installed successfully!${NC}"
        INSTALLED="$INSTALLED Maven"
    else
        echo -e "  ${RED}✗ Maven installation failed.${NC}"
        exit 1
    fi
fi
echo ""

# ═══════════════════════════════════════════
# STEP 4: Check / Install MySQL
# ═══════════════════════════════════════════
echo -e "${BLUE}${BOLD}[4/5] 🐬 Checking MySQL...${NC}"
if command -v mysql &>/dev/null; then
    MYSQL_VER=$(mysql --version 2>&1)
    echo -e "  ${GREEN}✓ Found: $MYSQL_VER${NC}"
else
    echo -e "  ${YELLOW}→ MySQL not found. Installing via Homebrew...${NC}"
    echo -e "  ${YELLOW}  (This may take a few minutes)${NC}"
    brew install mysql
    if command -v mysql &>/dev/null; then
        echo -e "  ${GREEN}✓ MySQL installed successfully!${NC}"
        INSTALLED="$INSTALLED MySQL"
    else
        echo -e "  ${RED}✗ MySQL installation failed.${NC}"
        exit 1
    fi
fi

# Start MySQL service
echo -e "  ${YELLOW}→ Starting MySQL service...${NC}"
brew services start mysql 2>/dev/null
sleep 3

# Verify MySQL is running
if mysql -u root -p -e "SELECT 1;" &>/dev/null; then
    echo -e "  ${GREEN}✓ MySQL is running.${NC}"
else
    # MySQL might need a moment to start
    echo -e "  ${YELLOW}→ Waiting for MySQL to start...${NC}"
    sleep 5
    if mysql -u root -e "SELECT 1;" &>/dev/null; then
        echo -e "  ${GREEN}✓ MySQL is running.${NC}"
    else
        echo -e "  ${RED}✗ Cannot connect to MySQL.${NC}"
        echo -e "  ${YELLOW}If MySQL has a root password, run:${NC}"
        echo -e "  ${YELLOW}  mysql -u root -p -e \"CREATE DATABASE IF NOT EXISTS tasksketch_db;\"${NC}"
        echo -e "  ${YELLOW}Then re-run this script.${NC}"
        exit 1
    fi
fi

# Setup the database, user, and tables
echo -e "  ${YELLOW}→ Setting up database and app user...${NC}"
mysql -u root -p <<EOSQL
-- Create the database
CREATE DATABASE IF NOT EXISTS tasksketch_db;

-- Create a dedicated app user (safe, no root dependency)
CREATE USER IF NOT EXISTS '${DB_USER}'@'localhost' IDENTIFIED BY '${DB_PASSWORD}';
GRANT ALL PRIVILEGES ON tasksketch_db.* TO '${DB_USER}'@'localhost';
FLUSH PRIVILEGES;
EOSQL

if [ $? -eq 0 ]; then
    echo -e "  ${GREEN}✓ Database 'tasksketch_db' created.${NC}"
    echo -e "  ${GREEN}✓ User '${DB_USER}' created.${NC}"
else
    echo -e "  ${RED}✗ Database setup failed.${NC}"
    exit 1
fi

# Load schema and sample data
mysql -u "${DB_USER}" -p"${DB_PASSWORD}" tasksketch_db < "$SCRIPT_DIR/sql/schema.sql" 2>/dev/null
if [ $? -eq 0 ]; then
    echo -e "  ${GREEN}✓ Tables created and sample data loaded.${NC}"
else
    echo -e "  ${RED}✗ Failed to load schema. Check sql/schema.sql${NC}"
    exit 1
fi
echo ""

# ═══════════════════════════════════════════
# STEP 5: Download Apache Tomcat 9
# ═══════════════════════════════════════════
echo -e "${BLUE}${BOLD}[5/5] 🐱 Setting up Apache Tomcat 9...${NC}"

if [ -d "$TOMCAT_DIR" ] && [ -f "$TOMCAT_DIR/bin/catalina.sh" ]; then
    echo -e "  ${GREEN}✓ Tomcat is already set up at .tomcat/${NC}"
else
    # Try to auto-detect latest Tomcat 9 version from Apache CDN
    echo -e "  ${YELLOW}→ Finding latest Tomcat 9 version...${NC}"
    TOMCAT_VERSION=""

    # Method 1: Parse the Apache download directory listing
    LATEST=$(curl -s --max-time 10 "https://dlcdn.apache.org/tomcat/tomcat-9/" 2>/dev/null \
        | grep -oE 'v9\.[0-9]+\.[0-9]+' \
        | sort -t. -k1,1n -k2,2n -k3,3n \
        | tail -1 \
        | sed 's/^v//')

    if [ -n "$LATEST" ]; then
        TOMCAT_VERSION="$LATEST"
        echo -e "  ${GREEN}  Found latest: $TOMCAT_VERSION${NC}"
    fi

    # Fallback versions to try if auto-detect fails
    VERSIONS_TO_TRY="${TOMCAT_VERSION} 9.0.102 9.0.100 9.0.98 9.0.97 9.0.96 9.0.93"
    DOWNLOADED=false

    for VER in $VERSIONS_TO_TRY; do
        [ -z "$VER" ] && continue

        # Try the CDN first, then the archive
        for BASE_URL in \
            "https://dlcdn.apache.org/tomcat/tomcat-9/v${VER}/bin" \
            "https://archive.apache.org/dist/tomcat/tomcat-9/v${VER}/bin"; do

            FILE_URL="${BASE_URL}/apache-tomcat-${VER}.tar.gz"
            echo -e "  ${YELLOW}→ Trying Tomcat ${VER}...${NC}"

            # Download with curl (follow redirects, fail silently on error)
            if curl -fSL --max-time 60 "$FILE_URL" -o "/tmp/tomcat-${VER}.tar.gz" 2>/dev/null; then
                # Verify it's actually a tar.gz (not an HTML error page)
                if file "/tmp/tomcat-${VER}.tar.gz" | grep -q "gzip"; then
                    echo -e "  ${GREEN}✓ Downloaded Tomcat ${VER}${NC}"

                    # Extract to .tomcat directory
                    mkdir -p "$TOMCAT_DIR"
                    tar -xzf "/tmp/tomcat-${VER}.tar.gz" -C "$TOMCAT_DIR" --strip-components=1
                    rm -f "/tmp/tomcat-${VER}.tar.gz"

                    # Make scripts executable
                    chmod +x "$TOMCAT_DIR/bin/"*.sh

                    DOWNLOADED=true
                    break 2
                else
                    rm -f "/tmp/tomcat-${VER}.tar.gz"
                fi
            fi
        done
    done

    if [ "$DOWNLOADED" = false ]; then
        echo -e "  ${RED}✗ Could not download Tomcat 9 automatically.${NC}"
        echo -e "  ${YELLOW}Please download manually from:${NC}"
        echo -e "  ${YELLOW}  https://tomcat.apache.org/download-90.cgi${NC}"
        echo -e "  ${YELLOW}Extract into: $TOMCAT_DIR${NC}"
        exit 1
    fi
fi
echo ""

# ═══════════════════════════════════════════
# DONE!
# ═══════════════════════════════════════════
echo -e "${GREEN}${BOLD}╔══════════════════════════════════════════╗${NC}"
echo -e "${GREEN}${BOLD}║     ✅ Setup Complete!                   ║${NC}"
echo -e "${GREEN}${BOLD}╚══════════════════════════════════════════╝${NC}"
echo ""

if [ -n "$INSTALLED" ]; then
    echo -e "  ${CYAN}Newly installed:${INSTALLED}${NC}"
fi

echo -e "  ${CYAN}Database:${NC}    tasksketch_db (MySQL)"
echo -e "  ${CYAN}DB User:${NC}     ${DB_USER} / <hidden>"
echo -e "  ${CYAN}Tomcat:${NC}      $TOMCAT_DIR"
echo ""
echo -e "  ${BOLD}Now run this to start the app:${NC}"
echo -e "  ${GREEN}${BOLD}  ./run.sh${NC}"
echo ""

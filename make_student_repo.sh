#!/bin/bash

# Creates the student template repo

if [ -z "$1" ] || [ -z "$2" ]; then
  echo "Usage: $0 <org> <leaderboard-repo>"
  echo "Example: $0 my-github-org hash-contest-leaderboard"
  exit 1
fi

ORG="$1"
LEADERBOARD_REPO="$2"
OUTPUT_DIR="student-repo"

rm -rf "$OUTPUT_DIR"
mkdir -p "$OUTPUT_DIR/.github/workflows"

# Copy MyString.java
cp src/main/java/MyString.java "$OUTPUT_DIR/"

# Create submit.yml with org and repo substituted
sed -e "s/your-org/$ORG/g" -e "s/hash-contest-leaderboard/$LEADERBOARD_REPO/g" \
  github-classroom-files/submit.yml > "$OUTPUT_DIR/.github/workflows/submit.yml"

echo "Created student repo in $OUTPUT_DIR/"
echo ""
echo "Contents:"
find "$OUTPUT_DIR" -type f | sort
#!/bin/bash

# Creates the leaderboard repo

if [ -z "$1" ] || [ -z "$2" ]; then
  echo "Usage: $0 <org> <leaderboard-repo>"
  echo "Example: $0 my-github-org hash-contest-leaderboard"
  exit 1
fi

ORG="$1"
LEADERBOARD_REPO="$2"
OUTPUT_DIR="leaderboard-repo"

rm -rf "$OUTPUT_DIR"
mkdir -p "$OUTPUT_DIR/.github/workflows"
mkdir -p "$OUTPUT_DIR/src"
mkdir -p "$OUTPUT_DIR/docs"

# Copy Java source files
cp src/main/java/AbstractController.java "$OUTPUT_DIR/src/"
cp src/main/java/GitHubController.java "$OUTPUT_DIR/src/"
cp src/main/java/HashSet.java "$OUTPUT_DIR/src/"

# Copy corpus
cp src/main/resources/corpus.txt "$OUTPUT_DIR/"

# Copy update-leaderboard.yml
cp github-classroom-files/update-leaderboard.yml "$OUTPUT_DIR/.github/workflows/"

# Create index.html
cp github-classroom-files/index.html "$OUTPUT_DIR/docs/"

# Generate leaderboard.json from sample submissions
echo "Generating leaderboard.json from sample submissions..."
TIMESTAMP=$(date -u +%Y-%m-%dT%H:%M:%SZ)

cd "$OUTPUT_DIR"

echo "[" > docs/leaderboard.json
FIRST=true

for dir in ../sample-submissions/*/; do
  name=$(basename "$dir")

  cp "$dir/MyString.java" src/

  mkdir -p out
  javac -d out src/*.java 2>/dev/null
  cp corpus.txt out/

  cd out
  OUTPUT=$(java GitHubController 2>&1)
  cd ..

  COLLISIONS=$(echo "$OUTPUT" | grep "^COLLISIONS:" | cut -d' ' -f2)
  PSEUDO=$(echo "$OUTPUT" | grep "^PSEUDONYM:" | cut -d' ' -f2-)

  if [ -n "$COLLISIONS" ]; then
    if [ "$FIRST" = true ]; then
      FIRST=false
    else
      echo "," >> docs/leaderboard.json
    fi
    echo -n "  {\"pseudonym\": \"$PSEUDO\", \"collisions\": $COLLISIONS, \"timestamp\": \"$TIMESTAMP\"}" >> docs/leaderboard.json
    echo "  $PSEUDO: $COLLISIONS collisions"
  else
    echo "  $name: failed to evaluate"
  fi
done

echo "" >> docs/leaderboard.json
echo "]" >> docs/leaderboard.json

rm -rf out src/MyString.java
cd ..

# Create README
cat > "$OUTPUT_DIR/README.md" << OUTER
# Hash Function Contest Leaderboard

This repository receives student hash function results and maintains a leaderboard
visible at: https://$ORG.github.io/$LEADERBOARD_REPO/
OUTER

echo ""
echo "Created leaderboard repo in $OUTPUT_DIR/"
echo ""
echo "Contents:"
find "$OUTPUT_DIR" -type f | sort

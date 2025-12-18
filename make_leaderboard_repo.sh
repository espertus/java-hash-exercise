
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

# Create update-leaderboard
cp github-classroom-files/update-leaderboard.yml "$OUTPUT_DIR/.github/workflows/"

# Create index.html with org and repo substituted
sed -e "s/your-org/$ORG/g" -e "s/hash-contest-leaderboard/$LEADERBOARD_REPO/g" \
  github-classroom-files/index.html > "$OUTPUT_DIR/docs/index.html"

# Generate leaderboard.json from sample submissions
echo "Generating leaderboard.json from sample submissions..."
TIMESTAMP=$(date -u +%Y-%m-%dT%H:%M:%SZ)

cd "$OUTPUT_DIR"

echo "[" > leaderboard.json
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
  PSEUDO=$(echo "$OUTPUT" | grep "^PSEUDONYM:" | cut -d' ' -f2)

  if [ -n "$COLLISIONS" ]; then
    if [ "$FIRST" = true ]; then
      FIRST=false
    else
      echo "," >> leaderboard.json
    fi
    echo -n "  {\"pseudonym\": \"$PSEUDO\", \"collisions\": $COLLISIONS, \"timestamp\": \"$TIMESTAMP\"}" >> leaderboard.json
    echo "  $PSEUDO: $COLLISIONS collisions"
  else
    echo "  $name: failed to evaluate"
  fi
done

echo "" >> leaderboard.json
echo "]" >> leaderboard.json

rm -rf out src/MyString.java
cd ..

# Create README
cat > "$OUTPUT_DIR/README.md" << EOF
# Hash Function Contest Leaderboard

This repository evaluates student hash function submissions and maintains the leaderboard.

## Setup

1. Upgrade this repo's organization to GitHub Team.
   1. Navigate to [https://education.github.com/](https://education.github.com/).
   2. Click on **Upgrade to GitHub Team**.
   3. Click on **Upgrade** next to the name of the organization.

2. Enable GitHub Pages within the leaderboard repo.
   1. Click **Settings**.
   2. In the left sidebar, click **Pages**.
   3. Under **Source**, select \`Deploy from a branch\`.
   4. Under **Branch**, select your main branch and then select \`/docs\` from the folder dropdown.
   5. Click **Save**.

3. Generate the leaderboard token.
   1. Click on your profile picture, then **Settings**.
   2. In the left sidebar, click **Developer settings**.
   3. Expand **Personal access tokens**.
   4. Click on **Fine-grained tokens**.
   5. Fill in these values:
     - **Token name:** \`Hash Contest Leaderboard\`
     - **Description:** you can leave this blank
     - **Resource owner:** your organization
     - **Expiration:** the day after the scheduled activity or the end of the semester
     - **Repository access: Only select repositories** your leaderboard repo
     - **Permissions**
        1. Click on **Add Permissions**, which will open a popup.
        2. Check **Contents**, then close the popup.
        3. Within the new **Contents** section, select \`Access: Read and write\`.
        4. There will also be a **Metadata** section, which you should leave as is.
   6. Click **Generate token**.
   7. Copy the token, which you will use in place of \`<token>\` in the next step.

4. Run \`make_student_autograder.sh $ORG $LEADERBOARD_REPO <token>\` to generate the autograder workflow.

5. In GitHub Classroom, create an assignment:
   - Use a template repo containing only \`MyString.java\`.
   - Under **Grading and feedback**, select **Custom YAML**.
   - Paste in the contents of \`student-autograder.yml\`.

## How it works

1. Student pushes \`MyString.java\` to their private repo.
2. GitHub Classroom runs the autograder, which compiles and evaluates locally.
3. Results are shown in the student's Actions tab.
4. The autograder triggers this repo to update the leaderboard.

## Leaderboard

View at: https://$ORG.github.io/$LEADERBOARD_REPO/
EOF

echo ""
echo "Created leaderboard repo in $OUTPUT_DIR/"
echo ""
echo "Contents:"
find "$OUTPUT_DIR" -type f | sort

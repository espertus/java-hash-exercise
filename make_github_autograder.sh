#!/bin/bash

# Creates the autograder workflow for GitHub Classroom

if [ -z "$1" ] || [ -z "$2" ] || [ -z "$3" ]; then
  echo "Usage: $0 <org> <leaderboard-repo> <leaderboard-token>"
  echo "Example: $0 my-github-org hash-contest-leaderboard ghp_xxxxxxxxxxxx"
  exit 1
fi

ORG="$1"
LEADERBOARD_REPO="$2"
LEADERBOARD_TOKEN="$3"

cat > student-autograder.yml << EOF
name: Evaluate Hash Function

on:
  push:
    paths:
      - 'MyString.java'
  workflow_dispatch:

env:
  ORG: $ORG
  LEADERBOARD_REPO: $LEADERBOARD_REPO
  LEADERBOARD_TOKEN: $LEADERBOARD_TOKEN

jobs:
  evaluate:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Download evaluation code
        run: |
          curl -sfL "https://raw.githubusercontent.com/\${{ env.ORG }}/\${{ env.LEADERBOARD_REPO }}/main/src/AbstractController.java" -o AbstractController.java
          curl -sfL "https://raw.githubusercontent.com/\${{ env.ORG }}/\${{ env.LEADERBOARD_REPO }}/main/src/GitHubController.java" -o GitHubController.java
          curl -sfL "https://raw.githubusercontent.com/\${{ env.ORG }}/\${{ env.LEADERBOARD_REPO }}/main/src/HashSet.java" -o HashSet.java
          curl -sfL "https://raw.githubusercontent.com/\${{ env.ORG }}/\${{ env.LEADERBOARD_REPO }}/main/corpus.txt" -o corpus.txt

      - name: Compile
        id: compile
        run: |
          mkdir -p out
          if ! javac -d out *.java 2>err.txt; then
            echo "## ❌ Compilation Failed" >> \$GITHUB_STEP_SUMMARY
            echo '\`\`\`' >> \$GITHUB_STEP_SUMMARY
            cat err.txt >> \$GITHUB_STEP_SUMMARY
            echo '\`\`\`' >> \$GITHUB_STEP_SUMMARY
            exit 1
          fi

      - name: Run evaluation
        id: eval
        run: |
          cp corpus.txt out/
          cd out

          if ! OUTPUT=\$(timeout 30s java GitHubController 2>&1); then
            echo "## ❌ Evaluation Failed" >> \$GITHUB_STEP_SUMMARY
            echo '\`\`\`' >> \$GITHUB_STEP_SUMMARY
            echo "\$OUTPUT" >> \$GITHUB_STEP_SUMMARY
            echo '\`\`\`' >> \$GITHUB_STEP_SUMMARY
            exit 1
          fi

          PSEUDO=\$(echo "\$OUTPUT" | grep "^PSEUDONYM:" | cut -d' ' -f2-)
          COLLISIONS=\$(echo "\$OUTPUT" | grep "^COLLISIONS:" | cut -d' ' -f2)

          echo "pseudonym=\$PSEUDO" >> \$GITHUB_OUTPUT
          echo "collisions=\$COLLISIONS" >> \$GITHUB_OUTPUT

          echo "## ✅ Results" >> \$GITHUB_STEP_SUMMARY
          echo "**Pseudonym:** \$PSEUDO" >> \$GITHUB_STEP_SUMMARY
          echo "**Collisions:** \$COLLISIONS" >> \$GITHUB_STEP_SUMMARY
          echo "" >> \$GITHUB_STEP_SUMMARY
          echo "[View Leaderboard](https://\${{ env.ORG }}.github.io/\${{ env.LEADERBOARD_REPO }}/)" >> \$GITHUB_STEP_SUMMARY

      - name: Update leaderboard
        if: steps.eval.outputs.collisions
        run: |
          curl -X POST \\
            -H "Accept: application/vnd.github+json" \\
            -H "Authorization: Bearer \${{ env.LEADERBOARD_TOKEN }}" \\
            "https://api.github.com/repos/\${{ env.ORG }}/\${{ env.LEADERBOARD_REPO }}/dispatches" \\
            -d '{
              "event_type": "submission",
              "client_payload": {
                "pseudonym": "\${{ steps.eval.outputs.pseudonym }}",
                "collisions": \${{ steps.eval.outputs.collisions }}
              }
            }'
EOF

echo "Created student-autograder.yml"
echo "When creating the GitHub Classroom assignment,"
echo "paste it as a Custom YAML autograder."



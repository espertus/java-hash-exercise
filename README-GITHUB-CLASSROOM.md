# Hash Function Contest Setup Instructions

## Prerequisites

- A GitHub organization linked to GitHub Classroom
- The organization upgraded to GitHub Team (free for educators)

## Overview

You will create:
1. A **leaderboard repo** (public) — receives scores and displays the leaderboard
2. A **template repo** (can be private) — contains only `MyString.java` for students to modify
3. A **GitHub Classroom assignment** — uses the template and runs the autograder

## Step 1: Upgrade Your Organization to GitHub Team

1. Navigate to the [GitHub Global Campus dashboard](https://education.github.com/globalcampus/teacher).
2. Click **Upgrade to GitHub Team**.
3. Click **Upgrade** next to your organization's name. (If you don't see the organization, that means
   it's already upgraded.)

## Step 2: Create the Leaderboard Repo

1. Create a new **public** repo (e.g., `java-hash-ex-leaderboard`) on GitHub
   within your organization. Do not add any files yet.

2. From your local `java-hash-exercise` directory, run:
   ```bash
   ./make_leaderboard_repo.sh <org> <leaderboard-repo>
   ```
   For example:
   ```bash
   ./make_leaderboard_repo.sh java-hash-ex-org java-hash-ex-leaderboard
   ```

3. Push the generated `leaderboard-repo/` contents to your GitHub repo.

4. Enable GitHub Pages:
    1. Go to the repo's **Settings**.
    2. Click **Pages** in the left sidebar.
    3. Under **Source**, select `Deploy from a branch`.
    4. Under **Branch**, select `main` and `/docs`.
    5. Click **Save**.

## Step 3: Create the Leaderboard Token

1. On GitHub, click your profile picture → **Settings**.
2. In the left sidebar, click **Developer settings**.
3. Expand **Personal access tokens** → click **Fine-grained tokens**
4. Click **Generate new token** and fill in:
    - **Token name:** `Hash Contest Leaderboard`
    - **Description:** you can leave this blank
    - **Expiration:** end of semester or day after the activity
    - **Resource owner:** your organization
    - **Repository access:** Only select repositories → select your leaderboard repo
    - **Permissions:** Contents
5. After closing the permissions dialog, set the **Contents** permissions to `Read and write`.
6. Click **Generate token**.
7. **Copy the token**, which you will need in the next step.

## Step 4: Create the Autograder Workflow

From your local `java-hash-exercise` directory, run:
```bash
./make_github_autograder.sh <org> <leaderboard-repo> <token>
```
For example:
```bash
./make_github_autograder.sh java-hash-ex-org java-hash-ex-leaderboard ghp_xxxxxxxxxxxx
```

This creates `student-autograder.yml` in the current directory.

## Step 5: Create the Template Repo
1. Create a new **private** repo (e.g., `hash-contest-template`) on GitHub
   within your organization.
2. Add the file `MyString.java` (from `src/main/java/MyString.java`).
   Here is the simplest way to do so:
   * On the new repo page, click on the link **uploading a new file**.
   * Drag in `MyString.java` (from `src/main/java/MyString.java`).
3. Go to the repo's **Settings**.
4. Chek the box for `Template repository`.

## Step 6: Create the GitHub Classroom Assignment

1. Go to [GitHub Classroom](https://classroom.github.com/).
2. Select your classroom.
3. Click **Create your first assignment** or **New assignment**.
4. Choose **Create a blank assignment**.
5. Fill in assignment details (name, deadline, etc.).
6. Under **Starter code and environment**, select your template repo.
7. Under **Grading and feedback**:
    - In the **Add autograding tests** section, select **Custom YAML**.
    - Paste the contents of `student-autograder.yml`.
    - Click the button **Convert to workflow file**.
8. Scroll to the bottom of the page and click **Create assignment**.

## Testing

1. Accept the assignment.
2. Modify `MyString.java` by changing the pseudonym and implementing `hashCode()`.
3. Add, commit, and push the changes.
4. Check the **Actions** tab on GitHub for the results (which may take a minute).
5. Verify the leaderboard updates at `https://<org>.github.io/<leaderboard-repo>/`

## How It Works

1. Student pushes `MyString.java` to their private repo
2. GitHub Classroom runs the autograder, which:
    - Downloads evaluation code from the leaderboard repo
    - Compiles and runs the student's implementation
    - Shows results in the Actions tab
    - Triggers the leaderboard repo to update
3. The leaderboard repo updates `leaderboard.json`
4. Students view rankings at the GitHub Pages URL

## Files Summary

| Script | Creates |
|--------|---------|
| `make_leaderboard_repo.sh` | `leaderboard-repo/` directory with all leaderboard files |
| `make_student_autograder.sh` | `student-autograder.yml` to paste into GitHub Classroom |
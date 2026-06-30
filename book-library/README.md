# Book Library — Setup

This project reads database and email credentials from **environment variables**,
not from `application.properties` directly. This means the file is safe to
commit to GitHub — it contains no real passwords.

## Required environment variables

| Variable        | Purpose                                  |
|------------------|-------------------------------------------|
| `DB_USERNAME`    | Your PostgreSQL username (default: `postgres`) |
| `DB_PASSWORD`    | Your PostgreSQL password                 |
| `MAIL_USERNAME`  | Your Gmail address                       |
| `MAIL_PASSWORD`  | Your Gmail App Password (16 characters)  |

Get a Gmail App Password at https://myaccount.google.com/apppasswords
(requires 2-Step Verification enabled on your Google account first).

## Setting environment variables

### Option A — In Eclipse (recommended for this project)

1. Right-click the project → **Run As → Run Configurations**
2. Select `BookLibraryApplication` in the left panel
3. Go to the **Environment** tab
4. Click **New** and add each variable:
   - `DB_PASSWORD` = your real Postgres password
   - `MAIL_USERNAME` = your Gmail address
   - `MAIL_PASSWORD` = your 16-character App Password
5. Click **Apply**, then **Run**

These are stored in Eclipse's local run configuration — never written into
any file that gets committed to Git.

### Option B — Windows System Environment Variables

1. Search "Environment Variables" in the Start menu → **Edit environment
   variables for your account**
2. Click **New** under "User variables" and add each one (same names as above)
3. **Restart Eclipse** completely so it picks up the new variables
4. Run the app as normal

## If you forget to set them

- `DB_PASSWORD` missing → app fails to start with a database connection error
- `MAIL_USERNAME` / `MAIL_PASSWORD` missing → the app still starts and book
  purchases still work (stock still decreases), but sending the confirmation
  email will fail — you'll see an authentication error in the console.

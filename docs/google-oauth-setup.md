# Google OAuth setup (employee login)

## 1. Create a Google Cloud OAuth client

1. Open [Google Cloud Console](https://console.cloud.google.com/) → APIs & Services → Credentials.
2. Create credentials → **OAuth client ID** → Application type **Web application**.
3. Authorized JavaScript origins:
   - `http://localhost:5173`
   - `https://team-9-project-3-src.vercel.app`
4. Copy the **Client ID** (looks like `….apps.googleusercontent.com`).

You do **not** need the client secret for the GIS ID-token flow we use.

## 2. Environment variables

### Local API (`src/api`)
```bash
export GOOGLE_CLIENT_ID='YOUR_CLIENT_ID.apps.googleusercontent.com'
export SESSION_SECRET='long-random-string'
```

### Local web (`src/web/.env.local`)
```bash
VITE_API_URL=http://localhost:8080
VITE_GOOGLE_CLIENT_ID=YOUR_CLIENT_ID.apps.googleusercontent.com
```

### Render
- `GOOGLE_CLIENT_ID` = same client ID
- `SESSION_SECRET` = long random string (keep stable across deploys)
- Keep existing `DATABASE_URL`

### Vercel
- `VITE_GOOGLE_CLIENT_ID` = same client ID
- Keep existing `VITE_API_URL`

After changing Vercel env vars, redeploy.

## 3. Employee emails in Neon

Only emails in the `employee` table can open cashier/manager pages.

```sql
SELECT id, email, name, role, is_active FROM employee ORDER BY id;
```

Update Jack’s row (or insert teammates) to the **exact Google account email** you will click in the sign-in button:

```sql
UPDATE employee
SET email = 'your.real.google@gmail.com'
WHERE id = 1;
```

## 4. Smoke test

1. Open the app → `/login`
2. Sign in with a linked Google account
3. Manager goes to `/manager`; cashier goes to `/cashier`
4. Open `/cashier` while signed out → redirected to `/login`
5. Wrong / unlisted Google account → clear error from the API

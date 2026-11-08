# Database (Neon)

Project 3 hosts externally, so Postgres lives on **Neon** (not the TAMU VPN host).

## One-time setup

1. Create a free project at [https://console.neon.tech](https://console.neon.tech).
2. Copy the connection string (`postgres://...`). Prefer the **pooled** URL for the API.
3. From this repo root, load schema + menu seed:

```bash
psql "$DATABASE_URL" -f db/schema.sql
psql "$DATABASE_URL" -f db/seed-menu.sql
```

4. Put the same URL in Render as `DATABASE_URL` (and optionally in `local/db.txt` for laptop runs — see below).
5. For employee Google login:

```bash
psql "$DATABASE_URL" -f db/employee.sql
```

Update the seeded emails to real Google accounts (see `docs/google-oauth-setup.md`).

## Full Project 2 data (optional later)

`db/seed-menu.sql` is enough for the Sprint 1 deploy demo. To load the full order history from Project 2, on a machine with VPN access:

```bash
pg_dump -h csce-315-db.engr.tamu.edu -p 5432 -U team9 -d team9db --data-only \
  > /tmp/team9db-data.sql
psql "$DATABASE_URL" -f /tmp/team9db-data.sql
```

Document any schema differences in `CHANGELOG.md`.

## Local API without exporting DATABASE_URL

Create `local/db.txt` (gitignored):

```
url=jdbc:postgresql://YOUR-NEON-HOST/neondb?sslmode=require
user=YOUR_USER
password=YOUR_PASSWORD
```

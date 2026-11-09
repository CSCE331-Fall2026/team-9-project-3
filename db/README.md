# Database (Neon)

Project 3 hosts externally, so Postgres lives on **Neon** (not the TAMU VPN host).

## One-time setup

1. Create a free project at [https://console.neon.tech](https://console.neon.tech).
2. Copy the connection string (`postgres://...`). Prefer the **pooled** URL for the API.
3. From this repo root, load schema + menu seed:

```bash
psql "$DATABASE_URL" -f db/schema.sql
psql "$DATABASE_URL" -f db/seed-menu.sql
psql "$DATABASE_URL" -f db/store.sql
psql "$DATABASE_URL" -f db/seed-inventory.sql
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

## Manager inventory view

After manager sign-in, choose **View inventory** or open `/manager/inventory`.
The read-only screen calls `GET /api/manager/inventory` with the employee session
and reads `inventory_item` from the configured database on every load/refresh.
Only managers may call this endpoint; cashiers receive HTTP 403 and missing or
invalid sessions receive HTTP 401. No orders are created by this screen.

The menu seed does not populate inventory. An empty `inventory_item` table shows
**No inventory items found**. Load your actual inventory records (including their
referenced stores) to see stock quantities; no sample quantities are substituted.

To verify with a populated database:

1. Sign in as a manager and open **View inventory**. Compare quantities and units
   with `SELECT * FROM inventory_item ORDER BY store_id, name, id;`.
2. Change a quantity through your normal database workflow, then select
   **Refresh inventory** and confirm the updated value appears.
3. Confirm quantities at or below the reorder threshold show **Reorder**.
4. Sign in as a cashier and confirm `/manager/inventory` redirects to cashier home;
   calling the inventory API with a cashier token should return 403.
5. Confirm database failures show an error with a refresh option.

# Changelog

All notable changes to the database schema and data set since Project 2 are documented here.

## Unreleased

- **Hosting/DB decision:** App hosted externally (Vercel + Render). Project 2 Postgres on the TAMU engineering AWS server cannot be used (VPN-only). Migration target: **Neon**.
- **Neon baseline:** Same Project 2 schema in `db/schema.sql`. Demo seed is menu rows only (`db/seed-menu.sql`); full order history can be loaded later from a Project 2 dump. No schema changes vs Project 2 `team9db`.
- **API scaffold:** Javalin service in `src/api` with `GET /api/health` and `GET /api/menu` over JDBC.
- **Web scaffold:** React (Vite) app in `src/web` that calls the API for health + menu.
- **Auth:** Added `employee` table (`db/employee.sql`) mapping Google emails to `cashier` / `manager`. API endpoints `POST /api/auth/google`, `GET /api/auth/me`, and role-gated `/api/cashier/home` + `/api/manager/home`.
- **Create order (Sprint 1):** Added `db/store.sql`, which inserts the one store row (id 1, same as Project 2). The menu seed had no store, and `orders.store_id` must point at a store, so orders could not be saved without it. No table changes.
- **Create order API:** Added `POST /api/orders` (cashier only). It saves one `orders` row and its `order_item` rows in one transaction. Prices are looked up in `menu_item`, not taken from the browser. Payment is a later story, so a new order is saved with `status = 'open'` and `payment_method = 'pending'`, and `customer_id` is empty (guest order).

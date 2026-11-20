# Changelog

All notable changes to the database schema and data set since Project 2 are documented here.

## Unreleased

- **Meal pricing:** Cashier save now takes `meals` (size + sides + entrees) plus extra `items`. A side inside a meal is stored with `unit_price` 0 because the size price already includes it. Premium entrees keep their upcharge. No schema change.

- **Hosting/DB decision:** App hosted externally (Vercel + Render). Project 2 Postgres on the TAMU engineering AWS server cannot be used (VPN-only). Migration target: **Neon**.
- **Neon baseline:** Same Project 2 schema in `db/schema.sql`. Demo seed is menu rows only (`db/seed-menu.sql`); full order history can be loaded later from a Project 2 dump. No schema changes vs Project 2 `team9db`.
- **API scaffold:** Javalin service in `src/api` with `GET /api/health` and `GET /api/menu` over JDBC.
- **Web scaffold:** React (Vite) app in `src/web` that calls the API for health + menu.
- **Auth:** Added `employee` table (`db/employee.sql`) mapping Google emails to `cashier` / `manager`. API endpoints `POST /api/auth/google`, `GET /api/auth/me`, and role-gated `/api/cashier/home` + `/api/manager/home`.
- **Create order (Sprint 1):** Added `db/store.sql`, which inserts the one store row (id 1, same as Project 2). The menu seed had no store, and `orders.store_id` must point at a store, so orders could not be saved without it. No table changes.
- **Manager inventory data:** Loaded the 30 Project 2 `inventory_item` rows for store 1 (`db/seed-inventory.sql`). No schema change. The manager inventory screen was empty because Neon had no inventory rows.
- **Create order API:** Added `POST /api/orders` (cashier only). It saves one `orders` row and its `order_item` rows in one transaction. Prices are looked up in `menu_item`, not taken from the browser. Payment is a later story, so a new order is saved with `status = 'open'` and `payment_method = 'pending'`, and `customer_id` is empty (guest order).
- **Shared cart (Sprint 2, #23):** Added a new `shared_cart` table (`db/shared-cart.sql`) with `id`, `join_code` (unique 6 character code), `host_email`, `host_name`, `status` (`open`), and `created_at`. It holds the cart itself only; items in a shared cart come with #24. New API endpoints `POST /api/shared-carts` and `GET /api/shared-carts/mine` (customer only). Creating a shared cart does not write to `orders`.
- **Shared cart items (Sprint 2, #24):** Added a `shared_cart_item` table to `db/shared-cart.sql` (`shared_cart_id`, `menu_item_id`, `added_by_email`, `added_by_name`, `quantity`, `added_at`) and a unique index so one person has one line per menu item per cart; adding the same item again raises its quantity. New customer-only endpoints `POST /api/shared-carts/join`, `GET /api/shared-carts/{code}`, and `POST /api/shared-carts/{code}/items`. A wrong or closed code returns 404. Items are stored server-side and never write to `orders`. Re-run `db/shared-cart.sql` on Neon to add the new table and index.

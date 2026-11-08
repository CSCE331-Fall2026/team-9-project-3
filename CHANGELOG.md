# Changelog

All notable changes to the database schema and data set since Project 2 are documented here.

## Unreleased

- **Hosting/DB decision:** App hosted externally (Vercel + Render). Project 2 Postgres on the TAMU engineering AWS server cannot be used (VPN-only). Migration target: **Neon**.
- **Neon baseline:** Same Project 2 schema in `db/schema.sql`. Demo seed is menu rows only (`db/seed-menu.sql`); full order history can be loaded later from a Project 2 dump. No schema changes vs Project 2 `team9db`.
- **API scaffold:** Javalin service in `src/api` with `GET /api/health` and `GET /api/menu` over JDBC.
- **Web scaffold:** React (Vite) app in `src/web` that calls the API for health + menu.
- **Auth:** Added `employee` table (`db/employee.sql`) mapping Google emails to `cashier` / `manager`. API endpoints `POST /api/auth/google`, `GET /api/auth/me`, and role-gated `/api/cashier/home` + `/api/manager/home`.

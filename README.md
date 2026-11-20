# Team 9 — Panda Express POS Web App

A publicly hosted web POS and ordering system for Panda Express that supports cashiers, managers, and customers, including shared group carts for pickup orders.

## Live Links
- **Deployed app:** https://team-9-project-3-src.vercel.app/
- **API:** https://team-9-project-3.onrender.com
- **Documentation:** TBD
- **Project board (backlog + burn-up):** https://github.com/orgs/CSCE331-Fall2026/projects/28/views/1

## Current sprint

Sprint 2 is closed for submission. Planned **28** points (#21–#27); completed **28** (#21–#27). Demo path: customer Browse with pictures → My cart → **Place pickup order** → create/join Shared cart and add items; manager Sales summary + inventory. Meetings + burn-up: `sprint-materials/sprint2-meetings.md`, `sprint-materials/sprint2-burn-up.jpg`. User study 1: `sprint-materials/user-study-1.md`. Board: link above. After pulling shared-cart changes, run `db/shared-cart.sql` on Neon so `shared_cart_item` exists.

## Roles
| Name | Role |
|------|------|
| Jack Berry | Project Manager & Full-stack |
| Nevin Kalloor | Front-end Lead |
| Dishant Patel | Front-end Lead |
| Ayaan Budhwani | Back-end Lead |

## Novel Feature
**What it is:** Group / shared cart ordering. Customers can create or join a shared order with a short code or link, each person adds their own items to one cart in real time, and one person submits a single pickup order when the group is ready.

**Motivation:** Groups often struggle to collect everyone’s picks into one ticket, which slows the line and causes wrong or incomplete orders. This feature helps **customers** order together without phone-tag, and gives **cashiers** one clean group ticket instead of several partial ones.

## Tech Stack
- **Front-end:** React (Vite) in `src/web`, hosted on Vercel
- **Back-end:** Java/JDBC + Javalin HTTP API in `src/api`, hosted on Render
- **Database:** PostgreSQL on Neon (migrated from Project 2 TAMU/AWS DB)
- **Documentation:** Cloudflare Pages (from `/docs`)
- **Authentication:** Google OAuth2 (Sprint 1 stories)

## Repo layout
```
src/web/     React + Vite front-end
src/api/     Javalin API (Dockerfile for Render)
db/          Neon schema + menu seed (see db/README.md)
```

## Local development

### Database
Follow `db/README.md` to create a Neon project and load `db/schema.sql` + `db/seed-menu.sql` + `db/store.sql` (orders need the store row).  
For employee Google login, also run `db/employee.sql` (see `docs/google-oauth-setup.md`).

### API
```bash
cd src/api
export DATABASE_URL='postgres://USER:PASS@HOST/DB?sslmode=require'
export GOOGLE_CLIENT_ID='YOUR_CLIENT_ID.apps.googleusercontent.com'
export SESSION_SECRET='long-random-string'
./mvnw -DskipTests package
java -jar target/team9-api.jar
```
Health: `http://localhost:8080/api/health` · Menu: `http://localhost:8080/api/menu`

Create order (cashier sign-in required): `POST /api/orders` with `{"items": [{"menuItemId": 2, "quantity": 1}]}`. Returns the new `orderId` and `subtotal`.

Shared cart (customer sign-in required): `POST /api/shared-carts` creates one and returns its `joinCode` (or the open cart the customer already hosts). `GET /api/shared-carts/mine` returns that cart, or `null`. `POST /api/shared-carts/join` with `{"joinCode": "K7QM4P"}` joins a cart (404 for a wrong or closed code). `GET /api/shared-carts/{code}` returns the cart and its items. `POST /api/shared-carts/{code}/items` with `{"menuItemId": 24}` adds an item. Needs `db/shared-cart.sql` loaded.

Sales summary (manager sign-in required): `GET /api/manager/sales-summary?days=7` returns the order count, sales total, average order, and latest orders for the last 1, 7, or 30 days. The page is `/manager/sales`.

### Web
```bash
cd src/web
cp .env.example .env.local
# set VITE_API_URL=http://localhost:8080
# set VITE_GOOGLE_CLIENT_ID to the same Google client ID
npm install
npm run dev
```

Employee login lives at `/login`. Cashier/manager homes are protected routes.

## Deploy (Sprint 1)
1. Neon: create DB, run schema + seed, copy connection string.
2. Render: create Web Service from this repo, root `src/api`, Docker runtime, set `DATABASE_URL`.
3. Vercel: import repo, root `src/web`, set `VITE_API_URL` to the Render URL, redeploy.
4. Update the Live Links above with the Vercel and Render URLs.

## Customer menu browsing

Open `/customer/login` and sign in with Google, then browse `/menu`.
Customer Google accounts do not need an employee record. The API issues a
customer session for browsing and checkout.
Employee-only inventory and order endpoints reject customer sessions.

The menu reads available items from the live database through `GET /api/menu`
(includes optional `imageUrl`). Customer `/menu` has three clear areas:
**Browse** (picture cards), **My cart** (personal session cart), and
**Shared cart** (group code). The menu opens on sizes. A bowl, plate, bigger
plate, kids meal, or a la carte meal has a fixed number of sides and entrees.
You cannot add extra entrees onto a full meal. Sides and regular entrees are
included in the size price; premium entrees add their upcharge. Apps, drinks,
and a side ordered on its own are charged separately. The personal cart stays
in the browser session until a pickup order is placed. Cashiers save the same
kind of meal. Needs `db/menu-images.sql` (or a fresh seed) for picture paths.
The menu API remains public for existing menu integrations.

To verify: sign in as a customer, confirm Browse cards show pictures, My cart
and Shared cart are labeled separately, choose Plate, add 1 side and 2 entrees,
and check the subtotal is the plate price plus any premium. A third entree
should be refused. Refresh and confirm the meal is still there, then select
**Place pickup order** and check the order number and subtotal. Sign in as a
cashier and save the same kind of meal. Sign out and open
`/menu` again; it should redirect to customer sign-in.
Local preview still requires Google OAuth configuration and a running API.

The API links pickup orders to the verified Google account ID in the signed
customer session. Each account gets a customer record on its first successful
pickup order. If the email already belongs to an older customer record without
a Google account ID, checkout directs the customer to the store for help.

For existing databases, run this migration before deploying the API:

```bash
psql "$DATABASE_URL" -v ON_ERROR_STOP=1 -f db/migrations/001-customer-google-subject.sql
```

Customers with sessions issued before this change need to sign in again.

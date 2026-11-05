# Team 9 — Panda Express POS Web App

A publicly hosted web POS and ordering system for Panda Express that supports cashiers, managers, and customers, including shared group carts for pickup orders.

## Live Links
- **Deployed app:** https://team-9-project-3-src.vercel.app/
- **API:** https://team-9-project-3.onrender.com
- **Documentation:** TBD
- **Project board (backlog + burn-up):** https://github.com/orgs/CSCE331-Fall2026/projects/28/views/1

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
Follow `db/README.md` to create a Neon project and load `db/schema.sql` + `db/seed-menu.sql`.

### API
```bash
cd src/api
export DATABASE_URL='postgres://USER:PASS@HOST/DB?sslmode=require'
./mvnw -DskipTests package
java -jar target/team9-api.jar
```
Health: `http://localhost:8080/api/health` · Menu: `http://localhost:8080/api/menu`

### Web
```bash
cd src/web
cp .env.example .env.local
# set VITE_API_URL=http://localhost:8080
npm install
npm run dev
```

## Deploy (Sprint 1)
1. Neon: create DB, run schema + seed, copy connection string.
2. Render: create Web Service from this repo, root `src/api`, Docker runtime, set `DATABASE_URL`.
3. Vercel: import repo, root `src/web`, set `VITE_API_URL` to the Render URL, redeploy.
4. Update the Live Links above with the Vercel and Render URLs.

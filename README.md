# Team 9 — Panda Express POS Web App

A publicly hosted web POS and ordering system for Panda Express that supports cashiers, managers, and customers, including shared group carts for pickup orders.

## Live Links
- **Deployed app:** TBD
- **Documentation:** https://github.com/orgs/CSCE331-Fall2026/projects/28/views/1
- **Project board (backlog + burn-up):** TBD

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
- **Front-end:** React (Vite), hosted on Vercel
- **Back-end:** Existing Java/JDBC from Project 2, exposed via a thin HTTP API layer (Javalin) — no full framework rewrite
- **Database:** PostgreSQL on AWS
- **API hosting:** Render
- **Documentation:** Cloudflare Pages (from `/docs`)
- **Authentication:** Google OAuth2

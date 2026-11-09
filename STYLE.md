# Style Guide

How we write code in this repo. Anyone on the team should be able to open a file and follow the structure without reformatting it.

## Layout

- `src/api` — Java / Javalin / JDBC API
- `src/web` — React (Vite) front-end
- `db` — SQL schema and seeds for Neon
- `docs` — hosted / setup docs
- `sprint-materials` — sprint goals, meetings, burn-up snapshots

Do not commit secrets (`.env`, `local/db.txt`, Neon or Google credentials).

## Indentation and spacing

- **Java:** 3 spaces per level (same as Project 2). No tabs.
- **JavaScript / JSX / CSS:** 2 spaces per level (Vite / React default in this repo). No tabs.
- **SQL:** 2 spaces; keywords in `UPPERCASE`; tables/columns in `snake_case`.

Put spaces after commas and around binary operators. No space before `(` in a call: `getPrice()`. Always use braces for `if` / `else` / `for` / `while` in Java, even for one-line bodies.

One blank line between methods. Do not stack blank lines.

## Names

**Java**
- Classes: `PascalCase`
- Methods / fields: `camelCase`
- Constants: `UPPER_SNAKE_CASE`
- Packages under `pos.api` by concern: `auth`, `menu`, `order`, `inventory`, `db`

**JavaScript / React**
- Components and hooks files: `PascalCase` components (`OrderPanel.jsx`), `useX` hooks (`useOrder.js`)
- Other modules: `camelCase` (`client.js`)
- Prefer clear names over abbreviations

**SQL**
- Tables / columns: `snake_case` matching Project 2 (`menu_item`, `qty_on_hand`)

## Architecture

- Browser code does not talk to Postgres. It calls the HTTP API.
- SQL stays in DAO / `db` helpers, not in React components.
- Auth: verify Google ID tokens on the API; issue our own session token; protect cashier/manager routes by role.
- Prices for saved orders are looked up in the database, not trusted from the client.

## React

- Functional components only.
- Keep pages thin; put fetch / order-building logic in `api/` and `hooks/`.
- Shared UI lives in `components/`.
- Environment: only `VITE_*` values in the front-end. Never put DB passwords or `SESSION_SECRET` in the web app.

## Java API

- Prefer small classes with one job (`MenuDao`, `OrderDao`, `AuthService`).
- Return JSON errors with a clear `error` string and the right HTTP status (401 / 403 / 400 / 500).
- Use prepared statements with `?` placeholders. Never build SQL from browser strings.
- Transactions for multi-row writes (order + order items): commit together or roll back.

## Commits and PRs

- Small, regular commits with a clear subject line (what changed and why).
- Stories merge to `main` through a PR when possible.
- Update `AI-USE.md` when AI helped write or configure something.
- Update `CHANGELOG.md` for schema or seed/data changes.

## Formatting check

Before you open a PR, skim for: wrong indent width, secrets, `System.out` debug left in new paths, and acceptance criteria for the story you claim is done.

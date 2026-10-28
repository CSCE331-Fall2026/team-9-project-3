# Sprint 0 Product Backlog — Issue Drafts

Use these as GitHub Issues. For each: create issue → add to Project 28 → set **Sprint**, **Story Points**, and **Status** (Product Backlog or Sprint Backlog for S1; Icebox/Product Backlog for S2–S3 epics).

---

## Sprint 1 — Detailed user stories

### [S1] As an employee, I want to log in with Google so that I can access role-appropriate features

**Sprint:** 1 | **Points:** 5 | **User:** Cashier / Manager (employee)

## User story
As an employee, I want to log in with Google OAuth2 so that I can access authorized POS features for my role.

## Acceptance criteria
- [ ] Given a valid Google account linked to an employee role, when I log in, then I reach the appropriate POS interface.
- [ ] Given an unauthenticated user, when I try to open a protected page, then I am prompted to log in.
- [ ] A successful login identifies the active user and their role (cashier or manager).
- [ ] Given login failure or denied access, when authentication fails, then a clear error message is shown.

---

### [S1] As a cashier, I want to view the menu so that I can find items and prices

**Sprint:** 1 | **Points:** 3 | **User:** Cashier

## User story
As a cashier, I want to view the menu so that I can find items and their prices.

## Acceptance criteria
- [ ] The menu displays available items with names and prices from the live database.
- [ ] Each displayed item has a readable description (or equivalent item detail).
- [ ] The cashier can view the menu without modifying menu data.
- [ ] Unavailable items are not offered for sale (or are clearly marked unavailable).

---

### [S1] As a cashier, I want to create an order so that I can ring up a customer

**Sprint:** 1 | **Points:** 5 | **User:** Cashier

## User story
As a cashier, I want to add menu items to a new order so that I can prepare a customer checkout.

## Acceptance criteria
- [ ] A cashier can start an empty order.
- [ ] A cashier can add one or more menu items to that order.
- [ ] The order shows each selected item and a running subtotal before tax.
- [ ] Order data is persisted through the Java/JDBC API against the existing Postgres database.

---

### [S1] As a manager, I want to view current inventory so that I can monitor stock levels

**Sprint:** 1 | **Points:** 3 | **User:** Manager

## User story
As a manager, I want to view current inventory so that I can monitor stock levels after hours.

## Acceptance criteria
- [ ] After manager login, I can open an inventory view.
- [ ] The view lists inventory items with quantity on hand (from the live database).
- [ ] The manager can view inventory without being able to place cashier orders from this screen.

---

### [S1] As a customer, I want to log in with Google so that I can use the customer ordering experience

**Sprint:** 1 | **Points:** 3 | **User:** Customer

## User story
As a customer, I want to log in with Google so that I can access the customer-facing ordering experience.

## Acceptance criteria
- [ ] A customer can authenticate with Google OAuth2.
- [ ] After login, the customer reaches a customer interface (distinct from cashier/manager).
- [ ] Customers without employee roles do not gain cashier or manager access.

---

### [S1] As a customer, I want to browse the menu so that I can see what I can order

**Sprint:** 1 | **Points:** 2 | **User:** Customer

## User story
As a customer, I want to browse the menu so that I can see available items and prices before ordering in a later sprint.

## Acceptance criteria
- [ ] Logged-in customers can view available menu items with names and prices.
- [ ] Menu data comes from the live database via the API.
- [ ] The browse view is read-only (no shared cart required yet in Sprint 1).

---

### [S1] As a developer, I want the web app and API deployed so that the Sprint 1 MVP is publicly demoable

**Sprint:** 1 | **Points:** 5 | **User:** Team / demo

## User story
As a team member, I want the React app on Vercel and the Java API on Render connected to our database so that we can demo a working Sprint 1 MVP.

## Acceptance criteria
- [ ] Front-end is publicly reachable on Vercel.
- [ ] API is publicly reachable on Render and serves menu/order (or health) endpoints needed for the demo.
- [ ] README live app link is updated from TBD to the deployed URL (docs may remain TBD).

---

## Sprint 2 — Epic placeholders

### [S2 Epic] Customer self-checkout ordering flow

**Sprint:** 2 | **Points:** (refine later) | **User:** Customer

## Epic
Customers can build and submit their own pickup orders (beyond browse-only), informed by user study 1 feedback.

## Notes for later refinement
- Cart for a single customer
- Submit/pay or confirm pickup order
- UX fixes from study 1

---

### [S2 Epic] Group / shared cart — create and join

**Sprint:** 2 | **Points:** (refine later) | **User:** Customer

## Epic
Start the novel feature: customers can create a shared cart and join via short code/link; basic multi-user add-to-cart (sync may still be limited).

## Notes for later refinement
- Create shared cart + join code
- Add items as a participant
- Persist shared cart server-side

---

### [S2 Epic] Manager reporting / inventory basics (expand)

**Sprint:** 2 | **Points:** (refine later) | **User:** Manager

## Epic
Expand manager capabilities (e.g. basic sales/inventory insights) beyond the Sprint 1 inventory view, incorporating study 1 feedback.

---

## Sprint 3 — Epic placeholders

### [S3 Epic] Finish group / shared cart ordering

**Sprint:** 3 | **Points:** (refine later) | **User:** Customer (+ cashier benefit)

## Epic
Complete the novel feature: multiple customers on one live shared cart submit a single pickup order.

## Notes for later refinement
- Live-enough sync for demo (polling OK)
- One submit/checkout for the group
- Cashier sees one clean group ticket if in scope

---

### [S3 Epic] WCAG 2.2 Level AA accessibility polish

**Sprint:** 3 | **Points:** (refine later) | **User:** All

## Epic
Bring customer, cashier, and manager interfaces to WCAG 2.2 Level AA and verify with Lighthouse / checklist.

---

### [S3 Epic] Final product polish and release

**Sprint:** 3 | **Points:** (refine later) | **User:** All

## Epic
Ship the final deployed product: remaining manager needs, study 2 feedback, docs hosted on Cloudflare Pages, CHANGELOG/AI-USE up to date, final demo readiness.

# Sprint 1 Meetings

**Sprint goal (confirmed):** Deploy a working web MVP where users authenticate with Google OAuth2 and a cashier can view the live menu and build a basic order against our Postgres database on Neon.

**Team-wide definition of done (agreed in planning):**
- Code works for the story’s acceptance criteria
- Merged to `main` via a reviewed pull request (or equivalent team review)
- Deployed / reachable on the public host (Vercel + Render) when the story needs a live demo path
- AI use cited in `AI-USE.md`
- Any DB schema or data changes noted in `CHANGELOG.md`
- No secrets committed to GitHub

**Planned story points:** 26  
(#1 login 5, #2 cashier menu 3, #3 create order 5, #4 manager inventory 3, #5 customer login 3, #6 customer browse 2, #7 deploy 5)

**Completed story points:** 26

---

## 1. Sprint planning

- **Date / time:** Sunday, Nov 2, 2026 · 8:00 PM
- **Attendees:** Jack Berry, Nevin Kalloor, Dishant Patel, Ayaan Budhwani
- **Blockers raised:** None yet. Neon migration and Google OAuth client still needed before deploy can finish.
- **Decisions:**
  - Confirmed Sprint 1 goal in `sprint-goals.md` (no change)
  - Agreed definition of done above
  - Jack owns #7 deploy and #1 employee Google login
  - Nevin owns #2 cashier menu and #6 customer browse
  - Dishant owns #3 create order
  - Ayaan owns #4 manager inventory and #5 customer Google login
  - Hosting stays Vercel (web) + Render (API) + Neon (Postgres)

---

## 2. Daily scrum

- **Date / time:** Tuesday, Nov 4, 2026 · 9:00 PM
- **Attendees:** Jack Berry, Nevin Kalloor, Dishant Patel, Ayaan Budhwani
- **Blockers raised:** Jack — Vercel import UI had no branch picker; needed feature-branch then switch production to `main` after merge. Neon connection string ready.
- **Decisions:** Finish deploy scaffold first so everyone can hit a public API. Jack continues #7; others start local API/menu spikes against Neon once health is up.

---

## 3. Daily scrum

- **Date / time:** Thursday, Nov 6, 2026 · 8:00 PM
- **Attendees:** Jack Berry, Nevin Kalloor, Dishant Patel, Ayaan Budhwani
- **Blockers raised:** Jack — Google OAuth consent screen / test users required before login works in Testing mode. Nevin — waiting on stable `/api/menu` shape for card details.
- **Decisions:** Deploy (#7) treated as done once Vercel + Render stay up and README links are set. Jack moves fully to #1. Nevin starts menu API client + cashier/customer pages on `frontend/menu-api-layer`. Dishant begins order API design against Project 2 `OrderDao` patterns. Ayaan sketches inventory query + customer auth path.

---

## 4. Daily scrum

- **Date / time:** Saturday, Nov 8, 2026 · 8:00 PM
- **Attendees:** Jack Berry, Nevin Kalloor, Dishant Patel, Ayaan Budhwani
- **Blockers raised:** Dishant — orders need a `store` row (`store_id`); menu seed alone was not enough. Ayaan — manager inventory UI would be empty until Neon has `inventory_item` rows. Jack — create-order demo needs a cashier Google account (managers get 403).
- **Decisions:** Merge menu branch into the create-order work path; add `db/store.sql`; seed inventory from Project 2; add `jack.faker002@gmail.com` as cashier for demos. Close Sprint 1 stories only after acceptance criteria + definition of done.

---

## 5. Sprint retrospective

- **Date / time:** Sunday, Nov 9, 2026 · 9:00 PM
- **Attendees:** Jack Berry, Nevin Kalloor, Dishant Patel, Ayaan Budhwani
- **Blockers raised:** None for Sprint 1 closeout. Still need burn-up snapshot uploaded and a GitHub Release for submission.

### What went well
- Split ownership by story kept PRs focused
- Deploy early (#7) unblocked everyone else from guessing at hosting
- Reused Project 2 JDBC / menu / order ideas instead of rewriting the data model
- Live end-to-end path works: employee login → menu → create order

### What did not go well
- Neon started with menu only; store + inventory seeds were easy to forget until order/inventory stories broke
- Vercel branch / root-directory setup cost extra time on #7
- Google OAuth “Testing” mode + exact email matching slowed login demos

### Velocity
- **Planned:** 26 story points  
- **Completed:** 26 story points  
- We finished the planned Sprint 1 backlog. Velocity felt sustainable once deploy was up; most of the pain was environment/config, not story size estimates.

### Takeaways for future sprints
- Seed every table a story touches (store, inventory, employees) in the same PR that adds the feature
- Keep a dedicated cashier test Google account in Neon from day one
- Update `sprint1-meetings.md` / board status the same day as stand-ups, not at the end
- Create the GitHub Release as soon as `main` is demo-ready, not after the live demo
- After user study 1, refine Sprint 2 epics (#8–#10) with concrete acceptance criteria before planning
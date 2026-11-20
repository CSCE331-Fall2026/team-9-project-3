# Sprint 2 Meetings

**Sprint goal (confirmed):** Add the customer ordering path and begin group/shared cart ordering, incorporating feedback from user study 1.

**Team-wide definition of done (carried from Sprint 1, still applies):**
- Code works for the story’s acceptance criteria
- Merged to `main` via a reviewed pull request (or equivalent team review)
- Deployed / reachable on the public host (Vercel + Render) when the story needs a live demo path
- AI use cited in `AI-USE.md`
- Any DB schema or data changes noted in `CHANGELOG.md`
- No secrets committed to GitHub
- No feature regressions on Sprint 1 login / cashier order / customer browse paths

**Planned story points:** 28  
(#21 personal cart 5, #22 submit pickup 5, #23 create shared cart 3, #24 join shared cart 5, #25 manager sales 5, #26 menu pictures 3, #27 link orders to Google 2)

**Completed story points:** 16  
(#21, #23, #25, #26)

**Not finished this sprint (carried toward Sprint 3 / next work):** #22 submit (5), #24 join shared cart (5), #27 link customer Google to orders (2) — 12 pts

---

## 1. Sprint planning

- **Date / time:** Sunday, Nov 16, 2026 · 8:00 PM
- **Attendees:** Jack Berry, Nevin Kalloor, Dishant Patel, Ayaan Budhwani
- **Blockers raised:** User study 1 showed the customer menu was hard to scan and “cart” language was unclear; shared-cart vs personal cart must stay separate.
- **Decisions:**
  - Confirmed Sprint 2 goal in `sprint-goals.md` (customer path + start shared cart)
  - Kept the same definition of done; added “no Sprint 1 regressions”
  - Locked backlog to issues #21–#27 (28 pts) from `sprint-materials/sprint2-issue-drafts.md`
  - Jack owns #21 personal cart and #26 menu pictures
  - Dishant owns #23 create shared cart and #25 manager sales summary
  - Nevin owns #24 join shared cart + add items
  - Ayaan owns #22 submit personal pickup order and #27 link orders to Google account
  - Demo target if time runs out: customer Browse with pictures + My cart + create shared cart code + manager sales (submit/join can slip with a clear retro note)

---

## 2. Daily scrum

- **Date / time:** Monday, Nov 17, 2026 · 8:30 PM
- **Attendees:** Jack Berry, Nevin Kalloor, Dishant Patel, Ayaan Budhwani
- **Blockers raised:** Jack — meal slots (bowl/plate/bigger) needed clear rules so entrees stop being added as free extras. Ayaan — submit story (#22) depends on a stable cart shape from #21.
- **Decisions:** Ship #21 first with meal-based cart + sessionStorage. Dishant starts `shared_cart` table + create/join-code API. Nevin waits on #23 host/code UI before join flow. Ayaan drafts submit payload against the same meal model cashiers use.

---

## 3. Daily scrum

- **Date / time:** Tuesday, Nov 18, 2026 · 8:00 PM
- **Attendees:** Jack Berry, Nevin Kalloor, Dishant Patel, Ayaan Budhwani
- **Blockers raised:** Dishant — Neon needed `db/shared-cart.sql` before create-cart demos. Nevin — join UI blocked until host code endpoint exists on `main`. Jack — pictures story (#26) waiting until cart labels are settled so Browse / My cart / Shared cart stay distinct.
- **Decisions:** Merge #21 when acceptance criteria pass. Dishant finishes #23 create shared cart + code copy UI. #25 sales summary can proceed in parallel (read-only). #24 stays after #23 lands on `main`.

---

## 4. Daily scrum

- **Date / time:** Wednesday, Nov 19, 2026 · 8:00 PM
- **Attendees:** Jack Berry, Nevin Kalloor, Dishant Patel, Ayaan Budhwani
- **Blockers raised:** Ayaan — #22/#27 not started enough for Sprint 2 close; kitchen submit + account link need more than a day. Nevin — #24 join/add-items still open on a feature branch. Dishant — #23/#25 ready to merge; careful with conflict on `main`.
- **Decisions:** Prioritize merging #23 and #25 tonight. Jack finishes #26 official menu photos + clearer Browse labels for the demo. Be honest in the retro that submit (#22), join (#24), and Google-linked orders (#27) miss Sprint 2; carry them with points into the next planning pass. Demo workflow for feedback: customer Google login → Browse with pictures → fill My cart → create Shared cart code; manager opens Sales summary.

---

## 5. Sprint retrospective

- **Date / time:** Thursday, Nov 19, 2026 · 9:30 PM
- **Attendees:** Jack Berry, Nevin Kalloor, Dishant Patel, Ayaan Budhwani
- **Blockers raised:** None for Sprint 2 closeout beyond unfinished stories already listed. Need burn-up snapshot + GitHub Release for submission.

### What went well
- User study 1 feedback turned into concrete stories (#26 labels/pictures, keep personal vs shared cart separate)
- #21 meal cart fixed a real pricing bug (sides/entrees inside a meal)
- #23 shared cart create + code is live and demoable without pretending full multi-user sync
- #25 manager sales summary is a clean read-only win with little schema risk
- Parallel ownership (Jack / Dishant / Nevin / Ayaan) still worked when dependencies were called out early

### What did not go well
- Planned 28 pts; finished 16 — too much packed after user-study planning
- #22 submit and #27 account-link depended on cart + auth details that landed late
- #24 join/add-items waited on #23 and did not close in Sprint 2
- Board / burn-up still needs merge-date discipline (same lesson as Sprint 1)

### Velocity
- **Planned:** 28 story points  
- **Completed:** 16 story points (#21=5, #23=3, #25=5, #26=3)  
- **Missed:** 12 story points (#22=5, #24=5, #27=2)  
- Velocity dropped vs Sprint 1’s 26 completed. Estimates for submit/join/link were optimistic once meal cart + shared-cart schema work showed up. For Sprint 3, plan closer to **16–20** pts unless join/submit are already mid-flight.

### Takeaways for future sprints
- Do not schedule both “create shared cart” and “join + add items” as must-finish in the same short sprint unless create is already on `main` on day 1
- Keep one end-to-end customer feedback path even when submit slips (Browse + My cart + create code was enough for study-ready UX feedback)
- Carry #22 / #24 / #27 explicitly into Sprint 3 planning with the novel feature (#11) instead of silently shrinking scope
- Upload burn-up + cut the GitHub Release the night before the live demo
- Continue citing AI help in `AI-USE.md` the same day as the PR

### Demoable Sprint 2 MVP (for user feedback)
1. Customer Google login → `/menu` Browse with item pictures  
2. Build a Plate / Bowl in **My cart** (add/remove, subtotal, meal rules)  
3. Create a **Shared cart** and copy the join code  
4. Manager Google login → **Sales summary** for 1 / 7 / 30 days  

Live app: https://team-9-project-3-src.vercel.app/ · API: https://team-9-project-3.onrender.com/

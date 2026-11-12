# User Study 1

**Product:** Team 9 Panda Express POS (Sprint 1 MVP)  
**App:** https://team-9-project-3-src.vercel.app/  
**API:** https://team-9-project-3.onrender.com  
**When:** after Sprint 1 deploy, before Sprint 2 build  
**Team size:** 4 (one session owner per member)

This file is the study instrument and the results record. Likert answers, quotes, and observations below are filled only from live sessions. Do not invent ratings or quotes.

## Study goals

1. Learn whether a cashier can sign in with Google, tell that they landed in the cashier POS (not manager or customer), find menu items and prices, start an order, add items, and read the running subtotal without help.
2. Learn whether a customer can sign in, browse the live menu, and correctly understand that ordering is not available yet, and what they expect the next step to be.
3. Learn which labels, empty states, or role switches confuse people, so Sprint 2 customer cart and shared-cart work does not copy those problems.

## Participants

One recruited user per team member. Do not use a teammate as the participant for that teammate's own session. Anonymize in this file (role + letter only). No names, emails, or student IDs.

| Session owner | Participant label | Intended role exercised |
|---------------|-------------------|-------------------------|
| Jack Berry | Participant A | Cashier, then a short customer browse |
| Nevin Kalloor | Participant B | Cashier, then a short customer browse |
| Dishant Patel | Participant C | Cashier, then a short customer browse |
| Ayaan Budhwani | Participant D | Manager inventory, then customer browse |

Give the cashier task the cashier test Google account. Give the customer task a normal Google account that is not an employee. Give the manager task the manager test account.

## Protocol

Run the session live on the deployed app. Read the task, then stay quiet. Ask the participant to think out loud. Help only if they are stuck for more than about a minute, and note that you helped. Session length target: 15–20 minutes.

**Cashier path (A, B, C)**

1. Open the deployed app and sign in as a cashier.
2. Find two specific items (for example orange chicken and fried rice), check the price of each, and add both to a new order.
3. Read the running subtotal out loud.
4. Sign out. Sign in as a customer, open the menu, and try to place an order with what is on screen.

**Manager path (D)**

1. Sign in as a manager and open inventory. Say what the screen is telling you.
2. Sign out. Sign in as a customer and try to place an order.

Do not demo the happy path first.

## Questions

Ask these after the tasks, in this order. Likert items are 1–5: 1 = strongly disagree, 5 = strongly agree. Record the number and any comment they attach.

### Likert (quantitative)

1. I could tell how to sign in and whether I was an employee or a customer.
2. After login, I knew which interface I was in (cashier, manager, or customer).
3. I could find item names and prices on the menu without help.
4. I could start an order, add items, and read the subtotal. (Cashier sessions. Manager session: mark N/A and use the inventory item below.)
5. I could tell what the inventory screen was showing. (Manager session. Cashier sessions: N/A.)
6. I understood that a customer can browse but cannot submit an order yet.
7. I could complete the tasks without wanting extra instructions on the screen.

### Open-ended (qualitative)

1. What did you think you were supposed to do on the first screen?
2. Where did you hesitate, and what were you looking for?
3. As a customer, what did you expect to be able to do that you could not?
4. If you were working a rush, what on this screen would slow you down?
5. What is the one change that would have helped you most?

## How to record a session

Copy the block below once per participant. Keep the label. Write short quotes in their words. Under observations, note misclicks, wrong routes, help you gave, and anything they did not say but you saw.

---

### Participant A — cashier

- **Session owner:** Jack Berry
- **Date:**
- **Task completed without help:** yes / partial / no

**Likert**

| # | Score (1–5 or N/A) | Comment |
|---|--------------------|---------|
| 1 | | |
| 2 | | |
| 3 | | |
| 4 | | |
| 5 | N/A | |
| 6 | | |
| 7 | | |

**Quotes**

- Q1:
- Q2:
- Q3:
- Q4:
- Q5:

**Observed behavior**

-

---

### Participant B — cashier

- **Session owner:** Nevin Kalloor
- **Date:**
- **Task completed without help:** yes / partial / no

**Likert**

| # | Score (1–5 or N/A) | Comment |
|---|--------------------|---------|
| 1 | | |
| 2 | | |
| 3 | | |
| 4 | | |
| 5 | N/A | |
| 6 | | |
| 7 | | |

**Quotes**

- Q1:
- Q2:
- Q3:
- Q4:
- Q5:

**Observed behavior**

-

---

### Participant C — cashier

- **Session owner:** Dishant Patel
- **Date:**
- **Task completed without help:** yes / partial / no

**Likert**

| # | Score (1–5 or N/A) | Comment |
|---|--------------------|---------|
| 1 | | |
| 2 | | |
| 3 | | |
| 4 | | |
| 5 | N/A | |
| 6 | | |
| 7 | | |

**Quotes**

- Q1:
- Q2:
- Q3:
- Q4:
- Q5:

**Observed behavior**

-

---

### Participant D — manager

- **Session owner:** Ayaan Budhwani
- **Date:**
- **Task completed without help:** yes / partial / no

**Likert**

| # | Score (1–5 or N/A) | Comment |
|---|--------------------|---------|
| 1 | | |
| 2 | | |
| 3 | | |
| 4 | N/A | |
| 5 | | |
| 6 | | |
| 7 | | |

**Quotes**

- Q1:
- Q2:
- Q3:
- Q4:
- Q5:

**Observed behavior**

-

---

## Results summary

Fill this only after all four sessions are recorded.

| Likert item | A | B | C | D | Notes |
|-------------|---|---|---|---|-------|
| 1 Sign-in role | | | | | |
| 2 Knew which interface | | | | | |
| 3 Found names and prices | | | | | |
| 4 Built an order | | | | N/A | |
| 5 Read inventory | N/A | N/A | N/A | | |
| 6 Customer cannot order yet | | | | | |
| 7 Enough on-screen guidance | | | | | |

Patterns (only patterns that show up in more than one session, each tied to a participant label):

1.
2.
3.

## Design moves

Prioritize after the results summary exists. Each move needs evidence (participant labels + quote or observation) and a sprint impact. Do not list a move that no session supports.

| Priority | Design move | Evidence | Sprint impact |
|----------|-------------|----------|---------------|
| 1 | | | |
| 2 | | | |
| 3 | | | |

### Backlog already groomed (not a substitute for the table above)

Sprint 1 velocity was 26 points planned and 26 completed. Sprint 2 is sized at **23 points**, under that velocity.

| Issue | Story | Points | Why it is in Sprint 2 |
|-------|--------|--------|------------------------|
| #21 | Personal customer cart | 5 | Sprint goal: customer can build an order. Customers cannot add items today. |
| #22 | Submit personal pickup order | 5 | Sprint goal: customer places an order. `POST /api/orders` is cashier-only. |
| #23 | Create shared cart + join code | 3 | Sprint goal: start the novel feature. |
| #24 | Join shared cart and add items | 5 | Sprint goal: a second customer can join. Group submit stays in Sprint 3 (#11). |
| #25 | Manager sales summary | 3 | Manager can only view inventory today. Separate from the inventory screen. |
| #26 | Clearer customer menu and cart labels | 2 | Slot for study-1 wording fixes. Rewrite the acceptance criteria to match the design-move table once sessions exist. |

Closed as duplicates before this plan: #8, #9, #10, #14, #18, #19, #20. Sprint 3 epics #11–#13 stay epic placeholders.

If a design move does not fit #21–#26, change that issue's acceptance criteria or add one small story and drop or shrink another so the sprint stays near 23 points.

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
- **Date:** 2026-11-15
- **Task completed without help:** partial

**Likert**

| # | Score (1–5 or N/A) | Comment |
|---|--------------------|---------|
| 1 | 5 | Found Google sign-in quickly and completed login without help. |
| 2 | 4 | Knew it was the cashier POS once the menu and order panel appeared; unsure for a moment whether manager was a separate login. |
| 3 | 5 | Found Orange Chicken and Fried Rice by category tabs and read prices out loud. |
| 4 | 3 | Did not notice “New order” at first; tried to tap menu items before an order existed. Subtotal made sense after the first add. |
| 5 | N/A | |
| 6 | 2 | As a customer, kept looking for Add / checkout and thought the app was broken until told browse-only is intentional. |
| 7 | 3 | Wanted clearer empty-order and customer-browse messaging so the next step is obvious. |

**Quotes**

- Q1: "The first thing I saw was Sign in with Google, so I assumed that was the door into the POS."
- Q2: "I hesitated on the cashier screen because the order side looked empty and I wasn't sure if I was supposed to pick food first or start an order first."
- Q3: "On the customer menu I expected pictures of the food and a button to add something to a bag. Right now it feels like a price list."
- Q4: "In a rush I'd lose time hunting for New order and scanning text-only cards with no photos."
- Q5: "Put photos on the customer menu and a short line that says ordering isn't available yet, plus make New order harder to miss for cashiers."

**Observed behavior**

- Signed in as cashier on the first try; landed on cashier menu with the order panel visible but empty.
- Clicked two entrees before starting an order; no lines appeared until after New order, then Orange Chicken and Fried Rice added and subtotal read correctly.
- After sign-out / customer sign-in, scrolled `/menu`, used a category tab, clicked items expecting a cart; no add controls appeared. Asked whether the site was unfinished.
- Brief help given once: pointed at New order after ~45 seconds of tapping items with no effect.

---

### Participant B — cashier

- **Session owner:** Nevin Kalloor
- **Date:** 2026-11-15
- **Task completed without help:** partial

**Likert**

| # | Score (1–5 or N/A) | Comment |
|---|--------------------|---------|
| 1 | 5 | The google sign in was easier than a normal sign in since memorizing a password was not necessary |
| 2 | 3 | I could tell I was in the cashier panel but I had to look around a bit. Instead of cashier being big and bold it stated something else, but it could have been any interface at that point|
| 3 | 5 | I was able to find any item and their correlated price easily due to the category tabs and the search bar|
| 4 | 4| Was initially confused about making an order and then the order of what buttons to press, but was able to understand after a bit of practice|
| 5 | N/A | |
| 6 | 3 | I tried to find the plus sign like food apps normally use them to add food to a cart but could not find it. I was then told I was only able to look through the menu|
| 7 | 4 | Most of the tasks were self-explanatory, although it would help if there were pop ups to describe mini tasks |

**Quotes**

- Q1: It was obvious that a sign in was needed for the very first task and having google made it a lot easier to sign in with an account rather than filling details on my own.
- Q2: I hesitated a little bit when I saw the cashier interface for the first time, as I didn't know if it was the right interface initially since there was no big bold word saying cashier interface but rather I had to use context clues like "return to cashier homepage".
- Q3: As a customer, I expected to add orders myself since most restaurant apps have that feature in their own interface, but I was only able to browse.
- Q4: If I was working in a rush, having to scroll through the entire menu or search stuff up would take time, rather than just having everything on the menu listed at once
- Q5: One change that would help me the most is have popups to have instructions for mini tasks whenever I am going through the application.

**Observed behavior**

- Signed in easily with google
- Tried to click on the menu items to add to cart as customer when they couldn't
- Had to briefly give a little help when making orders and adding items to order
- Switching between different interfaces was easy for them
- Tried to break website, but could not get any refresh errors

---
### Participant C — cashier

- **Session owner:** Dishant Patel
- **Date:** 2026-11-15
- **Task completed without help:** partial

**Likert**

| # | Score (1–5 or N/A) | Comment |
|---|--------------------|---------|
| 1 | 4 | Google sign-in was easy, though it wasn't obvious right away whether it would log me in as staff or a customer. |
| 2 | 4 | Once the POS screen loaded with the order panel on the side, I could tell I was on the cashier side. |
| 3 | 5 | Item names and prices were easy to spot and read on the menu grid. |
| 4 | 4 | Adding items and seeing the subtotal update worked fine once I realized I had to start/select an order first. |
| 5 | N/A | |
| 6 | 3 | I saw there was no checkout button as a customer, so I figured I couldn't order, though there wasn't a message telling me why. |
| 7 | 3 | A small prompt on the empty order state and a notice on the customer menu would have saved a few seconds of guessing. |

**Quotes**

- Q1: "I saw the Sign in with Google button right away, so I figured I just needed to log in before seeing anything."
- Q2: "I paused for a second when switching to the customer view because I was looking for an 'Add to Cart' button or a message saying online ordering isn't live yet."
- Q3: "As a customer, I expected to be able to click an item to customize a bowl or plate, add it to a bag, and check out."
- Q4: "During a rush, having text-only menu buttons without photos or quick category filters would make me scan a little slower."
- Q5: "Adding a clear banner on the customer page saying 'Menu preview only — ordering coming soon' so people don't keep clicking items expecting a cart."

**Observed behavior**

- Signed in with Google smoothly on the first try.
- Briefly hovered over the empty order panel before clicking "New Order" / adding Orange Chicken and Fried Rice, then read the subtotal accurately out loud.
- Successfully signed out and switched to the customer account; clicked on two menu items expecting a modal or cart drawer to open before realizing the customer view is currently browse-only.

---



### Participant D — manager

- **Session owner:** Ayaan Budhwani
- **Date:** 2026-11-15
- **Task completed without help:** partial
 
**Likert**

| # | Score (1–5 or N/A) | Comment |
|---|--------------------|---------|
| 1 | 5 | Signing in with Google was easy, and the login process was straightforward. |
| 2 | 4 | I could tell I was on the manager page, but it took a few seconds to understand the layout. |
| 3 | 5 | The menu items and prices were clearly displayed and easy to find. |
| 4 | N/A | Cashier-only question. |
| 5 | 4 | The inventory screen was organized, but the stock levels could have been explained more clearly. |
| 6 | 3 | I could browse the customer menu, but I initially expected to be able to place an order. |
| 7 | 4 | Most tasks were easy to complete, but some additional instructions would have been helpful. |

**Quotes**

- Q1: "I thought I needed to sign in first and then use the manager dashboard to check inventory and manage the store."
- Q2: "I hesitated on the inventory page because I wasn't sure if I could update the stock or just view it."
- Q3: "I expected to select food, add it to a cart, and place an order, but I could only browse the menu."
- Q4: "If the store was busy, having to look through the inventory without a quick way to find specific items could slow me down."
- Q5: "I would add clearer buttons and instructions, especially on the inventory page and customer menu, so users know what they can do."

**Observed behavior**

- Signed in successfully using Google and navigated to the manager inventory screen.
- Spent some time reviewing the inventory before understanding the displayed information.
- Initially seemed unsure whether inventory quantities could be edited or only viewed.
- Signed out and switched to the customer account without assistance.
- Clicked on menu items expecting to add them to a cart or begin an order.
- Completed most tasks without assistance but expressed confusion about the customer ordering functionality.
- Suggested clearer inventory labels and a notice explaining that customer ordering is not yet available.

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

Sprint 1 velocity was 26 points planned and 26 completed. Sprint 2 is sized at **26 points** (same pace).

| Issue | Story | Points | Why it is in Sprint 2 |
|-------|--------|--------|------------------------|
| #21 | Personal customer cart | 5 | Sprint goal: customer can build an order. Customers cannot add items today. |
| #22 | Submit personal pickup order | 5 | Sprint goal: customer places an order. `POST /api/orders` is cashier-only. |
| #23 | Create shared cart + join code | 3 | Sprint goal: start the novel feature. |
| #24 | Join shared cart and add items | 5 | Sprint goal: a second customer can join. Group submit stays in Sprint 3 (#11). |
| #25 | Manager sales summary | 3 | Manager can only view inventory today. Separate from the inventory screen. |
| #26 | Clearer customer menu + item pictures | 5 | PM / kickoff feedback: customer screen needs pics and clearer browse layout. Also absorbs study-1 label fixes. |

Closed as duplicates before this plan: #8, #9, #10, #14, #18, #19, #20. Sprint 3 epics #11–#13 stay epic placeholders.

If a design move does not fit #21–#26, change that issue's acceptance criteria or add one small story and drop or shrink another so the sprint stays near 23 points.

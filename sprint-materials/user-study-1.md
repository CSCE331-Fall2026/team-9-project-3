# User Study 1

Live Sprint 1 MVP: https://team-9-project-3-src.vercel.app/

## Study Goals

- Learn whether a cashier can sign in with Google, tell that they landed in the cashier POS (not manager or customer), find menu items and prices, start an order, add items, and read the running subtotal without help.
- Learn whether a customer can sign in, browse the live menu, and correctly understand that ordering is not available yet, and what they expect the next step to be.
- Learn which labels, empty states, or role switches confuse people, so Sprint 2 customer cart and shared-cart work does not copy those problems.

## Questions Asked

### Likert-scale

Scale: Strongly disagree, Disagree, Neutral, Agree, Strongly agree (recorded as 1–5).

1. I could tell how to sign in and whether I was an employee or a customer. — Strongly disagree, Disagree, Neutral, Agree, Strongly agree
2. After login, I knew which interface I was in (cashier, manager, or customer). — Strongly disagree, Disagree, Neutral, Agree, Strongly agree
3. I could find item names and prices on the menu without help. — Strongly disagree, Disagree, Neutral, Agree, Strongly agree
4. I could start an order, add items, and read the subtotal. (Cashier sessions; manager sessions mark N/A.) — Strongly disagree, Disagree, Neutral, Agree, Strongly agree
5. I could tell what the inventory screen was showing. (Manager sessions; cashier sessions mark N/A.) — Strongly disagree, Disagree, Neutral, Agree, Strongly agree
6. I understood that a customer can browse but cannot submit an order yet. — Strongly disagree, Disagree, Neutral, Agree, Strongly agree
7. I could complete the tasks without wanting extra instructions on the screen. — Strongly disagree, Disagree, Neutral, Agree, Strongly agree

### Open-ended / interview

1. What did you think you were supposed to do on the first screen?
2. Where did you hesitate, and what were you looking for?
3. As a customer, what did you expect to be able to do that you could not?
4. If you were working a rush, what on this screen would slow you down?
5. What is the one change that would have helped you most?

## Participant Results & Observations

### Participant A — cashier (session run by: Jack Berry)

- Date: 2026-11-15 · Task completed without help: partial
- Likert ratings:
  - 1: 5 (Agree/Strongly agree) — Found Google sign-in quickly and completed login without help.
  - 2: 4 — Knew it was the cashier POS once the menu and order panel appeared; unsure for a moment whether manager was a separate login.
  - 3: 5 — Found Orange Chicken and Fried Rice by category tabs and read prices out loud.
  - 4: 3 — Did not notice “New order” at first; tried to tap menu items before an order existed. Subtotal made sense after the first add.
  - 5: N/A
  - 6: 2 — As a customer, kept looking for Add / checkout and thought the app was broken until told browse-only is intentional.
  - 7: 3 — Wanted clearer empty-order and customer-browse messaging so the next step is obvious.
- Notable quotes:
  - "The first thing I saw was Sign in with Google, so I assumed that was the door into the POS."
  - "I hesitated on the cashier screen because the order side looked empty and I wasn't sure if I was supposed to pick food first or start an order first."
  - "On the customer menu I expected pictures of the food and a button to add something to a bag. Right now it feels like a price list."
  - "In a rush I'd lose time hunting for New order and scanning text-only cards with no photos."
  - "Put photos on the customer menu and a short line that says ordering isn't available yet, plus make New order harder to miss for cashiers."
- Observed behavior (hesitations, errors, friction):
  - Signed in as cashier on the first try; landed on cashier menu with the order panel visible but empty.
  - Clicked two entrees before starting an order; no lines appeared until after New order, then Orange Chicken and Fried Rice added and subtotal read correctly.
  - After sign-out / customer sign-in, scrolled `/menu`, used a category tab, clicked items expecting a cart; no add controls appeared. Asked whether the site was unfinished.
  - Brief help given once: pointed at New order after ~45 seconds of tapping items with no effect.

### Participant B — cashier (session run by: Nevin Kalloor)

- Date: 2026-11-15 · Task completed without help: partial
- Likert ratings:
  - 1: 5 — The google sign in was easier than a normal sign in since memorizing a password was not necessary.
  - 2: 3 — Could tell it was the cashier panel but had to look around; wanted a big bold “cashier” label.
  - 3: 5 — Found items and prices easily with category tabs and the search bar.
  - 4: 4 — Initially confused about making an order and which buttons to press first; figured it out after a bit of practice.
  - 5: N/A
  - 6: 3 — Looked for a plus button like food apps use; was then told the customer view is browse-only.
  - 7: 4 — Most tasks were self-explanatory; popups for mini tasks would still help.
- Notable quotes:
  - It was obvious that a sign in was needed for the very first task and having google made it a lot easier to sign in with an account rather than filling details on my own.
  - I hesitated a little bit when I saw the cashier interface for the first time, as I didn't know if it was the right interface initially since there was no big bold word saying cashier interface but rather I had to use context clues like "return to cashier homepage".
  - As a customer, I expected to add orders myself since most restaurant apps have that feature in their own interface, but I was only able to browse.
  - If I was working in a rush, having to scroll through the entire menu or search stuff up would take time, rather than just having everything on the menu listed at once.
  - One change that would help me the most is have popups to have instructions for mini tasks whenever I am going through the application.
- Observed behavior (hesitations, errors, friction):
  - Signed in easily with Google.
  - Tried to click menu items to add to cart as a customer when they couldn't.
  - Needed a little help making orders and adding items.
  - Switching between interfaces was easy for them.
  - Tried to break the site; no refresh errors.

### Participant C — cashier (session run by: Dishant Patel)

- Date: 2026-11-15 · Task completed without help: partial
- Likert ratings:
  - 1: 4 — Google sign-in was easy, though it wasn't obvious right away whether it would log in as staff or a customer.
  - 2: 4 — Once the POS screen loaded with the order panel on the side, could tell it was the cashier side.
  - 3: 5 — Item names and prices were easy to spot on the menu grid.
  - 4: 4 — Adding items and seeing the subtotal update worked fine once they realized they had to start/select an order first.
  - 5: N/A
  - 6: 3 — Saw there was no checkout button as a customer, so figured they couldn't order, though there wasn't a message saying why.
  - 7: 3 — A small prompt on the empty order state and a notice on the customer menu would have saved a few seconds of guessing.
- Notable quotes:
  - "I saw the Sign in with Google button right away, so I figured I just needed to log in before seeing anything."
  - "I paused for a second when switching to the customer view because I was looking for an 'Add to Cart' button or a message saying online ordering isn't live yet."
  - "As a customer, I expected to be able to click an item to customize a bowl or plate, add it to a bag, and check out."
  - "During a rush, having text-only menu buttons without photos or quick category filters would make me scan a little slower."
  - "Adding a clear banner on the customer page saying 'Menu preview only — ordering coming soon' so people don't keep clicking items expecting a cart."
- Observed behavior (hesitations, errors, friction):
  - Signed in with Google smoothly on the first try.
  - Briefly hovered over the empty order panel before clicking "New Order" / adding Orange Chicken and Fried Rice, then read the subtotal accurately out loud.
  - Signed out and switched to the customer account; clicked on two menu items expecting a modal or cart drawer before realizing browse-only.

### Participant D — manager (session run by: Ayaan Budhwani)

- Date: 2026-11-15 · Task completed without help: partial
- Likert ratings:
  - 1: 5 — Signing in with Google was easy and straightforward.
  - 2: 4 — Could tell it was the manager page, but took a few seconds to understand the layout.
  - 3: 5 — Menu items and prices were clearly displayed and easy to find.
  - 4: N/A — Cashier-only question.
  - 5: 4 — Inventory screen was organized, but stock levels could have been explained more clearly.
  - 6: 3 — Could browse the customer menu, but initially expected to place an order.
  - 7: 4 — Most tasks were easy; some additional instructions would have helped.
- Notable quotes:
  - "I thought I needed to sign in first and then use the manager dashboard to check inventory and manage the store."
  - "I hesitated on the inventory page because I wasn't sure if I could update the stock or just view it."
  - "I expected to select food, add it to a cart, and place an order, but I could only browse the menu."
  - "If the store was busy, having to look through the inventory without a quick way to find specific items could slow me down."
  - "I would add clearer buttons and instructions, especially on the inventory page and customer menu, so users know what they can do."
- Observed behavior (hesitations, errors, friction):
  - Signed in successfully and opened manager inventory.
  - Spent time reviewing inventory before understanding what it showed; unsure if quantities were editable or view-only.
  - Signed out and switched to customer without help; clicked menu items expecting a cart / order.
  - Completed most tasks without assistance but was confused that customer ordering wasn’t available.
  - Suggested clearer inventory labels and a notice that customer ordering isn’t live yet.

Cross-session scoreboard (same 1–5 scale):

| Likert item | A | B | C | D |
|-------------|---|---|---|---|
| 1 Sign-in | 5 | 5 | 4 | 5 |
| 2 Knew interface | 4 | 3 | 4 | 4 |
| 3 Names and prices | 5 | 5 | 5 | 5 |
| 4 Built an order | 3 | 4 | 4 | N/A |
| 5 Read inventory | N/A | N/A | N/A | 4 |
| 6 Customer cannot order yet | 2 | 3 | 3 | 3 |
| 7 Enough on-screen guidance | 3 | 4 | 3 | 4 |

## Proposed Design Moves (prioritized)

| Priority | Proposed change | Evidence / justification | Sprint impact |
|----------|-----------------|--------------------------|---------------|
| 1 | Ship customer cart + submit, and say clearly when the menu is still browse-only until that lands | A (score 2; thought the app was unfinished), B (looked for a plus button), C (“Add to Cart” / banner idea), D (clicked items expecting a cart). All four expected ordering. | #21 personal cart, #22 submit order; short browse-only copy until those ship. Matches the Sprint 2 demo. |
| 2 | Put food pictures on the customer menu and make the cards easier to scan | A (“feels like a price list”; wants photos), C (text-only slower in a rush; wants pics). B and D still expected normal add-to-bag ordering. | #26 customer menu pics + clearer layout. |
| 3 | Make “New order” / empty order state obvious for cashiers; clarify manager inventory is view-only | A (missed New order ~45s), B (button-order confusion), C (empty panel pause), D (unsure if inventory editable). | Small cashier empty-state copy while building #21–#22; optional inventory helper text near #25. |

Sprint 2 backlog after these moves: issues #21–#26 (26 points; Sprint 1 velocity was also 26). Group checkout stays in Sprint 3 (#11).

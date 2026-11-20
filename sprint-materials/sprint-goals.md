# Sprint Goals

*Instructions: For each sprint, write a one or two sentence goal plus the single
demoable outcome that proves the sprint succeeded. Keep in mind that you will
conduct user studies after sprints 1 and 2 to get feedback to inform future
development, so define MVP goals to be most helpful for that. The sprint 3 MVP
should be your final product.*

## Sprint 1
**Goal:** Deploy a working web MVP where users authenticate with Google OAuth2 and a cashier can view the live menu and build a basic order against our existing Postgres database.
**Outcome:** On the deployed app (Vercel + Render), an employee logs in, sees menu items with prices, and adds items to an order.

## Sprint 2
**Goal:** Add the customer ordering path and begin group/shared cart ordering, incorporating feedback from user study 1.
**Outcome (planned):** Demo a customer browsing and placing an order, plus create/join a shared cart by code (even if multi-user sync is still limited).
**Outcome (delivered for feedback):** Customer Browse with item pictures + personal My cart → Place pickup order (Google-linked) + create Shared cart join code; manager Sales summary. Join/add shared cart (#24) still open.
**Backlog:** Issues #21–#27, 28 story points planned; **23 completed** (#21, #22, #23, #25, #26, #27). #24 remains for Sprint 3 with group checkout (#11).

## Sprint 3
**Goal:** Finish group/shared cart ordering, meet WCAG 2.2 Level AA, and ship the final product with manager capabilities and polish from user study 2.
**Outcome:** Demo two customers on one shared cart submitting a single pickup order on the fully deployed final application.

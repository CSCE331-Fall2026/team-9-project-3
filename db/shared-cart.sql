-- Shared (group) carts for the novel feature (Sprint 2, issue #23).
-- A customer creates one and gets a short join code to give to friends.
-- Items in a shared cart come in a later story (#24), so this table only holds the cart itself.
-- Safe to run more than once.

CREATE TABLE IF NOT EXISTS shared_cart (
  id SERIAL PRIMARY KEY,
  join_code VARCHAR(8) UNIQUE NOT NULL,
  host_email VARCHAR(100) NOT NULL,
  host_name VARCHAR(100),
  status VARCHAR(20) NOT NULL DEFAULT 'open',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

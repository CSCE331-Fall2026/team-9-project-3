-- Shared (group) carts for the novel feature (Sprint 2, issue #23).
-- A customer creates one and gets a short join code to give to friends.
-- Items in a shared cart (Sprint 2, issue #24) live in shared_cart_item, one row per menu item
-- per person, so the host and everyone who joins add to the same cart.
-- Safe to run more than once.

CREATE TABLE IF NOT EXISTS shared_cart (
  id SERIAL PRIMARY KEY,
  join_code VARCHAR(8) UNIQUE NOT NULL,
  host_email VARCHAR(100) NOT NULL,
  host_name VARCHAR(100),
  status VARCHAR(20) NOT NULL DEFAULT 'open',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS shared_cart_item (
  id SERIAL PRIMARY KEY,
  shared_cart_id INT NOT NULL REFERENCES shared_cart (id),
  menu_item_id INT NOT NULL REFERENCES menu_item (id),
  added_by_email VARCHAR(100) NOT NULL,
  added_by_name VARCHAR(100),
  quantity INT NOT NULL DEFAULT 1 CHECK (quantity > 0),
  added_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS shared_cart_item_cart_idx ON shared_cart_item (shared_cart_id);

-- One line per person per menu item per cart. addItem relies on this: adding the same item
-- again adds 1 to quantity instead of making a second row, even if two taps arrive together.
CREATE UNIQUE INDEX IF NOT EXISTS shared_cart_item_one_per_person_idx
  ON shared_cart_item (shared_cart_id, menu_item_id, LOWER(added_by_email));

-- The one store every order belongs to (Sprint 1 create order story).
-- orders.store_id must point at a store row, so orders cannot be saved without this.
-- Same row as Project 2. Safe to run more than once.

INSERT INTO store (id, address, city, state, zipcode, phone_number) VALUES
  (1, '123 University Dr', 'College Station', 'TX', 77840, '979-555-0100')
ON CONFLICT (id) DO NOTHING;

-- Employee accounts for Google OAuth (Sprint 1).
-- Email must match the Google account used to sign in.

CREATE TABLE IF NOT EXISTS employee (
  id INT PRIMARY KEY,
  email VARCHAR(100) UNIQUE NOT NULL,
  name VARCHAR(100) NOT NULL,
  role VARCHAR(20) NOT NULL CHECK (role IN ('cashier', 'manager')),
  is_active BOOL NOT NULL DEFAULT TRUE
);

-- Replace these emails with real Google accounts before demo.
INSERT INTO employee (id, email, name, role, is_active) VALUES
  (1, 'jack.cfc@icloud.com', 'Jack Berry', 'manager', TRUE),
  (2, 'cashier@example.com', 'Demo Cashier', 'cashier', TRUE),
  (3, 'manager@example.com', 'Demo Manager', 'manager', TRUE)
ON CONFLICT (email) DO UPDATE
SET name = EXCLUDED.name,
    role = EXCLUDED.role,
    is_active = EXCLUDED.is_active;

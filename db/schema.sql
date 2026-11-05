-- Tables are created in an order Postgres accepts.
-- A table is created only after the tables it points to already exist.

CREATE TABLE customer (
  id INT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  phone VARCHAR(100),
  email VARCHAR(100) UNIQUE NOT NULL,
  loyalty_points INT DEFAULT 0
);

CREATE TABLE store (
  id INT PRIMARY KEY,
  address VARCHAR(200) NOT NULL,
  city VARCHAR(100) NOT NULL,
  state VARCHAR(100) NOT NULL,
  zipcode INT NOT NULL,
  phone_number VARCHAR(20)
);

CREATE TABLE menu_item (
  id INT PRIMARY KEY,
  name VARCHAR(100) NOT NULL UNIQUE,
  category VARCHAR(100) NOT NULL,
  base_price FLOAT NOT NULL,
  is_available BOOL NOT NULL DEFAULT true,
  calorie INT,
  protein INT,
  fat INT,
  carbohydrates INT,
  allergens VARCHAR(200)
);

CREATE TABLE inventory_item (
  id INT PRIMARY KEY,
  shelf_life INT,
  store_id INT NOT NULL REFERENCES store(id),
  name VARCHAR(100) NOT NULL,
  storage_location VARCHAR(100) NOT NULL,
  unit_of_measure VARCHAR(50) NOT NULL,
  qty_on_hand FLOAT NOT NULL DEFAULT 0,
  reorder_threshold FLOAT NOT NULL,
  cost_per_unit FLOAT
);

CREATE TABLE orders (
  id INT PRIMARY KEY,
  customer_id INT REFERENCES customer(id),
  store_id INT REFERENCES store(id) NOT NULL,
  order_type VARCHAR(100) NOT NULL,
  status VARCHAR(100) NOT NULL DEFAULT 'placed',
  total FLOAT NOT NULL,
  discount FLOAT DEFAULT 0,
  payment_method VARCHAR(100) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP
);

CREATE TABLE order_item (
  id INT PRIMARY KEY,
  order_id INT NOT NULL REFERENCES orders(id),
  menu_item_id INT NOT NULL REFERENCES menu_item(id),
  quantity INT NOT NULL DEFAULT 1,
  custom VARCHAR(200),
  unit_price FLOAT NOT NULL
);

CREATE TABLE payment (
  id INT PRIMARY KEY,
  order_id INT NOT NULL REFERENCES orders(id),
  type VARCHAR(100) NOT NULL,
  amount FLOAT NOT NULL,
  status BOOL NOT NULL DEFAULT false
);

CREATE TABLE recipe_line (
  id INT PRIMARY KEY,
  menu_item_id INT NOT NULL REFERENCES menu_item(id),
  inventory_id INT NOT NULL REFERENCES inventory_item(id),
  qty_used FLOAT NOT NULL
);

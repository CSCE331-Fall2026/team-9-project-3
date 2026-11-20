-- Sprint 2 #26: optional image path for each menu item.
-- Official Panda Express product photos live under src/web/public/menu/items/.

ALTER TABLE menu_item
  ADD COLUMN IF NOT EXISTS image_url VARCHAR(300);

UPDATE menu_item SET image_url = '/menu/items/' || id || '.jpg'
WHERE id IN (1,2,3,4,5,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27);

-- Hidden category header row stays without a food picture.
UPDATE menu_item SET image_url = NULL WHERE id = 6;

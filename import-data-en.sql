TRUNCATE TABLE inventory, ware_house, product, category CASCADE;

-- Add 3 Categories
INSERT INTO category (id, name, description, created_at, updated_at) VALUES 
(1, 'Men''s T-Shirts', 'Casual short-sleeve t-shirts for men', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Women''s Jeans', 'Fashionable wide-leg and skinny jeans', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Accessories', 'Bags, backpacks, and belts', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Reset sequence to avoid ID collision
SELECT setval('category_id_seq', (SELECT MAX(id) FROM category));

-- Add 10 Products
INSERT INTO product (id, name, price, category_id, created_at, updated_at) VALUES 
(1, 'Classic Polo Shirt', 25, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Basic Crewneck T-Shirt', 12, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Graphic Print T-Shirt', 18, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Colorblock Polo', 22, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'High-Waisted Baggy Jeans', 35, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'Ripped Knee Skinny Jeans', 32, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 'Vintage Flare Jeans', 28, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 'Unisex Leather Backpack', 45, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 'Mini Leather Wallet', 15, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 'Genuine Leather Belt', 20, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

SELECT setval('product_id_seq', (SELECT MAX(id) FROM product));

-- Add Warehouses
INSERT INTO ware_house (id, name, address, location_code, is_active, created_at, updated_at) VALUES 
(1, 'Ho Chi Minh Warehouse', 'District 1, HCMC', 'HCM', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Hanoi Warehouse', 'Cau Giay, Hanoi', 'HN', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

SELECT setval('ware_house_id_seq', (SELECT MAX(id) FROM ware_house));

-- Add Inventory
INSERT INTO inventory (product_id, warehouse_id, quantity, version, created_at, updated_at) VALUES 
(1, 1, 100, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), 
(2, 1, 100, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), 
(3, 1, 100, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), 
(4, 1, 100, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), 
(5, 2, 100, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 2, 100, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), 
(7, 2, 100, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), 
(8, 2, 100, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), 
(9, 2, 100, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), 
(10, 2, 100, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

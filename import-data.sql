-- Thêm 3 Category (Quần áo)
INSERT INTO category (name, description, created_at, updated_at) VALUES 
('Áo Thun Nam', 'Áo phông nam ngắn tay năng động', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Quần Jean Nữ', 'Quần ống rộng thời trang', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Phụ Kiện', 'Túi xách và balo', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Thêm 10 Product (với category_id ngẫu nhiên tương ứng 1, 2, 3)
INSERT INTO product (name, price, category_id, created_at, updated_at) VALUES 
('Áo thun nam Polo Cổ Bẻ', 250000, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Áo thun nam basic cổ tròn', 120000, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Áo thun nam in hình khủng long', 180000, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Áo polo nam phối màu', 220000, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Quần jean nữ ống rộng Baggy', 350000, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Quần jean nữ rách gối', 320000, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Quần bò nữ ống loe', 280000, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Balo da thời trang nam nữ', 450000, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Ví da mini nữ cầm tay', 150000, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Thắt lưng da bò thật', 200000, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Bổ sung luôn kho hàng (Warehouse) và Inventory cho các sản phẩm này
INSERT INTO ware_house (name, address, location_code, is_active, created_at, updated_at) VALUES 
('Kho Hồ Chí Minh', 'Quận 1, TP.HCM', 'HCM', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Kho Hà Nội', 'Cầu Giấy, Hà Nội', 'HN', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Thêm Inventory cho 10 sản phẩm (mỗi cái tồn kho 100 cái)
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

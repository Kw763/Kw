-- 测试数据初始化（幂等）：3个测试账号 + 商家 + 商品 + 用户地址
SET @pwd := '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2';

INSERT INTO sys_user (user_id, dept_id, user_name, nick_name, email, phonenumber, sex, avatar, password, status, del_flag, login_ip, login_date, create_by, create_time, update_by, update_time, remark)
SELECT 3, 103, 't_user', '测试用户', 'u@t.com', '13800000003', '0', '', @pwd, '0', '0', '', NULL, 'admin', sysdate(), '', NULL, '测试用户'
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE user_name = 't_user');

INSERT INTO sys_user (user_id, dept_id, user_name, nick_name, email, phonenumber, sex, avatar, password, status, del_flag, login_ip, login_date, create_by, create_time, update_by, update_time, remark)
SELECT 4, 103, 't_merchant', '测试商家', 'm@t.com', '13800000004', '0', '', @pwd, '0', '0', '', NULL, 'admin', sysdate(), '', NULL, '测试商家'
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE user_name = 't_merchant');

INSERT INTO sys_user (user_id, dept_id, user_name, nick_name, email, phonenumber, sex, avatar, password, status, del_flag, login_ip, login_date, create_by, create_time, update_by, update_time, remark)
SELECT 5, 103, 't_rider', '测试骑手', 'r@t.com', '13800000005', '0', '', @pwd, '0', '0', '', NULL, 'admin', sysdate(), '', NULL, '测试骑手'
WHERE NOT EXISTS (SELECT 1 FROM sys_user WHERE user_name = 't_rider');

-- 角色分配
INSERT IGNORE INTO sys_user_role (user_id, role_id) VALUES (3, 100);
INSERT IGNORE INTO sys_user_role (user_id, role_id) VALUES (4, 101);
INSERT IGNORE INTO sys_user_role (user_id, role_id) VALUES (5, 102);

-- 商家
INSERT INTO takeout_merchant (merchant_id, merchant_name, owner_user_id, contact_phone, address, status, create_by, create_time)
SELECT 1, '测试快餐店', 4, '13800000004', '测试路1号', '0', 'admin', sysdate()
WHERE NOT EXISTS (SELECT 1 FROM takeout_merchant WHERE merchant_id = 1);

-- 商品
INSERT INTO takeout_product (product_id, merchant_id, product_name, price, image, status, create_by, create_time)
SELECT 1, 1, '招牌汉堡', 25.00, '', '0', 'admin', sysdate()
WHERE NOT EXISTS (SELECT 1 FROM takeout_product WHERE product_id = 1);
INSERT INTO takeout_product (product_id, merchant_id, product_name, price, image, status, create_by, create_time)
SELECT 2, 1, '可乐', 8.00, '', '0', 'admin', sysdate()
WHERE NOT EXISTS (SELECT 1 FROM takeout_product WHERE product_id = 2);

-- 用户地址
INSERT INTO takeout_address (address_id, user_id, receiver_name, receiver_phone, province, city, district, detail_address, is_default, create_by, create_time)
SELECT 1, 3, '张三', '13800000003', '北京市', '北京市', '朝阳区', '望京SOHO', 1, 'admin', sysdate()
WHERE NOT EXISTS (SELECT 1 FROM takeout_address WHERE address_id = 1);

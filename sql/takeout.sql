-- 外卖业务初始化脚本（MySQL 8.0，可重复执行）
-- 主数据采用 del_flag 逻辑删除；订单、支付、配送任务和状态日志不提供物理删除。

create table if not exists takeout_merchant (
    merchant_id       bigint(20)    not null auto_increment comment '商家ID',
    merchant_name     varchar(100)  not null comment '商家名称',
    owner_user_id     bigint(20)    not null comment '商家所属用户ID',
    contact_phone     varchar(20)   default null comment '联系电话',
    logo              varchar(255)  default null comment 'Logo',
    address           varchar(255)  not null comment '商家地址',
    open_time         varchar(5)    not null default '00:00' comment '营业开始时间HH:mm',
    close_time        varchar(5)    not null default '23:59' comment '营业结束时间HH:mm',
    notice            varchar(500)  default null comment '商家公告',
    status            char(1)       not null default '0' comment '营业状态（0营业 1休息）',
    del_flag          char(1)       not null default '0' comment '删除标志（0存在 2删除）',
    version           int(11)       not null default 0 comment '乐观锁版本',
    create_by         varchar(64)   default '' comment '创建者',
    create_time       datetime      default null comment '创建时间',
    update_by         varchar(64)   default '' comment '更新者',
    update_time       datetime      default null comment '更新时间',
    remark            varchar(500)  default null comment '备注',
    primary key (merchant_id),
    unique key uk_merchant_owner (owner_user_id, del_flag),
    key idx_merchant_status (status, del_flag)
) engine=innodb comment='外卖商家表';

create table if not exists takeout_product (
    product_id       bigint(20)    not null auto_increment comment '商品ID',
    merchant_id      bigint(20)    not null comment '商家ID',
    product_name     varchar(100)  not null comment '商品名称',
    category_name    varchar(50)   default null comment '分类名称',
    price            decimal(10,2) not null comment '商品价格',
    image            varchar(255)  default null comment '商品图片',
    description      varchar(500)  default null comment '商品描述',
    status           char(1)       not null default '0' comment '上架状态（0上架 1下架）',
    del_flag         char(1)       not null default '0' comment '删除标志（0存在 2删除）',
    version          int(11)       not null default 0 comment '乐观锁版本',
    create_by        varchar(64)   default '' comment '创建者',
    create_time      datetime      default null comment '创建时间',
    update_by        varchar(64)   default '' comment '更新者',
    update_time      datetime      default null comment '更新时间',
    remark           varchar(500)  default null comment '备注',
    primary key (product_id),
    key idx_product_merchant (merchant_id, status, del_flag)
) engine=innodb comment='外卖商品表';

create table if not exists takeout_address (
    address_id       bigint(20)   not null auto_increment comment '地址ID',
    user_id          bigint(20)   not null comment '所属用户ID',
    receiver_name    varchar(50)  not null comment '收货人',
    receiver_phone   varchar(20)  not null comment '收货电话',
    province         varchar(50)  not null comment '省',
    city             varchar(50)  not null comment '市',
    district         varchar(50)  not null comment '区县',
    detail_address   varchar(255) not null comment '详细地址',
    is_default       char(1)      not null default 'N' comment '是否默认（Y是 N否）',
    del_flag         char(1)      not null default '0' comment '删除标志（0存在 2删除）',
    create_by        varchar(64)  default '' comment '创建者',
    create_time      datetime     default null comment '创建时间',
    update_by        varchar(64)  default '' comment '更新者',
    update_time      datetime     default null comment '更新时间',
    remark           varchar(500) default null comment '备注',
    primary key (address_id),
    key idx_address_user (user_id, del_flag, is_default)
) engine=innodb comment='用户收货地址表';

create table if not exists takeout_order (
    order_id         bigint(20)    not null auto_increment comment '订单ID',
    order_no         varchar(64)   not null comment '订单号',
    request_no       varchar(64)   not null comment '用户请求号',
    user_id          bigint(20)    not null comment '下单用户ID',
    merchant_id      bigint(20)    not null comment '商家ID',
    receiver_name    varchar(50)   not null comment '收货人快照',
    receiver_phone   varchar(20)   not null comment '收货电话快照',
    delivery_address varchar(500)  not null comment '完整收货地址快照',
    total_amount     decimal(10,2) not null comment '订单总金额',
    status           varchar(20)   not null comment '订单状态',
    version          int(11)       not null default 0 comment '乐观锁版本',
    create_by        varchar(64)   default '' comment '创建者',
    create_time      datetime      default null comment '创建时间',
    update_by        varchar(64)   default '' comment '更新者',
    update_time      datetime      default null comment '更新时间',
    remark           varchar(500)  default null comment '订单备注',
    primary key (order_id),
    unique key uk_order_no (order_no),
    unique key uk_order_user_request (user_id, request_no),
    key idx_order_user (user_id, status, create_time),
    key idx_order_merchant (merchant_id, status, create_time)
) engine=innodb comment='外卖订单表';

create table if not exists takeout_order_item (
    item_id       bigint(20)    not null auto_increment comment '明细ID',
    order_id      bigint(20)    not null comment '订单ID',
    product_id    bigint(20)    not null comment '商品ID',
    product_name  varchar(100)  not null comment '商品名称快照',
    product_image varchar(255)  default null comment '商品图片快照',
    product_price decimal(10,2) not null comment '商品单价快照',
    quantity      int(11)       not null comment '数量',
    subtotal      decimal(10,2) not null comment '小计',
    primary key (item_id),
    unique key uk_order_product (order_id, product_id)
) engine=innodb comment='外卖订单明细表';

create table if not exists takeout_payment (
    payment_id     bigint(20)    not null auto_increment comment '支付ID',
    payment_no     varchar(64)   not null comment '支付单号',
    order_id       bigint(20)    not null comment '订单ID',
    user_id        bigint(20)    not null comment '支付用户ID',
    amount         decimal(10,2) not null comment '支付金额',
    status         varchar(20)   not null comment '支付状态',
    transaction_no varchar(64)   default null comment '模拟交易流水号',
    paid_time      datetime      default null comment '支付时间',
    version        int(11)       not null default 0 comment '乐观锁版本',
    create_by      varchar(64)   default '' comment '创建者',
    create_time    datetime      default null comment '创建时间',
    update_by      varchar(64)   default '' comment '更新者',
    update_time    datetime      default null comment '更新时间',
    primary key (payment_id),
    unique key uk_payment_no (payment_no),
    unique key uk_payment_order (order_id),
    unique key uk_payment_transaction (transaction_no)
) engine=innodb comment='外卖模拟支付单表';

create table if not exists takeout_delivery_task (
    task_id       bigint(20)  not null auto_increment comment '配送任务ID',
    task_no       varchar(64) not null comment '配送任务号',
    order_id      bigint(20)  not null comment '订单ID',
    rider_id      bigint(20)  default null comment '骑手用户ID',
    status        varchar(20) not null comment '配送状态',
    merchant_name varchar(100) default '' comment '商家名称快照',
    merchant_address varchar(255) default '' comment '商家地址快照',
    receiver_name varchar(50) default '' comment '收件人快照',
    receiver_phone varchar(20) default '' comment '收件电话快照',
    delivery_address varchar(500) default '' comment '收货地址快照',
    accept_time   datetime    default null comment '接单时间',
    arrive_time   datetime    default null comment '到店时间',
    pickup_time   datetime    default null comment '取餐时间',
    deliver_time  datetime    default null comment '送达时间',
    version       int(11)     not null default 0 comment '乐观锁版本',
    create_by     varchar(64) default '' comment '创建者',
    create_time   datetime    default null comment '创建时间',
    update_by     varchar(64) default '' comment '更新者',
    update_time   datetime    default null comment '更新时间',
    remark        varchar(500) default null comment '备注',
    primary key (task_id),
    unique key uk_delivery_no (task_no),
    unique key uk_delivery_order (order_id),
    key idx_delivery_rider (rider_id, status, create_time),
    key idx_delivery_status (status, create_time)
) engine=innodb comment='外卖配送任务表';

create table if not exists takeout_status_log (
    log_id        bigint(20)  not null auto_increment comment '日志ID',
    biz_type      varchar(20) not null comment '业务类型ORDER/PAYMENT/DELIVERY',
    biz_id        bigint(20)  not null comment '业务ID',
    from_status   varchar(20) not null default '' comment '原状态，创建时为空字符串',
    to_status     varchar(20) not null comment '目标状态',
    operator_id   bigint(20)  default null comment '操作人ID',
    operator_name varchar(64) default '' comment '操作人名称',
    create_by     varchar(64) default '' comment '创建者',
    create_time   datetime    not null comment '创建时间',
    remark        varchar(500) default null comment '备注',
    primary key (log_id),
    unique key uk_status_log_event (biz_type, biz_id, from_status, to_status),
    key idx_status_log_biz (biz_type, biz_id, create_time)
) engine=innodb comment='外卖业务状态日志表';

-- 订单、配送、支付状态字典
insert into sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark)
select '外卖订单状态', 'takeout_order_status', '0', 'admin', sysdate(), '外卖订单状态'
where not exists (select 1 from sys_dict_type where dict_type = 'takeout_order_status');
insert into sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark)
select '外卖配送状态', 'takeout_delivery_status', '0', 'admin', sysdate(), '外卖配送状态'
where not exists (select 1 from sys_dict_type where dict_type = 'takeout_delivery_status');
insert into sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark)
select '外卖支付状态', 'takeout_payment_status', '0', 'admin', sysdate(), '外卖模拟支付状态'
where not exists (select 1 from sys_dict_type where dict_type = 'takeout_payment_status');
insert into sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark)
select '外卖商家状态', 'takeout_merchant_status', '0', 'admin', sysdate(), '外卖商家营业状态'
where not exists (select 1 from sys_dict_type where dict_type = 'takeout_merchant_status');
insert into sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark)
select '外卖商品状态', 'takeout_product_status', '0', 'admin', sysdate(), '外卖商品上架状态'
where not exists (select 1 from sys_dict_type where dict_type = 'takeout_product_status');

insert into sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark)
select 1, '待支付', 'WAIT_PAY', 'takeout_order_status', 'warning', 'N', '0', 'admin', sysdate(), ''
where not exists (select 1 from sys_dict_data where dict_type = 'takeout_order_status' and dict_value = 'WAIT_PAY');
insert into sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark)
select 2, '待接单', 'WAIT_ACCEPT', 'takeout_order_status', 'warning', 'N', '0', 'admin', sysdate(), ''
where not exists (select 1 from sys_dict_data where dict_type = 'takeout_order_status' and dict_value = 'WAIT_ACCEPT');
insert into sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark)
select 3, '备餐中', 'PREPARING', 'takeout_order_status', 'primary', 'N', '0', 'admin', sysdate(), ''
where not exists (select 1 from sys_dict_data where dict_type = 'takeout_order_status' and dict_value = 'PREPARING');
insert into sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark)
select 4, '待取餐', 'READY', 'takeout_order_status', 'primary', 'N', '0', 'admin', sysdate(), ''
where not exists (select 1 from sys_dict_data where dict_type = 'takeout_order_status' and dict_value = 'READY');
insert into sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark)
select 5, '配送中', 'DELIVERING', 'takeout_order_status', 'primary', 'N', '0', 'admin', sysdate(), ''
where not exists (select 1 from sys_dict_data where dict_type = 'takeout_order_status' and dict_value = 'DELIVERING');
insert into sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark)
select 6, '已完成', 'FINISHED', 'takeout_order_status', 'success', 'N', '0', 'admin', sysdate(), ''
where not exists (select 1 from sys_dict_data where dict_type = 'takeout_order_status' and dict_value = 'FINISHED');

insert into sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark)
select 1, '待派单', 'UNASSIGNED', 'takeout_delivery_status', 'info', 'N', '0', 'admin', sysdate(), ''
where not exists (select 1 from sys_dict_data where dict_type = 'takeout_delivery_status' and dict_value = 'UNASSIGNED');
insert into sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark)
select 2, '待骑手接单', 'ASSIGNED', 'takeout_delivery_status', 'warning', 'N', '0', 'admin', sysdate(), ''
where not exists (select 1 from sys_dict_data where dict_type = 'takeout_delivery_status' and dict_value = 'ASSIGNED');
insert into sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark)
select 3, '骑手已接单', 'ACCEPTED', 'takeout_delivery_status', 'primary', 'N', '0', 'admin', sysdate(), ''
where not exists (select 1 from sys_dict_data where dict_type = 'takeout_delivery_status' and dict_value = 'ACCEPTED');
insert into sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark)
select 4, '已到店', 'ARRIVED', 'takeout_delivery_status', 'primary', 'N', '0', 'admin', sysdate(), ''
where not exists (select 1 from sys_dict_data where dict_type = 'takeout_delivery_status' and dict_value = 'ARRIVED');
insert into sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark)
select 5, '已取餐', 'PICKED_UP', 'takeout_delivery_status', 'primary', 'N', '0', 'admin', sysdate(), ''
where not exists (select 1 from sys_dict_data where dict_type = 'takeout_delivery_status' and dict_value = 'PICKED_UP');
insert into sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark)
select 6, '已送达', 'DELIVERED', 'takeout_delivery_status', 'success', 'N', '0', 'admin', sysdate(), ''
where not exists (select 1 from sys_dict_data where dict_type = 'takeout_delivery_status' and dict_value = 'DELIVERED');

insert into sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark)
select 1, '未支付', 'UNPAID', 'takeout_payment_status', 'warning', 'Y', '0', 'admin', sysdate(), ''
where not exists (select 1 from sys_dict_data where dict_type = 'takeout_payment_status' and dict_value = 'UNPAID');
insert into sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark)
select 2, '支付成功', 'SUCCESS', 'takeout_payment_status', 'success', 'N', '0', 'admin', sysdate(), ''
where not exists (select 1 from sys_dict_data where dict_type = 'takeout_payment_status' and dict_value = 'SUCCESS');

insert into sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark)
select 1, '营业中', '0', 'takeout_merchant_status', 'success', 'Y', '0', 'admin', sysdate(), ''
where not exists (select 1 from sys_dict_data where dict_type = 'takeout_merchant_status' and dict_value = '0');
insert into sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark)
select 2, '休息中', '1', 'takeout_merchant_status', 'info', 'N', '0', 'admin', sysdate(), ''
where not exists (select 1 from sys_dict_data where dict_type = 'takeout_merchant_status' and dict_value = '1');

insert into sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark)
select 1, '上架', '0', 'takeout_product_status', 'success', 'Y', '0', 'admin', sysdate(), ''
where not exists (select 1 from sys_dict_data where dict_type = 'takeout_product_status' and dict_value = '0');
insert into sys_dict_data (dict_sort, dict_label, dict_value, dict_type, list_class, is_default, status, create_by, create_time, remark)
select 2, '下架', '1', 'takeout_product_status', 'info', 'N', '0', 'admin', sysdate(), ''
where not exists (select 1 from sys_dict_data where dict_type = 'takeout_product_status' and dict_value = '1');

-- 菜单按权限标识查找，所有菜单ID均由数据库自动生成。
drop procedure if exists takeout_init_menu;
delimiter $$
create procedure takeout_init_menu(
    in p_parent_id bigint,
    in p_menu_name varchar(50),
    in p_order_num int,
    in p_path varchar(200),
    in p_component varchar(255),
    in p_menu_type char(1),
    in p_perms varchar(100),
    in p_icon varchar(100)
)
begin
    declare v_menu_id bigint default null;
    select max(menu_id) into v_menu_id from sys_menu
    where parent_id = p_parent_id
      and ((p_perms <> '' and perms = p_perms)
           or (p_perms = '' and path = p_path and menu_name = p_menu_name));
    if v_menu_id is null then
        insert into sys_menu
            (menu_name, parent_id, order_num, path, component, query, route_name,
             is_frame, is_cache, menu_type, visible, status, perms, icon,
             create_by, create_time, update_by, remark)
        values
            (p_menu_name, p_parent_id, p_order_num, p_path, p_component, '', '',
             1, 0, p_menu_type, '0', '0', nullif(p_perms, ''), p_icon,
             'admin', sysdate(), '', '外卖业务初始化');
        set v_menu_id = last_insert_id();
    else
        update sys_menu set menu_name = p_menu_name, order_num = p_order_num,
            path = p_path, component = p_component, menu_type = p_menu_type,
            perms = nullif(p_perms, ''), icon = p_icon, status = '0', update_time = sysdate()
        where menu_id = v_menu_id;
    end if;
    select v_menu_id;
end$$
delimiter ;

set @takeout_root = 0;
call takeout_init_menu(0, '外卖管理', 5, 'takeout', null, 'M', '', 'shopping');
set @takeout_root = last_insert_id();
-- CALL 返回结果集时 LAST_INSERT_ID 在部分客户端不可依赖，按根菜单自然键重新读取。
set @takeout_root = (select max(menu_id) from sys_menu where parent_id = 0 and path = 'takeout' and menu_name = '外卖管理');

call takeout_init_menu(@takeout_root, '商家管理', 1, 'merchant', 'takeout/merchant/index', 'C', 'takeout:admin:merchant:list', 'peoples');
set @m_merchant = (select max(menu_id) from sys_menu where parent_id = @takeout_root and perms = 'takeout:admin:merchant:list');
call takeout_init_menu(@m_merchant, '商家查询', 1, '', '', 'F', 'takeout:admin:merchant:query', '#');
call takeout_init_menu(@m_merchant, '商家新增', 2, '', '', 'F', 'takeout:admin:merchant:add', '#');
call takeout_init_menu(@m_merchant, '商家修改', 3, '', '', 'F', 'takeout:admin:merchant:edit', '#');
call takeout_init_menu(@m_merchant, '商家删除', 4, '', '', 'F', 'takeout:admin:merchant:remove', '#');

call takeout_init_menu(@takeout_root, '商品管理', 2, 'product', 'takeout/product/index', 'C', 'takeout:admin:product:list', 'goods');
set @m_product = (select max(menu_id) from sys_menu where parent_id = @takeout_root and perms = 'takeout:admin:product:list');
call takeout_init_menu(@m_product, '商品查询', 1, '', '', 'F', 'takeout:admin:product:query', '#');
call takeout_init_menu(@m_product, '商品新增', 2, '', '', 'F', 'takeout:admin:product:add', '#');
call takeout_init_menu(@m_product, '商品修改', 3, '', '', 'F', 'takeout:admin:product:edit', '#');
call takeout_init_menu(@m_product, '商品删除', 4, '', '', 'F', 'takeout:admin:product:remove', '#');
call takeout_init_menu(@m_product, '商品上下架', 5, '', '', 'F', 'takeout:admin:product:status', '#');

call takeout_init_menu(@takeout_root, '地址管理', 3, 'address', 'takeout/address/index', 'C', 'takeout:admin:address:list', 'location');
set @m_address = (select max(menu_id) from sys_menu where parent_id = @takeout_root and perms = 'takeout:admin:address:list');
call takeout_init_menu(@m_address, '地址查询', 1, '', '', 'F', 'takeout:admin:address:query', '#');
call takeout_init_menu(@m_address, '地址新增', 2, '', '', 'F', 'takeout:admin:address:add', '#');
call takeout_init_menu(@m_address, '地址修改', 3, '', '', 'F', 'takeout:admin:address:edit', '#');
call takeout_init_menu(@m_address, '地址删除', 4, '', '', 'F', 'takeout:admin:address:remove', '#');

call takeout_init_menu(@takeout_root, '订单管理', 4, 'order', 'takeout/order/index', 'C', 'takeout:admin:order:list', 'list');
set @m_order = (select max(menu_id) from sys_menu where parent_id = @takeout_root and perms = 'takeout:admin:order:list');
call takeout_init_menu(@m_order, '订单查询', 1, '', '', 'F', 'takeout:admin:order:query', '#');

call takeout_init_menu(@takeout_root, '配送管理', 5, 'delivery', 'takeout/delivery/index', 'C', 'takeout:admin:delivery:list', 'send');
set @m_delivery = (select max(menu_id) from sys_menu where parent_id = @takeout_root and perms = 'takeout:admin:delivery:list');
call takeout_init_menu(@m_delivery, '配送查询', 1, '', '', 'F', 'takeout:admin:delivery:query', '#');
call takeout_init_menu(@m_delivery, '人工派单', 2, '', '', 'F', 'takeout:admin:delivery:assign', '#');
call takeout_init_menu(@m_delivery, '骑手选项', 3, '', '', 'F', 'takeout:admin:rider:options', '#');

call takeout_init_menu(@takeout_root, '状态日志', 6, 'status-log', 'takeout/statusLog/index', 'C', 'takeout:admin:log:list', 'log');
set @m_log = (select max(menu_id) from sys_menu where parent_id = @takeout_root and perms = 'takeout:admin:log:list');
call takeout_init_menu(@m_log, '日志查询', 1, '', '', 'F', 'takeout:admin:log:query', '#');

call takeout_init_menu(@takeout_root, '我的订单', 7, 'user-order', 'takeout/user/order', 'C', 'takeout:user:order:list', 'list');
set @m_user_order = (select max(menu_id) from sys_menu where parent_id = @takeout_root and perms = 'takeout:user:order:list');
call takeout_init_menu(@m_user_order, '订单详情', 1, '', '', 'F', 'takeout:user:order:query', '#');
call takeout_init_menu(@m_user_order, '创建订单', 2, '', '', 'F', 'takeout:user:order:add', '#');
call takeout_init_menu(@m_user_order, '模拟支付', 3, '', '', 'F', 'takeout:user:order:pay', '#');
call takeout_init_menu(@takeout_root, '我的地址', 8, 'user-address', 'takeout/user/address', 'C', 'takeout:user:address:list', 'location');
set @m_user_address = (select max(menu_id) from sys_menu where parent_id = @takeout_root and perms = 'takeout:user:address:list');
call takeout_init_menu(@m_user_address, '地址详情', 1, '', '', 'F', 'takeout:user:address:query', '#');
call takeout_init_menu(@m_user_address, '新增地址', 2, '', '', 'F', 'takeout:user:address:add', '#');
call takeout_init_menu(@m_user_address, '修改地址', 3, '', '', 'F', 'takeout:user:address:edit', '#');
call takeout_init_menu(@m_user_address, '删除地址', 4, '', '', 'F', 'takeout:user:address:remove', '#');
call takeout_init_menu(@m_user_address, '默认地址', 5, '', '', 'F', 'takeout:user:address:default', '#');

call takeout_init_menu(@takeout_root, '商家商品', 9, 'merchant-product', 'takeout/merchant/product', 'C', 'takeout:merchant:product:list', 'goods');
set @m_merchant_product = (select max(menu_id) from sys_menu where parent_id = @takeout_root and perms = 'takeout:merchant:product:list');
call takeout_init_menu(@m_merchant_product, '商品详情', 1, '', '', 'F', 'takeout:merchant:product:query', '#');
call takeout_init_menu(@m_merchant_product, '新增商品', 2, '', '', 'F', 'takeout:merchant:product:add', '#');
call takeout_init_menu(@m_merchant_product, '修改商品', 3, '', '', 'F', 'takeout:merchant:product:edit', '#');
call takeout_init_menu(@m_merchant_product, '删除商品', 4, '', '', 'F', 'takeout:merchant:product:remove', '#');
call takeout_init_menu(@m_merchant_product, '商品上下架', 5, '', '', 'F', 'takeout:merchant:product:status', '#');
call takeout_init_menu(@takeout_root, '商家订单', 10, 'merchant-order', 'takeout/merchant/order', 'C', 'takeout:merchant:order:list', 'list');
set @m_merchant_order = (select max(menu_id) from sys_menu where parent_id = @takeout_root and perms = 'takeout:merchant:order:list');
call takeout_init_menu(@m_merchant_order, '订单详情', 1, '', '', 'F', 'takeout:merchant:order:query', '#');
call takeout_init_menu(@m_merchant_order, '接受订单', 2, '', '', 'F', 'takeout:merchant:order:accept', '#');
call takeout_init_menu(@m_merchant_order, '完成备餐', 3, '', '', 'F', 'takeout:merchant:order:ready', '#');

call takeout_init_menu(@takeout_root, '骑手任务', 11, 'rider-delivery', 'takeout/rider/delivery', 'C', 'takeout:rider:delivery:list', 'send');
set @m_rider = (select max(menu_id) from sys_menu where parent_id = @takeout_root and perms = 'takeout:rider:delivery:list');
call takeout_init_menu(@m_rider, '任务详情', 1, '', '', 'F', 'takeout:rider:delivery:query', '#');
call takeout_init_menu(@m_rider, '骑手接单', 2, '', '', 'F', 'takeout:rider:delivery:accept', '#');
call takeout_init_menu(@m_rider, '确认到店', 3, '', '', 'F', 'takeout:rider:delivery:arrive', '#');
call takeout_init_menu(@m_rider, '确认取餐', 4, '', '', 'F', 'takeout:rider:delivery:pickup', '#');
call takeout_init_menu(@m_rider, '确认送达', 5, '', '', 'F', 'takeout:rider:delivery:deliver', '#');

drop procedure if exists takeout_init_menu;

insert into sys_role (role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, create_by, create_time, remark)
select '外卖用户', 'takeout_user', 10, '1', 1, 1, '0', '0', 'admin', sysdate(), '外卖下单用户'
where not exists (select 1 from sys_role where role_key = 'takeout_user' and del_flag = '0');
insert into sys_role (role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, create_by, create_time, remark)
select '外卖商家', 'takeout_merchant', 11, '1', 1, 1, '0', '0', 'admin', sysdate(), '外卖商家用户'
where not exists (select 1 from sys_role where role_key = 'takeout_merchant' and del_flag = '0');
insert into sys_role (role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, create_by, create_time, remark)
select '外卖骑手', 'takeout_rider', 12, '1', 1, 1, '0', '0', 'admin', sysdate(), '外卖骑手用户'
where not exists (select 1 from sys_role where role_key = 'takeout_rider' and del_flag = '0');

set @takeout_user_role = (select max(role_id) from sys_role where role_key = 'takeout_user' and del_flag = '0');
set @takeout_merchant_role = (select max(role_id) from sys_role where role_key = 'takeout_merchant' and del_flag = '0');
set @takeout_rider_role = (select max(role_id) from sys_role where role_key = 'takeout_rider' and del_flag = '0');

insert ignore into sys_role_menu (role_id, menu_id)
select @takeout_user_role, menu_id from sys_menu
where menu_id = @takeout_root or perms like 'takeout:user:%';
insert ignore into sys_role_menu (role_id, menu_id)
select @takeout_merchant_role, menu_id from sys_menu
where menu_id = @takeout_root or perms like 'takeout:merchant:%';
insert ignore into sys_role_menu (role_id, menu_id)
select @takeout_rider_role, menu_id from sys_menu
where menu_id = @takeout_root or perms like 'takeout:rider:%';

MERGE INTO `order` (id, `value`) KEY(id) VALUES ('demo', 'dongboot');
MERGE INTO item (sku_id, name) KEY(sku_id) VALUES ('demo', 'dongboot');

MERGE INTO live_session (id, title, session_code, anchor_name, operator, status, planned_start_time, planned_end_time) KEY(id)
VALUES (1, '5G智能手机本地联通演示', 'LOCAL-20260714-001', 'JoyCue演示主播', '本地操作员', 'READY', '2026-07-14 14:00:00', '2026-07-14 16:00:00');

MERGE INTO sku (id, session_id, sku_id, sku_name, main_image, category, brand, price, stock) KEY(id)
VALUES (1, 1, '100018374', '京东自营 5G智能手机 骁龙8 Gen3 5000mAh', '', '手机', 'JoyPhone', '2999', '128');
MERGE INTO sku (id, session_id, sku_id, sku_name, main_image, category, brand, price, stock) KEY(id)
VALUES (2, 1, '100018375', '京东自营 无线蓝牙耳机 降噪Pro', '', '耳机', 'JoyAudio', '399', '520');
MERGE INTO sku (id, session_id, sku_id, sku_name, main_image, category, brand, price, stock) KEY(id)
VALUES (3, 1, '100018376', '世界级画质 4K超高清智能电视 65英寸', '', '电视', 'JoyVision', '3299', '36');

MERGE INTO alert (id, session_id, alert_type, level, sku_id, message, handled) KEY(id)
VALUES (1, 1, 'INVENTORY', 'WARN', '100018376', '本地示例：电视库存低于 50 件', '0');
MERGE INTO danmaku (id, session_id, content, sender_nick, intent, answer, aigc, human_confirmed, replied) KEY(id)
VALUES (1, 1, '这款手机支持快充吗？', '本地观众A', 'PRODUCT_QA', '支持 67W 快充，具体以商品详情页为准。', TRUE, TRUE, TRUE);

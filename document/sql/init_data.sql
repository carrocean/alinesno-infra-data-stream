-- 初始化系统配置
INSERT INTO `system_config` VALUES (1703219130924048386, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0, '2023-09-17 09:30:49', 0, NULL, 'flink_home', '/home/alinesno/flink-1.15.3/', 'SYS', 'flink安装目录', 1);
INSERT INTO `system_config` VALUES (1703219131037294594, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0, '2023-09-17 09:30:50', 0, NULL, 'flink_pipeline_home', '/home/alinesno/', 'SYS', '数据实时计算安装目录', 2);
INSERT INTO `system_config` VALUES (1703219131062460418, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0, '2023-09-17 09:27:39', 0, NULL, 'auto_savepoint', '是', 'SYS', '是否开启保存点', 3);
INSERT INTO `system_config` VALUES (1703219131133763585, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0, '2023-09-17 09:31:21', 0, NULL, 'yarn_rm_http_address', 'http://localhost:8088/', 'SYS', 'Yarn RM Http地址', 4);
INSERT INTO `system_config` VALUES (1703219131217649665, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0, '2023-09-17 09:31:22', 0, NULL, 'flink_rest_http_address', 'http://localhost:8083/', 'SYS', 'Flink服务HTTP地址', 5);
INSERT INTO `system_config` VALUES (1703219131293147138, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0, '2023-09-17 09:31:41', 0, NULL, 'flink_rest_ha_http_address', 'http://localhost:8083/', 'SYS', 'Flink HA服务HTTP地址', 6);
INSERT INTO `system_config` VALUES (1703219131326701569, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0, '2023-09-17 09:31:53', 0, NULL, 'dingding_alart_url', 'https://oapi.dingtalk.com/robot/send?access_token=29e0f6ac8ee0a993057d30b97480332f91445c000480b96000e899fc9605c057', 'ALART', '钉钉告警', 7);
INSERT INTO `system_config` VALUES (1703219131439947777, 0, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0, '2023-09-17 09:31:56', 0, NULL, 'callback_alart_url', 'http://localhost:30214/', 'ALART', '自定义回调', 8);


DROP TABLE IF EXISTS `mq_serial_num`;
CREATE TABLE `mq_serial_num`  (
    `id` int(10) UNSIGNED NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of mq_serial_num
-- ----------------------------
INSERT INTO `mq_serial_num` VALUES (0);
INSERT INTO `mq_serial_num` VALUES (1);
INSERT INTO `mq_serial_num` VALUES (2);
INSERT INTO `mq_serial_num` VALUES (3);
INSERT INTO `mq_serial_num` VALUES (4);
INSERT INTO `mq_serial_num` VALUES (5);
INSERT INTO `mq_serial_num` VALUES (6);
INSERT INTO `mq_serial_num` VALUES (7);
INSERT INTO `mq_serial_num` VALUES (8);
INSERT INTO `mq_serial_num` VALUES (9);
INSERT INTO `mq_serial_num` VALUES (10);
INSERT INTO `mq_serial_num` VALUES (11);
INSERT INTO `mq_serial_num` VALUES (12);
INSERT INTO `mq_serial_num` VALUES (13);
INSERT INTO `mq_serial_num` VALUES (14);
INSERT INTO `mq_serial_num` VALUES (15);
INSERT INTO `mq_serial_num` VALUES (16);
INSERT INTO `mq_serial_num` VALUES (17);
INSERT INTO `mq_serial_num` VALUES (18);
INSERT INTO `mq_serial_num` VALUES (19);
INSERT INTO `mq_serial_num` VALUES (20);
INSERT INTO `mq_serial_num` VALUES (21);
INSERT INTO `mq_serial_num` VALUES (22);
INSERT INTO `mq_serial_num` VALUES (23);
INSERT INTO `mq_serial_num` VALUES (24);
INSERT INTO `mq_serial_num` VALUES (25);
INSERT INTO `mq_serial_num` VALUES (26);
INSERT INTO `mq_serial_num` VALUES (27);
INSERT INTO `mq_serial_num` VALUES (28);
INSERT INTO `mq_serial_num` VALUES (29);
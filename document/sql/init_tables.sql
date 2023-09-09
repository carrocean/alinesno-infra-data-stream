

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for alart_log
-- ----------------------------
DROP TABLE IF EXISTS `alart_log`;
CREATE TABLE `alart_log`  (
  `has_delete` INT COMMENT '是否删除(1删除|0正常|null正常)',
  `delete_manager` varchar(255) COMMENT '删除的人',
  `application_id` BIGINT COMMENT '所属应用 应用权限: 只能看到所属应用的权限【默认】',
  `application_name` VARCHAR(64) COMMENT '应用名称，唯一性，用于做应用标识【最好与springboot的application.name对应】',
  `tenant_id` BIGINT COMMENT '所属租户 , 租户权限',
  `field_id` BIGINT COMMENT '字段权限：部分字段权限无法加密或者不显示，或者大于某个值',
  `department_id` BIGINT COMMENT '部门权限: 只能看到自己所在部门的数据',
  `field_prop` VARCHAR(255) COMMENT '字段属性',
  `add_time` DATETIME COMMENT '添加时间',
  `delete_time` DATETIME COMMENT '删除时间',
  `has_status` INT COMMENT '状态(0启用|1禁用)',
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `operator_id` BIGINT COMMENT '操作员 用户权限: 只能看到自己操作的数据',
  `last_update_operator_id` BIGINT COMMENT '最后更新操作员 用户权限: 只能看到自己操作的数据',
  `job_config_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'job_config的id  如果0代表的是测试,',
  `job_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'jobName',
  `message` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息内容',
  `type` int(11) NULL DEFAULT NULL COMMENT '1:钉钉',
  `status` int(11) NULL DEFAULT NULL COMMENT '1:成功 0:失败',
  `fail_log` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '失败原因',
  INDEX `index_job_config_id`(`job_config_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;


-- ----------------------------
DROP TABLE IF EXISTS `ip_status`;
CREATE TABLE `ip_status`  (
  `has_delete` INT COMMENT '是否删除(1删除|0正常|null正常)',
  `delete_manager` varchar(255) COMMENT '删除的人',
  `application_id` BIGINT COMMENT '所属应用 应用权限: 只能看到所属应用的权限【默认】',
  `application_name` VARCHAR(64) COMMENT '应用名称，唯一性，用于做应用标识【最好与springboot的application.name对应】',
  `tenant_id` BIGINT COMMENT '所属租户 , 租户权限',
  `field_id` BIGINT COMMENT '字段权限：部分字段权限无法加密或者不显示，或者大于某个值',
  `department_id` BIGINT COMMENT '部门权限: 只能看到自己所在部门的数据',
  `field_prop` VARCHAR(255) COMMENT '字段属性',
  `add_time` DATETIME COMMENT '添加时间',
  `delete_time` DATETIME COMMENT '删除时间',
  `has_status` INT COMMENT '状态(0启用|1禁用)',
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `operator_id` BIGINT COMMENT '操作员 用户权限: 只能看到自己操作的数据',
  `last_update_operator_id` BIGINT COMMENT '最后更新操作员 用户权限: 只能看到自己操作的数据',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip',
  `status` int(11) NULL DEFAULT NULL COMMENT '1:运行 -1:停止 ',
  `last_time` datetime NULL DEFAULT NULL COMMENT '最后心跳时间'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------

-- ----------------------------
DROP TABLE IF EXISTS `job_alarm_config`;
CREATE TABLE `job_alarm_config`  (
  `has_delete` INT COMMENT '是否删除(1删除|0正常|null正常)',
  `delete_manager` varchar(255) COMMENT '删除的人',
  `application_id` BIGINT COMMENT '所属应用 应用权限: 只能看到所属应用的权限【默认】',
  `application_name` VARCHAR(64) COMMENT '应用名称，唯一性，用于做应用标识【最好与springboot的application.name对应】',
  `tenant_id` BIGINT COMMENT '所属租户 , 租户权限',
  `field_id` BIGINT COMMENT '字段权限：部分字段权限无法加密或者不显示，或者大于某个值',
  `department_id` BIGINT COMMENT '部门权限: 只能看到自己所在部门的数据',
  `field_prop` VARCHAR(255) COMMENT '字段属性',
  `add_time` DATETIME COMMENT '添加时间',
  `delete_time` DATETIME COMMENT '删除时间',
  `has_status` INT COMMENT '状态(0启用|1禁用)',
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `operator_id` BIGINT COMMENT '操作员 用户权限: 只能看到自己操作的数据',
  `last_update_operator_id` BIGINT COMMENT '最后更新操作员 用户权限: 只能看到自己操作的数据',
  `job_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'job_config主表id',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型 1:钉钉告警 2:url回调 3:异常自动拉起任务',
  `version` bigint(20) NULL DEFAULT NULL COMMENT '更新版本号  ',
  INDEX `uk_index_job_id`(`job_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------

-- ----------------------------
DROP TABLE IF EXISTS `job_config`;
CREATE TABLE `job_config`  (
  `has_delete` INT COMMENT '是否删除(1删除|0正常|null正常)',
  `delete_manager` varchar(255) COMMENT '删除的人',
  `application_id` BIGINT COMMENT '所属应用 应用权限: 只能看到所属应用的权限【默认】',
  `application_name` VARCHAR(64) COMMENT '应用名称，唯一性，用于做应用标识【最好与springboot的application.name对应】',
  `tenant_id` BIGINT COMMENT '所属租户 , 租户权限',
  `field_id` BIGINT COMMENT '字段权限：部分字段权限无法加密或者不显示，或者大于某个值',
  `department_id` BIGINT COMMENT '部门权限: 只能看到自己所在部门的数据',
  `field_prop` VARCHAR(255) COMMENT '字段属性',
  `add_time` DATETIME COMMENT '添加时间',
  `delete_time` DATETIME COMMENT '删除时间',
  `has_status` INT COMMENT '状态(0启用|1禁用)',
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `operator_id` BIGINT COMMENT '操作员 用户权限: 只能看到自己操作的数据',
  `last_update_operator_id` BIGINT COMMENT '最后更新操作员 用户权限: 只能看到自己操作的数据',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `job_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务描述',
  `deploy_mode` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提交模式: standalone 、yarn 、yarn-session ',
  `flink_run_config` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'flink运行配置',
  `flink_sql` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'sql语句',
  `flink_checkpoint_config` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'checkPoint配置',
  `job_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '运行后的任务id',
  `is_open` int(11) NULL DEFAULT NULL COMMENT '1:开启 0: 关闭',
  `status` int(11) NULL DEFAULT NULL COMMENT '1:运行中 0: 停止中 -1:运行失败',
  `cron` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批任务定时调度 如 0/20 * * * * ? 表示每20秒 执行任务 ',
  `ext_jar_path` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'udf地址已经连接器jar 如http://xxx.xxx.com/flink-streaming-udf.jar',
  `last_start_time` date NULL DEFAULT NULL COMMENT '最后一次启动时间',
  `last_run_log_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后一次日志',
  `version` int(11) NOT NULL DEFAULT 0 COMMENT '更新版本号 用于乐观锁',
  `job_type` int(11) NULL DEFAULT NULL COMMENT '任务类型 0:sql 1:自定义jar',
  `custom_args` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '启动jar可能需要使用的自定义参数',
  `custom_main_class` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '程序入口类',
  `custom_jar_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '自定义jar的http地址 如:http://ccblog.cn/xx.jar',
  INDEX `uk_index`(`job_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------

-- ----------------------------
DROP TABLE IF EXISTS `job_config_history`;
CREATE TABLE `job_config_history`  (
  `has_delete` INT COMMENT '是否删除(1删除|0正常|null正常)',
  `delete_manager` varchar(255) COMMENT '删除的人',
  `application_id` BIGINT COMMENT '所属应用 应用权限: 只能看到所属应用的权限【默认】',
  `application_name` VARCHAR(64) COMMENT '应用名称，唯一性，用于做应用标识【最好与springboot的application.name对应】',
  `tenant_id` BIGINT COMMENT '所属租户 , 租户权限',
  `field_id` BIGINT COMMENT '字段权限：部分字段权限无法加密或者不显示，或者大于某个值',
  `department_id` BIGINT COMMENT '部门权限: 只能看到自己所在部门的数据',
  `field_prop` VARCHAR(255) COMMENT '字段属性',
  `add_time` DATETIME COMMENT '添加时间',
  `delete_time` DATETIME COMMENT '删除时间',
  `has_status` INT COMMENT '状态(0启用|1禁用)',
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `operator_id` BIGINT COMMENT '操作员 用户权限: 只能看到自己操作的数据',
  `last_update_operator_id` BIGINT COMMENT '最后更新操作员 用户权限: 只能看到自己操作的数据',
  `job_config_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'job_config主表Id',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `job_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务描述',
  `deploy_mode` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提交模式: standalone 、yarn 、yarn-session ',
  `flink_run_config` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'flink运行配置',
  `flink_sql` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'sql语句',
  `flink_checkpoint_config` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'checkPoint配置',
  `ext_jar_path` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'udf地址及连接器jar 如http://xxx.xxx.com/flink-streaming-udf.jar',
  `version` bigint(20) NULL DEFAULT NULL COMMENT '更新版本号',
  `cron` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批任务定时调度 如 0/20 * * * * ? 表示每20秒 执行任务 ',
  `job_type` int(11) NULL DEFAULT NULL COMMENT '任务类型 0:sql 1:自定义jar',
  INDEX `index_job_config_id`(`job_config_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------

-- ----------------------------
DROP TABLE IF EXISTS `job_run_log`;
CREATE TABLE `job_run_log`  (
  `has_delete` INT COMMENT '是否删除(1删除|0正常|null正常)',
  `delete_manager` varchar(255) COMMENT '删除的人',
  `application_id` BIGINT COMMENT '所属应用 应用权限: 只能看到所属应用的权限【默认】',
  `application_name` VARCHAR(64) COMMENT '应用名称，唯一性，用于做应用标识【最好与springboot的application.name对应】',
  `tenant_id` BIGINT COMMENT '所属租户 , 租户权限',
  `field_id` BIGINT COMMENT '字段权限：部分字段权限无法加密或者不显示，或者大于某个值',
  `department_id` BIGINT COMMENT '部门权限: 只能看到自己所在部门的数据',
  `field_prop` VARCHAR(255) COMMENT '字段属性',
  `add_time` DATETIME COMMENT '添加时间',
  `delete_time` DATETIME COMMENT '删除时间',
  `has_status` INT COMMENT '状态(0启用|1禁用)',
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `operator_id` BIGINT COMMENT '操作员 用户权限: 只能看到自己操作的数据',
  `last_update_operator_id` BIGINT COMMENT '最后更新操作员 用户权限: 只能看到自己操作的数据',
  `job_config_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'jobConfigId',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `job_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务描述',
  `deploy_mode` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提交模式: standalone 、yarn 、yarn-session ',
  `job_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '运行后的任务id',
  `local_log` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '启动时本地日志',
  `run_ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务运行所在的机器',
  `remote_log_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '远程日志url的地址',
  `start_time` datetime NULL DEFAULT NULL COMMENT '启动时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '启动时间',
  `job_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务状态'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------


-- ----------------------------
DROP TABLE IF EXISTS `savepoint_backup`;
CREATE TABLE `savepoint_backup`  (
  `has_delete` INT COMMENT '是否删除(1删除|0正常|null正常)',
  `delete_manager` varchar(255) COMMENT '删除的人',
  `application_id` BIGINT COMMENT '所属应用 应用权限: 只能看到所属应用的权限【默认】',
  `application_name` VARCHAR(64) COMMENT '应用名称，唯一性，用于做应用标识【最好与springboot的application.name对应】',
  `tenant_id` BIGINT COMMENT '所属租户 , 租户权限',
  `field_id` BIGINT COMMENT '字段权限：部分字段权限无法加密或者不显示，或者大于某个值',
  `department_id` BIGINT COMMENT '部门权限: 只能看到自己所在部门的数据',
  `field_prop` VARCHAR(255) COMMENT '字段属性',
  `add_time` DATETIME COMMENT '添加时间',
  `delete_time` DATETIME COMMENT '删除时间',
  `has_status` INT COMMENT '状态(0启用|1禁用)',
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `operator_id` BIGINT COMMENT '操作员 用户权限: 只能看到自己操作的数据',
  `last_update_operator_id` BIGINT COMMENT '最后更新操作员 用户权限: 只能看到自己操作的数据',
  `job_config_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'jobConfigId',
  `savepoint_path` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地址',
  `backup_time` datetime NULL DEFAULT NULL COMMENT '备份时间',
  INDEX `index`(`job_config_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;



-- ----------------------------
-- Table structure for system_config
-- ----------------------------
DROP TABLE IF EXISTS `system_config`;
CREATE TABLE `system_config`  (
  `has_delete` INT COMMENT '是否删除(1删除|0正常|null正常)',
  `delete_manager` varchar(255) COMMENT '删除的人',
  `application_id` BIGINT COMMENT '所属应用 应用权限: 只能看到所属应用的权限【默认】',
  `application_name` VARCHAR(64) COMMENT '应用名称，唯一性，用于做应用标识【最好与springboot的application.name对应】',
  `tenant_id` BIGINT COMMENT '所属租户 , 租户权限',
  `field_id` BIGINT COMMENT '字段权限：部分字段权限无法加密或者不显示，或者大于某个值',
  `department_id` BIGINT COMMENT '部门权限: 只能看到自己所在部门的数据',
  `field_prop` VARCHAR(255) COMMENT '字段属性',
  `add_time` DATETIME COMMENT '添加时间',
  `delete_time` DATETIME COMMENT '删除时间',
  `has_status` INT COMMENT '状态(0启用|1禁用)',
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `operator_id` BIGINT COMMENT '操作员 用户权限: 只能看到自己操作的数据',
  `last_update_operator_id` BIGINT COMMENT '最后更新操作员 用户权限: 只能看到自己操作的数据',
  `sys_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'key值',
  `sys_val` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'value',
  `sys_type` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型 如:sys  alarm',
  `sys_desc` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `order_id` int(11) NULL DEFAULT NULL COMMENT '序号'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for upload_file
-- ----------------------------
DROP TABLE IF EXISTS `upload_file`;
CREATE TABLE `upload_file`  (
  `has_delete` INT COMMENT '是否删除(1删除|0正常|null正常)',
  `delete_manager` varchar(255) COMMENT '删除的人',
  `application_id` BIGINT COMMENT '所属应用 应用权限: 只能看到所属应用的权限【默认】',
  `application_name` VARCHAR(64) COMMENT '应用名称，唯一性，用于做应用标识【最好与springboot的application.name对应】',
  `tenant_id` BIGINT COMMENT '所属租户 , 租户权限',
  `field_id` BIGINT COMMENT '字段权限：部分字段权限无法加密或者不显示，或者大于某个值',
  `department_id` BIGINT COMMENT '部门权限: 只能看到自己所在部门的数据',
  `field_prop` VARCHAR(255) COMMENT '字段属性',
  `add_time` DATETIME COMMENT '添加时间',
  `delete_time` DATETIME COMMENT '删除时间',
  `has_status` INT COMMENT '状态(0启用|1禁用)',
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `operator_id` BIGINT COMMENT '操作员 用户权限: 只能看到自己操作的数据',
  `last_update_operator_id` BIGINT COMMENT '最后更新操作员 用户权限: 只能看到自己操作的数据',
  `file_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件名字',
  `file_path` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件路径',
  `type` int(11) NULL DEFAULT NULL COMMENT '1:jar'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `has_delete` INT COMMENT '是否删除(1删除|0正常|null正常)',
  `delete_manager` varchar(255) COMMENT '删除的人',
  `application_id` BIGINT COMMENT '所属应用 应用权限: 只能看到所属应用的权限【默认】',
  `application_name` VARCHAR(64) COMMENT '应用名称，唯一性，用于做应用标识【最好与springboot的application.name对应】',
  `tenant_id` BIGINT COMMENT '所属租户 , 租户权限',
  `field_id` BIGINT COMMENT '字段权限：部分字段权限无法加密或者不显示，或者大于某个值',
  `department_id` BIGINT COMMENT '部门权限: 只能看到自己所在部门的数据',
  `field_prop` VARCHAR(255) COMMENT '字段属性',
  `add_time` DATETIME COMMENT '添加时间',
  `delete_time` DATETIME COMMENT '删除时间',
  `has_status` INT COMMENT '状态(0启用|1禁用)',
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `operator_id` BIGINT COMMENT '操作员 用户权限: 只能看到自己操作的数据',
  `last_update_operator_id` BIGINT COMMENT '最后更新操作员 用户权限: 只能看到自己操作的数据',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户帐号',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户姓名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '密码',
  `status` int(11) NULL DEFAULT NULL COMMENT '1:启用 0: 停用'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;

SET NAMES utf8mb4;
$$
SET FOREIGN_KEY_CHECKS = 0;
$$
DROP TABLE IF EXISTS `spider_list`;
$$
CREATE TABLE `spider_list`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '逐渐自增',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容',
  `link` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '连接',
  `status` int(2) NULL DEFAULT 0 COMMENT '状态0初始化',
  `gmt_create` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `gmt_modify` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 637 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;
$$
SET FOREIGN_KEY_CHECKS = 1;

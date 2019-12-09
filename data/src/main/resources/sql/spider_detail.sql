SET NAMES utf8mb4;
$$
SET FOREIGN_KEY_CHECKS = 0;
$$
DROP TABLE IF EXISTS `spider_detail`;
$$
CREATE TABLE `spider_detail`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '详情名字',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '详情内容',
  `link` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '详情链接',
  `list_link` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '列表链接',
  `gmt_create` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `gmt_modify` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;
$$
SET FOREIGN_KEY_CHECKS = 1;

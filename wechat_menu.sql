
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for wechat_menu
-- ----------------------------
DROP TABLE IF EXISTS `wechat_menu`;
CREATE TABLE `wechat_menu`  (
  `menu_id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `menu_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'view菜单对应的url或者是图片对应的url',
  `menu_eventkey` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '菜单对应的eventkey',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '菜单的名字',
  `menu_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '包含图片和media以及返回菜单的文字内容',
  `menu_order` int NULL DEFAULT 0 COMMENT '菜单顺序 1开始正序',
  `menu_level` int NULL DEFAULT 1 COMMENT '0表示作为一级菜单，二级菜单为一级菜单的主键',
  `menu_type` int NULL DEFAULT 0 COMMENT '菜单类型(二级底部菜单为0) 0 底部菜单 1 url类型、2 回复文字 类型、3小程序、4 回复图片类型 ',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` int NULL DEFAULT 0 COMMENT '菜单是否删除 0：否 1:是',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '微信菜单管理' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

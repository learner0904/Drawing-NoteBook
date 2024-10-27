SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
USE `notebooks`;
-- ----------------------------
-- Table structure for notes
-- ----------------------------
DROP TABLE IF EXISTS `notes`;
CREATE TABLE `notes`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '评论内容',
  `date` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `code` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '代码片段',
  `picture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图片路径',
  `admin_id` int(10) NOT NULL COMMENT '管理员ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '笔记表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;

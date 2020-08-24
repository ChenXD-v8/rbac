/*
Navicat MySQL Data Transfer

Source Server         : MYSQL
Source Server Version : 50710
Source Host           : localhost:3306
Source Database       : rbac

Target Server Type    : MYSQL
Target Server Version : 50710
File Encoding         : 65001

Date: 2020-08-24 20:22:33
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for rbac_permission
-- ----------------------------
DROP TABLE IF EXISTS `rbac_permission`;
CREATE TABLE `rbac_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `enable` bit(1) DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `permission_key` varchar(32) COLLATE utf8mb4_bin NOT NULL,
  `type` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `weight` int(11) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `path` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `resource` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKarkyg4p1bouosuixjo7rebdjn` (`parent_id`)
) ENGINE=MyISAM AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of rbac_permission
-- ----------------------------
INSERT INTO `rbac_permission` VALUES ('4', '', '', '系统管理', 'system', 'MENU', '0', null, null, '/system');
INSERT INTO `rbac_permission` VALUES ('5', '权限管理', '', '权限管理', 'system/permission', 'MENU', '0', '4', '/permission?path=/system/permission', '/system/permission/list');
INSERT INTO `rbac_permission` VALUES ('6', '角色管理', '', '角色管理', 'system/role', 'MENU', '1', '4', '/role?path=/system/role', '/system/role/list,/system/role/permissionTree,/system/role/permission/*');
INSERT INTO `rbac_permission` VALUES ('7', '用户管理', '', '用户管理', 'system/user', 'MENU', '1', '4', '/user?path=/system/user', '/system/user/list');
INSERT INTO `rbac_permission` VALUES ('14', '', '', '编辑', 'system:user:edit', 'FUNCTION', '1', '7', '', '/system/user/load*,/system/user/update');
INSERT INTO `rbac_permission` VALUES ('15', '', '', '删除', 'system:user:delete', 'FUNCTION', null, '7', '', '/system/user/delete');
INSERT INTO `rbac_permission` VALUES ('16', '', '', '创建', 'system:user:creat', 'FUNCTION', null, '7', '', '/system/user/creat,/system/user/form,/system/user/save');
INSERT INTO `rbac_permission` VALUES ('17', '', '', '创建', 'system:role:create', 'FUNCTION', null, '6', '', '/system/role/create,/system/role/form,/system/role/save');
INSERT INTO `rbac_permission` VALUES ('18', '', '', '删除', 'system:role:delete', 'FUNCTION', null, '6', '', '/system/role/delete');
INSERT INTO `rbac_permission` VALUES ('19', '', '', '编辑', 'system:role:edit', 'FUNCTION', null, '6', '', '/system/role/load*,/system/role/update');
INSERT INTO `rbac_permission` VALUES ('20', '', '', '分配权限', 'system:role:rolePermission', 'FUNCTION', null, '6', '', '/system/role/permisonSa');
INSERT INTO `rbac_permission` VALUES ('24', '', '', '创建', 'system:permission:create', 'FUNCTION', null, '5', '', '/system/permission/create,/system/permission/form,/system/permission/combo,/system/permission/save');
INSERT INTO `rbac_permission` VALUES ('25', '', '', '删除', 'system:permission:delete', 'FUNCTION', null, '5', '', '/system/permission/delete');
INSERT INTO `rbac_permission` VALUES ('26', '', '', '编辑', 'system:permission:edit', 'FUNCTION', null, '5', '', '/system/permission/load*,/system/permission/update,/system/permission/combo');
INSERT INTO `rbac_permission` VALUES ('43', '', '', '查看', 'system:permission:look', 'FUNCTION', '1', '5', '', '/system/role/*');
INSERT INTO `rbac_permission` VALUES ('44', '', '', '查看', 'system:role:look', 'FUNCTION', null, '6', '', '/system/role/look');
INSERT INTO `rbac_permission` VALUES ('45', '', '', '查看', 'system:user:look', 'FUNCTION', null, '7', '', '/system/user');

-- ----------------------------
-- Table structure for rbac_role
-- ----------------------------
DROP TABLE IF EXISTS `rbac_role`;
CREATE TABLE `rbac_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `enable` bit(1) DEFAULT NULL,
  `role_key` varchar(32) COLLATE utf8mb4_bin NOT NULL,
  `role_name` varchar(32) COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_iace11lm41qsi7dstkaiecion` (`role_key`),
  UNIQUE KEY `UK_eu9uvi1fl9j2kmtul6bmcu0mh` (`role_name`)
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of rbac_role
-- ----------------------------
INSERT INTO `rbac_role` VALUES ('4', '具有部分权限', '', 'public', '普通用户');
INSERT INTO `rbac_role` VALUES ('3', '管理员角色', '', 'admin', '管理员');
INSERT INTO `rbac_role` VALUES ('9', '测试一下角色管理', '', 'role_test', '测试角色');
INSERT INTO `rbac_role` VALUES ('10', '只具有创建用户的权限', '', 'create_user', '用户创建');
INSERT INTO `rbac_role` VALUES ('11', '只具有给角色分配权限的角色', '', 'givePermission', '分配权限');
INSERT INTO `rbac_role` VALUES ('12', '只具有查看的权限', '', 'vistor', '访客角色');

-- ----------------------------
-- Table structure for rbac_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `rbac_role_permission`;
CREATE TABLE `rbac_role_permission` (
  `role_id` bigint(20) NOT NULL,
  `permission_id` bigint(20) NOT NULL,
  PRIMARY KEY (`role_id`,`permission_id`),
  KEY `FK6l1rpsk0jgvg41t538728fjm4` (`permission_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of rbac_role_permission
-- ----------------------------
INSERT INTO `rbac_role_permission` VALUES ('4', '4');
INSERT INTO `rbac_role_permission` VALUES ('4', '5');
INSERT INTO `rbac_role_permission` VALUES ('4', '6');
INSERT INTO `rbac_role_permission` VALUES ('4', '7');
INSERT INTO `rbac_role_permission` VALUES ('4', '14');
INSERT INTO `rbac_role_permission` VALUES ('4', '17');
INSERT INTO `rbac_role_permission` VALUES ('4', '19');
INSERT INTO `rbac_role_permission` VALUES ('4', '20');
INSERT INTO `rbac_role_permission` VALUES ('4', '24');
INSERT INTO `rbac_role_permission` VALUES ('4', '26');
INSERT INTO `rbac_role_permission` VALUES ('4', '43');
INSERT INTO `rbac_role_permission` VALUES ('9', '4');
INSERT INTO `rbac_role_permission` VALUES ('9', '5');
INSERT INTO `rbac_role_permission` VALUES ('9', '6');
INSERT INTO `rbac_role_permission` VALUES ('9', '7');
INSERT INTO `rbac_role_permission` VALUES ('9', '16');
INSERT INTO `rbac_role_permission` VALUES ('9', '18');
INSERT INTO `rbac_role_permission` VALUES ('9', '19');
INSERT INTO `rbac_role_permission` VALUES ('9', '43');
INSERT INTO `rbac_role_permission` VALUES ('9', '44');
INSERT INTO `rbac_role_permission` VALUES ('9', '45');
INSERT INTO `rbac_role_permission` VALUES ('10', '4');
INSERT INTO `rbac_role_permission` VALUES ('10', '7');
INSERT INTO `rbac_role_permission` VALUES ('10', '16');
INSERT INTO `rbac_role_permission` VALUES ('10', '45');
INSERT INTO `rbac_role_permission` VALUES ('11', '4');
INSERT INTO `rbac_role_permission` VALUES ('11', '6');
INSERT INTO `rbac_role_permission` VALUES ('11', '20');
INSERT INTO `rbac_role_permission` VALUES ('12', '4');
INSERT INTO `rbac_role_permission` VALUES ('12', '5');
INSERT INTO `rbac_role_permission` VALUES ('12', '6');
INSERT INTO `rbac_role_permission` VALUES ('12', '7');
INSERT INTO `rbac_role_permission` VALUES ('12', '43');
INSERT INTO `rbac_role_permission` VALUES ('12', '44');
INSERT INTO `rbac_role_permission` VALUES ('12', '45');

-- ----------------------------
-- Table structure for rbac_user
-- ----------------------------
DROP TABLE IF EXISTS `rbac_user`;
CREATE TABLE `rbac_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account` varchar(16) COLLATE utf8mb4_bin NOT NULL,
  `enable` bit(1) DEFAULT NULL,
  `password` varchar(128) COLLATE utf8mb4_bin NOT NULL,
  `tel` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `user_name` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_dxesfklauarqhov4147i100ud` (`account`)
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of rbac_user
-- ----------------------------
INSERT INTO `rbac_user` VALUES ('0', 'admin', '', 'e10adc3949ba59abbe56e057f20f883e', '12121', '超级管理员');
INSERT INTO `rbac_user` VALUES ('4', 'qq', '', 'e10adc3949ba59abbe56e057f20f883e', '111', '小老弟');
INSERT INTO `rbac_user` VALUES ('7', '111', '\0', '698d51a19d8a121ce581499d7b701668', '111', '111');
INSERT INTO `rbac_user` VALUES ('6', 'test', '\0', 'e10adc3949ba59abbe56e057f20f883e', '123456', '张三');
INSERT INTO `rbac_user` VALUES ('8', 'admin1', '', 'e10adc3949ba59abbe56e057f20f883e', '114', '测试用户1');
INSERT INTO `rbac_user` VALUES ('11', 'test2', '', 'e10adc3949ba59abbe56e057f20f883e', '11', '测试用户2');
INSERT INTO `rbac_user` VALUES ('10', 'test3', '', 'e10adc3949ba59abbe56e057f20f883e', '111', '测试用户3');
INSERT INTO `rbac_user` VALUES ('12', 'vistor', '', 'e10adc3949ba59abbe56e057f20f883e', '111', '访客');
INSERT INTO `rbac_user` VALUES ('13', 'test4', '', 'e10adc3949ba59abbe56e057f20f883e', '1212', '测试用户4');

-- ----------------------------
-- Table structure for rbac_user_role
-- ----------------------------
DROP TABLE IF EXISTS `rbac_user_role`;
CREATE TABLE `rbac_user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKnviybsn4jexeg8t4n0n4bagi5` (`role_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of rbac_user_role
-- ----------------------------
INSERT INTO `rbac_user_role` VALUES ('4', '4');
INSERT INTO `rbac_user_role` VALUES ('5', '5');
INSERT INTO `rbac_user_role` VALUES ('6', '3');
INSERT INTO `rbac_user_role` VALUES ('6', '4');
INSERT INTO `rbac_user_role` VALUES ('8', '9');
INSERT INTO `rbac_user_role` VALUES ('10', '11');
INSERT INTO `rbac_user_role` VALUES ('11', '10');
INSERT INTO `rbac_user_role` VALUES ('12', '12');
INSERT INTO `rbac_user_role` VALUES ('13', '9');
INSERT INTO `rbac_user_role` VALUES ('13', '10');
INSERT INTO `rbac_user_role` VALUES ('13', '11');

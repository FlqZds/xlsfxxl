/*
 Navicat Premium Dump SQL

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80017 (8.0.17)
 Source Host           : localhost:3306
 Source Schema         : behind_manage

 Target Server Type    : MySQL
 Target Server Version : 80017 (8.0.17)
 File Encoding         : 65001

 Date: 06/01/2025 20:39:50
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin_actionright
-- ----------------------------
DROP TABLE IF EXISTS `admin_actionright`;
CREATE TABLE `admin_actionright`  (
  `action_id` int(11) NOT NULL AUTO_INCREMENT,
  `right_code` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限码',
  `righttext` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '动作权限文本描述',
  `is_parent` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '0: 父节点 1:子节点',
  `parentid` int(11) NULL DEFAULT NULL COMMENT '父Id',
  PRIMARY KEY (`action_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin_actionright
-- ----------------------------
INSERT INTO `admin_actionright` VALUES (29, '', '动作权限', '0', 0);
INSERT INTO `admin_actionright` VALUES (30, 'Query_All', '仅查看', '1', 29);
INSERT INTO `admin_actionright` VALUES (31, 'Update_ALl', '修改数据', '1', 29);

-- ----------------------------
-- Table structure for admin_adv
-- ----------------------------
DROP TABLE IF EXISTS `admin_adv`;
CREATE TABLE `admin_adv`  (
  `adv_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '广告位id',
  `app_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '应用id',
  `adv_type_id` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '广告类型',
  `adv_secret` varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '广告位密钥'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin_adv
-- ----------------------------
INSERT INTO `admin_adv` VALUES ('103133818', '176', '激励', '53fcd8d38b013168f59359304a35a98b');
INSERT INTO `admin_adv` VALUES ('103132823', '176', '开屏', '');
INSERT INTO `admin_adv` VALUES ('103131294', '176', '信息流', '');
INSERT INTO `admin_adv` VALUES ('103133640', '176', '横幅', '');
INSERT INTO `admin_adv` VALUES ('103131387', '176', '插屏', '');
INSERT INTO `admin_adv` VALUES ('103286313', '12419', '开屏', '');
INSERT INTO `admin_adv` VALUES ('103286411', '12419', '插屏', '');
INSERT INTO `admin_adv` VALUES ('103285868', '12419', '信息流', '');
INSERT INTO `admin_adv` VALUES ('103284761', '12419', '激励', 'f2ddf558d8ad66aa064616f490516f52');
INSERT INTO `admin_adv` VALUES ('103133818', '12423', '激励', 'cb7ebcca2f3fde73ea853d96aae91336');
INSERT INTO `admin_adv` VALUES ('103133640', '12423', '横幅', '');
INSERT INTO `admin_adv` VALUES ('103132823', '12423', '开屏', '');
INSERT INTO `admin_adv` VALUES ('103131294', '12423', '信息流', '');
INSERT INTO `admin_adv` VALUES ('103131387', '12423', '插屏', '');
INSERT INTO `admin_adv` VALUES ('103315855', '12419', '横幅', '');

-- ----------------------------
-- Table structure for admin_codebit
-- ----------------------------
DROP TABLE IF EXISTS `admin_codebit`;
CREATE TABLE `admin_codebit`  (
  `code_bit_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '代码位id',
  `adv_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '广告位id',
  `info` varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '对代码位的描述',
  `adn_id` varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '广告网络',
  PRIMARY KEY (`code_bit_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin_codebit
-- ----------------------------
INSERT INTO `admin_codebit` VALUES ('11', '103284761', '11', '2');
INSERT INTO `admin_codebit` VALUES ('12', '103284761', '1', NULL);
INSERT INTO `admin_codebit` VALUES ('17498551', '103133818', '百度_激励', '4');
INSERT INTO `admin_codebit` VALUES ('17510531', '103131387', '百度_插屏竞价', '4');
INSERT INTO `admin_codebit` VALUES ('17774212', '103284761', '百度竞价', '4');
INSERT INTO `admin_codebit` VALUES ('18490000033', '103133818', '测试_快手', '3');
INSERT INTO `admin_codebit` VALUES ('18490000035', '103132823', '测试_快手_开屏', '3');
INSERT INTO `admin_codebit` VALUES ('18490000036', '103131294', '测试_快手_信息流', '3');
INSERT INTO `admin_codebit` VALUES ('18490000037', '103131387', '测试_快手_插屏', '3');
INSERT INTO `admin_codebit` VALUES ('18490000039', '103131294', '测试_自渲染_信息流', '3');
INSERT INTO `admin_codebit` VALUES ('18490000041', '103284761', '快手竞价', '3');
INSERT INTO `admin_codebit` VALUES ('18490000043', '103286411', '快手_插屏', '3');
INSERT INTO `admin_codebit` VALUES ('18490000044', '103285868', '快手_信息流', '3');
INSERT INTO `admin_codebit` VALUES ('18490000045', '103286313', '快手测试开屏', '3');
INSERT INTO `admin_codebit` VALUES ('3113245701573269', '103315855', '测试横幅', '2');
INSERT INTO `admin_codebit` VALUES ('4133111354011109', '103284761', '竞价_优量汇', '2');
INSERT INTO `admin_codebit` VALUES ('4161279582468364', '103131387', '测试_插屏', '2');
INSERT INTO `admin_codebit` VALUES ('5151684547052955', '103131294', '测试_信息流', '2');
INSERT INTO `admin_codebit` VALUES ('6141578552866166', '103133818', '测试_激励', '2');
INSERT INTO `admin_codebit` VALUES ('6173149437817541', '103285868', '测试信息流', '2');
INSERT INTO `admin_codebit` VALUES ('889910249', '103132823', '开屏广告_auto_add_1725972438', '1');
INSERT INTO `admin_codebit` VALUES ('889910250', '103132823', '开屏广告_gm_adx_09_10_20:47', '4');
INSERT INTO `admin_codebit` VALUES ('889910251', '103132823', '开屏广告_bidding_09_10_20:47', '1');
INSERT INTO `admin_codebit` VALUES ('890476609', '103286313', '开屏广告_auto_add_1733986496', '1');
INSERT INTO `admin_codebit` VALUES ('890476610', '103286313', '开屏广告_gm_adx_12_12_14:54', '1');
INSERT INTO `admin_codebit` VALUES ('890476611', '103286313', '开屏广告_bidding_12_12_14:54', '1');
INSERT INTO `admin_codebit` VALUES ('9123448784758103', '103286313', '优量汇测试开屏', '2');
INSERT INTO `admin_codebit` VALUES ('9171472592469420', '103132823', '测试_开屏', '2');
INSERT INTO `admin_codebit` VALUES ('960394319', '103133818', '激励广告_auto_add_1725972375', '1');
INSERT INTO `admin_codebit` VALUES ('960394320', '103133818', '激励广告_bidding_09_10_20:46', '1');
INSERT INTO `admin_codebit` VALUES ('960394321', '103133818', '激励广告_gm_adx_09_10_20:46', '4');
INSERT INTO `admin_codebit` VALUES ('960394322', '103131387', '插屏广告_gm_adx_09_10_20:46', '4');
INSERT INTO `admin_codebit` VALUES ('960394323', '103131387', '插屏广告_auto_add_1725972408', '1');
INSERT INTO `admin_codebit` VALUES ('960394324', '103131387', '插屏广告_bidding_09_10_20:46', '1');
INSERT INTO `admin_codebit` VALUES ('960394325', '103131294', '信息流_auto_add_1725972424', '1');
INSERT INTO `admin_codebit` VALUES ('960394326', '103131294', '信息流_bidding_09_10_20:47', '1');
INSERT INTO `admin_codebit` VALUES ('960394327', '103131294', '信息流_gm_adx_09_10_20:47', '4');
INSERT INTO `admin_codebit` VALUES ('962518214', '103284761', '激励广告_gm_adx_12_12_14:53', '1');
INSERT INTO `admin_codebit` VALUES ('962518215', '103284761', '激励广告_auto_add_1733986390', '1');
INSERT INTO `admin_codebit` VALUES ('962518216', '103284761', '激励广告_bidding_12_12_14:53', '1');
INSERT INTO `admin_codebit` VALUES ('962518283', '103286411', '插屏广告_gm_adx_12_12_14:53', '1');
INSERT INTO `admin_codebit` VALUES ('962518284', '103286411', '插屏广告_bidding_12_12_14:53', '1');
INSERT INTO `admin_codebit` VALUES ('962518285', '103286411', '插屏广告_auto_add_1733986438', '1');
INSERT INTO `admin_codebit` VALUES ('962518353', '103285868', '信息流_bidding_12_12_14:55', '1');
INSERT INTO `admin_codebit` VALUES ('962518354', '103285868', '信息流_gm_adx_12_12_14:55', '1');
INSERT INTO `admin_codebit` VALUES ('962518355', '103285868', '信息流_auto_add_1733986522', '1');
INSERT INTO `admin_codebit` VALUES ('962994728', '103315855', '横幅_auto_add_1735810285', '1');
INSERT INTO `admin_codebit` VALUES ('962994729', '103315855', '横幅_bidding_01_02_17:31', '1');
INSERT INTO `admin_codebit` VALUES ('962994730', '103315855', '横幅_gm_adx_01_02_17:31', '1');

-- ----------------------------
-- Table structure for admin_fun_center
-- ----------------------------
DROP TABLE IF EXISTS `admin_fun_center`;
CREATE TABLE `admin_fun_center`  (
  `fun_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '功能id',
  `amdin_fun_center_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '应用中心功能菜单',
  `admin_fun__center_url` varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '应用中心功能路径',
  PRIMARY KEY (`fun_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin_fun_center
-- ----------------------------
INSERT INTO `admin_fun_center` VALUES (1, '应用大厅', '/fun/Applist');
INSERT INTO `admin_fun_center` VALUES (2, '截图评估', '/fun/ScreenShot');
INSERT INTO `admin_fun_center` VALUES (3, '全局代理', '/fun/GlobalProxy');
INSERT INTO `admin_fun_center` VALUES (4, '全局渠道', '/fun/GlobalOriginal');
INSERT INTO `admin_fun_center` VALUES (6, '全局统计', '/fun/AllSum');
INSERT INTO `admin_fun_center` VALUES (7, '全局聚集', '/fun/GlobalGath');
INSERT INTO `admin_fun_center` VALUES (14, '设备信息', '/fun/DeviceInfo');
INSERT INTO `admin_fun_center` VALUES (15, '设备品牌', '/fun/DeviceBrand');

-- ----------------------------
-- Table structure for admin_menuright
-- ----------------------------
DROP TABLE IF EXISTS `admin_menuright`;
CREATE TABLE `admin_menuright`  (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `menu_title` varchar(75) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '菜单名称',
  `menu_router` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '菜单路由',
  `menu_icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '菜单图标',
  `menu_type` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '是否父节点，0父节点 , 1子节点',
  `menu_parentID` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '父菜单id',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100013 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin_menuright
-- ----------------------------
INSERT INTO `admin_menuright` VALUES (40, '广告位管理', '/AdvertiseManage', 'el-icon-menu', '00', '00');
INSERT INTO `admin_menuright` VALUES (50, '提现配置', '/Withdraw', 'el-icon-wallet', '00', '00');
INSERT INTO `admin_menuright` VALUES (60, '游戏设置', '/GameSetting', 'el-icon-setting', '00', '00');
INSERT INTO `admin_menuright` VALUES (80, '广告记录', '/Summary', 'el-icon-view', '00', '00');
INSERT INTO `admin_menuright` VALUES (81, '横幅记录', '/Summary/rowAdv', '', '01', '80');
INSERT INTO `admin_menuright` VALUES (83, '信息流记录', '/Summary/streamAdv', '', '01', '80');
INSERT INTO `admin_menuright` VALUES (85, '插屏记录', '/Summary/inAdv', '', '01', '80');
INSERT INTO `admin_menuright` VALUES (87, '开屏记录', '/Summary/openAdv', '', '01', '80');
INSERT INTO `admin_menuright` VALUES (89, '激励记录', '/Summary/encAdv', '', '01', '80');
INSERT INTO `admin_menuright` VALUES (90, '特殊统计', '/ExceptionsRecord', 'el-icon-thumb', '00', '00');
INSERT INTO `admin_menuright` VALUES (100, '用户查看', '/UserCheck', 'el-icon-user', '00', '00');
INSERT INTO `admin_menuright` VALUES (110, '数据统计', '/EcpmDetails', 'el-icon-notebook-1', '00', '00');
INSERT INTO `admin_menuright` VALUES (125, '快手', '/EcpmDetails/Kuai', '', '01', '110');
INSERT INTO `admin_menuright` VALUES (130, '优量汇', '/EcpmDetails/You', '', '01', '110');
INSERT INTO `admin_menuright` VALUES (135, '穿山甲', '/EcpmDetails/GroMore', '', '01', '110');
INSERT INTO `admin_menuright` VALUES (140, '广告联盟', '/EcpmDetails/AdvLegends', '', '00', '110');
INSERT INTO `admin_menuright` VALUES (145, '明细汇总', '/EcpmDetails/profileTotal', '', '00', '110');
INSERT INTO `admin_menuright` VALUES (150, '百度', '/EcpmDetails/Baidu', '', '01', '110');
INSERT INTO `admin_menuright` VALUES (155, 'ECPM统计', '/EcpmDetails/All', '', '01', '110');
INSERT INTO `admin_menuright` VALUES (170, '异常记录', '/ErrorRecord', 'el-icon-warning', '00', '00');
INSERT INTO `admin_menuright` VALUES (180, '登录异常', '/ErrorRecord/loginError', '', '01', '170');
INSERT INTO `admin_menuright` VALUES (200, '我的代理', '/mine', 'el-icon-s-help\r\n', '00', '00');
INSERT INTO `admin_menuright` VALUES (220, '子级代理', '/son', 'el-icon-help', '00', '00');
INSERT INTO `admin_menuright` VALUES (300, '代理系统', '/sonMine', 'el-icon-service', '00', '00');
INSERT INTO `admin_menuright` VALUES (330, '查询功能', '/sonQuery', '', '00', '00');
INSERT INTO `admin_menuright` VALUES (420, '所有用户', '/UserCheck/All', '', '01', '100');
INSERT INTO `admin_menuright` VALUES (425, '操作记录', '/UserCheck/optionSummary', '', '01', '100');
INSERT INTO `admin_menuright` VALUES (435, '提现设置', '/Withdraw/Setting', '', '01', '50');
INSERT INTO `admin_menuright` VALUES (440, '提现记录', '/Withdraw/Record', '', '01', '50');
INSERT INTO `admin_menuright` VALUES (500, '相同广告ID', '/ExceptionsRecord/SameAdvID', '', '01', '90');
INSERT INTO `admin_menuright` VALUES (550, '广告数量统计', '/ExceptionsRecord/AdvCount', '', '01', '90');
INSERT INTO `admin_menuright` VALUES (570, '用户行为统计', '/ExceptionsRecord/UserBehaviorSum', '', '01', '90');
INSERT INTO `admin_menuright` VALUES (600, '点击量统计', '/ExceptionsRecord/ClickSum', '', '01', '90');
INSERT INTO `admin_menuright` VALUES (620, '总点击量统计', '/ExceptionsRecord/TotalClickSum', '', '01', '90');
INSERT INTO `admin_menuright` VALUES (700, '权限分配', '/Assign', 'el-icon-help', '00', '00');
INSERT INTO `admin_menuright` VALUES (720, '应用列表', '/apk/download', 'el-icon-download', '00', '00');

-- ----------------------------
-- Table structure for admin_role_action
-- ----------------------------
DROP TABLE IF EXISTS `admin_role_action`;
CREATE TABLE `admin_role_action`  (
  `role_act_id` int(11) NOT NULL COMMENT '动作权限_角色id',
  `role` tinyint(4) NULL DEFAULT NULL COMMENT '管理员类型',
  `action_id` int(11) NULL DEFAULT NULL COMMENT '动作权限id',
  PRIMARY KEY (`role_act_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin_role_action
-- ----------------------------
INSERT INTO `admin_role_action` VALUES (1, 1, 30);
INSERT INTO `admin_role_action` VALUES (2, 1, 31);
INSERT INTO `admin_role_action` VALUES (3, 2, 30);

-- ----------------------------
-- Table structure for admin_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `admin_role_menu`;
CREATE TABLE `admin_role_menu`  (
  ` roleMenu_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色和菜单建立关系的id',
  `roleId` tinyint(4) NOT NULL COMMENT '角色id',
  `menuId` int(11) NULL DEFAULT NULL COMMENT '菜单id',
  PRIMARY KEY (` roleMenu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1000005 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin_role_menu
-- ----------------------------
INSERT INTO `admin_role_menu` VALUES (7, 1, 110);
INSERT INTO `admin_role_menu` VALUES (8, 1, 40);
INSERT INTO `admin_role_menu` VALUES (9, 1, 50);
INSERT INTO `admin_role_menu` VALUES (10, 1, 60);
INSERT INTO `admin_role_menu` VALUES (12, 1, 80);
INSERT INTO `admin_role_menu` VALUES (13, 1, 90);
INSERT INTO `admin_role_menu` VALUES (14, 1, 100);
INSERT INTO `admin_role_menu` VALUES (16, 2, 200);
INSERT INTO `admin_role_menu` VALUES (17, 2, 220);
INSERT INTO `admin_role_menu` VALUES (18, 3, 300);
INSERT INTO `admin_role_menu` VALUES (20, 1, 81);
INSERT INTO `admin_role_menu` VALUES (21, 1, 83);
INSERT INTO `admin_role_menu` VALUES (22, 1, 85);
INSERT INTO `admin_role_menu` VALUES (23, 1, 87);
INSERT INTO `admin_role_menu` VALUES (24, 1, 89);
INSERT INTO `admin_role_menu` VALUES (25, 1, 140);
INSERT INTO `admin_role_menu` VALUES (26, 1, 155);
INSERT INTO `admin_role_menu` VALUES (27, 1, 145);
INSERT INTO `admin_role_menu` VALUES (28, 1, 130);
INSERT INTO `admin_role_menu` VALUES (29, 1, 135);
INSERT INTO `admin_role_menu` VALUES (30, 1, 120);
INSERT INTO `admin_role_menu` VALUES (31, 1, 115);
INSERT INTO `admin_role_menu` VALUES (32, 1, 150);
INSERT INTO `admin_role_menu` VALUES (33, 1, 125);
INSERT INTO `admin_role_menu` VALUES (34, 1, 170);
INSERT INTO `admin_role_menu` VALUES (35, 1, 180);
INSERT INTO `admin_role_menu` VALUES (37, 1, 420);
INSERT INTO `admin_role_menu` VALUES (44, 1, 425);
INSERT INTO `admin_role_menu` VALUES (50, 1, 435);
INSERT INTO `admin_role_menu` VALUES (53, 1, 440);
INSERT INTO `admin_role_menu` VALUES (54, 1, 500);
INSERT INTO `admin_role_menu` VALUES (55, 1, 550);
INSERT INTO `admin_role_menu` VALUES (56, 1, 570);
INSERT INTO `admin_role_menu` VALUES (57, 1, 600);
INSERT INTO `admin_role_menu` VALUES (58, 1, 620);
INSERT INTO `admin_role_menu` VALUES (59, 1, 700);
INSERT INTO `admin_role_menu` VALUES (60, 2, 210);
INSERT INTO `admin_role_menu` VALUES (61, 2, 720);
INSERT INTO `admin_role_menu` VALUES (63, 3, 720);

-- ----------------------------
-- Table structure for adn
-- ----------------------------
DROP TABLE IF EXISTS `adn`;
CREATE TABLE `adn`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '广告商id',
  `adn_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '广告商名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '记录广告商名称，代码位' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of adn
-- ----------------------------
INSERT INTO `adn` VALUES (1, '穿山甲');
INSERT INTO `adn` VALUES (2, '优量汇');
INSERT INTO `adn` VALUES (3, '快手');
INSERT INTO `adn` VALUES (4, '百度');

-- ----------------------------
-- Table structure for adv_type
-- ----------------------------
DROP TABLE IF EXISTS `adv_type`;
CREATE TABLE `adv_type`  (
  `id` int(11) NULL DEFAULT NULL COMMENT '广告类型id，1是开屏，2是插屏，3是信息流，4是横幅，5是激励',
  `adv_type_name` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '广告类型名称'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '记录广告都有哪些类型，广告位' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of adv_type
-- ----------------------------
INSERT INTO `adv_type` VALUES (1, '开屏');
INSERT INTO `adv_type` VALUES (2, '插屏');
INSERT INTO `adv_type` VALUES (3, '信息流');
INSERT INTO `adv_type` VALUES (4, '横幅');
INSERT INTO `adv_type` VALUES (5, '激励');

-- ----------------------------
-- Table structure for application
-- ----------------------------
DROP TABLE IF EXISTS `application`;
CREATE TABLE `application`  (
  `application_id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '游戏唯一标识',
  `package_name` varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '游戏包名',
  `application_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '游戏名',
  `application_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '游戏简介',
  `download_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '应用管理的前端地址',
  `application_create_time` datetime NULL DEFAULT NULL COMMENT '应用添加的时间',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '运行状态 //true - 1代表关闭，false - 0代表正常开启,前端是true，后端是1',
  `info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '运行中' COMMENT '运行信息',
  `qq_group` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '官方QQ群',
  `appid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信id',
  `app_secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信secret',
  `is_global_gath` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否全局聚集(0为全局聚集 ,1为不全局聚集)',
  `db_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '数据库名称',
  PRIMARY KEY (`application_id`) USING BTREE,
  UNIQUE INDEX `pkname_pk`(`package_name` ASC) USING BTREE COMMENT '包名唯一',
  UNIQUE INDEX `game_pk`(`application_name` ASC) USING BTREE COMMENT '应用名称唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 12424 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '游戏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of application
-- ----------------------------
INSERT INTO `application` VALUES (12419, 'com.layabox.gameyzzs', '联机版', '哇', '21424312312', '2024-12-13 00:24:33', 1, '12412', '5', 'wx29d88fe25fd2b103', 'c6aeb37fd7e0b4f2bbe05d628e401831', NULL, 'yunting_app_test');
INSERT INTO `application` VALUES (12422, 'e12', 'e12', '124r', 'e12', '2024-12-19 20:36:25', 1, 'e12', 'e12', 'fdas', 'qew', NULL, NULL);
INSERT INTO `application` VALUES (12423, 'com.xlsfxxl.app', '单机版', NULL, NULL, '2024-12-24 19:15:28', 1, NULL, NULL, 'wx002f69ed067c703d', 'c14303e871f0d0bd1cb2ae81f287e3ee', NULL, NULL);

-- ----------------------------
-- Table structure for brand_inapproval
-- ----------------------------
DROP TABLE IF EXISTS `brand_inapproval`;
CREATE TABLE `brand_inapproval`  (
  `brand_id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `brand_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备品牌',
  `install_machine` varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '安装器(包名)',
  `appstore` varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '应用市场(包名)',
  PRIMARY KEY (`brand_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 83 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of brand_inapproval
-- ----------------------------
INSERT INTO `brand_inapproval` VALUES (78, '1212', '122', 'd12');
INSERT INTO `brand_inapproval` VALUES (80, '123213', '123123', '1231');
INSERT INTO `brand_inapproval` VALUES (81, '123', '123', '213');

-- ----------------------------
-- Table structure for game_setting
-- ----------------------------
DROP TABLE IF EXISTS `game_setting`;
CREATE TABLE `game_setting`  (
  `app_setting_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '游戏设置id',
  `appid` bigint(20) NULL DEFAULT NULL COMMENT '应用id',
  `package_name` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '应用包名',
  `app_name` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '应用名称',
  `adv_watch_interval` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '对应请求广告与收下奖励时间间隔',
  `user_reward_setting_id` int(11) NULL DEFAULT NULL COMMENT '用户奖励比例设置id',
  `anti__ad_setting_id` int(11) NULL DEFAULT NULL COMMENT '限制用户观看广告设置id',
  `user_gathering_setting_id` int(11) NULL DEFAULT NULL COMMENT '用户聚集限制设置id',
  `screenshot_setting_id` bigint(20) NULL DEFAULT NULL COMMENT '游戏截图设置id',
  `device_System_Condition` int(1) NULL DEFAULT NULL COMMENT '设备系统低于此以下禁止登录',
  `retain_way` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '留存方式 （0是自然日记录 ，1是24小时记录）',
  `reset_max_time` int(11) NULL DEFAULT NULL COMMENT '应用后台最大闲置时长',
  `prohibt_see_adv_start` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '禁止观看广告时间的时间段，起始时间',
  `prohibt_see_adv_end` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '禁止观看广告时间的时间段，终止时间',
  `is_enable_prohibt` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否启用功能 ：禁止观看广告时间段',
  `is_anchor_weekend` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否启用功能 ：周末是否限制时间段',
  `active_standard` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '当日活跃标准（观看激励广告次数）',
  `notice_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '通知公告',
  PRIMARY KEY (`app_setting_id`, `package_name`, `app_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12396 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of game_setting
-- ----------------------------
INSERT INTO `game_setting` VALUES (12391, 12419, 'com.layabox.gameyzzs', '测试', '1', 149, NULL, 143, 139, 122, '0', 100, '22:59', '23:59', '0', '0', '0', '1233461234');
INSERT INTO `game_setting` VALUES (12392, 12420, '142', '412', '0', 150, NULL, 144, 140, 0, '0', NULL, '16', '00', '0', '0', '0', '');
INSERT INTO `game_setting` VALUES (12393, 12421, '124123', '11243', '0', 151, NULL, 145, 141, 0, '0', NULL, '16', '00', '0', '0', '0', '');
INSERT INTO `game_setting` VALUES (12394, 12422, 'e12', 'e12', '0', 152, NULL, 146, 142, 0, '0', NULL, '16', '00', '0', '0', '0', '');
INSERT INTO `game_setting` VALUES (12395, 12423, 'com.xlsfxxl.app', '单机版', '0', 153, NULL, 147, 143, 0, '0', 0, '16', '00', '0', '0', '0', '');

-- ----------------------------
-- Table structure for global_gath
-- ----------------------------
DROP TABLE IF EXISTS `global_gath`;
CREATE TABLE `global_gath`  (
  `gb_id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '全局聚集的id',
  `gb_gath_choice` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '全局聚集选择项 (大范围还是小范围,0为小,1为大)',
  `gb_gath_l` int(11) NULL DEFAULT NULL COMMENT '全局聚集大范围人数',
  `gb_gath_s` int(11) NULL DEFAULT NULL COMMENT '小范围人数',
  `gb_mac_usr` int(11) NULL DEFAULT NULL COMMENT '允许同mac在线用户',
  `gb_model_lm` int(11) NULL DEFAULT NULL COMMENT '同型号设备数',
  PRIMARY KEY (`gb_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of global_gath
-- ----------------------------
INSERT INTO `global_gath` VALUES (1, '1', 329, 36, 123, 1);

-- ----------------------------
-- Table structure for image_mapping
-- ----------------------------
DROP TABLE IF EXISTS `image_mapping`;
CREATE TABLE `image_mapping`  (
  `img_id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '图片主键id',
  `img_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '访问图片的url',
  `directory` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '哪个目录下',
  `fileName` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件名称',
  `fileHash` varchar(85) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件hash值',
  `img_business` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商户名',
  `img_business_id` varchar(37) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商户单号',
  `img_money` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '充值金额',
  `img_trans` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '交易单号',
  `img_pay_time` datetime NULL DEFAULT NULL COMMENT '充值时间',
  `img_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片类型, o来源 t订单 g游戏截图',
  `img_order_id` varchar(38) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '该图属于哪笔订单',
  `player_id` bigint(20) NULL DEFAULT NULL COMMENT '上传者',
  `uploadTime` datetime NULL DEFAULT NULL COMMENT '该图上传日期',
  `is_approval` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'i' COMMENT '该图片是否通过,默认i是inapproval,未通过a是approval,通过',
  `adv_encourage_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '对应激励广告id',
  PRIMARY KEY (`img_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 233 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of image_mapping
-- ----------------------------
INSERT INTO `image_mapping` VALUES (215, 'http://192.168.99.22:9000/12419/ab56972ccec54a01a8ab1cd35a55ecb92024/12/27/SS_416cc8a2-6797-4e6a-8cbe-1416f7eca20e.jpeg', 'ab56972ccec54a01a8ab1cd35a55ecb92024/12/27', 'SS_416cc8a2-6797-4e6a-8cbe-1416f7eca20e', 'ae05f31cfbbdfb53995f37909a0352b9bfcf2e4c93a924bbb2736cd84e98a2f4', '北京晟宏之兴信息技术有限公司', '88042944201', '6.00', '4200002352202410059266778040', '2024-10-05 09:56:03', 't', '37', 1000030, '2024-12-27 16:12:30', 'a', NULL);
INSERT INTO `image_mapping` VALUES (216, 'http://192.168.99.22:9000/12419/ab56972ccec54a01a8ab1cd35a55ecb92024/12/27/SD_416cc8a2-6797-4e6a-8cbe-1416f7eca20e.jpeg', 'ab56972ccec54a01a8ab1cd35a55ecb92024/12/27', 'SD_416cc8a2-6797-4e6a-8cbe-1416f7eca20e', '963cd729799ef499ca4333e755a21045ac2efa7df07e43405edfa6107591004b', '北京晟宏之兴信息技术有限公司', '88042944201', '6.00', '4200002352202410059266778040', '2024-10-05 09:56:03', 't', '37', 1000030, '2024-12-27 16:12:30', 'a', NULL);
INSERT INTO `image_mapping` VALUES (217, 'http://192.168.99.22:9000/12419/ab56972ccec54a01a8ab1cd35a55ecb92024/12/27/SW_416cc8a2-6797-4e6a-8cbe-1416f7eca20e.jpeg', 'ab56972ccec54a01a8ab1cd35a55ecb92024/12/27', 'SW_416cc8a2-6797-4e6a-8cbe-1416f7eca20e', '1f83bd617829c19046ca7ab3cd7feaf1f6f36d23e4918a8acd566da87cf125aa', '北京晟宏之兴信息技术有限公司', '88042944201', '6.00', '4200002352202410059266778040', '2024-10-05 09:56:03', 't', '37', 1000030, '2024-12-27 16:12:30', 'a', NULL);
INSERT INTO `image_mapping` VALUES (218, 'http://192.168.99.22:9000/12419/d1f996ce714442f586a5502d7080804c2024/12/27/SS_6071a992-6398-444b-8b38-eb8579937ae2.jpeg', 'd1f996ce714442f586a5502d7080804c2024/12/27', 'SS_6071a992-6398-444b-8b38-eb8579937ae2', '9ae6bf6218bfd838bae2e7927b416bb736ab8f87c83ef688d01c7f7807d1a50d', NULL, NULL, NULL, NULL, NULL, 'o', '37', 1000030, '2024-12-27 16:12:59', 'i', NULL);
INSERT INTO `image_mapping` VALUES (219, 'http://192.168.99.22:9000/12419/d1f996ce714442f586a5502d7080804c2024/12/27/SD_6071a992-6398-444b-8b38-eb8579937ae2.jpeg', 'd1f996ce714442f586a5502d7080804c2024/12/27', 'SD_6071a992-6398-444b-8b38-eb8579937ae2', '6c0511fda566e7ccca8fc51819baedce8461f21c9b6abb584c29bd0714900904', NULL, NULL, NULL, NULL, NULL, 'o', '37', 1000030, '2024-12-27 16:12:59', 'i', NULL);
INSERT INTO `image_mapping` VALUES (220, 'http://192.168.99.22:9000/12419/d1f996ce714442f586a5502d7080804c2024/12/27/SW_6071a992-6398-444b-8b38-eb8579937ae2.jpeg', 'd1f996ce714442f586a5502d7080804c2024/12/27', 'SW_6071a992-6398-444b-8b38-eb8579937ae2', '1f83bd617829c19046ca7ab3cd7feaf1f6f36d23e4918a8acd566da87cf125aa', NULL, NULL, NULL, NULL, NULL, 'o', '37', 1000030, '2024-12-27 16:12:59', 'i', NULL);
INSERT INTO `image_mapping` VALUES (221, NULL, '52e1935090f3428cb4d339d8cee45eac2024/12/27', 'SS_786d054a-98d9-4d65-93f1-cbe22fa4b39d', '174bd34860499e6da40b180d974c1b08b412b076318c9cc656d95cc75ab092fc', '广州崧高网络科技有限公司', '1034_8_2410012339ndQ7', '3.30', '4200002450202410029297402850', '2024-12-27 07:39:45', 't', NULL, 1000030, '2024-12-27 17:11:16', 'i', NULL);
INSERT INTO `image_mapping` VALUES (222, NULL, '52e1935090f3428cb4d339d8cee45eac2024/12/27', 'SD_786d054a-98d9-4d65-93f1-cbe22fa4b39d', 'a8d74bd3a735262b57cfde64691e4aa4ca1aa66d12f1c53dc9177760d9f54c8f', '广州崧高网络科技有限公司', '1034_8_2410012339ndQ7', '3.30', '4200002450202410029297402850', '2024-12-27 07:39:45', 't', NULL, 1000030, '2024-12-27 17:11:16', 'i', NULL);
INSERT INTO `image_mapping` VALUES (223, NULL, '52e1935090f3428cb4d339d8cee45eac2024/12/27', 'SW_786d054a-98d9-4d65-93f1-cbe22fa4b39d', '1f83bd617829c19046ca7ab3cd7feaf1f6f36d23e4918a8acd566da87cf125aa', '广州崧高网络科技有限公司', '1034_8_2410012339ndQ7', '3.30', '4200002450202410029297402850', '2024-12-27 07:39:45', 't', NULL, 1000030, '2024-12-27 17:11:16', 'i', NULL);
INSERT INTO `image_mapping` VALUES (224, 'http://192.168.99.22:9000/12419/a85b95fb839144d3997a2e5962dbc6552024/12/27/SS_cf420129-d80d-489f-bf87-4cfe62c9843f.jpeg', 'a85b95fb839144d3997a2e5962dbc6552024/12/27', 'SS_cf420129-d80d-489f-bf87-4cfe62c9843f', '3d365ebfe5f1b9e9d6e6c3539829ad0ddda092fd50f7622b49d75ddef2464e72', NULL, NULL, NULL, NULL, NULL, 'o', 'a85b95fb839144d3997a2e5962dbc655', 1000030, '2024-12-27 17:35:14', 'i', NULL);
INSERT INTO `image_mapping` VALUES (225, 'http://192.168.99.22:9000/12419/a85b95fb839144d3997a2e5962dbc6552024/12/27/SD_cf420129-d80d-489f-bf87-4cfe62c9843f.jpeg', 'a85b95fb839144d3997a2e5962dbc6552024/12/27', 'SD_cf420129-d80d-489f-bf87-4cfe62c9843f', 'aa9160b2fd4a53c009da835cd06e74a1580fca91f3684d1e6ab61c0dcfef4983', NULL, NULL, NULL, NULL, NULL, 'o', 'a85b95fb839144d3997a2e5962dbc655', 1000030, '2024-12-27 17:35:14', 'i', NULL);
INSERT INTO `image_mapping` VALUES (226, 'http://192.168.99.22:9000/12419/a85b95fb839144d3997a2e5962dbc6552024/12/27/SW_cf420129-d80d-489f-bf87-4cfe62c9843f.jpeg', 'a85b95fb839144d3997a2e5962dbc6552024/12/27', 'SW_cf420129-d80d-489f-bf87-4cfe62c9843f', '1f83bd617829c19046ca7ab3cd7feaf1f6f36d23e4918a8acd566da87cf125aa', NULL, NULL, NULL, NULL, NULL, 'o', 'a85b95fb839144d3997a2e5962dbc655', 1000030, '2024-12-27 17:35:14', 'i', NULL);
INSERT INTO `image_mapping` VALUES (227, 'http://192.168.99.22:9000/12419/6baeda48ef4c4b5dab87bfe097cfd2622024/12/27/SS_2e2b86e0-d3d0-4cd8-9b39-9347b6bf000a.jpeg', '6baeda48ef4c4b5dab87bfe097cfd2622024/12/27', 'SS_2e2b86e0-d3d0-4cd8-9b39-9347b6bf000a', '0e25c0e53d147b2b7b90dc0d9a00ef479baa7a4387dc013a721c759ce8451bd5', '上海有赢网络科技有限公司', '24092917365448197485674', '6.00', '4200002415202409292272200832', '2024-12-27 17:37:04', 't', '6baeda48ef4c4b5dab87bfe097cfd262', 1000030, '2024-12-27 17:36:25', 'i', NULL);
INSERT INTO `image_mapping` VALUES (228, 'http://192.168.99.22:9000/12419/6baeda48ef4c4b5dab87bfe097cfd2622024/12/27/SD_2e2b86e0-d3d0-4cd8-9b39-9347b6bf000a.jpeg', '6baeda48ef4c4b5dab87bfe097cfd2622024/12/27', 'SD_2e2b86e0-d3d0-4cd8-9b39-9347b6bf000a', '856bb27ae4e2cb9c8b1b1fc5a237eb150e4f12747e5ad3ba1b48f2a68628b6cb', '上海有赢网络科技有限公司', '24092917365448197485674', '6.00', '4200002415202409292272200832', '2024-12-27 17:37:04', 't', '6baeda48ef4c4b5dab87bfe097cfd262', 1000030, '2024-12-27 17:36:25', 'i', NULL);
INSERT INTO `image_mapping` VALUES (229, 'http://192.168.99.22:9000/12419/6baeda48ef4c4b5dab87bfe097cfd2622024/12/27/SW_2e2b86e0-d3d0-4cd8-9b39-9347b6bf000a.jpeg', '6baeda48ef4c4b5dab87bfe097cfd2622024/12/27', 'SW_2e2b86e0-d3d0-4cd8-9b39-9347b6bf000a', '1f83bd617829c19046ca7ab3cd7feaf1f6f36d23e4918a8acd566da87cf125aa', '上海有赢网络科技有限公司', '24092917365448197485674', '6.00', '4200002415202409292272200832', '2024-12-27 17:37:04', 't', '6baeda48ef4c4b5dab87bfe097cfd262', 1000030, '2024-12-27 17:36:25', 'i', NULL);
INSERT INTO `image_mapping` VALUES (230, NULL, 'c088cd74800c45cb9d3657db29f4943e2024/12/28', 'SS_6eb17594-2221-426b-9cd9-52fe4470f9a9', '9e8b54952a3273e1c57de1112967807e9b5b04b3931ffd57431ef1aa8817115b', NULL, NULL, NULL, NULL, NULL, 'o', NULL, 1000030, '2024-12-28 22:12:35', 'i', NULL);
INSERT INTO `image_mapping` VALUES (231, NULL, 'c088cd74800c45cb9d3657db29f4943e2024/12/28', 'SD_6eb17594-2221-426b-9cd9-52fe4470f9a9', '1538f37425473f09baa466350cc55dd2241b6b7d228b45a57a0421f7d8e415d1', NULL, NULL, NULL, NULL, NULL, 'o', NULL, 1000030, '2024-12-28 22:12:35', 'i', NULL);
INSERT INTO `image_mapping` VALUES (232, NULL, 'c088cd74800c45cb9d3657db29f4943e2024/12/28', 'SW_6eb17594-2221-426b-9cd9-52fe4470f9a9', '1f83bd617829c19046ca7ab3cd7feaf1f6f36d23e4918a8acd566da87cf125aa', NULL, NULL, NULL, NULL, NULL, 'o', NULL, 1000030, '2024-12-28 22:12:35', 'i', NULL);

-- ----------------------------
-- Table structure for imgorder
-- ----------------------------
DROP TABLE IF EXISTS `imgorder`;
CREATE TABLE `imgorder`  (
  `order_id` varchar(38) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单id',
  `order_player_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '该笔订单由谁发起',
  `withdraw_percentage` decimal(10, 2) NULL DEFAULT NULL COMMENT '当时的提现比例',
  `androidID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '安卓id',
  `wxName` varchar(59) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'wx微信昵称',
  `application_name` varchar(35) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '应用名称',
  `packageName` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '包名,哪个游戏中提交的订单,哪个游戏的包名',
  `appid` bigint(20) NULL DEFAULT NULL COMMENT '应用id,哪个游戏的id,',
  `is_get` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'x' COMMENT '订单状态\r\n(0是已领取,1是待领取 ,x是待通过,y是已通过,b是驳回)\r\n',
  `back_reason` varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单状态为b(back)时,订单有驳回原因',
  `order_business` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商户名',
  `order_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '订单金额',
  `order_business_id` varchar(35) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商户单号',
  `order_trans_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '交易单号',
  `order_pay_time` datetime NULL DEFAULT NULL COMMENT '充值时间',
  `adv_encourage_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '激励广告id',
  PRIMARY KEY (`order_id`) USING BTREE,
  UNIQUE INDEX `transIndex`(`order_trans_id` ASC) USING BTREE COMMENT '订单号是唯一的'
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of imgorder
-- ----------------------------
INSERT INTO `imgorder` VALUES ('37', 1000030, 10000.00, 'b9c922e4a2d8756a', '一叶知秋', '联机版', 'com.layabox.gameyzzs', 12419, '1', NULL, '北京晟宏之兴信息技术有限公司', 6.00, '88042944201', '4200002352202410059266778040', '2024-10-05 09:56:03', NULL);
INSERT INTO `imgorder` VALUES ('a85b95fb839144d3997a2e5962dbc655', 1000030, 10000.00, 'b9c922e4a2d8756a', '一叶知秋', '联机版', 'com.layabox.gameyzzs', 12419, 'b', '测试g', '上海有赢网络科技有限公司', 6.00, '24092917365448197485674', '4200002415202409292272200832', '2024-12-27 17:37:04', NULL);

-- ----------------------------
-- Table structure for master
-- ----------------------------
DROP TABLE IF EXISTS `master`;
CREATE TABLE `master`  (
  `admin_id` varchar(38) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '管理员id',
  `admin_account` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '管理员账号',
  `admin_pwd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '管理员密码',
  `role` tinyint(4) NOT NULL COMMENT ' 管理员类型',
  PRIMARY KEY (`admin_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of master
-- ----------------------------
INSERT INTO `master` VALUES ('09748c69933d4561941c58c335d6c168', '31_0', '123456', 3);
INSERT INTO `master` VALUES ('11943519b5134f49a2b5f83ccac51a0c', '小陈_1', '123456', 3);
INSERT INTO `master` VALUES ('40558d5484ca409d9da593f00b418a45', '小张', '888888', 2);
INSERT INTO `master` VALUES ('499fc43a-5f35-4986-b920-417f382c4c79', '123zzz', '123123', 1);
INSERT INTO `master` VALUES ('4f3057e4154548ed94219866c28cf93e', '小陈_2', '123456', 3);
INSERT INTO `master` VALUES ('857a02f9f80c4f55bbbae3d662441326', '31_1', '123456', 3);
INSERT INTO `master` VALUES ('8dbac3548dc54459a7e71d5829a47420', '31', '123456', 2);
INSERT INTO `master` VALUES ('97acd4a3654e44b38af6c551bab744f7', '小陈', '123456', 2);
INSERT INTO `master` VALUES ('bc2ef88f232a4a0dbc0ee2910e6bb660', '小陈_0', '123456', 3);
INSERT INTO `master` VALUES ('d63dbda33225479a8a23ba66b39b4790', '小张_0', '123456', 3);

-- ----------------------------
-- Table structure for mobile_detail
-- ----------------------------
DROP TABLE IF EXISTS `mobile_detail`;
CREATE TABLE `mobile_detail`  (
  `mobile_id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `device_name` varchar(140) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '设备名称',
  `device_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机品牌',
  `device_detail` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机型号',
  `mobile_cpu` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'CPU',
  `mobile_cpu_fluency` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'CPU频率',
  `mobile_system` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备系统',
  PRIMARY KEY (`mobile_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 68 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mobile_detail
-- ----------------------------
INSERT INTO `mobile_detail` VALUES (1, 'nova 8 Pro', 'HUAWEI', 'BRQ-AL00', 'vendor Kirin985||kirin985', '4*1844MHz|3*2400MHz|1*2583MHz', NULL);
INSERT INTO `mobile_detail` VALUES (2, 'vivo S12', 'vivo', 'V2162A', 'MT6891Z/CZA|MT6891Z/CZA|mt6893', '4*2000MHz|4*2600MHz', NULL);
INSERT INTO `mobile_detail` VALUES (3, 'MI 9', 'Xiaomi', 'MI 9', 'Qualcomm Technologies, Inc SM8150||qcom', '4*1785MHz|3*2419MHz|1*2841MHz', NULL);
INSERT INTO `mobile_detail` VALUES (4, '荣耀 X20', 'HONOR', 'NTN-AN20', 'MT6877V/ZA|MT6877V/ZA|mt6877', '6*2000MHz|2*2400MHz', NULL);
INSERT INTO `mobile_detail` VALUES (5, '荣耀 8X', 'HONOR', 'JSN-AL00a', 'Hisilicon Kirin710||kirin710', '4*1709MHz|4*2189MHz', NULL);
INSERT INTO `mobile_detail` VALUES (6, 'OPPO Find X6 Pro', 'OPPO', 'PGEM10', '|SM8550|qcom', '3*2016MHz|4*2803MHz|1*3187MHz', NULL);
INSERT INTO `mobile_detail` VALUES (7, '荣耀Play4 Pro', 'HONOR', 'OXP-AN00', 'vendor Kirin990||kirin990', '4*1863MHz|2*2088MHz|2*2861MHz', NULL);
INSERT INTO `mobile_detail` VALUES (8, 'vivo S15', 'vivo', 'V2203A', 'Qualcomm Technologies, Inc SM8250|SM8250|qcom', '4*1804MHz|3*2419MHz|1*3187MHz', NULL);
INSERT INTO `mobile_detail` VALUES (9, '荣耀 V30', 'HONOR', 'OXF-AN00', 'vendor Kirin990||kirin990', '4*1863MHz|2*2088MHz|2*2861MHz', NULL);
INSERT INTO `mobile_detail` VALUES (10, '荣耀 30', 'HONOR', 'BMH-AN10', 'vendor Kirin985||kirin985', '4*1844MHz|3*2400MHz|1*2583MHz', NULL);
INSERT INTO `mobile_detail` VALUES (11, 'vivo S10', 'vivo', 'V2121A', 'MT6891Z/CZA|MT6891Z/CZA|mt6893', '4*2000MHz|4*2600MHz', NULL);
INSERT INTO `mobile_detail` VALUES (12, 'vivo S9', 'vivo', 'V2072A', 'MT6891Z/CZA|MT6891Z/CZA|mt6893', '4*2000MHz|4*2600MHz', NULL);
INSERT INTO `mobile_detail` VALUES (13, '荣耀畅玩50 Plus', 'HONOR', 'CLK-AN00', 'MT6833|MT6833|mt6833', '6*2000MHz|2*2203MHz', NULL);
INSERT INTO `mobile_detail` VALUES (14, 'HUAWEI P40', 'HUAWEI', 'ANA-AN00', '|kirin|kirin990', '4*1954MHz|2*2360MHz|2*2861MHz', NULL);
INSERT INTO `mobile_detail` VALUES (15, '荣耀Play8T', 'HONOR', 'CLK-AN00', 'MT6833|MT6833|mt6833', '6*2000MHz|2*2400MHz', NULL);
INSERT INTO `mobile_detail` VALUES (16, 'MAIMANG 9 5G', 'Tianyi', 'TYH601M', 'MT6873||mt6873', '8*2000MHz', NULL);
INSERT INTO `mobile_detail` VALUES (17, '荣耀Play5活力版 ', 'HONOR', 'NEW-AN90', 'MT6877V/ZA|MT6877V/ZA|mt6877', '6*2000MHz|2*2400MHz', NULL);
INSERT INTO `mobile_detail` VALUES (18, '荣耀Play7T Pro', 'HONOR', 'DIO-AN00', 'MT6833|MT6833|mt6833', '6*2000MHz|2*2203MHz', NULL);
INSERT INTO `mobile_detail` VALUES (19, '华为畅享20 Pro', 'HUAWEI', 'DVC-AN20', 'MT6873||mt6873', '8*2000MHz', NULL);
INSERT INTO `mobile_detail` VALUES (20, 'nova 7 SE 5G', 'HUAWEI', 'CDY-AN00', 'vendor Kirin820||kirin820', '4*1844MHz|3*2228MHz|1*2362MHz', NULL);
INSERT INTO `mobile_detail` VALUES (21, 'nova 7 SE 5G 活力版', 'HUAWEI', 'CND-AN00', 'MT6853V/TNZA||mt6853', '6*2000MHz|2*2400MHz', NULL);
INSERT INTO `mobile_detail` VALUES (22, 'nova 7 SE 5G 乐活版', 'HUAWEI', 'CDL-AN50', 'vendor Kirin820E||kirin820E', '3*1844MHz|3*2228MHz', NULL);
INSERT INTO `mobile_detail` VALUES (26, 'nova 8 SE', 'HUAWEI', 'JSC-AN00', 'MT6853V/TNZA||mt6853', '6*2000MHz|2*2400MHz', NULL);
INSERT INTO `mobile_detail` VALUES (27, '华为畅享20 Plus 5G', 'HUAWEI', 'FRL-AN00a', 'MT6853V/ZA||mt6853', '8*2000MHz', NULL);
INSERT INTO `mobile_detail` VALUES (28, '荣耀Play6T', 'HONOR', 'CMA-AN40', 'MT6833V/ZA||mt6833', '6*2000MHz|2*2203MHz', NULL);
INSERT INTO `mobile_detail` VALUES (29, '荣耀50 SE', 'HUAWEI', 'CDY-AN20', 'vendor Kirin820||kirin820', '4*1844MHz|3*2228MHz|1*2362MHz', NULL);
INSERT INTO `mobile_detail` VALUES (30, '荣耀Play5活力版', 'HONOR', 'JLH-AN00', 'MT6877V/ZA|MT6877V/ZA|mt6877', '6*2000MHz|2*2400MHz', NULL);
INSERT INTO `mobile_detail` VALUES (31, '荣耀Play6T Pro', 'HONOR', 'TFY-AN40', 'MT6833V/PNZA|MT6833V/PNZA|mt6833', '6*2000MHz|2*2400MHz', NULL);
INSERT INTO `mobile_detail` VALUES (32, '荣耀Play7T', 'HONOR', 'RKY-AN10', 'MT6833|MT6833|mt6833', '6*2000MHz|2*2203MHz', NULL);
INSERT INTO `mobile_detail` VALUES (33, '荣耀Play4', 'HONOR', 'TNNH-AN00', 'MT6873||mt6873', '8*2000MHz', NULL);
INSERT INTO `mobile_detail` VALUES (34, 'nova 7 5G', 'HUAWEI', 'JEF-AN20', '|kirin|kirin985', '4*1844MHz|3*2400MHz|1*2583MHz', NULL);
INSERT INTO `mobile_detail` VALUES (35, 'nova 6 (5G)', 'HUAWEI', 'WLZ-AN00', 'vendor Kirin990||kirin990', '4*1863MHz|2*2088MHz|2*2861MHz', NULL);
INSERT INTO `mobile_detail` VALUES (36, 'OPPO K11 5G', 'OPPO', 'PJC110', 'Qualcomm Technologies, Inc SDM782G|SM7325|qcom', '4*1804MHz|3*2400MHz|1*2707MHz', NULL);
INSERT INTO `mobile_detail` VALUES (37, 'vivo S7', 'vivo', 'V2020CA', 'Qualcomm Technologies, Inc SM7250||qcom', '6*1804MHz|1*2208MHz|1*2400MHz', NULL);
INSERT INTO `mobile_detail` VALUES (38, 'nova 7 5G', 'HUAWEI', 'JEF-TN00', '|kirin|kirin985', '4*1844MHz|3*2400MHz|1*2583MHz', NULL);
INSERT INTO `mobile_detail` VALUES (39, 'Redmi Note 11T Pro', 'Redmi', '22041216C', '|MT6895Z/TCZA|mt6895', '4*2000MHz|4*2850MHz', NULL);
INSERT INTO `mobile_detail` VALUES (40, 'MI 8', 'Xiaomi', 'MI 8 UD', 'Qualcomm Technologies, Inc SDM845||qcom', '4*1766MHz|4*2803MHz', NULL);
INSERT INTO `mobile_detail` VALUES (41, 'nova 5i', 'HUAWEI', 'GLK-AL00', 'vendor Kirin710||kirin710', '4*1709MHz|4*2189MHz', NULL);
INSERT INTO `mobile_detail` VALUES (42, '测试2', '1', '1', '1', '1', 'Android 10');
INSERT INTO `mobile_detail` VALUES (43, '测试1', '13', '1', '1', '1', 'Android 13');
INSERT INTO `mobile_detail` VALUES (44, '', 'OPPO', 'PEAM00', 'MT6853V/ZA|MT6853V/TNZA|mt6853', '8*2000MHz', NULL);
INSERT INTO `mobile_detail` VALUES (45, '', 'OPPO', 'PEYM00', 'MT6893Z/CZA|MT6893|mt6893', '4*2000MHz|3*2600MHz|1*3000MHz', NULL);
INSERT INTO `mobile_detail` VALUES (46, '', 'HONOR', 'DIO-AN00', 'MT6833', '6*2000MHz|2*2203MHz', 'Android 12');
INSERT INTO `mobile_detail` VALUES (47, '测试1', '14', '1', '1', '1', 'Android 10');
INSERT INTO `mobile_detail` VALUES (48, '测试1', '14', '2', '2', '2', 'Android 10');
INSERT INTO `mobile_detail` VALUES (49, '', '15', '2', '2', '2', 'Android 13');
INSERT INTO `mobile_detail` VALUES (50, '', '15', '1', '1', '1', 'Android 10');
INSERT INTO `mobile_detail` VALUES (51, '假的', 'HUAWEI', 'LIO-AN00', 'Qualcomm Technologies, Inc MSM8998||qcom', '4*2865MHz', NULL);
INSERT INTO `mobile_detail` VALUES (52, '', 'Redmi', '2311DRK48C', '|MT6897|mt6897', '4*2200MHz|3*3200MHz|1*3350MHz', NULL);
INSERT INTO `mobile_detail` VALUES (53, '12d41', '123', 'MI 8 UD', 'Qualcomm Technologies, Inc SDM845||qcom', '4*1766MHz|4*2803MHz', NULL);
INSERT INTO `mobile_detail` VALUES (56, '', 'test', 'test', 'test', 'test', 'test');
INSERT INTO `mobile_detail` VALUES (64, '', '123', 'V2162A', 'MT6891Z/CZA|MT6891Z/CZA|mt6893', '4*2000MHz|4*2600MHz', NULL);
INSERT INTO `mobile_detail` VALUES (65, '', '123', 'MI 9', 'Qualcomm Technologies, Inc SM8150||qcom', '4*1785MHz|3*2419MHz|1*2841MHz', NULL);
INSERT INTO `mobile_detail` VALUES (66, '', '123', 'BRQ-AL00', 'vendor Kirin985||kirin985', '4*1844MHz|3*2400MHz|1*2583MHz', NULL);
INSERT INTO `mobile_detail` VALUES (67, '', '123', '22041216C', '|MT6895Z/TCZA|mt6895', '4*2000MHz|4*2850MHz', NULL);

-- ----------------------------
-- Table structure for proxy_game
-- ----------------------------
DROP TABLE IF EXISTS `proxy_game`;
CREATE TABLE `proxy_game`  (
  `proxy_game_id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '代理和他对应代理游戏的id',
  `proxy_package` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '代理他代理的这个游戏的包名',
  `master_id` varchar(35) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '该代理的id',
  `proxy_game_time` datetime NULL DEFAULT NULL COMMENT '该代理什么时候代理的这个游戏',
  `proxy_game_cash` decimal(10, 2) NULL DEFAULT NULL COMMENT '该代理在这个游戏中的余额',
  PRIMARY KEY (`proxy_game_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of proxy_game
-- ----------------------------
INSERT INTO `proxy_game` VALUES (15, 'com.layabox.gameyzzs', '94fa33014abb45d1a19dd625d1e81e38', '2024-12-23 16:46:26', 0.00);
INSERT INTO `proxy_game` VALUES (16, 'e12', '94fa33014abb45d1a19dd625d1e81e38', '2024-12-23 16:46:26', 0.00);
INSERT INTO `proxy_game` VALUES (17, 'com.layabox.gameyzzs', 'c731c40d3c134e0e8cf856cf4236182c', '2024-12-23 16:50:35', 0.00);
INSERT INTO `proxy_game` VALUES (18, 'e12', 'c731c40d3c134e0e8cf856cf4236182c', '2024-12-23 16:50:35', 0.00);
INSERT INTO `proxy_game` VALUES (19, 'com.layabox.gameyzzs', 'caacb3b8dd434db99a4f1a0f2ee8d555', '2024-12-23 16:50:49', 0.00);
INSERT INTO `proxy_game` VALUES (20, 'e12', 'caacb3b8dd434db99a4f1a0f2ee8d555', '2024-12-23 16:50:49', 0.00);
INSERT INTO `proxy_game` VALUES (21, 'com.layabox.gameyzzs', '8dbac3548dc54459a7e71d5829a47420', '2024-12-23 17:00:19', 0.00);
INSERT INTO `proxy_game` VALUES (22, 'e12', '8dbac3548dc54459a7e71d5829a47420', '2024-12-23 17:00:19', 0.00);
INSERT INTO `proxy_game` VALUES (23, 'com.layabox.gameyzzs', '09748c69933d4561941c58c335d6c168', '2024-12-23 17:00:46', 0.00);
INSERT INTO `proxy_game` VALUES (24, 'e12', '09748c69933d4561941c58c335d6c168', '2024-12-23 17:00:47', 0.00);
INSERT INTO `proxy_game` VALUES (25, 'com.layabox.gameyzzs', '857a02f9f80c4f55bbbae3d662441326', '2024-12-23 17:00:54', 0.00);
INSERT INTO `proxy_game` VALUES (26, 'e12', '857a02f9f80c4f55bbbae3d662441326', '2024-12-23 17:00:54', 0.00);
INSERT INTO `proxy_game` VALUES (27, 'com.layabox.gameyzzs', '40558d5484ca409d9da593f00b418a45', '2024-12-23 17:27:19', 0.00);
INSERT INTO `proxy_game` VALUES (28, 'e12', '40558d5484ca409d9da593f00b418a45', '2024-12-23 17:27:19', 0.00);
INSERT INTO `proxy_game` VALUES (29, 'com.layabox.gameyzzs', 'd63dbda33225479a8a23ba66b39b4790', '2024-12-23 17:27:52', 0.00);
INSERT INTO `proxy_game` VALUES (30, 'e12', 'd63dbda33225479a8a23ba66b39b4790', '2024-12-23 17:27:52', 0.00);
INSERT INTO `proxy_game` VALUES (31, 'com.layabox.gameyzzs', '97acd4a3654e44b38af6c551bab744f7', '2024-12-23 18:14:20', 0.00);
INSERT INTO `proxy_game` VALUES (32, 'e12', '97acd4a3654e44b38af6c551bab744f7', '2024-12-23 18:14:20', 0.00);
INSERT INTO `proxy_game` VALUES (33, 'com.layabox.gameyzzs', 'bc2ef88f232a4a0dbc0ee2910e6bb660', '2024-12-23 18:15:13', 0.00);
INSERT INTO `proxy_game` VALUES (34, 'e12', 'bc2ef88f232a4a0dbc0ee2910e6bb660', '2024-12-23 18:15:13', 0.00);
INSERT INTO `proxy_game` VALUES (35, 'com.layabox.gameyzzs', '11943519b5134f49a2b5f83ccac51a0c', '2024-12-23 20:28:40', 0.00);
INSERT INTO `proxy_game` VALUES (36, 'e12', '11943519b5134f49a2b5f83ccac51a0c', '2024-12-23 20:28:40', 0.00);
INSERT INTO `proxy_game` VALUES (37, 'com.layabox.gameyzzs', '4f3057e4154548ed94219866c28cf93e', '2024-12-25 17:53:28', 0.00);
INSERT INTO `proxy_game` VALUES (38, 'e12', '4f3057e4154548ed94219866c28cf93e', '2024-12-25 17:53:28', 0.00);
INSERT INTO `proxy_game` VALUES (39, 'com.xlsfxxl.app', '4f3057e4154548ed94219866c28cf93e', '2024-12-25 17:53:28', 0.00);

-- ----------------------------
-- Table structure for proxy_team
-- ----------------------------
DROP TABLE IF EXISTS `proxy_team`;
CREATE TABLE `proxy_team`  (
  `master_id` varchar(38) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '对应master的id',
  `proxy_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT ' 团队标识(32位随机uuid)',
  `balance` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '账户所有游戏的总余额',
  `proxy_create_time` datetime NULL DEFAULT NULL COMMENT '该代理被创建的时间',
  `proxy_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '代理团队的状态 ，0为开，1为关',
  `enable_two_proxy` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '代表该代理是一级还是二级(0为一级，1为二级）',
  `proxy_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标识渠道还是代理  p代表代理,c代表渠道     channel和proxy',
  `proxyGamePropotion` double(10, 2) NULL DEFAULT NULL COMMENT '该代理的比例',
  `calculateWay` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '该代理的计算方式(一级代理才会有) 0代表提现方式,1代表奖励方式'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of proxy_team
-- ----------------------------
INSERT INTO `proxy_team` VALUES ('8dbac3548dc54459a7e71d5829a47420', '31', 0.00, '2024-12-23 17:00:19', '0', '0', 'p', 21.00, '0');
INSERT INTO `proxy_team` VALUES ('40558d5484ca409d9da593f00b418a45', '小张', 0.00, '2024-12-23 17:27:19', '0', '0', 'p', 12.00, '1');
INSERT INTO `proxy_team` VALUES ('97acd4a3654e44b38af6c551bab744f7', '小陈', 0.00, '2024-12-23 18:14:20', '', '0', 'p', 12.00, NULL);

-- ----------------------------
-- Table structure for proxy_team_son
-- ----------------------------
DROP TABLE IF EXISTS `proxy_team_son`;
CREATE TABLE `proxy_team_son`  (
  `son_master_id` varchar(38) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '二级代理的id',
  `proxy_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '团队标识',
  `parent_id` varchar(38) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '该二级代理属于哪个一级代理(对应的团队标识）',
  `proxy_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '代理团队的状态，0为开，1为关',
  `proxy_create_time` datetime NULL DEFAULT NULL COMMENT '该代理被创建的时间',
  `proxy_proportion` double(20, 4) NULL DEFAULT NULL COMMENT '代理比例',
  `balance` decimal(10, 2) NULL DEFAULT NULL COMMENT '账户余额',
  PRIMARY KEY (`son_master_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of proxy_team_son
-- ----------------------------
INSERT INTO `proxy_team_son` VALUES ('09748c69933d4561941c58c335d6c168', '31_0', '8dbac3548dc54459a7e71d5829a47420', '0', '2024-12-23 17:00:46', 21.0000, 0.00);
INSERT INTO `proxy_team_son` VALUES ('11943519b5134f49a2b5f83ccac51a0c', '小陈_1', '97acd4a3654e44b38af6c551bab744f7', '0', '2024-12-23 20:28:40', 12.0000, 0.00);
INSERT INTO `proxy_team_son` VALUES ('211', '德玛西亚', '127', '0', '2024-10-15 20:13:56', 20.1000, 412.23);
INSERT INTO `proxy_team_son` VALUES ('4f3057e4154548ed94219866c28cf93e', '小陈_2', '97acd4a3654e44b38af6c551bab744f7', '0', '2024-12-25 17:53:28', 31.0000, 0.00);
INSERT INTO `proxy_team_son` VALUES ('857a02f9f80c4f55bbbae3d662441326', '31_1', '8dbac3548dc54459a7e71d5829a47420', '0', '2024-12-23 17:00:54', 31.0000, 0.00);
INSERT INTO `proxy_team_son` VALUES ('bc2ef88f232a4a0dbc0ee2910e6bb660', '小陈_0', '97acd4a3654e44b38af6c551bab744f7', '0', '2024-12-23 18:15:13', 12.0000, 0.00);
INSERT INTO `proxy_team_son` VALUES ('c731c40d3c134e0e8cf856cf4236182c', '8888_1', 'c1e187b1-4539-405b-9a6b-dd33129f0d0e', '0', '2024-12-23 16:50:35', 21.0000, 0.00);
INSERT INTO `proxy_team_son` VALUES ('caacb3b8dd434db99a4f1a0f2ee8d555', '8888_2', 'c1e187b1-4539-405b-9a6b-dd33129f0d0e', '0', '2024-12-23 16:50:49', 12.0000, 0.00);
INSERT INTO `proxy_team_son` VALUES ('d63dbda33225479a8a23ba66b39b4790', '小张_0', '40558d5484ca409d9da593f00b418a45', '0', '2024-12-23 17:27:52', 12.0000, 0.00);

-- ----------------------------
-- Table structure for risk_control_setting
-- ----------------------------
DROP TABLE IF EXISTS `risk_control_setting`;
CREATE TABLE `risk_control_setting`  (
  `risk_controll_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '风控参数id',
  `package_name` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '应用包名',
  `root_enable` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否可以开启root (0 是开启 ，1是关闭)',
  `no_barrier` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '无障碍(0 是开启 ，1是关闭)',
  `usb` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否开启usb(0 是开启 ，1是关闭)',
  `otg` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否开启otg(0 是开启 ，1是关闭)',
  `adb` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否开启adb(0 是开启 ，1是关闭)',
  `simulator` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模拟器和云手机Enable',
  `bluetooth` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否开启蓝牙(0 是开启 ，1是关闭)',
  `charge` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否充电(0 是开启 ，1是关闭)',
  `vpn` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否使用VPN(0 是开启 ，1是关闭)',
  `sim` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户设备是否存在SIM卡(0 是开启 ，1是关闭)',
  `hotdot` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户是否开启热点 (0 是开启 ，1是关闭)',
  PRIMARY KEY (`risk_controll_id`, `package_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '风控控制表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of risk_control_setting
-- ----------------------------
INSERT INTO `risk_control_setting` VALUES (13, 'com.layabox.gameyzzs', '0', '1', '1', '1', '1', '0', '1', '1', '1', '1', '0');
INSERT INTO `risk_control_setting` VALUES (14, '142', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `risk_control_setting` VALUES (15, '124123', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `risk_control_setting` VALUES (16, 'e12', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `risk_control_setting` VALUES (17, 'com.xlsfxxl.app', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');

-- ----------------------------
-- Table structure for screenshot_setting
-- ----------------------------
DROP TABLE IF EXISTS `screenshot_setting`;
CREATE TABLE `screenshot_setting`  (
  `screenshot_setting_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '截图设置id',
  `game_id` bigint(20) NULL DEFAULT NULL COMMENT '游戏id',
  `screenshot_setting_val` double(10, 2) NULL DEFAULT NULL COMMENT '代码位ecpm值大于',
  `trans_limit_daily` int(11) NULL DEFAULT NULL COMMENT '当日允许用户提交截图订单上限',
  `trans_reward_cont` int(11) NULL DEFAULT NULL COMMENT '\r\n领取奖励观看广告次数',
  `screenshot_setting_options` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '截图选项1代表关闭,0代表开启',
  PRIMARY KEY (`screenshot_setting_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 144 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of screenshot_setting
-- ----------------------------
INSERT INTO `screenshot_setting` VALUES (139, 12419, 200.00, 2, 12, '0');
INSERT INTO `screenshot_setting` VALUES (140, 12420, 200.00, 20, 12, '1');
INSERT INTO `screenshot_setting` VALUES (141, 12421, 200.00, 20, 12, '1');
INSERT INTO `screenshot_setting` VALUES (142, 12422, 200.00, NULL, NULL, NULL);
INSERT INTO `screenshot_setting` VALUES (143, 12423, 200.00, 10, 10, '1');

-- ----------------------------
-- Table structure for sum_count_newplayer
-- ----------------------------
DROP TABLE IF EXISTS `sum_count_newplayer`;
CREATE TABLE `sum_count_newplayer`  (
  `sum_id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '统计唯一id',
  `proxy_name` varchar(38) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '团队标识',
  `sum_day` date NULL DEFAULT NULL COMMENT '哪一天',
  `new_player_Count` bigint(20) NOT NULL COMMENT '该日新用户的个数',
  `appid` bigint(20) NULL DEFAULT NULL COMMENT '属于哪个游戏的统计',
  PRIMARY KEY (`sum_id` DESC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 606 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sum_count_newplayer
-- ----------------------------
INSERT INTO `sum_count_newplayer` VALUES (605, '小陈', '2025-01-06', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (604, '小张', '2025-01-06', 1, 12419);
INSERT INTO `sum_count_newplayer` VALUES (603, '31', '2025-01-06', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (602, '小张_0', '2025-01-06', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (601, '8888_2', '2025-01-06', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (600, '8888_1', '2025-01-06', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (599, '小陈_0', '2025-01-06', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (598, '31_1', '2025-01-06', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (597, '小陈_2', '2025-01-06', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (596, '德玛西亚', '2025-01-06', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (595, '小陈_1', '2025-01-06', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (594, '31_0', '2025-01-06', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (593, '小陈', '2025-01-14', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (592, '小张', '2025-01-14', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (591, '31', '2025-01-14', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (590, '小张_0', '2025-01-14', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (589, '8888_2', '2025-01-14', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (588, '8888_1', '2025-01-14', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (587, '小陈_0', '2025-01-14', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (586, '31_1', '2025-01-14', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (585, '小陈_2', '2025-01-14', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (584, '德玛西亚', '2025-01-14', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (583, '小陈_1', '2025-01-14', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (582, '31_0', '2025-01-14', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (581, '小陈', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (580, '小张', '2025-01-04', 1, 12419);
INSERT INTO `sum_count_newplayer` VALUES (579, '31', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (578, '小张_0', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (577, '8888_2', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (576, '8888_1', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (575, '小陈_0', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (574, '31_1', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (573, '小陈_2', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (572, '德玛西亚', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (571, '小陈_1', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (570, '31_0', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (569, '小张', '2025-01-04', 1, 12419);
INSERT INTO `sum_count_newplayer` VALUES (568, '31', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (567, '小张_0', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (566, '8888_2', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (565, '8888_1', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (564, '小陈_0', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (563, '31_1', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (562, '小陈_2', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (561, '德玛西亚', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (560, '小陈_1', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (559, '31_0', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (558, '小张', '2025-01-04', 1, 12419);
INSERT INTO `sum_count_newplayer` VALUES (557, '31', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (556, '小张_0', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (555, '8888_2', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (554, '8888_1', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (553, '小陈_0', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (552, '31_1', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (551, '小陈_2', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (550, '德玛西亚', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (549, '小陈_1', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (548, '31_0', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (547, '小陈', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (546, '小张', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (545, '31', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (544, '小张_0', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (543, '8888_2', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (542, '8888_1', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (541, '小陈_0', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (540, '31_1', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (539, '小陈_2', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (538, '德玛西亚', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (537, '小陈_1', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (536, '31_0', '2025-01-04', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (535, '小陈', '2025-01-03', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (534, '小张', '2025-01-03', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (533, '31', '2025-01-03', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (532, '小张_0', '2025-01-03', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (531, '8888_2', '2025-01-03', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (530, '8888_1', '2025-01-03', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (529, '小陈_0', '2025-01-03', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (528, '31_1', '2025-01-03', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (527, '小陈_2', '2025-01-03', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (526, '德玛西亚', '2025-01-03', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (525, '小陈_1', '2025-01-03', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (524, '31_0', '2025-01-03', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (523, '小陈', '2025-01-03', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (522, '小张', '2025-01-03', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (521, '31', '2025-01-03', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (520, '小张_0', '2025-01-03', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (519, '8888_2', '2025-01-03', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (518, '8888_1', '2025-01-03', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (517, '小陈_0', '2025-01-03', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (516, '31_1', '2025-01-03', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (515, '小陈_2', '2025-01-03', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (514, '德玛西亚', '2025-01-03', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (513, '小陈_1', '2025-01-03', 0, 12419);
INSERT INTO `sum_count_newplayer` VALUES (512, '31_0', '2025-01-03', 0, 12419);

-- ----------------------------
-- Table structure for sum_proxy
-- ----------------------------
DROP TABLE IF EXISTS `sum_proxy`;
CREATE TABLE `sum_proxy`  (
  `sum_proxy_id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `sum_proxy_cash` decimal(10, 6) NULL DEFAULT NULL COMMENT '该日代理分销红包奖励',
  `sum_proxy_all_cash` decimal(10, 6) NULL DEFAULT NULL COMMENT '代理+子级代理的该日总红包奖励',
  `sum_proxy_commission` decimal(10, 6) NULL DEFAULT NULL COMMENT '该代理当日佣金',
  `sum_day` date NULL DEFAULT NULL COMMENT '该日',
  `proxy_name` varchar(38) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '哪个代理',
  `adn_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '哪个广告联盟',
  PRIMARY KEY (`sum_proxy_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 896 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sum_proxy
-- ----------------------------
INSERT INTO `sum_proxy` VALUES (426, 0.000000, NULL, NULL, '2025-01-03', '31_0', '百度');
INSERT INTO `sum_proxy` VALUES (427, 0.000000, NULL, NULL, '2025-01-03', '31_0', '优量汇');
INSERT INTO `sum_proxy` VALUES (428, 0.000000, NULL, NULL, '2025-01-03', '31_0', '穿山甲');
INSERT INTO `sum_proxy` VALUES (429, 0.000000, NULL, NULL, '2025-01-03', '31_0', '快手');
INSERT INTO `sum_proxy` VALUES (430, 0.000000, NULL, NULL, '2025-01-03', '31_0', 'all');
INSERT INTO `sum_proxy` VALUES (431, 0.000000, NULL, NULL, '2025-01-03', '小陈_1', '百度');
INSERT INTO `sum_proxy` VALUES (432, 0.000000, NULL, NULL, '2025-01-03', '小陈_1', '优量汇');
INSERT INTO `sum_proxy` VALUES (433, 0.000000, NULL, NULL, '2025-01-03', '小陈_1', '穿山甲');
INSERT INTO `sum_proxy` VALUES (434, 0.000000, NULL, NULL, '2025-01-03', '小陈_1', '快手');
INSERT INTO `sum_proxy` VALUES (435, 0.000000, NULL, NULL, '2025-01-03', '小陈_1', 'all');
INSERT INTO `sum_proxy` VALUES (436, 0.000000, NULL, NULL, '2025-01-03', '德玛西亚', '百度');
INSERT INTO `sum_proxy` VALUES (437, 0.000000, NULL, NULL, '2025-01-03', '德玛西亚', '优量汇');
INSERT INTO `sum_proxy` VALUES (438, 0.000000, NULL, NULL, '2025-01-03', '德玛西亚', '穿山甲');
INSERT INTO `sum_proxy` VALUES (439, 0.000000, NULL, NULL, '2025-01-03', '德玛西亚', '快手');
INSERT INTO `sum_proxy` VALUES (440, 0.000000, NULL, NULL, '2025-01-03', '德玛西亚', 'all');
INSERT INTO `sum_proxy` VALUES (441, 0.000000, NULL, NULL, '2025-01-03', '小陈_2', '百度');
INSERT INTO `sum_proxy` VALUES (442, 0.000000, NULL, NULL, '2025-01-03', '小陈_2', '优量汇');
INSERT INTO `sum_proxy` VALUES (443, 0.000000, NULL, NULL, '2025-01-03', '小陈_2', '穿山甲');
INSERT INTO `sum_proxy` VALUES (444, 0.000000, NULL, NULL, '2025-01-03', '小陈_2', '快手');
INSERT INTO `sum_proxy` VALUES (445, 0.000000, NULL, NULL, '2025-01-03', '小陈_2', 'all');
INSERT INTO `sum_proxy` VALUES (446, 0.000000, NULL, NULL, '2025-01-03', '31_1', '百度');
INSERT INTO `sum_proxy` VALUES (447, 0.000000, NULL, NULL, '2025-01-03', '31_1', '优量汇');
INSERT INTO `sum_proxy` VALUES (448, 0.000000, NULL, NULL, '2025-01-03', '31_1', '穿山甲');
INSERT INTO `sum_proxy` VALUES (449, 0.000000, NULL, NULL, '2025-01-03', '31_1', '快手');
INSERT INTO `sum_proxy` VALUES (450, 0.000000, NULL, NULL, '2025-01-03', '31_1', 'all');
INSERT INTO `sum_proxy` VALUES (451, 0.000000, NULL, NULL, '2025-01-03', '小陈_0', '百度');
INSERT INTO `sum_proxy` VALUES (452, 0.000000, NULL, NULL, '2025-01-03', '小陈_0', '优量汇');
INSERT INTO `sum_proxy` VALUES (453, 0.000000, NULL, NULL, '2025-01-03', '小陈_0', '穿山甲');
INSERT INTO `sum_proxy` VALUES (454, 0.000000, NULL, NULL, '2025-01-03', '小陈_0', '快手');
INSERT INTO `sum_proxy` VALUES (455, 0.000000, NULL, NULL, '2025-01-03', '小陈_0', 'all');
INSERT INTO `sum_proxy` VALUES (456, 0.000000, NULL, NULL, '2025-01-03', '8888_1', '百度');
INSERT INTO `sum_proxy` VALUES (457, 0.000000, NULL, NULL, '2025-01-03', '8888_1', '优量汇');
INSERT INTO `sum_proxy` VALUES (458, 0.000000, NULL, NULL, '2025-01-03', '8888_1', '穿山甲');
INSERT INTO `sum_proxy` VALUES (459, 0.000000, NULL, NULL, '2025-01-03', '8888_1', '快手');
INSERT INTO `sum_proxy` VALUES (460, 0.000000, NULL, NULL, '2025-01-03', '8888_1', 'all');
INSERT INTO `sum_proxy` VALUES (461, 0.000000, NULL, NULL, '2025-01-03', '8888_2', '百度');
INSERT INTO `sum_proxy` VALUES (462, 0.000000, NULL, NULL, '2025-01-03', '8888_2', '优量汇');
INSERT INTO `sum_proxy` VALUES (463, 0.000000, NULL, NULL, '2025-01-03', '8888_2', '穿山甲');
INSERT INTO `sum_proxy` VALUES (464, 0.000000, NULL, NULL, '2025-01-03', '8888_2', '快手');
INSERT INTO `sum_proxy` VALUES (465, 0.000000, NULL, NULL, '2025-01-03', '8888_2', 'all');
INSERT INTO `sum_proxy` VALUES (466, 0.000000, NULL, NULL, '2025-01-03', '小张_0', '百度');
INSERT INTO `sum_proxy` VALUES (467, 0.000000, NULL, NULL, '2025-01-03', '小张_0', '优量汇');
INSERT INTO `sum_proxy` VALUES (468, 0.000000, NULL, NULL, '2025-01-03', '小张_0', '穿山甲');
INSERT INTO `sum_proxy` VALUES (469, 0.000000, NULL, NULL, '2025-01-03', '小张_0', '快手');
INSERT INTO `sum_proxy` VALUES (470, 0.000000, NULL, NULL, '2025-01-03', '小张_0', 'all');
INSERT INTO `sum_proxy` VALUES (471, 0.000000, NULL, NULL, '2025-01-03', '31', '百度');
INSERT INTO `sum_proxy` VALUES (472, 0.000000, NULL, NULL, '2025-01-03', '31', '优量汇');
INSERT INTO `sum_proxy` VALUES (473, 0.000000, NULL, NULL, '2025-01-03', '31', '穿山甲');
INSERT INTO `sum_proxy` VALUES (474, 0.000000, NULL, NULL, '2025-01-03', '31', '快手');
INSERT INTO `sum_proxy` VALUES (475, 0.000000, NULL, NULL, '2025-01-03', '31', 'all');
INSERT INTO `sum_proxy` VALUES (476, 0.000000, NULL, NULL, '2025-01-03', '小张', '百度');
INSERT INTO `sum_proxy` VALUES (477, 0.000000, NULL, NULL, '2025-01-03', '小张', '优量汇');
INSERT INTO `sum_proxy` VALUES (478, 0.000000, NULL, NULL, '2025-01-03', '小张', '穿山甲');
INSERT INTO `sum_proxy` VALUES (479, 0.000000, NULL, NULL, '2025-01-03', '小张', '快手');
INSERT INTO `sum_proxy` VALUES (480, 0.000000, NULL, NULL, '2025-01-03', '小张', 'all');
INSERT INTO `sum_proxy` VALUES (481, 0.000000, NULL, NULL, '2025-01-03', '小陈', '百度');
INSERT INTO `sum_proxy` VALUES (482, 0.000000, NULL, NULL, '2025-01-03', '小陈', '优量汇');
INSERT INTO `sum_proxy` VALUES (483, 0.000000, NULL, NULL, '2025-01-03', '小陈', '穿山甲');
INSERT INTO `sum_proxy` VALUES (484, 0.000000, NULL, NULL, '2025-01-03', '小陈', '快手');
INSERT INTO `sum_proxy` VALUES (485, 0.000000, NULL, NULL, '2025-01-03', '小陈', 'all');
INSERT INTO `sum_proxy` VALUES (486, 0.000000, NULL, NULL, '2025-01-03', '31_0', '百度');
INSERT INTO `sum_proxy` VALUES (487, 0.000000, NULL, NULL, '2025-01-03', '31_0', '优量汇');
INSERT INTO `sum_proxy` VALUES (488, 0.000000, NULL, NULL, '2025-01-03', '31_0', '穿山甲');
INSERT INTO `sum_proxy` VALUES (489, 0.000000, NULL, NULL, '2025-01-03', '31_0', '快手');
INSERT INTO `sum_proxy` VALUES (490, 0.000000, NULL, NULL, '2025-01-03', '31_0', 'all');
INSERT INTO `sum_proxy` VALUES (491, 0.000000, NULL, NULL, '2025-01-03', '小陈_1', '百度');
INSERT INTO `sum_proxy` VALUES (492, 0.000000, NULL, NULL, '2025-01-03', '小陈_1', '优量汇');
INSERT INTO `sum_proxy` VALUES (493, 0.000000, NULL, NULL, '2025-01-03', '小陈_1', '穿山甲');
INSERT INTO `sum_proxy` VALUES (494, 0.000000, NULL, NULL, '2025-01-03', '小陈_1', '快手');
INSERT INTO `sum_proxy` VALUES (495, 0.000000, NULL, NULL, '2025-01-03', '小陈_1', 'all');
INSERT INTO `sum_proxy` VALUES (496, 0.000000, NULL, NULL, '2025-01-03', '德玛西亚', '百度');
INSERT INTO `sum_proxy` VALUES (497, 0.000000, NULL, NULL, '2025-01-03', '德玛西亚', '优量汇');
INSERT INTO `sum_proxy` VALUES (498, 0.000000, NULL, NULL, '2025-01-03', '德玛西亚', '穿山甲');
INSERT INTO `sum_proxy` VALUES (499, 0.000000, NULL, NULL, '2025-01-03', '德玛西亚', '快手');
INSERT INTO `sum_proxy` VALUES (500, 0.000000, NULL, NULL, '2025-01-03', '德玛西亚', 'all');
INSERT INTO `sum_proxy` VALUES (501, 0.000000, NULL, NULL, '2025-01-03', '小陈_2', '百度');
INSERT INTO `sum_proxy` VALUES (502, 0.000000, NULL, NULL, '2025-01-03', '小陈_2', '优量汇');
INSERT INTO `sum_proxy` VALUES (503, 0.000000, NULL, NULL, '2025-01-03', '小陈_2', '穿山甲');
INSERT INTO `sum_proxy` VALUES (504, 0.000000, NULL, NULL, '2025-01-03', '小陈_2', '快手');
INSERT INTO `sum_proxy` VALUES (505, 0.000000, NULL, NULL, '2025-01-03', '小陈_2', 'all');
INSERT INTO `sum_proxy` VALUES (506, 0.000000, NULL, NULL, '2025-01-03', '31_1', '百度');
INSERT INTO `sum_proxy` VALUES (507, 0.000000, NULL, NULL, '2025-01-03', '31_1', '优量汇');
INSERT INTO `sum_proxy` VALUES (508, 0.000000, NULL, NULL, '2025-01-03', '31_1', '穿山甲');
INSERT INTO `sum_proxy` VALUES (509, 0.000000, NULL, NULL, '2025-01-03', '31_1', '快手');
INSERT INTO `sum_proxy` VALUES (510, 0.000000, NULL, NULL, '2025-01-03', '31_1', 'all');
INSERT INTO `sum_proxy` VALUES (511, 0.000000, NULL, NULL, '2025-01-03', '小陈_0', '百度');
INSERT INTO `sum_proxy` VALUES (512, 0.000000, NULL, NULL, '2025-01-03', '小陈_0', '优量汇');
INSERT INTO `sum_proxy` VALUES (513, 0.000000, NULL, NULL, '2025-01-03', '小陈_0', '穿山甲');
INSERT INTO `sum_proxy` VALUES (514, 0.000000, NULL, NULL, '2025-01-03', '小陈_0', '快手');
INSERT INTO `sum_proxy` VALUES (515, 0.000000, NULL, NULL, '2025-01-03', '小陈_0', 'all');
INSERT INTO `sum_proxy` VALUES (516, 0.000000, NULL, NULL, '2025-01-03', '8888_1', '百度');
INSERT INTO `sum_proxy` VALUES (517, 0.000000, NULL, NULL, '2025-01-03', '8888_1', '优量汇');
INSERT INTO `sum_proxy` VALUES (518, 0.000000, NULL, NULL, '2025-01-03', '8888_1', '穿山甲');
INSERT INTO `sum_proxy` VALUES (519, 0.000000, NULL, NULL, '2025-01-03', '8888_1', '快手');
INSERT INTO `sum_proxy` VALUES (520, 0.000000, NULL, NULL, '2025-01-03', '8888_1', 'all');
INSERT INTO `sum_proxy` VALUES (521, 0.000000, NULL, NULL, '2025-01-03', '8888_2', '百度');
INSERT INTO `sum_proxy` VALUES (522, 0.000000, NULL, NULL, '2025-01-03', '8888_2', '优量汇');
INSERT INTO `sum_proxy` VALUES (523, 0.000000, NULL, NULL, '2025-01-03', '8888_2', '穿山甲');
INSERT INTO `sum_proxy` VALUES (524, 0.000000, NULL, NULL, '2025-01-03', '8888_2', '快手');
INSERT INTO `sum_proxy` VALUES (525, 0.000000, NULL, NULL, '2025-01-03', '8888_2', 'all');
INSERT INTO `sum_proxy` VALUES (526, 0.000000, NULL, NULL, '2025-01-03', '小张_0', '百度');
INSERT INTO `sum_proxy` VALUES (527, 0.000000, NULL, NULL, '2025-01-03', '小张_0', '优量汇');
INSERT INTO `sum_proxy` VALUES (528, 0.000000, NULL, NULL, '2025-01-03', '小张_0', '穿山甲');
INSERT INTO `sum_proxy` VALUES (529, 0.000000, NULL, NULL, '2025-01-03', '小张_0', '快手');
INSERT INTO `sum_proxy` VALUES (530, 0.000000, NULL, NULL, '2025-01-03', '小张_0', 'all');
INSERT INTO `sum_proxy` VALUES (531, 0.000000, NULL, NULL, '2025-01-03', '31', '百度');
INSERT INTO `sum_proxy` VALUES (532, 0.000000, NULL, NULL, '2025-01-03', '31', '优量汇');
INSERT INTO `sum_proxy` VALUES (533, 0.000000, NULL, NULL, '2025-01-03', '31', '穿山甲');
INSERT INTO `sum_proxy` VALUES (534, 0.000000, NULL, NULL, '2025-01-03', '31', '快手');
INSERT INTO `sum_proxy` VALUES (535, 0.000000, NULL, NULL, '2025-01-03', '31', 'all');
INSERT INTO `sum_proxy` VALUES (536, 0.000000, NULL, NULL, '2025-01-03', '小张', '百度');
INSERT INTO `sum_proxy` VALUES (537, 0.000000, NULL, NULL, '2025-01-03', '小张', '优量汇');
INSERT INTO `sum_proxy` VALUES (538, 0.000000, NULL, NULL, '2025-01-03', '小张', '穿山甲');
INSERT INTO `sum_proxy` VALUES (539, 0.000000, NULL, NULL, '2025-01-03', '小张', '快手');
INSERT INTO `sum_proxy` VALUES (540, 0.000000, NULL, NULL, '2025-01-03', '小张', 'all');
INSERT INTO `sum_proxy` VALUES (541, 0.000000, NULL, NULL, '2025-01-03', '小陈', '百度');
INSERT INTO `sum_proxy` VALUES (542, 0.000000, NULL, NULL, '2025-01-03', '小陈', '优量汇');
INSERT INTO `sum_proxy` VALUES (543, 0.000000, NULL, NULL, '2025-01-03', '小陈', '穿山甲');
INSERT INTO `sum_proxy` VALUES (544, 0.000000, NULL, NULL, '2025-01-03', '小陈', '快手');
INSERT INTO `sum_proxy` VALUES (545, 0.000000, NULL, NULL, '2025-01-03', '小陈', 'all');
INSERT INTO `sum_proxy` VALUES (546, 0.000000, NULL, NULL, '2025-01-04', '31_0', '百度');
INSERT INTO `sum_proxy` VALUES (547, 0.000000, NULL, NULL, '2025-01-04', '31_0', '优量汇');
INSERT INTO `sum_proxy` VALUES (548, 0.000000, NULL, NULL, '2025-01-04', '31_0', '穿山甲');
INSERT INTO `sum_proxy` VALUES (549, 0.000000, NULL, NULL, '2025-01-04', '31_0', '快手');
INSERT INTO `sum_proxy` VALUES (550, 0.000000, NULL, NULL, '2025-01-04', '31_0', 'all');
INSERT INTO `sum_proxy` VALUES (551, 0.000000, NULL, NULL, '2025-01-04', '小陈_1', '百度');
INSERT INTO `sum_proxy` VALUES (552, 0.000000, NULL, NULL, '2025-01-04', '小陈_1', '优量汇');
INSERT INTO `sum_proxy` VALUES (553, 0.000000, NULL, NULL, '2025-01-04', '小陈_1', '穿山甲');
INSERT INTO `sum_proxy` VALUES (554, 0.000000, NULL, NULL, '2025-01-04', '小陈_1', '快手');
INSERT INTO `sum_proxy` VALUES (555, 0.000000, NULL, NULL, '2025-01-04', '小陈_1', 'all');
INSERT INTO `sum_proxy` VALUES (556, 0.000000, NULL, NULL, '2025-01-04', '德玛西亚', '百度');
INSERT INTO `sum_proxy` VALUES (557, 0.000000, NULL, NULL, '2025-01-04', '德玛西亚', '优量汇');
INSERT INTO `sum_proxy` VALUES (558, 0.000000, NULL, NULL, '2025-01-04', '德玛西亚', '穿山甲');
INSERT INTO `sum_proxy` VALUES (559, 0.000000, NULL, NULL, '2025-01-04', '德玛西亚', '快手');
INSERT INTO `sum_proxy` VALUES (560, 0.000000, NULL, NULL, '2025-01-04', '德玛西亚', 'all');
INSERT INTO `sum_proxy` VALUES (561, 0.000000, NULL, NULL, '2025-01-04', '小陈_2', '百度');
INSERT INTO `sum_proxy` VALUES (562, 0.000000, NULL, NULL, '2025-01-04', '小陈_2', '优量汇');
INSERT INTO `sum_proxy` VALUES (563, 0.000000, NULL, NULL, '2025-01-04', '小陈_2', '穿山甲');
INSERT INTO `sum_proxy` VALUES (564, 0.000000, NULL, NULL, '2025-01-04', '小陈_2', '快手');
INSERT INTO `sum_proxy` VALUES (565, 0.000000, NULL, NULL, '2025-01-04', '小陈_2', 'all');
INSERT INTO `sum_proxy` VALUES (566, 0.000000, NULL, NULL, '2025-01-04', '31_1', '百度');
INSERT INTO `sum_proxy` VALUES (567, 0.000000, NULL, NULL, '2025-01-04', '31_1', '优量汇');
INSERT INTO `sum_proxy` VALUES (568, 0.000000, NULL, NULL, '2025-01-04', '31_1', '穿山甲');
INSERT INTO `sum_proxy` VALUES (569, 0.000000, NULL, NULL, '2025-01-04', '31_1', '快手');
INSERT INTO `sum_proxy` VALUES (570, 0.000000, NULL, NULL, '2025-01-04', '31_1', 'all');
INSERT INTO `sum_proxy` VALUES (571, 0.000000, NULL, NULL, '2025-01-04', '小陈_0', '百度');
INSERT INTO `sum_proxy` VALUES (572, 0.000000, NULL, NULL, '2025-01-04', '小陈_0', '优量汇');
INSERT INTO `sum_proxy` VALUES (573, 0.000000, NULL, NULL, '2025-01-04', '小陈_0', '穿山甲');
INSERT INTO `sum_proxy` VALUES (574, 0.000000, NULL, NULL, '2025-01-04', '小陈_0', '快手');
INSERT INTO `sum_proxy` VALUES (575, 0.000000, NULL, NULL, '2025-01-04', '小陈_0', 'all');
INSERT INTO `sum_proxy` VALUES (576, 0.000000, NULL, NULL, '2025-01-04', '8888_1', '百度');
INSERT INTO `sum_proxy` VALUES (577, 0.000000, NULL, NULL, '2025-01-04', '8888_1', '优量汇');
INSERT INTO `sum_proxy` VALUES (578, 0.000000, NULL, NULL, '2025-01-04', '8888_1', '穿山甲');
INSERT INTO `sum_proxy` VALUES (579, 0.000000, NULL, NULL, '2025-01-04', '8888_1', '快手');
INSERT INTO `sum_proxy` VALUES (580, 0.000000, NULL, NULL, '2025-01-04', '8888_1', 'all');
INSERT INTO `sum_proxy` VALUES (581, 0.000000, NULL, NULL, '2025-01-04', '8888_2', '百度');
INSERT INTO `sum_proxy` VALUES (582, 0.000000, NULL, NULL, '2025-01-04', '8888_2', '优量汇');
INSERT INTO `sum_proxy` VALUES (583, 0.000000, NULL, NULL, '2025-01-04', '8888_2', '穿山甲');
INSERT INTO `sum_proxy` VALUES (584, 0.000000, NULL, NULL, '2025-01-04', '8888_2', '快手');
INSERT INTO `sum_proxy` VALUES (585, 0.000000, NULL, NULL, '2025-01-04', '8888_2', 'all');
INSERT INTO `sum_proxy` VALUES (586, 0.000000, NULL, NULL, '2025-01-04', '小张_0', '百度');
INSERT INTO `sum_proxy` VALUES (587, 0.000000, NULL, NULL, '2025-01-04', '小张_0', '优量汇');
INSERT INTO `sum_proxy` VALUES (588, 0.000000, NULL, NULL, '2025-01-04', '小张_0', '穿山甲');
INSERT INTO `sum_proxy` VALUES (589, 0.000000, NULL, NULL, '2025-01-04', '小张_0', '快手');
INSERT INTO `sum_proxy` VALUES (590, 0.000000, NULL, NULL, '2025-01-04', '小张_0', 'all');
INSERT INTO `sum_proxy` VALUES (591, 0.000000, NULL, NULL, '2025-01-04', '31', '百度');
INSERT INTO `sum_proxy` VALUES (592, 0.000000, NULL, NULL, '2025-01-04', '31', '优量汇');
INSERT INTO `sum_proxy` VALUES (593, 0.000000, NULL, NULL, '2025-01-04', '31', '穿山甲');
INSERT INTO `sum_proxy` VALUES (594, 0.000000, NULL, NULL, '2025-01-04', '31', '快手');
INSERT INTO `sum_proxy` VALUES (595, 0.000000, NULL, NULL, '2025-01-04', '31', 'all');
INSERT INTO `sum_proxy` VALUES (596, 0.000000, NULL, NULL, '2025-01-04', '小张', '百度');
INSERT INTO `sum_proxy` VALUES (597, 0.000000, NULL, NULL, '2025-01-04', '小张', '优量汇');
INSERT INTO `sum_proxy` VALUES (598, 0.000000, NULL, NULL, '2025-01-04', '小张', '穿山甲');
INSERT INTO `sum_proxy` VALUES (599, 0.000000, NULL, NULL, '2025-01-04', '小张', '快手');
INSERT INTO `sum_proxy` VALUES (600, 0.000000, NULL, NULL, '2025-01-04', '小张', 'all');
INSERT INTO `sum_proxy` VALUES (601, 0.000000, NULL, NULL, '2025-01-04', '小陈', '百度');
INSERT INTO `sum_proxy` VALUES (602, 0.000000, NULL, NULL, '2025-01-04', '小陈', '优量汇');
INSERT INTO `sum_proxy` VALUES (603, 0.000000, NULL, NULL, '2025-01-04', '小陈', '穿山甲');
INSERT INTO `sum_proxy` VALUES (604, 0.000000, NULL, NULL, '2025-01-04', '小陈', '快手');
INSERT INTO `sum_proxy` VALUES (605, 0.000000, NULL, NULL, '2025-01-04', '小陈', 'all');
INSERT INTO `sum_proxy` VALUES (606, 0.000000, NULL, NULL, '2025-01-04', '31_0', '百度');
INSERT INTO `sum_proxy` VALUES (607, 0.000000, NULL, NULL, '2025-01-04', '31_0', '优量汇');
INSERT INTO `sum_proxy` VALUES (608, 0.000000, NULL, NULL, '2025-01-04', '31_0', '穿山甲');
INSERT INTO `sum_proxy` VALUES (609, 0.000000, NULL, NULL, '2025-01-04', '31_0', '快手');
INSERT INTO `sum_proxy` VALUES (610, 0.000000, NULL, NULL, '2025-01-04', '31_0', 'all');
INSERT INTO `sum_proxy` VALUES (611, 0.000000, NULL, NULL, '2025-01-04', '小陈_1', '百度');
INSERT INTO `sum_proxy` VALUES (612, 0.000000, NULL, NULL, '2025-01-04', '小陈_1', '优量汇');
INSERT INTO `sum_proxy` VALUES (613, 0.000000, NULL, NULL, '2025-01-04', '小陈_1', '穿山甲');
INSERT INTO `sum_proxy` VALUES (614, 0.000000, NULL, NULL, '2025-01-04', '小陈_1', '快手');
INSERT INTO `sum_proxy` VALUES (615, 0.000000, NULL, NULL, '2025-01-04', '小陈_1', 'all');
INSERT INTO `sum_proxy` VALUES (616, 0.000000, NULL, NULL, '2025-01-04', '德玛西亚', '百度');
INSERT INTO `sum_proxy` VALUES (617, 0.000000, NULL, NULL, '2025-01-04', '德玛西亚', '优量汇');
INSERT INTO `sum_proxy` VALUES (618, 0.000000, NULL, NULL, '2025-01-04', '德玛西亚', '穿山甲');
INSERT INTO `sum_proxy` VALUES (619, 0.000000, NULL, NULL, '2025-01-04', '德玛西亚', '快手');
INSERT INTO `sum_proxy` VALUES (620, 0.000000, NULL, NULL, '2025-01-04', '德玛西亚', 'all');
INSERT INTO `sum_proxy` VALUES (621, 0.000000, NULL, NULL, '2025-01-04', '小陈_2', '百度');
INSERT INTO `sum_proxy` VALUES (622, 0.000000, NULL, NULL, '2025-01-04', '小陈_2', '优量汇');
INSERT INTO `sum_proxy` VALUES (623, 0.000000, NULL, NULL, '2025-01-04', '小陈_2', '穿山甲');
INSERT INTO `sum_proxy` VALUES (624, 0.000000, NULL, NULL, '2025-01-04', '小陈_2', '快手');
INSERT INTO `sum_proxy` VALUES (625, 0.000000, NULL, NULL, '2025-01-04', '小陈_2', 'all');
INSERT INTO `sum_proxy` VALUES (626, 0.000000, NULL, NULL, '2025-01-04', '31_1', '百度');
INSERT INTO `sum_proxy` VALUES (627, 0.000000, NULL, NULL, '2025-01-04', '31_1', '优量汇');
INSERT INTO `sum_proxy` VALUES (628, 0.000000, NULL, NULL, '2025-01-04', '31_1', '穿山甲');
INSERT INTO `sum_proxy` VALUES (629, 0.000000, NULL, NULL, '2025-01-04', '31_1', '快手');
INSERT INTO `sum_proxy` VALUES (630, 0.000000, NULL, NULL, '2025-01-04', '31_1', 'all');
INSERT INTO `sum_proxy` VALUES (631, 0.000000, NULL, NULL, '2025-01-04', '小陈_0', '百度');
INSERT INTO `sum_proxy` VALUES (632, 0.000000, NULL, NULL, '2025-01-04', '小陈_0', '优量汇');
INSERT INTO `sum_proxy` VALUES (633, 0.000000, NULL, NULL, '2025-01-04', '小陈_0', '穿山甲');
INSERT INTO `sum_proxy` VALUES (634, 0.000000, NULL, NULL, '2025-01-04', '小陈_0', '快手');
INSERT INTO `sum_proxy` VALUES (635, 0.000000, NULL, NULL, '2025-01-04', '小陈_0', 'all');
INSERT INTO `sum_proxy` VALUES (636, 0.000000, NULL, NULL, '2025-01-04', '8888_1', '百度');
INSERT INTO `sum_proxy` VALUES (637, 0.000000, NULL, NULL, '2025-01-04', '8888_1', '优量汇');
INSERT INTO `sum_proxy` VALUES (638, 0.000000, NULL, NULL, '2025-01-04', '8888_1', '穿山甲');
INSERT INTO `sum_proxy` VALUES (639, 0.000000, NULL, NULL, '2025-01-04', '8888_1', '快手');
INSERT INTO `sum_proxy` VALUES (640, 0.000000, NULL, NULL, '2025-01-04', '8888_1', 'all');
INSERT INTO `sum_proxy` VALUES (641, 0.000000, NULL, NULL, '2025-01-04', '8888_2', '百度');
INSERT INTO `sum_proxy` VALUES (642, 0.000000, NULL, NULL, '2025-01-04', '8888_2', '优量汇');
INSERT INTO `sum_proxy` VALUES (643, 0.000000, NULL, NULL, '2025-01-04', '8888_2', '穿山甲');
INSERT INTO `sum_proxy` VALUES (644, 0.000000, NULL, NULL, '2025-01-04', '8888_2', '快手');
INSERT INTO `sum_proxy` VALUES (645, 0.000000, NULL, NULL, '2025-01-04', '8888_2', 'all');
INSERT INTO `sum_proxy` VALUES (646, 0.000000, NULL, NULL, '2025-01-04', '小张_0', '百度');
INSERT INTO `sum_proxy` VALUES (647, 0.000000, NULL, NULL, '2025-01-04', '小张_0', '优量汇');
INSERT INTO `sum_proxy` VALUES (648, 0.000000, NULL, NULL, '2025-01-04', '小张_0', '穿山甲');
INSERT INTO `sum_proxy` VALUES (649, 0.000000, NULL, NULL, '2025-01-04', '小张_0', '快手');
INSERT INTO `sum_proxy` VALUES (650, 0.000000, NULL, NULL, '2025-01-04', '小张_0', 'all');
INSERT INTO `sum_proxy` VALUES (651, 0.000000, NULL, NULL, '2025-01-04', '31', '百度');
INSERT INTO `sum_proxy` VALUES (652, 0.000000, NULL, NULL, '2025-01-04', '31', '优量汇');
INSERT INTO `sum_proxy` VALUES (653, 0.000000, NULL, NULL, '2025-01-04', '31', '穿山甲');
INSERT INTO `sum_proxy` VALUES (654, 0.000000, NULL, NULL, '2025-01-04', '31', '快手');
INSERT INTO `sum_proxy` VALUES (655, 0.000000, NULL, NULL, '2025-01-04', '31', 'all');
INSERT INTO `sum_proxy` VALUES (656, 0.000000, NULL, NULL, '2025-01-04', '小张', '百度');
INSERT INTO `sum_proxy` VALUES (657, 0.000000, NULL, NULL, '2025-01-04', '小张', '优量汇');
INSERT INTO `sum_proxy` VALUES (658, 0.000000, NULL, NULL, '2025-01-04', '小张', '穿山甲');
INSERT INTO `sum_proxy` VALUES (659, 0.000000, NULL, NULL, '2025-01-04', '小张', '快手');
INSERT INTO `sum_proxy` VALUES (660, 0.000000, NULL, NULL, '2025-01-04', '小张', 'all');
INSERT INTO `sum_proxy` VALUES (661, 0.000000, NULL, NULL, '2025-01-04', '31_0', '百度');
INSERT INTO `sum_proxy` VALUES (662, 0.000000, NULL, NULL, '2025-01-04', '31_0', '优量汇');
INSERT INTO `sum_proxy` VALUES (663, 0.000000, NULL, NULL, '2025-01-04', '31_0', '穿山甲');
INSERT INTO `sum_proxy` VALUES (664, 0.000000, NULL, NULL, '2025-01-04', '31_0', '快手');
INSERT INTO `sum_proxy` VALUES (665, 0.000000, NULL, NULL, '2025-01-04', '31_0', 'all');
INSERT INTO `sum_proxy` VALUES (666, 0.000000, NULL, NULL, '2025-01-04', '小陈_1', '百度');
INSERT INTO `sum_proxy` VALUES (667, 0.000000, NULL, NULL, '2025-01-04', '小陈_1', '优量汇');
INSERT INTO `sum_proxy` VALUES (668, 0.000000, NULL, NULL, '2025-01-04', '小陈_1', '穿山甲');
INSERT INTO `sum_proxy` VALUES (669, 0.000000, NULL, NULL, '2025-01-04', '小陈_1', '快手');
INSERT INTO `sum_proxy` VALUES (670, 0.000000, NULL, NULL, '2025-01-04', '小陈_1', 'all');
INSERT INTO `sum_proxy` VALUES (671, 0.000000, NULL, NULL, '2025-01-04', '德玛西亚', '百度');
INSERT INTO `sum_proxy` VALUES (672, 0.000000, NULL, NULL, '2025-01-04', '德玛西亚', '优量汇');
INSERT INTO `sum_proxy` VALUES (673, 0.000000, NULL, NULL, '2025-01-04', '德玛西亚', '穿山甲');
INSERT INTO `sum_proxy` VALUES (674, 0.000000, NULL, NULL, '2025-01-04', '德玛西亚', '快手');
INSERT INTO `sum_proxy` VALUES (675, 0.000000, NULL, NULL, '2025-01-04', '德玛西亚', 'all');
INSERT INTO `sum_proxy` VALUES (676, 0.000000, NULL, NULL, '2025-01-04', '小陈_2', '百度');
INSERT INTO `sum_proxy` VALUES (677, 0.000000, NULL, NULL, '2025-01-04', '小陈_2', '优量汇');
INSERT INTO `sum_proxy` VALUES (678, 0.000000, NULL, NULL, '2025-01-04', '小陈_2', '穿山甲');
INSERT INTO `sum_proxy` VALUES (679, 0.000000, NULL, NULL, '2025-01-04', '小陈_2', '快手');
INSERT INTO `sum_proxy` VALUES (680, 0.000000, NULL, NULL, '2025-01-04', '小陈_2', 'all');
INSERT INTO `sum_proxy` VALUES (681, 0.000000, NULL, NULL, '2025-01-04', '31_1', '百度');
INSERT INTO `sum_proxy` VALUES (682, 0.000000, NULL, NULL, '2025-01-04', '31_1', '优量汇');
INSERT INTO `sum_proxy` VALUES (683, 0.000000, NULL, NULL, '2025-01-04', '31_1', '穿山甲');
INSERT INTO `sum_proxy` VALUES (684, 0.000000, NULL, NULL, '2025-01-04', '31_1', '快手');
INSERT INTO `sum_proxy` VALUES (685, 0.000000, NULL, NULL, '2025-01-04', '31_1', 'all');
INSERT INTO `sum_proxy` VALUES (686, 0.000000, NULL, NULL, '2025-01-04', '小陈_0', '百度');
INSERT INTO `sum_proxy` VALUES (687, 0.000000, NULL, NULL, '2025-01-04', '小陈_0', '优量汇');
INSERT INTO `sum_proxy` VALUES (688, 0.000000, NULL, NULL, '2025-01-04', '小陈_0', '穿山甲');
INSERT INTO `sum_proxy` VALUES (689, 0.000000, NULL, NULL, '2025-01-04', '小陈_0', '快手');
INSERT INTO `sum_proxy` VALUES (690, 0.000000, NULL, NULL, '2025-01-04', '小陈_0', 'all');
INSERT INTO `sum_proxy` VALUES (691, 0.000000, NULL, NULL, '2025-01-04', '8888_1', '百度');
INSERT INTO `sum_proxy` VALUES (692, 0.000000, NULL, NULL, '2025-01-04', '8888_1', '优量汇');
INSERT INTO `sum_proxy` VALUES (693, 0.000000, NULL, NULL, '2025-01-04', '8888_1', '穿山甲');
INSERT INTO `sum_proxy` VALUES (694, 0.000000, NULL, NULL, '2025-01-04', '8888_1', '快手');
INSERT INTO `sum_proxy` VALUES (695, 0.000000, NULL, NULL, '2025-01-04', '8888_1', 'all');
INSERT INTO `sum_proxy` VALUES (696, 0.000000, NULL, NULL, '2025-01-04', '8888_2', '百度');
INSERT INTO `sum_proxy` VALUES (697, 0.000000, NULL, NULL, '2025-01-04', '8888_2', '优量汇');
INSERT INTO `sum_proxy` VALUES (698, 0.000000, NULL, NULL, '2025-01-04', '8888_2', '穿山甲');
INSERT INTO `sum_proxy` VALUES (699, 0.000000, NULL, NULL, '2025-01-04', '8888_2', '快手');
INSERT INTO `sum_proxy` VALUES (700, 0.000000, NULL, NULL, '2025-01-04', '8888_2', 'all');
INSERT INTO `sum_proxy` VALUES (701, 0.000000, NULL, NULL, '2025-01-04', '小张_0', '百度');
INSERT INTO `sum_proxy` VALUES (702, 0.000000, NULL, NULL, '2025-01-04', '小张_0', '优量汇');
INSERT INTO `sum_proxy` VALUES (703, 0.000000, NULL, NULL, '2025-01-04', '小张_0', '穿山甲');
INSERT INTO `sum_proxy` VALUES (704, 0.000000, NULL, NULL, '2025-01-04', '小张_0', '快手');
INSERT INTO `sum_proxy` VALUES (705, 0.000000, NULL, NULL, '2025-01-04', '小张_0', 'all');
INSERT INTO `sum_proxy` VALUES (706, 0.000000, NULL, NULL, '2025-01-04', '31', '百度');
INSERT INTO `sum_proxy` VALUES (707, 0.000000, NULL, NULL, '2025-01-04', '31', '优量汇');
INSERT INTO `sum_proxy` VALUES (708, 0.000000, NULL, NULL, '2025-01-04', '31', '穿山甲');
INSERT INTO `sum_proxy` VALUES (709, 0.000000, NULL, NULL, '2025-01-04', '31', '快手');
INSERT INTO `sum_proxy` VALUES (710, 0.000000, NULL, NULL, '2025-01-04', '31', 'all');
INSERT INTO `sum_proxy` VALUES (711, 0.000000, NULL, NULL, '2025-01-04', '小张', '百度');
INSERT INTO `sum_proxy` VALUES (712, 0.000000, NULL, NULL, '2025-01-04', '小张', '优量汇');
INSERT INTO `sum_proxy` VALUES (713, 0.000000, NULL, NULL, '2025-01-04', '小张', '穿山甲');
INSERT INTO `sum_proxy` VALUES (714, 0.000000, NULL, NULL, '2025-01-04', '小张', '快手');
INSERT INTO `sum_proxy` VALUES (715, 0.000000, NULL, NULL, '2025-01-04', '小张', 'all');
INSERT INTO `sum_proxy` VALUES (716, 0.000000, NULL, NULL, '2025-01-04', '31_0', '百度');
INSERT INTO `sum_proxy` VALUES (717, 0.000000, NULL, NULL, '2025-01-04', '31_0', '优量汇');
INSERT INTO `sum_proxy` VALUES (718, 0.000000, NULL, NULL, '2025-01-04', '31_0', '穿山甲');
INSERT INTO `sum_proxy` VALUES (719, 0.000000, NULL, NULL, '2025-01-04', '31_0', '快手');
INSERT INTO `sum_proxy` VALUES (720, 0.000000, NULL, NULL, '2025-01-04', '31_0', 'all');
INSERT INTO `sum_proxy` VALUES (721, 0.000000, NULL, NULL, '2025-01-04', '小陈_1', '百度');
INSERT INTO `sum_proxy` VALUES (722, 0.000000, NULL, NULL, '2025-01-04', '小陈_1', '优量汇');
INSERT INTO `sum_proxy` VALUES (723, 0.000000, NULL, NULL, '2025-01-04', '小陈_1', '穿山甲');
INSERT INTO `sum_proxy` VALUES (724, 0.000000, NULL, NULL, '2025-01-04', '小陈_1', '快手');
INSERT INTO `sum_proxy` VALUES (725, 0.000000, NULL, NULL, '2025-01-04', '小陈_1', 'all');
INSERT INTO `sum_proxy` VALUES (726, 0.000000, NULL, NULL, '2025-01-04', '德玛西亚', '百度');
INSERT INTO `sum_proxy` VALUES (727, 0.000000, NULL, NULL, '2025-01-04', '德玛西亚', '优量汇');
INSERT INTO `sum_proxy` VALUES (728, 0.000000, NULL, NULL, '2025-01-04', '德玛西亚', '穿山甲');
INSERT INTO `sum_proxy` VALUES (729, 0.000000, NULL, NULL, '2025-01-04', '德玛西亚', '快手');
INSERT INTO `sum_proxy` VALUES (730, 0.000000, NULL, NULL, '2025-01-04', '德玛西亚', 'all');
INSERT INTO `sum_proxy` VALUES (731, 0.000000, NULL, NULL, '2025-01-04', '小陈_2', '百度');
INSERT INTO `sum_proxy` VALUES (732, 0.000000, NULL, NULL, '2025-01-04', '小陈_2', '优量汇');
INSERT INTO `sum_proxy` VALUES (733, 0.000000, NULL, NULL, '2025-01-04', '小陈_2', '穿山甲');
INSERT INTO `sum_proxy` VALUES (734, 0.000000, NULL, NULL, '2025-01-04', '小陈_2', '快手');
INSERT INTO `sum_proxy` VALUES (735, 0.000000, NULL, NULL, '2025-01-04', '小陈_2', 'all');
INSERT INTO `sum_proxy` VALUES (736, 0.000000, NULL, NULL, '2025-01-04', '31_1', '百度');
INSERT INTO `sum_proxy` VALUES (737, 0.000000, NULL, NULL, '2025-01-04', '31_1', '优量汇');
INSERT INTO `sum_proxy` VALUES (738, 0.000000, NULL, NULL, '2025-01-04', '31_1', '穿山甲');
INSERT INTO `sum_proxy` VALUES (739, 0.000000, NULL, NULL, '2025-01-04', '31_1', '快手');
INSERT INTO `sum_proxy` VALUES (740, 0.000000, NULL, NULL, '2025-01-04', '31_1', 'all');
INSERT INTO `sum_proxy` VALUES (741, 0.000000, NULL, NULL, '2025-01-04', '小陈_0', '百度');
INSERT INTO `sum_proxy` VALUES (742, 0.000000, NULL, NULL, '2025-01-04', '小陈_0', '优量汇');
INSERT INTO `sum_proxy` VALUES (743, 0.000000, NULL, NULL, '2025-01-04', '小陈_0', '穿山甲');
INSERT INTO `sum_proxy` VALUES (744, 0.000000, NULL, NULL, '2025-01-04', '小陈_0', '快手');
INSERT INTO `sum_proxy` VALUES (745, 0.000000, NULL, NULL, '2025-01-04', '小陈_0', 'all');
INSERT INTO `sum_proxy` VALUES (746, 0.000000, NULL, NULL, '2025-01-04', '8888_1', '百度');
INSERT INTO `sum_proxy` VALUES (747, 0.000000, NULL, NULL, '2025-01-04', '8888_1', '优量汇');
INSERT INTO `sum_proxy` VALUES (748, 0.000000, NULL, NULL, '2025-01-04', '8888_1', '穿山甲');
INSERT INTO `sum_proxy` VALUES (749, 0.000000, NULL, NULL, '2025-01-04', '8888_1', '快手');
INSERT INTO `sum_proxy` VALUES (750, 0.000000, NULL, NULL, '2025-01-04', '8888_1', 'all');
INSERT INTO `sum_proxy` VALUES (751, 0.000000, NULL, NULL, '2025-01-04', '8888_2', '百度');
INSERT INTO `sum_proxy` VALUES (752, 0.000000, NULL, NULL, '2025-01-04', '8888_2', '优量汇');
INSERT INTO `sum_proxy` VALUES (753, 0.000000, NULL, NULL, '2025-01-04', '8888_2', '穿山甲');
INSERT INTO `sum_proxy` VALUES (754, 0.000000, NULL, NULL, '2025-01-04', '8888_2', '快手');
INSERT INTO `sum_proxy` VALUES (755, 0.000000, NULL, NULL, '2025-01-04', '8888_2', 'all');
INSERT INTO `sum_proxy` VALUES (756, 0.000000, NULL, NULL, '2025-01-04', '小张_0', '百度');
INSERT INTO `sum_proxy` VALUES (757, 0.000000, NULL, NULL, '2025-01-04', '小张_0', '优量汇');
INSERT INTO `sum_proxy` VALUES (758, 0.000000, NULL, NULL, '2025-01-04', '小张_0', '穿山甲');
INSERT INTO `sum_proxy` VALUES (759, 0.000000, NULL, NULL, '2025-01-04', '小张_0', '快手');
INSERT INTO `sum_proxy` VALUES (760, 0.000000, NULL, NULL, '2025-01-04', '小张_0', 'all');
INSERT INTO `sum_proxy` VALUES (761, 0.000000, NULL, NULL, '2025-01-04', '31', '百度');
INSERT INTO `sum_proxy` VALUES (762, 0.000000, NULL, NULL, '2025-01-04', '31', '优量汇');
INSERT INTO `sum_proxy` VALUES (763, 0.000000, NULL, NULL, '2025-01-04', '31', '穿山甲');
INSERT INTO `sum_proxy` VALUES (764, 0.000000, NULL, NULL, '2025-01-04', '31', '快手');
INSERT INTO `sum_proxy` VALUES (765, 0.000000, NULL, NULL, '2025-01-04', '31', 'all');
INSERT INTO `sum_proxy` VALUES (766, 0.000000, NULL, NULL, '2025-01-04', '小张', '百度');
INSERT INTO `sum_proxy` VALUES (767, 0.000000, NULL, NULL, '2025-01-04', '小张', '优量汇');
INSERT INTO `sum_proxy` VALUES (768, 0.000000, NULL, NULL, '2025-01-04', '小张', '穿山甲');
INSERT INTO `sum_proxy` VALUES (769, 0.000000, NULL, NULL, '2025-01-04', '小张', '快手');
INSERT INTO `sum_proxy` VALUES (770, 0.000000, NULL, NULL, '2025-01-04', '小张', 'all');
INSERT INTO `sum_proxy` VALUES (771, 0.000000, NULL, NULL, '2025-01-04', '小陈', '百度');
INSERT INTO `sum_proxy` VALUES (772, 0.000000, NULL, NULL, '2025-01-04', '小陈', '优量汇');
INSERT INTO `sum_proxy` VALUES (773, 0.000000, NULL, NULL, '2025-01-04', '小陈', '穿山甲');
INSERT INTO `sum_proxy` VALUES (774, 0.000000, NULL, NULL, '2025-01-04', '小陈', '快手');
INSERT INTO `sum_proxy` VALUES (775, 0.000000, NULL, NULL, '2025-01-04', '小陈', 'all');
INSERT INTO `sum_proxy` VALUES (776, 0.000000, NULL, NULL, '2025-01-14', '31_0', '百度');
INSERT INTO `sum_proxy` VALUES (777, 0.000000, NULL, NULL, '2025-01-14', '31_0', '优量汇');
INSERT INTO `sum_proxy` VALUES (778, 0.000000, NULL, NULL, '2025-01-14', '31_0', '穿山甲');
INSERT INTO `sum_proxy` VALUES (779, 0.000000, NULL, NULL, '2025-01-14', '31_0', '快手');
INSERT INTO `sum_proxy` VALUES (780, 0.000000, NULL, NULL, '2025-01-14', '31_0', 'all');
INSERT INTO `sum_proxy` VALUES (781, 0.000000, NULL, NULL, '2025-01-14', '小陈_1', '百度');
INSERT INTO `sum_proxy` VALUES (782, 0.000000, NULL, NULL, '2025-01-14', '小陈_1', '优量汇');
INSERT INTO `sum_proxy` VALUES (783, 0.000000, NULL, NULL, '2025-01-14', '小陈_1', '穿山甲');
INSERT INTO `sum_proxy` VALUES (784, 0.000000, NULL, NULL, '2025-01-14', '小陈_1', '快手');
INSERT INTO `sum_proxy` VALUES (785, 0.000000, NULL, NULL, '2025-01-14', '小陈_1', 'all');
INSERT INTO `sum_proxy` VALUES (786, 0.000000, NULL, NULL, '2025-01-14', '德玛西亚', '百度');
INSERT INTO `sum_proxy` VALUES (787, 0.000000, NULL, NULL, '2025-01-14', '德玛西亚', '优量汇');
INSERT INTO `sum_proxy` VALUES (788, 0.000000, NULL, NULL, '2025-01-14', '德玛西亚', '穿山甲');
INSERT INTO `sum_proxy` VALUES (789, 0.000000, NULL, NULL, '2025-01-14', '德玛西亚', '快手');
INSERT INTO `sum_proxy` VALUES (790, 0.000000, NULL, NULL, '2025-01-14', '德玛西亚', 'all');
INSERT INTO `sum_proxy` VALUES (791, 0.000000, NULL, NULL, '2025-01-14', '小陈_2', '百度');
INSERT INTO `sum_proxy` VALUES (792, 0.000000, NULL, NULL, '2025-01-14', '小陈_2', '优量汇');
INSERT INTO `sum_proxy` VALUES (793, 0.000000, NULL, NULL, '2025-01-14', '小陈_2', '穿山甲');
INSERT INTO `sum_proxy` VALUES (794, 0.000000, NULL, NULL, '2025-01-14', '小陈_2', '快手');
INSERT INTO `sum_proxy` VALUES (795, 0.000000, NULL, NULL, '2025-01-14', '小陈_2', 'all');
INSERT INTO `sum_proxy` VALUES (796, 0.000000, NULL, NULL, '2025-01-14', '31_1', '百度');
INSERT INTO `sum_proxy` VALUES (797, 0.000000, NULL, NULL, '2025-01-14', '31_1', '优量汇');
INSERT INTO `sum_proxy` VALUES (798, 0.000000, NULL, NULL, '2025-01-14', '31_1', '穿山甲');
INSERT INTO `sum_proxy` VALUES (799, 0.000000, NULL, NULL, '2025-01-14', '31_1', '快手');
INSERT INTO `sum_proxy` VALUES (800, 0.000000, NULL, NULL, '2025-01-14', '31_1', 'all');
INSERT INTO `sum_proxy` VALUES (801, 0.000000, NULL, NULL, '2025-01-14', '小陈_0', '百度');
INSERT INTO `sum_proxy` VALUES (802, 0.000000, NULL, NULL, '2025-01-14', '小陈_0', '优量汇');
INSERT INTO `sum_proxy` VALUES (803, 0.000000, NULL, NULL, '2025-01-14', '小陈_0', '穿山甲');
INSERT INTO `sum_proxy` VALUES (804, 0.000000, NULL, NULL, '2025-01-14', '小陈_0', '快手');
INSERT INTO `sum_proxy` VALUES (805, 0.000000, NULL, NULL, '2025-01-14', '小陈_0', 'all');
INSERT INTO `sum_proxy` VALUES (806, 0.000000, NULL, NULL, '2025-01-14', '8888_1', '百度');
INSERT INTO `sum_proxy` VALUES (807, 0.000000, NULL, NULL, '2025-01-14', '8888_1', '优量汇');
INSERT INTO `sum_proxy` VALUES (808, 0.000000, NULL, NULL, '2025-01-14', '8888_1', '穿山甲');
INSERT INTO `sum_proxy` VALUES (809, 0.000000, NULL, NULL, '2025-01-14', '8888_1', '快手');
INSERT INTO `sum_proxy` VALUES (810, 0.000000, NULL, NULL, '2025-01-14', '8888_1', 'all');
INSERT INTO `sum_proxy` VALUES (811, 0.000000, NULL, NULL, '2025-01-14', '8888_2', '百度');
INSERT INTO `sum_proxy` VALUES (812, 0.000000, NULL, NULL, '2025-01-14', '8888_2', '优量汇');
INSERT INTO `sum_proxy` VALUES (813, 0.000000, NULL, NULL, '2025-01-14', '8888_2', '穿山甲');
INSERT INTO `sum_proxy` VALUES (814, 0.000000, NULL, NULL, '2025-01-14', '8888_2', '快手');
INSERT INTO `sum_proxy` VALUES (815, 0.000000, NULL, NULL, '2025-01-14', '8888_2', 'all');
INSERT INTO `sum_proxy` VALUES (816, 0.000000, NULL, NULL, '2025-01-14', '小张_0', '百度');
INSERT INTO `sum_proxy` VALUES (817, 0.000000, NULL, NULL, '2025-01-14', '小张_0', '优量汇');
INSERT INTO `sum_proxy` VALUES (818, 0.000000, NULL, NULL, '2025-01-14', '小张_0', '穿山甲');
INSERT INTO `sum_proxy` VALUES (819, 0.000000, NULL, NULL, '2025-01-14', '小张_0', '快手');
INSERT INTO `sum_proxy` VALUES (820, 0.000000, NULL, NULL, '2025-01-14', '小张_0', 'all');
INSERT INTO `sum_proxy` VALUES (821, 0.000000, NULL, NULL, '2025-01-14', '31', '百度');
INSERT INTO `sum_proxy` VALUES (822, 0.000000, NULL, NULL, '2025-01-14', '31', '优量汇');
INSERT INTO `sum_proxy` VALUES (823, 0.000000, NULL, NULL, '2025-01-14', '31', '穿山甲');
INSERT INTO `sum_proxy` VALUES (824, 0.000000, NULL, NULL, '2025-01-14', '31', '快手');
INSERT INTO `sum_proxy` VALUES (825, 0.000000, NULL, NULL, '2025-01-14', '31', 'all');
INSERT INTO `sum_proxy` VALUES (826, 0.000000, NULL, NULL, '2025-01-14', '小张', '百度');
INSERT INTO `sum_proxy` VALUES (827, 0.000000, NULL, NULL, '2025-01-14', '小张', '优量汇');
INSERT INTO `sum_proxy` VALUES (828, 0.000000, NULL, NULL, '2025-01-14', '小张', '穿山甲');
INSERT INTO `sum_proxy` VALUES (829, 0.000000, NULL, NULL, '2025-01-14', '小张', '快手');
INSERT INTO `sum_proxy` VALUES (830, 0.000000, NULL, NULL, '2025-01-14', '小张', 'all');
INSERT INTO `sum_proxy` VALUES (831, 0.000000, NULL, NULL, '2025-01-14', '小陈', '百度');
INSERT INTO `sum_proxy` VALUES (832, 0.000000, NULL, NULL, '2025-01-14', '小陈', '优量汇');
INSERT INTO `sum_proxy` VALUES (833, 0.000000, NULL, NULL, '2025-01-14', '小陈', '穿山甲');
INSERT INTO `sum_proxy` VALUES (834, 0.000000, NULL, NULL, '2025-01-14', '小陈', '快手');
INSERT INTO `sum_proxy` VALUES (835, 0.000000, NULL, NULL, '2025-01-14', '小陈', 'all');
INSERT INTO `sum_proxy` VALUES (836, 0.000000, NULL, NULL, '2025-01-06', '31_0', '百度');
INSERT INTO `sum_proxy` VALUES (837, 0.000000, NULL, NULL, '2025-01-06', '31_0', '优量汇');
INSERT INTO `sum_proxy` VALUES (838, 0.000000, NULL, NULL, '2025-01-06', '31_0', '穿山甲');
INSERT INTO `sum_proxy` VALUES (839, 0.000000, NULL, NULL, '2025-01-06', '31_0', '快手');
INSERT INTO `sum_proxy` VALUES (840, 0.000000, NULL, NULL, '2025-01-06', '31_0', 'all');
INSERT INTO `sum_proxy` VALUES (841, 0.000000, NULL, NULL, '2025-01-06', '小陈_1', '百度');
INSERT INTO `sum_proxy` VALUES (842, 0.000000, NULL, NULL, '2025-01-06', '小陈_1', '优量汇');
INSERT INTO `sum_proxy` VALUES (843, 0.000000, NULL, NULL, '2025-01-06', '小陈_1', '穿山甲');
INSERT INTO `sum_proxy` VALUES (844, 0.000000, NULL, NULL, '2025-01-06', '小陈_1', '快手');
INSERT INTO `sum_proxy` VALUES (845, 0.000000, NULL, NULL, '2025-01-06', '小陈_1', 'all');
INSERT INTO `sum_proxy` VALUES (846, 0.000000, NULL, NULL, '2025-01-06', '德玛西亚', '百度');
INSERT INTO `sum_proxy` VALUES (847, 0.000000, NULL, NULL, '2025-01-06', '德玛西亚', '优量汇');
INSERT INTO `sum_proxy` VALUES (848, 0.000000, NULL, NULL, '2025-01-06', '德玛西亚', '穿山甲');
INSERT INTO `sum_proxy` VALUES (849, 0.000000, NULL, NULL, '2025-01-06', '德玛西亚', '快手');
INSERT INTO `sum_proxy` VALUES (850, 0.000000, NULL, NULL, '2025-01-06', '德玛西亚', 'all');
INSERT INTO `sum_proxy` VALUES (851, 0.000000, NULL, NULL, '2025-01-06', '小陈_2', '百度');
INSERT INTO `sum_proxy` VALUES (852, 0.000000, NULL, NULL, '2025-01-06', '小陈_2', '优量汇');
INSERT INTO `sum_proxy` VALUES (853, 0.000000, NULL, NULL, '2025-01-06', '小陈_2', '穿山甲');
INSERT INTO `sum_proxy` VALUES (854, 0.000000, NULL, NULL, '2025-01-06', '小陈_2', '快手');
INSERT INTO `sum_proxy` VALUES (855, 0.000000, NULL, NULL, '2025-01-06', '小陈_2', 'all');
INSERT INTO `sum_proxy` VALUES (856, 0.000000, NULL, NULL, '2025-01-06', '31_1', '百度');
INSERT INTO `sum_proxy` VALUES (857, 0.000000, NULL, NULL, '2025-01-06', '31_1', '优量汇');
INSERT INTO `sum_proxy` VALUES (858, 0.000000, NULL, NULL, '2025-01-06', '31_1', '穿山甲');
INSERT INTO `sum_proxy` VALUES (859, 0.000000, NULL, NULL, '2025-01-06', '31_1', '快手');
INSERT INTO `sum_proxy` VALUES (860, 0.000000, NULL, NULL, '2025-01-06', '31_1', 'all');
INSERT INTO `sum_proxy` VALUES (861, 0.000000, NULL, NULL, '2025-01-06', '小陈_0', '百度');
INSERT INTO `sum_proxy` VALUES (862, 0.000000, NULL, NULL, '2025-01-06', '小陈_0', '优量汇');
INSERT INTO `sum_proxy` VALUES (863, 0.000000, NULL, NULL, '2025-01-06', '小陈_0', '穿山甲');
INSERT INTO `sum_proxy` VALUES (864, 0.000000, NULL, NULL, '2025-01-06', '小陈_0', '快手');
INSERT INTO `sum_proxy` VALUES (865, 0.000000, NULL, NULL, '2025-01-06', '小陈_0', 'all');
INSERT INTO `sum_proxy` VALUES (866, 0.000000, NULL, NULL, '2025-01-06', '8888_1', '百度');
INSERT INTO `sum_proxy` VALUES (867, 0.000000, NULL, NULL, '2025-01-06', '8888_1', '优量汇');
INSERT INTO `sum_proxy` VALUES (868, 0.000000, NULL, NULL, '2025-01-06', '8888_1', '穿山甲');
INSERT INTO `sum_proxy` VALUES (869, 0.000000, NULL, NULL, '2025-01-06', '8888_1', '快手');
INSERT INTO `sum_proxy` VALUES (870, 0.000000, NULL, NULL, '2025-01-06', '8888_1', 'all');
INSERT INTO `sum_proxy` VALUES (871, 0.000000, NULL, NULL, '2025-01-06', '8888_2', '百度');
INSERT INTO `sum_proxy` VALUES (872, 0.000000, NULL, NULL, '2025-01-06', '8888_2', '优量汇');
INSERT INTO `sum_proxy` VALUES (873, 0.000000, NULL, NULL, '2025-01-06', '8888_2', '穿山甲');
INSERT INTO `sum_proxy` VALUES (874, 0.000000, NULL, NULL, '2025-01-06', '8888_2', '快手');
INSERT INTO `sum_proxy` VALUES (875, 0.000000, NULL, NULL, '2025-01-06', '8888_2', 'all');
INSERT INTO `sum_proxy` VALUES (876, 0.000000, NULL, NULL, '2025-01-06', '小张_0', '百度');
INSERT INTO `sum_proxy` VALUES (877, 0.000000, NULL, NULL, '2025-01-06', '小张_0', '优量汇');
INSERT INTO `sum_proxy` VALUES (878, 0.000000, NULL, NULL, '2025-01-06', '小张_0', '穿山甲');
INSERT INTO `sum_proxy` VALUES (879, 0.000000, NULL, NULL, '2025-01-06', '小张_0', '快手');
INSERT INTO `sum_proxy` VALUES (880, 0.000000, NULL, NULL, '2025-01-06', '小张_0', 'all');
INSERT INTO `sum_proxy` VALUES (881, 0.000000, NULL, NULL, '2025-01-06', '31', '百度');
INSERT INTO `sum_proxy` VALUES (882, 0.000000, NULL, NULL, '2025-01-06', '31', '优量汇');
INSERT INTO `sum_proxy` VALUES (883, 0.000000, NULL, NULL, '2025-01-06', '31', '穿山甲');
INSERT INTO `sum_proxy` VALUES (884, 0.000000, NULL, NULL, '2025-01-06', '31', '快手');
INSERT INTO `sum_proxy` VALUES (885, 0.000000, NULL, NULL, '2025-01-06', '31', 'all');
INSERT INTO `sum_proxy` VALUES (886, 0.000000, NULL, NULL, '2025-01-06', '小张', '百度');
INSERT INTO `sum_proxy` VALUES (887, 0.000000, NULL, NULL, '2025-01-06', '小张', '优量汇');
INSERT INTO `sum_proxy` VALUES (888, 0.000000, NULL, NULL, '2025-01-06', '小张', '穿山甲');
INSERT INTO `sum_proxy` VALUES (889, 0.000000, NULL, NULL, '2025-01-06', '小张', '快手');
INSERT INTO `sum_proxy` VALUES (890, 0.000000, NULL, NULL, '2025-01-06', '小张', 'all');
INSERT INTO `sum_proxy` VALUES (891, 0.000000, NULL, NULL, '2025-01-06', '小陈', '百度');
INSERT INTO `sum_proxy` VALUES (892, 0.000000, NULL, NULL, '2025-01-06', '小陈', '优量汇');
INSERT INTO `sum_proxy` VALUES (893, 0.000000, NULL, NULL, '2025-01-06', '小陈', '穿山甲');
INSERT INTO `sum_proxy` VALUES (894, 0.000000, NULL, NULL, '2025-01-06', '小陈', '快手');
INSERT INTO `sum_proxy` VALUES (895, 0.000000, NULL, NULL, '2025-01-06', '小陈', 'all');

-- ----------------------------
-- Table structure for user_gathering_setting
-- ----------------------------
DROP TABLE IF EXISTS `user_gathering_setting`;
CREATE TABLE `user_gathering_setting`  (
  `user_gathering_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '聚集设置id',
  `gathering_choice` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '聚集选择项(0是小范围,1是大范围)',
  `gathering_population_large` int(11) NULL DEFAULT NULL COMMENT '聚集大范围限制的人数',
  `gathering_population_small` int(3) NULL DEFAULT NULL COMMENT '聚集小范围限制的人数',
  `same_mac_population` int(11) NULL DEFAULT NULL COMMENT '允许同一个mac地址在线用户数量',
  `gathering_deviceLimit` int(11) NULL DEFAULT 1 COMMENT '聚集手机同型号限制的设备数量',
  PRIMARY KEY (`user_gathering_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 148 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_gathering_setting
-- ----------------------------
INSERT INTO `user_gathering_setting` VALUES (136, '0', 10, 10, 10, 0);
INSERT INTO `user_gathering_setting` VALUES (137, '0', 10, 10, 10, 0);
INSERT INTO `user_gathering_setting` VALUES (138, '0', 10, 10, 10, 0);
INSERT INTO `user_gathering_setting` VALUES (139, '0', 10, 10, 10, 0);
INSERT INTO `user_gathering_setting` VALUES (140, '0', 10, 10, 10, 0);
INSERT INTO `user_gathering_setting` VALUES (141, '0', 10, 10, 10, 0);
INSERT INTO `user_gathering_setting` VALUES (143, '0', 102, 10, 10, 28);
INSERT INTO `user_gathering_setting` VALUES (144, '0', 10, 10, 10, 0);
INSERT INTO `user_gathering_setting` VALUES (145, '0', 10, 10, 10, 0);
INSERT INTO `user_gathering_setting` VALUES (146, '0', 10, 10, 10, 0);
INSERT INTO `user_gathering_setting` VALUES (147, '0', 10, 10, 10, 0);

-- ----------------------------
-- Table structure for user_reward_setting
-- ----------------------------
DROP TABLE IF EXISTS `user_reward_setting`;
CREATE TABLE `user_reward_setting`  (
  `user_reward_setting_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户奖励比例设置id',
  `get_reward_max_val` int(11) NOT NULL COMMENT '用户可获取奖励最高值',
  `user_adv_percentage` double(20, 4) NOT NULL COMMENT '用户广告比例（%）',
  PRIMARY KEY (`user_reward_setting_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 154 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_reward_setting
-- ----------------------------
INSERT INTO `user_reward_setting` VALUES (149, 12, 98.0000);
INSERT INTO `user_reward_setting` VALUES (150, 0, 10.0000);
INSERT INTO `user_reward_setting` VALUES (151, 0, 10.0000);
INSERT INTO `user_reward_setting` VALUES (152, 0, 10.0000);
INSERT INTO `user_reward_setting` VALUES (153, 0, 10.0000);

-- ----------------------------
-- Table structure for withdraw_reback
-- ----------------------------
DROP TABLE IF EXISTS `withdraw_reback`;
CREATE TABLE `withdraw_reback`  (
  `withdraw_reback_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户返现比例id',
  `withdraw_reback_percentage` double NULL DEFAULT NULL COMMENT '用户返现比例',
  `withdraw_money_min` decimal(10, 2) NULL DEFAULT NULL COMMENT '用户提现金额最小值',
  `withdraw_money_max` decimal(10, 2) NULL DEFAULT NULL COMMENT '用户提现金额最大值',
  PRIMARY KEY (`withdraw_reback_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of withdraw_reback
-- ----------------------------
INSERT INTO `withdraw_reback` VALUES (1, 10, 0.30, 10000.00);

-- ----------------------------
-- Table structure for withdraw_record
-- ----------------------------
DROP TABLE IF EXISTS `withdraw_record`;
CREATE TABLE `withdraw_record`  (
  `withdraw_record_id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '提现记录id',
  `player_id` bigint(6) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '玩家id',
  `wx_nickname` varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '\r\n玩家昵称',
  `package_name` varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '哪个游戏',
  `withdraw_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT ' 提现状态 0正常通过，1审核,2驳回',
  `withdraw_money` decimal(20, 2) NULL DEFAULT NULL COMMENT '提现金额',
  `return_money` decimal(20, 2) NULL DEFAULT NULL COMMENT '返现金额',
  `withdraw_time` datetime NULL DEFAULT NULL COMMENT '提现订单的生成时间',
  `withdraw_percentage_now` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '订单生成时当时提现比例',
  `withdraw_from` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '提现订单的来源(红包提现 ,订单提现)',
  `pay_login_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `real_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`withdraw_record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 215 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of withdraw_record
-- ----------------------------
INSERT INTO `withdraw_record` VALUES (1, 1000019, 'sup', 'com.layabox.gameyzzs', '1', 12999.00, 9.90, '2024-10-22 22:49:03', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (2, 001415, '巴嘎压路', 'com.layabox.gameyzzs', '1', 12.00, 0.10, '2024-10-13 03:39:21', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (124, 001451, '无敌艾克大王', 'com.layabox.gameyzzs', '1', 12451.96, 0.00, '2024-10-25 22:37:59', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (129, 1000027, 'sup', 'com.layabox.gameyzzs', '1', 0.99, 0.00, '2024-11-30 17:26:24', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (130, 1000027, 'sup', 'com.layabox.gameyzzs', '1', 2.00, 0.60, '2024-11-30 18:35:40', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (131, 1000027, 'sup', 'com.layabox.gameyzzs', '1', 1.00, 0.20, '2024-11-30 19:50:26', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (132, 1000027, 'sup', 'com.layabox.gameyzzs', '1', 1.00, 0.00, '2024-11-30 19:56:57', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (133, 1000027, 'sup', 'com.layabox.gameyzzs', '1', 1.01, 0.20, '2024-11-30 19:58:12', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (134, 1000027, 'sup', 'com.layabox.gameyzzs', '1', 0.99, 0.00, '2024-11-30 20:28:03', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (135, 1000027, 'sup', 'com.layabox.gameyzzs', '1', 1.00, 0.20, '2024-11-30 20:34:50', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (136, 1000027, 'sup', 'com.layabox.gameyzzs', '1', 0.99, 0.00, '2024-11-30 20:36:37', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (137, 1000027, 'sup', 'com.layabox.gameyzzs', '1', 1.00, 0.20, '2024-11-30 20:37:53', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (138, 1000027, 'sup', 'com.layabox.gameyzzs', '1', 0.99, 0.00, '2024-11-30 20:38:55', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (139, 1000027, 'sup', 'com.layabox.gameyzzs', '1', 1.00, 0.10, '2024-11-30 20:43:08', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (140, 1000027, 'sup', 'com.layabox.gameyzzs', '1', 2.00, 0.51, '2024-11-30 20:43:53', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (141, 1000027, 'sup', 'com.layabox.gameyzzs', '2', 1.00, 0.10, '2024-11-30 20:51:51', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (142, 1000027, 'sup', 'com.layabox.gameyzzs', '2', 1.00, 0.10, '2024-11-30 20:54:29', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (143, 1000027, 'sup', 'com.layabox.gameyzzs', '2', 2.00, 0.50, '2024-11-30 20:55:06', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (144, 1000027, 'sup', 'com.layabox.gameyzzs', '1', 0.99, 0.00, '2024-11-30 20:56:38', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (145, 1000027, 'sup', 'com.layabox.gameyzzs', '2', 1.00, 0.20, '2024-11-30 20:57:22', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (146, 1000027, 'sup', 'com.layabox.gameyzzs', '0', 1.00, 0.40, '2024-11-30 20:58:08', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (147, 1000027, 'sup', 'com.layabox.gameyzzs', '0', 0.99, 0.00, '2024-11-30 21:19:31', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (148, 1000027, 'sup', 'com.layabox.gameyzzs', '0', 1.00, 0.19, '2024-11-30 21:20:23', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (149, 1000027, 'sup', 'com.layabox.gameyzzs', '0', 1.00, 0.40, '2024-11-30 21:21:05', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (150, 1000027, 'sup', 'com.layabox.gameyzzs', '0', 1.00, 0.10, '2024-11-30 21:22:18', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (151, 1000027, 'sup', 'com.layabox.gameyzzs', '0', 2.00, 0.50, '2024-11-30 21:23:01', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (152, 1000027, 'sup', 'com.layabox.gameyzzs', '0', 1.00, 0.10, '2024-11-30 21:24:00', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (153, 1000027, 'sup', 'com.layabox.gameyzzs', '0', 1.00, 0.10, '2024-11-30 21:25:46', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (154, 1000027, 'sup', 'com.layabox.gameyzzs', '0', 0.30, 0.03, '2024-11-30 21:28:50', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (155, 1000027, 'sup', 'com.layabox.gameyzzs', '0', 0.80, 0.19, '2024-11-30 21:32:51', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (156, 1000027, 'sup', 'com.layabox.gameyzzs', '0', 0.70, 0.32, '2024-11-30 21:38:09', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (157, 1000027, 'sup', 'com.layabox.gameyzzs', '0', 0.50, 0.15, '2024-11-30 21:58:11', '10000', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (158, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.10, 0.00, '2025-01-02 17:49:16', '10000.0', 'r', NULL, NULL);
INSERT INTO `withdraw_record` VALUES (159, 1000027, 'sup', 'com.layabox.gameyzzs', '0', 0.10, 0.00, '2025-01-02 19:32:57', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (160, 1000027, 'sup', 'com.layabox.gameyzzs', '0', 0.10, 0.00, '2025-01-02 19:52:42', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (161, 1000027, 'sup', 'com.layabox.gameyzzs', '0', 0.10, 0.00, '2025-01-02 20:12:37', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (162, 1000027, 'sup', 'com.layabox.gameyzzs', '0', 0.10, 0.00, '2025-01-02 20:23:05', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (163, 1000027, 'sup', 'com.layabox.gameyzzs', '0', 0.10, 0.00, '2025-01-02 20:27:57', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (164, 1000027, 'sup', 'com.layabox.gameyzzs', '0', 0.10, 0.00, '2025-01-02 20:30:35', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (165, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '1', 0.10, 0.00, '2025-01-02 20:50:37', '10000.0', 'r', '15159647660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (166, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.30, 0.00, '2025-01-03 00:34:45', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (167, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '1', 0.40, 0.05, '2025-01-03 00:34:45', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (168, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.40, 0.04, '2025-01-03 01:05:51', '10000.0', 'r', '13775256007', '唐允增');
INSERT INTO `withdraw_record` VALUES (169, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.30, -0.09, '2025-01-03 01:11:34', '10000.0', 'r', '13775256007', '唐允增');
INSERT INTO `withdraw_record` VALUES (170, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.30, 0.00, '2025-01-03 01:24:18', '10000.0', 'r', '13775256007', '唐允增');
INSERT INTO `withdraw_record` VALUES (171, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.30, 0.00, '2025-01-03 01:36:06', '10000.0', 'r', '13775256007', '唐允增');
INSERT INTO `withdraw_record` VALUES (172, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.30, 0.00, '2025-01-03 01:41:45', '10000.0', 'r', '13775256007', '唐允增');
INSERT INTO `withdraw_record` VALUES (173, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.30, 0.00, '2025-01-03 01:47:58', '10000.0', 'r', '13775256007', '唐允增');
INSERT INTO `withdraw_record` VALUES (175, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.30, 0.00, '2025-01-03 16:20:48', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (176, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.30, 0.00, '2025-01-03 16:26:55', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (177, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '1', 0.10, 0.00, '2025-01-03 16:30:13', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (178, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.30, 0.00, '2025-01-03 16:48:10', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (179, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '1', 0.30, 0.00, '2025-01-03 16:49:02', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (180, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '1', 0.70, 0.00, '2025-01-03 17:07:00', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (181, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '1', 0.70, 0.00, '2025-01-03 17:12:34', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (182, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.30, 0.00, '2025-01-03 17:15:13', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (183, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '1', 0.40, 0.00, '2025-01-03 17:15:57', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (184, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '1', 0.40, 0.00, '2025-01-03 17:23:32', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (185, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.30, 0.00, '2025-01-03 17:24:51', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (186, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '2', 0.40, 0.00, '2025-01-03 17:25:29', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (187, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.40, 0.00, '2025-01-03 17:41:42', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (188, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '2', 0.30, 0.00, '2025-01-03 18:31:15', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (189, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '2', 0.40, 0.00, '2025-01-03 18:33:20', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (190, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.40, 0.04, '2025-01-03 19:04:05', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (191, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '2', 0.40, 0.04, '2025-01-03 19:10:24', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (192, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.30, 0.03, '2025-01-03 19:12:56', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (193, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '2', 0.40, 0.04, '2025-01-03 19:14:26', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (194, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '2', 0.30, 0.03, '2025-01-03 19:15:28', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (195, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.50, 0.05, '2025-01-03 19:25:01', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (196, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.50, 0.05, '2025-01-03 19:26:48', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (197, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.50, 0.20, '2025-01-03 19:28:40', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (198, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.65, 0.13, '2025-01-03 19:32:00', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (199, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.30, 0.00, '2025-01-03 19:39:12', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (200, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.30, 0.00, '2025-01-03 19:49:27', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (201, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '2', 0.30, 0.03, '2025-01-03 19:50:53', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (202, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.30, 0.03, '2025-01-03 19:57:21', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (203, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.30, 0.03, '2025-01-03 19:58:13', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (204, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '1', 0.50, 0.16, '2025-01-03 19:59:38', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (205, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.30, 0.03, '2025-01-03 20:05:13', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (206, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '1', 0.30, 0.00, '2025-01-05 23:29:00', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (207, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '1', 0.50, 0.00, '2025-01-05 23:34:18', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (208, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.60, 0.00, '2025-01-05 23:36:14', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (209, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.60, 0.06, '2025-01-05 23:38:21', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (210, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.70, 0.00, '2025-01-05 23:39:57', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (211, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.30, 0.00, '2025-01-05 23:40:47', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (212, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.50, 0.15, '2025-01-05 23:50:22', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (213, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.30, 0.00, '2025-01-05 23:57:55', '10000.0', 'r', '15159617660', '陈惠琳');
INSERT INTO `withdraw_record` VALUES (214, 1000030, '一叶知秋', 'com.layabox.gameyzzs', '0', 0.60, 0.06, '2025-01-06 00:01:09', '10000.0', 'r', '15159617660', '陈惠琳');

-- ----------------------------
-- Table structure for withdraw_setting
-- ----------------------------
DROP TABLE IF EXISTS `withdraw_setting`;
CREATE TABLE `withdraw_setting`  (
  `withdraw_setting_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '提现配置的id',
  `package_name` varchar(70) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '包名',
  `withdraw_percentage` double(10, 2) NULL DEFAULT NULL COMMENT '提现比例',
  `withdraw_count` int(11) NULL DEFAULT NULL COMMENT '每日提现次数',
  `withdraw_nojudge_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '用户免审核提现总金额\r\n',
  `withdraw_reback_id` int(11) NULL DEFAULT NULL COMMENT '用户返现比例id',
  `count_switch` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否启用每日提现次数开关(0是启用,1是不启用)',
  PRIMARY KEY (`withdraw_setting_id`) USING BTREE,
  UNIQUE INDEX `bagname`(`package_name` ASC) USING BTREE COMMENT '给包名加唯一索引,防止重复的游戏提现配置'
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of withdraw_setting
-- ----------------------------
INSERT INTO `withdraw_setting` VALUES (1, 'com.layabox.gameyzzs', 10000.00, 2, 1.50, NULL, '1');
INSERT INTO `withdraw_setting` VALUES (2, '142', 0.00, 0, 0.00, NULL, '0');
INSERT INTO `withdraw_setting` VALUES (3, '124123', 0.00, 0, 0.00, NULL, '0');
INSERT INTO `withdraw_setting` VALUES (4, 'e12', 0.00, 0, 0.00, NULL, '0');
INSERT INTO `withdraw_setting` VALUES (5, 'com.xlsfxxl.app', 0.00, 0, 0.00, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;

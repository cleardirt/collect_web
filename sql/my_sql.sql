SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for report
-- ----------------------------
DROP TABLE IF EXISTS `report`;
CREATE TABLE `report`  (
                           `id` int(11) NOT NULL AUTO_INCREMENT,
                           `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                           `create_time` datetime(0) NULL DEFAULT NULL,
                           #`audit_status` tinyint(2) NOT NULL DEFAULT 0, /*报告的审核状态，-1，0，1分别表示审核不通过、正在审核、审核通过*/
                           `worker_id` int(11) NOT NULL,
                           `worker_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                           `task_id` int(11) NOT NULL,
                           `device_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                           `father_id` int(11) NULL DEFAULT NULL,
                           `score` decimal(3,2) NULL DEFAULT 0.00,
                           `real_rater_number` int(11) NULL DEFAULT 0,
                           `weighted_rater_number` decimal(12,1) NULL DEFAULT 0.0,
                           `bug_description` varchar(255) NOT NULL,
                           `step_explanation` varchar(255) NOT NULL,
                           PRIMARY KEY (`id`) USING BTREE,
                           INDEX `fk_worker_report`(`worker_id`) USING BTREE,
                           INDEX `fk_task_report`(`task_id`) USING BTREE,
                           INDEX `fk_father_report`(`father_id`) USING BTREE,
                           CONSTRAINT `fk_worker_report` FOREIGN KEY (`worker_id`) REFERENCES `userinfo`(`Id`) ON DELETE NO ACTION ON UPDATE CASCADE,
                           CONSTRAINT `fk_task_report` FOREIGN KEY (`task_id`) REFERENCES `task`(`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
                           CONSTRAINT `fk_father_report` FOREIGN KEY (`father_id`) REFERENCES `report`(`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for text_hash
-- ----------------------------
DROP TABLE IF EXISTS `text_hash`;
CREATE TABLE `text_hash` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `report_id` int(11) NOT NULL,
    `task_id` int(11) NOT NULL,
    `title_hash` varchar(64) NOT NULL ,
    `description_hash` varchar(64) NOT NULL ,
    `step_hash` varchar(64) NOT NULL ,
    PRIMARY KEY (`id`) USING BTREE ,
    INDEX `fk_report_text`(`report_id`) USING BTREE ,
    INDEX `fk_task_text`(`task_id`) USING BTREE ,
    CONSTRAINT `fk_report_text` FOREIGN KEY (`report_id`) REFERENCES `report`(`id`) ON DELETE CASCADE ON UPDATE CASCADE ,
    CONSTRAINT `fk_task_text` FOREIGN KEY (`task_id`) REFERENCES `task`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bug_screenshot
-- ----------------------------
DROP TABLE IF EXISTS `bug_screenshot`;
CREATE TABLE `bug_screenshot`(
                        `id` int(11) NOT NULL AUTO_INCREMENT,
                        `screenshot` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                        `report_id` int(11) NULL DEFAULT NULL,
                        `fingerprint` tinyblob NOT NULL,
                        PRIMARY KEY (`id`) USING BTREE,
                        INDEX `fk_report_shot`(`report_id`) USING BTREE,
                        CONSTRAINT `fk_report_shot` FOREIGN KEY (`report_id`) REFERENCES `report`(`id`) ON DELETE CASCADE ON UPDATE CASCADE

)  ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_task_recommendation
-- ----------------------------
DROP TABLE IF EXISTS `user_task_recommendation`;
CREATE TABLE `user_task_recommendation`(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `uid` int(11) NOT NULL,
    `recommend_list` varchar(255) NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `fk_user_recommend`(`uid`) USING BTREE,
    CONSTRAINT `fk_user_recommend` FOREIGN KEY (`uid`) REFERENCES `userinfo`(`Id`) ON DELETE CASCADE ON UPDATE CASCADE
)  ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;
-- ----------------------------
-- Records of user_task_recommendation
-- ----------------------------
INSERT INTO `user_task_recommendation` VALUES (1, 2, '1,2');


-- ----------------------------
-- Table structure for user_device
-- ----------------------------
DROP TABLE IF EXISTS `user_device`;
CREATE TABLE `user_device`(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `user_id` int(11) NOT NULL,
    `windows` int(11) NULL DEFAULT 0,
    `linux` int(11) NULL DEFAULT 0,
    `macos` int(11) NULL DEFAULT 0,
    `harmonyos` int(11) NULL DEFAULT 0,
    `ios` int(11) NULL DEFAULT 0,
    `android` int(11) NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `fk_user_device`(`user_id`) USING BTREE,
    CONSTRAINT `fk_user_device` FOREIGN KEY (`user_id`) REFERENCES `userinfo`(`Id`) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;
-- ----------------------------
-- Records of user_device
-- ----------------------------
INSERT INTO `user_device` VALUES (1, 2, 0, 0, 0, 0,0,0);

-- ----------------------------
-- Table structure for device_info
-- 每个task可有多个device_info
-- ----------------------------
DROP TABLE IF EXISTS `device_info`;
CREATE TABLE `device_info`(
                                    `id` int(11) NOT NULL AUTO_INCREMENT,
                                    `explanation` varchar(255) NOT NULL,
                                    `task_id` int(11) NOT NULL ,  /*task的device要求*/
                                    PRIMARY KEY (`id`) USING BTREE,
                                    INDEX `fk_task_device`(`task_id`) USING BTREE,
                                    CONSTRAINT `fk_task_device` FOREIGN KEY (`task_id`) REFERENCES `task`(`id`) ON DELETE CASCADE ON UPDATE CASCADE
)  ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task`  (
                           `id` int(11) NOT NULL AUTO_INCREMENT,
                           `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                           `brief_intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                           `test_start_time` datetime(0) NOT NULL,
                           `test_end_time` datetime(0) NOT NULL,
                           `is_open` tinyint(1) NOT NULL, /* 0/false: recruitment closed; 1/true: still open*/
                           `worker_num` int(11) NOT NULL,
                           `test_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                           `create_time` datetime(0) NOT NULL,
                           `delete_time` datetime(0) NULL DEFAULT NULL,
                           `auftraggeber_id` int(11) NOT NULL,
                           `auftraggeber_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                           `select_state` int(11) NULL DEFAULT NULL,
                           `in_progress` int(11) NULL DEFAULT NULL,
                           `difficulty` int(11) NOT NULL,
                           PRIMARY KEY (`id`) USING BTREE,
                           INDEX `fk_auftra_task`(`auftraggeber_id`) USING BTREE,
                           CONSTRAINT `fk_auftra_task` FOREIGN KEY (`auftraggeber_id`) REFERENCES `userinfo` (`Id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of task
-- ----------------------------
INSERT INTO `task` VALUES (1, '金融业务安全测试', '本项目为高级项目请勿对外公开。需遵守漏洞盒子保密协议相关规定，不影响正常业务情况下进行测试，不得以测试漏洞为借口，利用漏洞进行损害用户利益、影响业务运作、盗取用户数据等行为。',
                           '2021-12-20 10:00:00','2022-12-20 10:00:00', 0, 3000000,
                           '功能测试',
                            '2021-12-20 10:00:00', NULL, 3, '芳醇溶', null, null,0);
INSERT INTO `task` VALUES (2, '运输业务安全测试', '本项目为高级项目请勿对外公开。', '2022-2-20 10:00:00',
                           '2022-12-20 23:00:00', 1, 20000,
                           '性能测试',
                           '2022-2-2 10:00:00', NULL, 3, '醇溶', null, null,1);

-- ----------------------------
-- Table structure for app
-- ----------------------------
DROP TABLE IF EXISTS `app`;
CREATE TABLE `app`  (
                                `id` int(11) NOT NULL AUTO_INCREMENT,
                                `task_id` int(11) NOT NULL,
                                `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                `file_size` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                `upload_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
                                PRIMARY KEY (`id`) USING BTREE,
                                INDEX `fk_task_app`(`task_id`) USING BTREE,
                                CONSTRAINT `fk_task_app` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 198 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of app
-- ----------------------------
INSERT INTO `app` VALUES (2, 1, 'qq', NULL, NULL, '2022-2-19 10:00:00');
INSERT INTO `app` VALUES (3, 2, 'wechat', NULL, NULL, '2022-2-18 10:00:00');

-- ----------------------------
-- Table structure for doc
-- ----------------------------
DROP TABLE IF EXISTS `doc`;
CREATE TABLE `doc`  (
                        `id` int(11) NOT NULL AUTO_INCREMENT,
                        `task_id` int(11) NOT NULL,
                        `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                        `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                        `file_size` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                        `upload_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
                        PRIMARY KEY (`id`) USING BTREE,
                        INDEX `fk_task_doc`(`task_id`) USING BTREE,
                        CONSTRAINT `fk_task_doc` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 198 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of doc
-- ----------------------------
INSERT INTO `doc` VALUES (2, 1, 'qq测试文档', NULL, NULL, '2022-2-18 10:00:00');
INSERT INTO `doc` VALUES (3, 2, 'wechat测试文档', NULL, NULL, '2022-2-18 10:00:00');



-- ----------------------------
-- Table structure for task_progress
-- 每创建一条数据表示worker x选择了task y
-- ----------------------------
DROP TABLE IF EXISTS `task_progress`;
CREATE TABLE `task_progress`(
                        `id` int(11) NOT NULL AUTO_INCREMENT,
                        `worker_id` int(11) NOT NULL,
                        `task_id` int(11) NOT NULL ,
                        `is_finished` tinyint(1) NULL DEFAULT 0, /* 0: to-do; 1: finished历史完成的任务*/
                        PRIMARY KEY (`id`) USING BTREE,
                        INDEX `fk_worker`(`worker_id`) USING BTREE,
                        CONSTRAINT `fk_worker` FOREIGN KEY (`worker_id`) REFERENCES `userinfo` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
                        INDEX `fk_task`(`task_id`) USING BTREE,
                        CONSTRAINT `fk_task` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
)  ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for userinfo
-- 'user_role' enum(3) "admin","worker","auftraggeber"
-- ----------------------------
DROP TABLE IF EXISTS `userinfo`;
CREATE TABLE `userinfo`  (
                              `Id` int(11) NOT NULL AUTO_INCREMENT,
                              `UserName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                              `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                              `UserPass` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                              `user_role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                              `create_time` datetime(0) NOT NULL,
                              `activity` int(11) NULL DEFAULT 0,
                              `professional_ability` decimal(6,3) NULL DEFAULT 0.0,
                              `rating_ability` decimal(6,3) NULL DEFAULT 0.0,
                              `credit` decimal(6,3) NULL DEFAULT 10.0,
                              PRIMARY KEY (`Id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `userinfo` VALUES (1, 'asd', '10112345678', '123456','ADMIN', '2022-2-19 10:00:00',2,2,0.0,10.0);
INSERT INTO `userinfo` VALUES (2, 'zwr', '10212345678', '123456', 'TESTER', '2022-2-19 10:00:00',2,3,0.0,10.0);
INSERT INTO `userinfo` VALUES (3, '1', '1', '1', 'AUFTRA', '2022-1-10 10:00:00',44,55,0.0,10.0);
SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Table structure for user_rating
-- ----------------------------
DROP TABLE IF EXISTS `user_rating`;
CREATE TABLE `user_rating`(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `report_id` int(11) NOT NULL,
    `user_id` int(11) NOT NULL,
    `rating` int(11) NOT NULL,
    `comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `fk_report_rating`(`report_id`) USING BTREE,
    CONSTRAINT `fk_report_rating` FOREIGN KEY (`report_id`) REFERENCES `report` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
    INDEX `fk_user_rating`(`user_id`) USING BTREE,
    CONSTRAINT `fk_user_rating` FOREIGN KEY (`user_id`) REFERENCES `userinfo` (`Id`) ON DELETE NO ACTION ON UPDATE CASCADE

)ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rating_for_scan
-- ----------------------------
DROP TABLE IF EXISTS `rating_for_scan`;
CREATE TABLE `rating_for_scan`(
                              `id` int(11) NOT NULL AUTO_INCREMENT,
                              `report_id` int(11) NOT NULL,
                              `user_id` int(11) NOT NULL,
                              `rating` int(11) NOT NULL,
                              `rating_time` datetime(0) NOT NULL,
                              `report_end_time` datetime(0) NOT NULL,
                              PRIMARY KEY (`id`) USING BTREE,
                              INDEX `fk_report_rating_for_scan`(`report_id`) USING BTREE,
                              CONSTRAINT `fk_report_rating_for_scan` FOREIGN KEY (`report_id`) REFERENCES `report` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
                              INDEX `fk_user_rating_for_scan`(`user_id`) USING BTREE,
                              CONSTRAINT `fk_user_rating_for_scan` FOREIGN KEY (`user_id`) REFERENCES `userinfo` (`Id`) ON DELETE NO ACTION ON UPDATE CASCADE

)ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_select
-- ----------------------------
DROP TABLE IF EXISTS `user_select`;
CREATE TABLE `user_select`(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `user_id` int(11) NOT NULL,
    `function_test` int(11) NULL DEFAULT 0,
    `performance_test` int(11) NULL DEFAULT 0,
    `bug_explore` int(11) NULL DEFAULT 0,
    `usecase_execution` int(11) NULL DEFAULT 0,
    `device_connection` int(11) NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `fk_user_select`(`user_id`) USING BTREE,
    CONSTRAINT `fk_user_select` FOREIGN KEY (`user_id`) REFERENCES `userinfo`(`Id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;
-- ----------------------------
-- Records of user_select
-- ----------------------------
INSERT INTO `user_select` VALUES (1, 2, 0, 0, 0, 0,0);

-- ----------------------------
-- Table structure for rule
-- ----------------------------
DROP TABLE IF EXISTS `rule`;
CREATE TABLE `rule`(
      `id` int(11) NOT NULL AUTO_INCREMENT,
      `professional_ability_ranking` bit(1) NOT NULL DEFAULT 1,
      `sort_by_task_category` bit (1) NULL DEFAULT 1,
      `test_equipment_sorting` bit (1) NULL DEFAULT 1,
      `selected` bit (1) NOT NULL DEFAULT 0,
      PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;
-- ----------------------------
-- Records of rule
-- ----------------------------
INSERT INTO `rule` VALUES (1, 1, 1, 1, 1);

-- ----------------------------
-- Table structure for report_to_rate
-- ----------------------------
DROP TABLE IF EXISTS `report_to_rate`;
CREATE TABLE `report_to_rate`(
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `uid` int(11) NOT NULL,
    `report_to_rate` varchar(255) NOT NULL, # 分隔符为'_'
    `rated_report` varchar(255) NOT NULL, # 分隔符为'_'
    `rating_willingness` decimal(6,3) NULL DEFAULT 5.0,
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `fk_rater_report`(`uid`) USING BTREE,
    CONSTRAINT `fk_rater_report` FOREIGN KEY (`uid`) REFERENCES `userinfo`(`Id`) ON DELETE CASCADE ON UPDATE CASCADE
)  ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`(
                                 `id` int(11) NOT NULL AUTO_INCREMENT,
                                 `uid` int(11) NOT NULL,
                                 `type` varchar(64) NOT NULL, # 消息类型
                                 `content` varchar(255) NOT NULL, # 消息内容
                                 `is_read` tinyint(1) NULL DEFAULT 0, # 0未读
                                 `sent_date` datetime(0) NOT NULL,
                                 PRIMARY KEY (`id`) USING BTREE,
                                 INDEX `fk_user_notice`(`uid`) USING BTREE,
                                 CONSTRAINT `fk_user_notice` FOREIGN KEY (`uid`) REFERENCES `userinfo`(`Id`) ON DELETE CASCADE ON UPDATE CASCADE
)  ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for achievement
-- ----------------------------
DROP TABLE IF EXISTS `achievement`;
CREATE TABLE `achievement`(
                         `id` int(11) NOT NULL AUTO_INCREMENT,
                         `uid` int(11) NOT NULL,
                         `type` varchar(64) NOT NULL, # 成就
                         `content` varchar(255) NOT NULL, # 成就内容
                         `get_time` datetime(0) NOT NULL,
                         PRIMARY KEY (`id`) USING BTREE,
                         INDEX `fk_user_achievement`(`uid`) USING BTREE,
                         CONSTRAINT `fk_user_achievement` FOREIGN KEY (`uid`) REFERENCES `userinfo`(`Id`) ON DELETE CASCADE ON UPDATE CASCADE
)  ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;
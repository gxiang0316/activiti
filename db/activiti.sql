DROP TABLE IF EXISTS `oa_leave`;
CREATE TABLE `oa_leave` (
  `id` bigint(20) NOT NULL auto_increment,
  `processInstanceId` varchar(255) default NULL,
  `reason` varchar(255) default NULL,
  `userId` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` varchar(64) NOT NULL,
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `password` varchar(32) DEFAULT NULL,
  `realname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `business` varchar(255) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `headpicture` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `createdate` timestamp NULL DEFAULT NULL,
  `updatedate` timestamp NULL DEFAULT NULL,
  `state` tinyint(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
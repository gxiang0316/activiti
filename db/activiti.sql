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
  `user_name` varchar(50) DEFAULT NULL,
  `password` varchar(32) DEFAULT NULL,
  `real_name` varchar(50) DEFAULT NULL,
  `business` varchar(255) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `head_picture` varchar(255) DEFAULT NULL,
  `create_date` timestamp NULL DEFAULT NULL,
  `update_date` timestamp NULL DEFAULT NULL,
  `state` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
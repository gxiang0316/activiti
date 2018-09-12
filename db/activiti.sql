DROP TABLE IF EXISTS `oa_leave`;
CREATE TABLE `oa_leave` (
  `id` bigint(20) NOT NULL auto_increment,
  `processInstanceId` varchar(255) default NULL,
  `reason` varchar(255) default NULL,
  `userId` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
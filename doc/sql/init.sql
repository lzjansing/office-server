CREATE DATABASE swopi;
USE swopi;

CREATE TABLE `convert_log` (
  `id` varchar(36) NOT NULL,
  `my_file_id` varchar(36) DEFAULT NULL,
  `cost` bigint(20) DEFAULT NULL,
  `use_cache` tinyint(4) DEFAULT NULL,
  `convert_cost` bigint(20) DEFAULT NULL,
  `valid` tinyint(2) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `my_file` (
  `id` varchar(36) NOT NULL,
  `name` varchar(36) DEFAULT NULL,
  `ext` varchar(5) NOT NULL,
  `size` int(11) NOT NULL,
  `char_set` varchar(10) DEFAULT NULL,
  `sha256` char(44) NOT NULL,
  `md5` varchar(45) DEFAULT NULL,
  `app_id` varchar(36) DEFAULT NULL,
  `app_file_id` varchar(36) DEFAULT NULL,
  `valid` tinyint(4) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `SAME_FILE` (`ext`,`size`,`sha256`) USING BTREE COMMENT '做同文件检验，如果按ext、length、sha256的顺序匹配成功，则认为该文件已存在。'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `my_file_cache` (
  `id` varchar(36) NOT NULL,
  `valid` tinyint(2) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `my_file_id` varchar(36) NOT NULL,
  `origin_path` varchar(255) DEFAULT NULL,
  `convert_path` varchar(255) DEFAULT NULL,
  `servlet_path` varchar(255) DEFAULT NULL COMMENT '在集群下，记录文件所在服务器的地址。',
  PRIMARY KEY (`id`),
  KEY `index2` (`my_file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
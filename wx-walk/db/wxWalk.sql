
CREATE DATABASE /*!32312 IF NOT EXISTS*/`wxWalk` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `wxWalk`;

/*Table structure for table `tb_content` */

DROP TABLE IF EXISTS `user_location`;
CREATE TABLE `user_location` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `open_id` varchar(255) NOT NULL,
  `lng` varchar(200) NOT NULL,
  `lat` varchar(100) NOT NULL,
  `bd09_lng` varchar(500) NOT NULL,
  `bd09_lat` varchar(500) NOT NULL,
  `location_cn` varchar(255) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

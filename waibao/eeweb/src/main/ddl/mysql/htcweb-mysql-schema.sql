-- DROP DATABASE htcweb
DROP DATABASE IF EXISTS `htcweb`;

-- CREATE DATABASE htcweb
CREATE DATABASE IF NOT EXISTS `htcweb`;

USE `htcweb`;

-- DROP TABLE tphonelist
DROP TABLE IF EXISTS `tphonelist`;

-- CREATE TABLE tphonelist
CREATE TABLE `tphonelist` (
  `listId` smallint(2) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `useless` tinyint(1) UNSIGNED NOT NULL DEFAULT '0',
  PRIMARY KEY(`listId`)
)
ENGINE=INNODB;

-- DROP TABLE thisrecordmonth
DROP TABLE IF EXISTS `thisrecordmonth`;

-- CREATE TABLE thisrecordmonth
CREATE TABLE `thisrecordmonth` (
  `recId` int(4) UNSIGNED NOT NULL AUTO_INCREMENT,
  `equipmentId` smallint(2) UNSIGNED NOT NULL DEFAULT '0',
  `recTime` timestamp DEFAULT '0000-00-00 00:00:00',
  `tempavg` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `tempmax` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `tempmin` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `humiavg` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `humimax` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `humimin` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY(`recId`),
  INDEX `Index_2`(`recTime`, `equipmentId`)
)
ENGINE=INNODB;

-- DROP TABLE tpower
DROP TABLE IF EXISTS `tpower`;

-- CREATE TABLE tpower
CREATE TABLE `tpower` (
  `powerId` smallint(2) UNSIGNED NOT NULL AUTO_INCREMENT,
  `powerName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY(`powerId`)
)
ENGINE=INNODB;

-- DROP TABLE tworkplace
DROP TABLE IF EXISTS `tworkplace`;

-- CREATE TABLE tworkplace
CREATE TABLE `tworkplace` (
  `placeId` smallint(2) UNSIGNED NOT NULL AUTO_INCREMENT,
  `placeName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `useless` tinyint(1) UNSIGNED NOT NULL DEFAULT '0',
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0',
  PRIMARY KEY(`placeId`)
)
ENGINE=INNODB;

-- DROP TABLE tuser
DROP TABLE IF EXISTS `tuser`;

-- CREATE TABLE tuser
CREATE TABLE `tuser` (
  `userId` smallint(2) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `power` smallint(2) UNSIGNED NOT NULL DEFAULT '0',
  `useless` tinyint(1) UNSIGNED NOT NULL DEFAULT '0',
  `placeAStr` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `placeBStr` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `personStr` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY(`userId`)
)
ENGINE=INNODB;

-- DROP TABLE trecord
DROP TABLE IF EXISTS `trecord`;

-- CREATE TABLE trecord
CREATE TABLE `trecord` (
  `temperature` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `humidity` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `recTime` timestamp DEFAULT '0000-00-00 00:00:00',
  `equipmentId` smallint(2) UNSIGNED NOT NULL DEFAULT '0',
  INDEX `Index_2`(`recTime`, `equipmentId`)
)
ENGINE=INNODB;

-- DROP TABLE trecordmin
DROP TABLE IF EXISTS `trecordmin`;

-- CREATE TABLE trecordmin
CREATE TABLE  `trecordmin` (
  `recId` int(4) UNSIGNED NOT NULL AUTO_INCREMENT,
  `equipmentId` smallint(2) unsigned NOT NULL DEFAULT '0',
  `address` smallint(2) unsigned NOT NULL DEFAULT '0',
  `temp` float NOT NULL DEFAULT '0',
  `humi` float NOT NULL DEFAULT '0',
  `dewPoint` float NOT NULL DEFAULT '0',
  `powerV` float NOT NULL DEFAULT '0',        
  `state` smallint(2) unsigned NOT NULL DEFAULT '0',
  `recTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `recLong` bigint(20) unsigned NOT NULL DEFAULT '0',
  `mark` smallint(2) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`recId`),
  KEY `Index_1` (`recLong`,`equipmentId`) 
) 
ENGINE=INNODB;

-- DROP TABLE talarmrec
DROP TABLE IF EXISTS `talarmrec`;

-- CREATE TABLE talarmrec
CREATE TABLE `talarmrec` (
  `alarmId` int(4) UNSIGNED NOT NULL AUTO_INCREMENT,
  `temperature` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `humidity` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `alarmStart` bigint(20) unsigned NOT NULL DEFAULT '0',
  `alarmEnd` bigint(20) unsigned NOT NULL DEFAULT '0',
  `alarmtype` tinyint(1) NOT NULL DEFAULT '0',
  `state` smallint(2) UNSIGNED NOT NULL DEFAULT '0',
  `alarmmode` tinyint(1) NOT NULL DEFAULT '0',
  `equipmentId` smallint(2) UNSIGNED NOT NULL DEFAULT '0',
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `placeName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `normalArea` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `equitype` smallint(2) unsigned NOT NULL DEFAULT '0',
  `gprsFlag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY(`alarmId`),
  INDEX `Index_2`(`alarmStart`, `alarmEnd`, `equipmentId`)
)
ENGINE=INNODB;

-- DROP TABLE tbackuplist
DROP TABLE IF EXISTS `tbackuplist`;

-- CREATE TABLE tbackuplist
CREATE TABLE `tbackuplist` (
  `backId` smallint(2) UNSIGNED NOT NULL AUTO_INCREMENT,
  `fileName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `backtime` timestamp DEFAULT '0000-00-00 00:00:00',
	`remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,  
  PRIMARY KEY(`backId`)
)
ENGINE=INNODB;

-- DROP TABLE thisrecorddailay
DROP TABLE IF EXISTS `thisrecorddailay`;

-- CREATE TABLE thisrecorddailay
CREATE TABLE `thisrecorddailay` (
  `recId` int(4) UNSIGNED NOT NULL AUTO_INCREMENT,
  `equipmentId` smallint(2) UNSIGNED NOT NULL DEFAULT '0',
  `recTime` timestamp DEFAULT '0000-00-00 00:00:00',
  `tempavg` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `tempmax` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `tempmin` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `humiavg` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `humimax` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `humimin` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY(`recId`),
  INDEX `Index_2`(`recTime`, `equipmentId`)
)
ENGINE=INNODB;

-- DROP TABLE tgprsset
DROP TABLE IF EXISTS `tgprsset`;

-- CREATE TABLE tgprsset
CREATE TABLE `tgprsset` (
  `gprsSetId` smallint(2) UNSIGNED NOT NULL AUTO_INCREMENT,
  `numId` smallint(2) UNSIGNED NOT NULL DEFAULT '0',
  `alias` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `mesFormat` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY(`gprsSetId`)
)
ENGINE=INNODB;

-- DROP TABLE tequitype
DROP TABLE IF EXISTS `tequitype`;

-- CREATE TABLE tequitype
CREATE TABLE `tequitype` (
  `tyepId` smallint(2) UNSIGNED NOT NULL AUTO_INCREMENT,
  `typename` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY(`tyepId`)
)
ENGINE=INNODB;

-- DROP TABLE tequipdata
DROP TABLE IF EXISTS `tequipdata`;

-- CREATE TABLE tequipdata
CREATE TABLE `tequipdata` (
  `equipmentId` smallint(2) UNSIGNED NOT NULL AUTO_INCREMENT,
  `address` smallint(2) UNSIGNED NOT NULL DEFAULT '0',
  `equitype` smallint(2) UNSIGNED NOT NULL DEFAULT '0',
  `mark` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0',
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0',
  `tempUp` float NOT NULL DEFAULT '0',
  `tempDown` float NOT NULL DEFAULT '0',
  `tempDev` float NOT NULL DEFAULT '0',
  `humiUp` float NOT NULL DEFAULT '0',
  `humiDown` float NOT NULL DEFAULT '0',
  `humiDev` float NOT NULL DEFAULT '0',
  `equiorder` smallint(2) UNSIGNED NOT NULL DEFAULT '0',
  `placeId` smallint(2) UNSIGNED NOT NULL DEFAULT '0',
  `useless` tinyint(1) UNSIGNED NOT NULL DEFAULT '0',
  `showPower` tinyint(1) UNSIGNED NOT NULL DEFAULT '0',
  `powerType` smallint(2) UNSIGNED NOT NULL DEFAULT '0',
  `dsrsn` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0',
  `showAccess` tinyint(1) UNSIGNED NOT NULL DEFAULT '0',
  `conndata` tinyint(1) UNSIGNED NOT NULL DEFAULT '0',
  PRIMARY KEY(`equipmentId`),
  INDEX `FK_tequipdata_1`(`placeId`),
  CONSTRAINT `FK_tequipdata_1` FOREIGN KEY (`placeId`)
    REFERENCES `tworkplace`(`placeId`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
)
ENGINE=INNODB;

-- DROP TABLE tlog
DROP TABLE IF EXISTS `tlog`;

-- CREATE TABLE tlog
CREATE TABLE  tlog (
	`id` int(4) UNSIGNED NOT NULL AUTO_INCREMENT,
  `logtype` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `logtime` timestamp DEFAULT '0000-00-00 00:00:00',
  `logcontent` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`)
)
ENGINE=INNODB;

-- DROP TABLE tsmsrecord
DROP TABLE IF EXISTS tsmsrecord;

-- CREATE TABLE tsmsrecord
CREATE TABLE  tsmsrecord (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `smsphone` varchar(20) NOT NULL DEFAULT '',
  `smscontent` varchar(200) NOT NULL DEFAULT '',
  `smsrectime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `smstype` tinyint(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `Index_2` (`smsphone`,`smsrectime`)  
) ENGINE=INNODB;

-- DROP TABLE tsysparam
DROP TABLE IF EXISTS `tsysparam`;

-- CREATE TABLE tsysparam
CREATE TABLE  tsysparam (
  `id` int(4) unsigned NOT NULL auto_increment,
  `argsKey` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `argsValue` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=INNODB;
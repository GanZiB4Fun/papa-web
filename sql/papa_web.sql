/*
Navicat MySQL Data Transfer

Source Server         : local-mysql
Source Server Version : 50718
Source Host           : 127.0.0.1:3306
Source Database       : papa_web

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2018-07-06 14:44:52
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for app_advise_message
-- ----------------------------
DROP TABLE IF EXISTS `app_advise_message`;
CREATE TABLE `app_advise_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for app_document
-- ----------------------------
DROP TABLE IF EXISTS `app_document`;
CREATE TABLE `app_document` (
  `doc_id` varchar(64) NOT NULL,
  `title` varchar(255) NOT NULL,
  `content` longtext NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `author_id` varchar(255) DEFAULT NULL,
  `tags` varchar(255) DEFAULT NULL,
  `descri` varchar(255) DEFAULT NULL,
  `source_url` varchar(255) DEFAULT NULL,
  `source` varchar(255) DEFAULT NULL,
  `cover_img` varchar(255) DEFAULT NULL,
  `publish_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `author_name` varchar(255) DEFAULT NULL,
  `author_head_img` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`doc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for app_jianshu_spider_author
-- ----------------------------
DROP TABLE IF EXISTS `app_jianshu_spider_author`;
CREATE TABLE `app_jianshu_spider_author` (
  `spider_author_id` varchar(64) NOT NULL,
  `author_name` varchar(255) DEFAULT NULL,
  `url` varchar(255) NOT NULL,
  `img` varchar(255) DEFAULT NULL,
  `last_crawl_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`spider_author_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for app_user
-- ----------------------------
DROP TABLE IF EXISTS `app_user`;
CREATE TABLE `app_user` (
  `user_id` varchar(255) NOT NULL,
  `nick_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `sex` char(2) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `sign_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

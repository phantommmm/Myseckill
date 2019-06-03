


DROP TABLE IF EXISTS `goods`;

CREATE TABLE `goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goods_name` varchar(512) DEFAULT NULL,
  `goods_title` varchar(512) DEFAULT NULL,
  `goods_img` varchar(512) DEFAULT NULL,
  `goods_detail` text,
  `goods_price` double DEFAULT NULL,
  `goods_stock` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;




DROP TABLE IF EXISTS `miusa_goods`;

CREATE TABLE `miusa_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goods_id` bigint(20) DEFAULT NULL,
  `miusa_price` decimal(5,2) NOT NULL,
  `stock_count` int(11) DEFAULT NULL,
  `start_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `end_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;




DROP TABLE IF EXISTS `miusa_order`;

CREATE TABLE `miusa_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `goods_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique` (`user_id`,`goods_id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8;




DROP TABLE IF EXISTS `order_info`;

CREATE TABLE `order_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `goods_id` bigint(20) DEFAULT NULL,
  `delivery_addr_id` bigint(20) DEFAULT NULL COMMENT '收货地址ID',
  `goods_name` varchar(512) DEFAULT NULL,
  `goods_count` int(11) DEFAULT NULL COMMENT '购买商品的数量',
  `goods_price` double DEFAULT NULL COMMENT '下单时的价格',
  `order_channel` int(11) DEFAULT NULL COMMENT '订单渠道',
  `status` int(11) DEFAULT NULL COMMENT '订单状态，0代表未支付，1代表已支付，2代表已发货，3代表已收货，4代表已退款，5代表已完成。 ',
  `create_date` datetime DEFAULT NULL,
  `pay_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL COMMENT '手机号',
  `nickname` varchar(512) DEFAULT NULL,
  `password` varchar(512) DEFAULT NULL,
  `salt` varchar(256) DEFAULT NULL,
  `head` varchar(512) DEFAULT NULL,
  `register_date` datetime DEFAULT NULL,
  `last_login_date` datetime DEFAULT NULL,
  `login_count` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


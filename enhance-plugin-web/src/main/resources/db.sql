CREATE TABLE `plugin_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) NOT NULL COMMENT '插件名称',
  `class_name` varchar(100) NOT NULL COMMENT '插件通知类名',
  `version` varchar(20) NOT NULL COMMENT '插件版本号',
  `jar_remote_Url` varchar(500) NOT NULL COMMENT '插件下载地址',
  `misc_desc` varchar(200) DEFAULT NULL COMMENT '备注',
  `status` int(1) NOT NULL DEFAULT '1' COMMENT '是否有效（0 无效 1 有效）',
  `create_time` timestamp(3) NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `create_oper_id` bigint(20) NOT NULL  DEFAULT '-1' COMMENT '创建人ID',
  `create_oper_name` varchar(20) NOT NULL COMMENT '创建人姓名',
  `update_time` timestamp(3) NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `update_oper_id` bigint(20) NOT NULL  DEFAULT '-1' COMMENT '更新人ID',
  `update_oper_name` varchar(20) NOT NULL COMMENT '更新人名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='插件信息';

CREATE TABLE `application_plugin_manager` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `application_id` bigint(20) NOT NULL  COMMENT '应用id',
  `plugin_id` bigint(20) NOT NULL COMMENT '插件Id',
  `install_status` int(1) NOT NULL DEFAULT '1' COMMENT '是否安装 1 安装 0 卸载',
  `active_status` int(1) NOT NULL DEFAULT '1' COMMENT '是否激活 1 激活 0 停用',
  `misc_desc` varchar(200) DEFAULT NULL COMMENT '备注',
  `status` int(1) NOT NULL DEFAULT '1' COMMENT '是否有效（0 无效 1 有效）',
  `create_time` timestamp(3) NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `create_oper_id` bigint(20) NOT NULL  DEFAULT '-1' COMMENT '创建人ID',
  `create_oper_name` varchar(20) NOT NULL COMMENT '创建人姓名',
  `update_time` timestamp(3) NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `update_oper_id` bigint(20) NOT NULL  DEFAULT '-1' COMMENT '更新人ID',
  `update_oper_name` varchar(20) NOT NULL COMMENT '更新人名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='用户插件管理';


CREATE TABLE `application_manager` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `application_name` varchar(50) NOT NULL COMMENT '应用名称',
  `application_ip_address` varchar(20) NOT NULL COMMENT '应用地址',
  `misc_desc` varchar(200) DEFAULT NULL COMMENT '备注',
  `status` int(1) NOT NULL DEFAULT '1' COMMENT '是否有效（0 无效 1 有效）',
  `create_time` timestamp(3) NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `create_oper_id` bigint(20) NOT NULL DEFAULT '-1' COMMENT '创建人ID',
  `create_oper_name` varchar(20) NOT NULL COMMENT '创建人姓名',
  `update_time` timestamp(3) NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  `update_oper_id` bigint(20) NOT NULL DEFAULT '-1' COMMENT '更新人ID',
  `update_oper_name` varchar(20) NOT NULL COMMENT '更新人名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `IDX_1` (`application_ip_address`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='应用管理表';


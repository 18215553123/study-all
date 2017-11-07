DROP TABLE IF EXISTS `add_dish_group_temp`;
CREATE TABLE `add_dish_group_temp` (
  `group_id` VARCHAR(32) NOT NULL,
  `ent_id` VARCHAR(32) DEFAULT NULL,
  `group_name` VARCHAR(64) DEFAULT NULL,
  `group_type` INT(11) DEFAULT '0',
  `sort` INT(11) DEFAULT '0',
  PRIMARY KEY (`group_id`)
) ENGINE=INNODB DEFAULT CHARSET=gbk;


/* 第1步，创建租户升级存储过程 */
DROP PROCEDURE IF EXISTS `upgradeTenantDB`;
DELIMITER $$
CREATE PROCEDURE `upgradeTenantDB`()
	BEGIN
	DECLARE done INT DEFAULT 0;
	DECLARE a,c CHAR(64);
	DECLARE e VARCHAR(100)DEFAULT '0';
	DECLARE groupId VARCHAR(32);
	DECLARE group1 VARCHAR(32) DEFAULT '主食';
	DECLARE group2 VARCHAR(32) DEFAULT '餐位';
	/* 第一次升级使用下面第一行，如果有异常租户库，以后每次调用下面第二行并注解下面第一行*/
	DECLARE curTenant CURSOR FOR SELECT ent_id, catalog_name FROM tenant WHERE is_activated = 1;
	/*DECLARE curTenant CURSOR FOR SELECT ent_id, catalog_name FROM helpdesk.db_upgrade_error_log;*/

	/*DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done = 1;*/
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	DECLARE CONTINUE HANDLER FOR SQLSTATE '42S02' SET e ='表不存在|未知表';/*已测试*/
	DECLARE CONTINUE HANDLER FOR SQLSTATE '42S01' SET e ='表已存在';/*已测试*/
	DECLARE CONTINUE HANDLER FOR SQLSTATE '42S21' SET e ='重复列名';/*已测试*/
	DECLARE CONTINUE HANDLER FOR SQLSTATE '42S22' SET e ='列不存在';

	DECLARE CONTINUE HANDLER FOR SQLSTATE '23000' SET e ='重复插入主键|外键约束|列不能为空|列不明确|其它情况出错';/*已测试*/
	DECLARE CONTINUE HANDLER FOR SQLSTATE '42000' SET e ='库不存在|拒绝访问库|语句中有sum函数和相同语句中的列|重复键名称|查询为空|非唯一的表/别名|定义了多个主键|键列在表中不存在|不正确的数据库名|不正确的表名|不正确的列名|其它情况出错';

	DECLARE CONTINUE HANDLER FOR SQLWARNING SET e='发生SQLWARNING';

	DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET e='发生SQLEXCEPTION';/*已测试*/

		OPEN curTenant;
				DELETE FROM `db_upgrade_error_log`;
				DELETE FROM `db_upgrade_log`;
				DELETE FROM `add_dish_group_temp`;
				REPEAT
					FETCH curTenant INTO a, c;
					IF NOT done THEN
					IF EXISTS(SELECT 1 FROM information_schema.schemata WHERE schema_name=c) THEN
						/*增加字段*/
						SET @sql = CONCAT("ALTER TABLE ",c,".`t_dish_group` ADD COLUMN group_type INT DEFAULT 0 COMMENT '大分类：0菜品，1主食，2餐位'");
						PREPARE stmt FROM @sql;
						EXECUTE stmt;
						DEALLOCATE PREPARE stmt;

						SET @sql = CONCAT("ALTER TABLE ",c,".`t_order_dishlist` ADD COLUMN group_type INT DEFAULT 0 COMMENT '大分类：0菜品，1主食，2餐位'");
						PREPARE stmt FROM @sql;
						EXECUTE stmt;
						DEALLOCATE PREPARE stmt;

						SET @sql = CONCAT("ALTER TABLE ",c,".`t_order_reduce_dishlist` ADD COLUMN group_type INT DEFAULT 0 COMMENT '大分类：0菜品，1主食，2餐位'");
						PREPARE stmt FROM @sql;
						EXECUTE stmt;
						DEALLOCATE PREPARE stmt;
					SET e='0';

					SET @old_group_id = '0';
					/* 记录操作开始日志 */
					SET @LOG = CONCAT('INSERT INTO db_upgrade_log (ent_id, catalog_name, start_time) values ("',a,'","',c,'",now());');
					PREPARE stmt FROM @LOG;
					EXECUTE stmt;

					/*主食-租户库 有则更新 无则添加*/
					SET @sql = CONCAT("SELECT id into @old_group_id FROM ",c,".`t_dish_group` WHERE dish_group_name='",group1,"'");
					PREPARE stmt FROM @sql;
					EXECUTE stmt;
					DEALLOCATE PREPARE stmt;
					SET groupId = REPLACE(UUID(),'-','');
					IF @old_group_id != '0' THEN
						/*如果商家已有此分类 则更新id sort group_type*/
						SET @sql = CONCAT("UPDATE ",c,".`t_dish_group` SET sort=-2000,group_type=1,update_time=now() WHERE dish_group_name='",group1,"'");
						PREPARE stmt FROM @sql;
						EXECUTE stmt;
						DEALLOCATE PREPARE stmt;

					ELSE
						/*添加主食-租户库*/
						SET @sql = CONCAT("INSERT INTO ",c,".`t_dish_group` (`id`, `dish_group_name`, `create_time`, `update_time`, `sort`, `group_type`) VALUES ('",groupId,"', '",group1,"', now(), now(), '-2000', '1')");
						PREPARE stmt FROM @sql;
						EXECUTE stmt;
						DEALLOCATE PREPARE stmt;
					END IF;

					/*添加主食-临时表*/
					SET @sql = CONCAT("INSERT INTO `add_dish_group_temp` (`group_id`, `ent_id`, `group_name`, `group_type`, `sort`) VALUES ('",groupId,"', '",a,"', '",group1,"', '1', '-2000')");
					PREPARE stmt FROM @sql;
					EXECUTE stmt;
					DEALLOCATE PREPARE stmt;

					/*餐位-租户库 有则更新 无则添加*/
					SET groupId = REPLACE(UUID(),'-','');
					SET @old_group_id='0';
					SET @sql = CONCAT("SELECT id into @old_group_id FROM ",c,".`t_dish_group` WHERE dish_group_name='",group2,"'");
					PREPARE stmt FROM @sql;
					EXECUTE stmt;
					DEALLOCATE PREPARE stmt;
					IF @old_group_id !='0' THEN
						/*如果商家已有此分类 则更新id sort group_type*/
						SET @sql = CONCAT("UPDATE ",c,".`t_dish_group` SET sort=-1000,group_type=2,update_time=now() WHERE dish_group_name='",group2,"'");
						PREPARE stmt FROM @sql;
						EXECUTE stmt;
						DEALLOCATE PREPARE stmt;
					ELSE
						/*添加餐位-租户库*/
						SET @sql = CONCAT("INSERT INTO ",c,".`t_dish_group` (`id`, `dish_group_name`, `create_time`, `update_time`, `sort`, `group_type`) VALUES ('",groupId,"', '",group2,"', now(), now(), '-1000', '2')");
						PREPARE stmt FROM @sql;
						EXECUTE stmt;
						DEALLOCATE PREPARE stmt;
					END IF;

					/*添加餐位-临时表*/
					SET @sql = CONCAT("INSERT INTO `add_dish_group_temp` (`group_id`, `ent_id`, `group_name`, `group_type`, `sort`) VALUES ('",groupId,"', '",a,"', '",group2,"', '2', '-1000')");
					PREPARE stmt FROM @sql;
					EXECUTE stmt;
					DEALLOCATE PREPARE stmt;

					/* 升级脚本结束 */

					/* 记录日志，记录异常数据库 */
					IF e!='0' THEN
						INSERT INTO db_upgrade_error_log VALUES(a,c);
						UPDATE db_upgrade_log SET error_time = NOW(),error_message= e ,end_time = NOW() WHERE ent_id = a;
					ELSE
						SET @LOG = CONCAT('update db_upgrade_log set end_time = now() where ent_id = "',a,'";');
						PREPARE stmt FROM @LOG;
						EXECUTE stmt;
					END IF;
					SET e='0';
					END IF;
					END IF;
				UNTIL done END REPEAT;
		CLOSE curTenant;

	END$$
DELIMITER ;

/**第2步，执行存储过程*/
CALL `upgradeTenantDB`();

/**第3步，销毁存储过程*/
DROP PROCEDURE IF EXISTS `upgradeTenantDB`;

ALTER TABLE dish_group ADD COLUMN group_type INT DEFAULT 0 COMMENT '大分类：0菜品，1主食，2餐位' ;
ALTER TABLE `order_dishlist` ADD COLUMN group_type INT DEFAULT 0 COMMENT '大分类：0菜品，1主食，2餐位' ;
ALTER TABLE `order_reduce_dishlist` ADD COLUMN group_type INT DEFAULT 0 COMMENT '大分类：0菜品，1主食，2餐位' ;
/* 第1步，创建租户升级存储过程 */
DROP PROCEDURE IF EXISTS `insertGroup`;
DELIMITER $$
CREATE PROCEDURE `insertGroup`()
	BEGIN
	DECLARE done INT DEFAULT 0;
	DECLARE entId,groupId CHAR(64);
	DECLARE groupName VARCHAR(64);
	DECLARE groupType INT DEFAULT 0;
	DECLARE s INT DEFAULT 0;
	DECLARE e VARCHAR(100)DEFAULT '0';

	DECLARE addGroup CURSOR FOR SELECT ent_id,group_id,group_name,group_type,sort FROM add_dish_group_temp;

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
		OPEN addGroup;
				DELETE FROM `db_upgrade_error_log`;
				DELETE FROM `db_upgrade_log`;
				REPEAT
					FETCH addGroup INTO entId, groupId, groupName, groupType, s;


					IF NOT done THEN
					SET @mirc_group_id = '0';
					/* 记录操作开始日志 */
					SET @LOG = CONCAT('INSERT INTO db_upgrade_log (ent_id, catalog_name, start_time) values ("',groupId,'","',entId,'",now());');
					PREPARE stmt FROM @LOG;
					EXECUTE stmt;

					/*主食-微官网 有则更新 无则添加*/
					SET @sql = CONCAT("SELECT id into @mirc_group_id FROM `dish_group` WHERE ent_id='",entId,"' AND dish_group_name='",groupName,"'");
					PREPARE stmt FROM @sql;
					EXECUTE stmt;
					DEALLOCATE PREPARE stmt;
					IF @mirc_group_id != '0' THEN
						SET @sql = CONCAT("UPDATE `dish_group` SET sort=",s,",group_type=",groupType,",update_time=now() WHERE ent_id='",entId,"' AND id='",@mirc_group_id,"'");
						PREPARE stmt FROM @sql;
						EXECUTE stmt;
						DEALLOCATE PREPARE stmt;
					ELSE
						/*添加主食-微官网*/
						SET @sql = CONCAT("INSERT INTO `dish_group`(id,ent_id,dish_group_name,sort,group_type,create_time,update_time) SELECT group_id,ent_id,group_name,sort,group_type,NOW(),NOW() FROM `add_dish_group_temp` WHERE group_id='",groupId,"'");
						PREPARE stmt FROM @sql;
						EXECUTE stmt;
						DEALLOCATE PREPARE stmt;
					END IF;

					/* 升级脚本结束 */

					/* 记录日志，记录异常数据库 */
					IF e!='0' THEN
						INSERT INTO db_upgrade_error_log VALUES(groupId,groupName);
						UPDATE db_upgrade_log SET error_time = NOW(),error_message= e ,end_time = NOW() WHERE ent_id = groupId;
					ELSE
						SET @LOG = CONCAT('update db_upgrade_log set end_time = now() where ent_id = "',groupId,'";');
						PREPARE stmt FROM @LOG;
						EXECUTE stmt;
					END IF;

					SET e='0';
					END IF;

				UNTIL done END REPEAT;
		CLOSE addGroup;

	END$$
DELIMITER ;


/**第2步，执行存储过程*/
CALL `insertGroup`();


/**第3步，销毁存储过程*/
DROP PROCEDURE IF EXISTS `insertGroup`;


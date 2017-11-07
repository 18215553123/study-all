/* 第1步，创建租户升级存储过程 */
DROP PROCEDURE IF EXISTS `upgradeTenantDB`;
DELIMITER $$
CREATE PROCEDURE `upgradeTenantDB`()
	BEGIN
	DECLARE done INT DEFAULT 0;
	DECLARE a,c CHAR(64);
	DECLARE e VARCHAR(100)DEFAULT '0';
	/* 第一次升级使用下面第一行，如果有异常租户库，以后每次调用下面第二行并注解下面第一行*/
	DECLARE curTenant CURSOR FOR SELECT ent_id, catalog_name FROM tenant WHERE is_activated = 1;
	/*DECLARE curTenant CURSOR FOR SELECT ent_id, catalog_name FROM helpdesk.db_upgrade_error_log;*/

	DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done = 1;
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
				REPEAT
					FETCH curTenant INTO a, c;

					IF NOT done THEN
					/* 记录操作开始日志 */
					SET @LOG = CONCAT('INSERT INTO db_upgrade_log (ent_id, catalog_name, start_time) values ("',a,'","',c,'",now());');
					PREPARE stmt FROM @LOG;
					EXECUTE stmt;

					SET @mst = CONCAT('ALTER TABLE  ',c,'.t_customers_collect  ADD COLUMN last_groupbuy_time datetime COMMENT "最后一次团购时间";');
					PREPARE stmt1 FROM @mst;
					EXECUTE stmt1;

					SET @mst = CONCAT('ALTER TABLE  ',c,'.t_customers_collect  ADD COLUMN groupbuy_count INT default 0 COMMENT "团购次数";');
					PREPARE stmt1 FROM @mst;
					EXECUTE stmt1;


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

				UNTIL done END REPEAT;
		CLOSE curTenant;

	END$$
DELIMITER ;




/**第2步，执行存储过程*/
CALL `upgradeTenantDB`();





/**第3步，销毁存储过程*/
DROP PROCEDURE IF EXISTS `upgradeTenantDB`;

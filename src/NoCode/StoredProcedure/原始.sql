DELIMITER $$

USE `all_basic_data`$$

DROP PROCEDURE IF EXISTS `GetBasicData`$$

CREATE DEFINER=`root`@`%` PROCEDURE `GetBasicData`()
BEGIN
	DECLARE ent_id_cur VARCHAR(64);
	DECLARE ent_name_cur VARCHAR(64);
	DECLARE address_cur VARCHAR(64);
	DECLARE open_time_cur DATE;
	DECLARE activated_time_cur DATE;
	DECLARE area_id_cur VARCHAR(32);
	DECLARE catalog_name_cur VARCHAR(32);
	DECLARE e VARCHAR(100)DEFAULT '0';
	SELECT sync_date INTO @first_day_max  FROM data_sync_log;

		SET @LAST_DAY = DATE_FORMAT(NOW(),'%Y-%m-%d');

			  SET @first_day = @first_day_max;
			  SET @first_day = DATE_ADD(@first_day,INTERVAL 1 DAY);/*开始时间为最大时间的后一天*/
			WHILE @first_day<@LAST_DAY DO
	 				SET @statistic_time = @first_day;  /* 当前日期 */
					SET @first_day = DATE_ADD(@first_day,INTERVAL 1 DAY);
	 BEGIN
	/*依赖企业信息，使用其中的企业ID*/
		DECLARE done INT DEFAULT 0;

	DECLARE curEnt CURSOR FOR SELECT ent_id,ent_name,created_time,address,catalog_name,activated_time,areaid FROM `helpdesk_g3_o_bzcy`.tenant WHERE  `activated_time` < @first_day AND ( STATUS='1'  OR (STATUS != '1' AND DATE_FORMAT(status_change_time,'%Y-%m-%d') >= @statistic_time )) ;
	DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done = 1;
	DECLARE CONTINUE HANDLER FOR SQLSTATE '42S02' SET e ='表不存在|未知表';/*已测试*/
	DECLARE CONTINUE HANDLER FOR SQLSTATE '42S01' SET e ='表已存在';/*已测试*/
	DECLARE CONTINUE HANDLER FOR SQLSTATE '42S21' SET e ='重复列名';/*已测试*/
	DECLARE CONTINUE HANDLER FOR SQLSTATE '42S22' SET e ='列不存在';
	DECLARE CONTINUE HANDLER FOR SQLSTATE '23000' SET e ='重复插入主键|外键约束|列不能为空|列不明确|其它情况出错';/*已测试*/
	DECLARE CONTINUE HANDLER FOR SQLSTATE '42000' SET e ='库不存在|拒绝访问库|语句中有sum函数和相同语句中的列|重复键名称|查询为空|非唯一的表/别名|定义了多个主键|键列在表中不存在|不正确的数据库名|不正确的表名|不正确的列名|其它情况出错';
	DECLARE CONTINUE HANDLER FOR SQLWARNING SET e='发生SQLWARNING';

	DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET e='发生SQLEXCEPTION';/*已测试*/


	OPEN curEnt;
	REPEAT
		FETCH curEnt INTO ent_id_cur,ent_name_cur,open_time_cur,address_cur,catalog_name_cur,activated_time_cur,area_id_cur;
		IF NOT done THEN
		SET @create_contact_count_a=0;
		SET @edit_contact_count_a=0;
		SET @delete_contact_count_a=0;
		SET @contact_count_a=0;
					/*写入优惠券和抽奖券码*/
					SET @sql=CONCAT("INSERT INTO all_basic_data.g1_coupon (`ent_id`,`phone`,`coupon_type`,`receive_date`,`market_id`,`count`) SELECT '",ent_id_cur,"',t.phone,CASE WHEN ttt.template_type=7 THEN 3 ELSE 1 END '1',t.create_time,tt.id,COUNT(t.phone) FROM ",catalog_name_cur,".t_act_key t ,  ",catalog_name_cur,".t_marketing_activity tt , ",catalog_name_cur,".t_marketing_wap ttt WHERE t.marketing_activity_id=tt.id AND tt.marketingwap_id=ttt.id AND t.key_type > '1' AND DATE_FORMAT(t.create_time,\'%Y-%m-%d\') = @statistic_time GROUP BY t.phone,ttt.template_type,tt.id");
          PREPARE stmt FROM @sql;
          EXECUTE stmt;
          DEALLOCATE PREPARE stmt;
					/*写入返券记录*/
		  SET @sql=CONCAT("INSERT INTO g1_coupon (`ent_id`,`phone`,`coupon_type`,`receive_date`,`count`,`market_id`) SELECT '",ent_id_cur,"',k.phone,k.key_type,k.create_time,COUNT(k.id),r.return_coupon_id FROM ",
		  catalog_name_cur,".t_act_key k ,",catalog_name_cur,".t_return_coupon_item_detail r  WHERE k.id=r.act_key_id and k.key_type=2 AND DATE_FORMAT(k.create_time,\'%Y-%m-%d\') = @statistic_time group by k.phone,r.return_coupon_id;");
          PREPARE stmt FROM @sql;
          EXECUTE stmt;
          DEALLOCATE PREPARE stmt;

          /*用反券记录*/
          SET @sql=CONCAT("INSERT INTO g1_use_coupon_record (`ent_id`,`phone`,`coupon_type`,`use_date`,`use_type`,`count`,`market_id`) SELECT  '",
		  ent_id_cur,"',t.phone,t.key_type,t.use_time,tt.verify_type,COUNT(t.phone),r.return_coupon_id FROM ",catalog_name_cur,".t_act_key t, ",catalog_name_cur,
		  ".t_act_key_record tt ,",catalog_name_cur,".t_return_coupon_item_detail r WHERE tt.key_status='2' AND t.key_type > '1' and t.id=tt.act_key_id
		  AND t.key_type=2 AND t.id=r.act_key_id AND DATE_FORMAT(t.use_time,\'%Y-%m-%d\') = @statistic_time GROUP BY t.phone,tt.verify_type,r.return_coupon_id; ");
          PREPARE stmt FROM @sql;
          EXECUTE stmt;
          DEALLOCATE PREPARE stmt;

          /*优惠券和抽奖券使用记录*/
          SET @sql=CONCAT("INSERT INTO all_basic_data.g1_use_coupon_record (`ent_id`,`phone`,`coupon_type`,`use_date`,`use_type`,`count`,`market_id`) SELECT  '",ent_id_cur,"',t.phone,CASE WHEN tttt.template_type=7 THEN 3 ELSE 1 END '1',t.use_time,tt.verify_type,COUNT(t.phone),ttt.id  FROM ",catalog_name_cur,".t_act_key t,",catalog_name_cur,".t_act_key_record tt,",catalog_name_cur,".t_marketing_activity ttt,",catalog_name_cur,".t_marketing_wap tttt  WHERE tt.key_status='2' and t.id=tt.act_key_id AND t.marketing_activity_id=ttt.id AND ttt.marketingwap_id=tttt.id AND DATE_FORMAT(t.use_time,\'%Y-%m-%d\') = @statistic_time GROUP BY t.phone,tttt.template_type,tt.verify_type,ttt.id ;");
          PREPARE stmt FROM @sql;
          EXECUTE stmt;
          DEALLOCATE PREPARE stmt;

          /*通话记录*/
          SET @sql=CONCAT("INSERT INTO  all_basic_data.g1_boundlog SELECT '",ent_id_cur,"',OPPSITE_NUMBER,CALL_TYPE,(COUNT(OPPSITE_NUMBER)-SUM(IS_ANSWERED)),SUM(DURATION),COUNT(OPPSITE_NUMBER),CREATE_TIME FROM ",catalog_name_cur,".t_boundlog WHERE DATE_FORMAT(CREATE_TIME,\'%Y-%m-%d\') = @statistic_time GROUP BY OPPSITE_NUMBER,CALL_TYPE,IS_ANSWERED,DURATION;");
          PREPARE stmt FROM @sql;
          EXECUTE stmt;
          DEALLOCATE PREPARE stmt;

          /*新建联系人次数*/
          SET @SQL=CONCAT("SELECT COUNT(*) into @create_contact_count_a FROM ",catalog_name_cur,".t_contacts WHERE `create_time`>UNIX_TIMESTAMP(DATE_FORMAT(@statistic_time,\'%Y-%m-%d\'))*1000 AND `create_time`<=UNIX_TIMESTAMP(DATE_FORMAT(@first_day,\'%Y-%m-%d\'))*1000");
          PREPARE stmt FROM @sql;
          EXECUTE stmt;
          DEALLOCATE PREPARE stmt;

          /*编辑联系人次数*/
          SET @SQL=CONCAT("SELECT COUNT(*) into @edit_contact_count_a FROM ",catalog_name_cur,".t_contacts WHERE `timestamp`>UNIX_TIMESTAMP(DATE_FORMAT(@statistic_time,\'%Y-%m-%d\'))*1000 AND `timestamp`<=UNIX_TIMESTAMP(DATE_FORMAT(@first_day,\'%Y-%m-%d\'))*1000 and is_deleted='0'");
          PREPARE stmt FROM @sql;
          EXECUTE stmt;
          DEALLOCATE PREPARE stmt;

          /*删除联系人次数*/
           SET @SQL=CONCAT("SELECT COUNT(*) into @delete_contact_count_a FROM ",catalog_name_cur,".t_contacts WHERE `timestamp`>UNIX_TIMESTAMP(DATE_FORMAT(@statistic_time,\'%Y-%m-%d\'))*1000 AND `timestamp`<=UNIX_TIMESTAMP(DATE_FORMAT(@first_day,\'%Y-%m-%d\'))*1000 and is_deleted='1'");
          PREPARE stmt FROM @sql;
          EXECUTE stmt;
          DEALLOCATE PREPARE stmt;

          SET @contact_count_a=@create_contact_count_a+@edit_contact_count_a+@delete_contact_count_a;
          IF @contact_count_a > 0 THEN
                    /*会员编辑次数*/
          SET @sql=CONCAT("INSERT INTO  all_basic_data.g1_contacts SELECT '",ent_id_cur,"',@create_contact_count_a,@edit_contact_count_a,@delete_contact_count_a,@statistic_time;");
          PREPARE stmt FROM @sql;
          EXECUTE stmt;
          DEALLOCATE PREPARE stmt;
          END IF ;


          /*记录会员电话*/
          SET @sql=CONCAT("INSERT INTO  all_basic_data.g1_contacts_phone SELECT '",ent_id_cur,"',t.phone_number,@statistic_time FROM ",catalog_name_cur,".t_contacts_phone t ,",catalog_name_cur,".t_contacts tt  WHERE t.contacts_id=tt.id AND ((`create_time`>UNIX_TIMESTAMP(DATE_FORMAT(@statistic_time,\'%Y-%m-%d\'))*1000 AND `create_time`<=UNIX_TIMESTAMP(DATE_FORMAT(@first_day,\'%Y-%m-%d\'))*1000) OR (`timestamp`>UNIX_TIMESTAMP(DATE_FORMAT(@statistic_time,\'%Y-%m-%d\'))*1000 AND `timestamp`<=UNIX_TIMESTAMP(DATE_FORMAT(@first_day,\'%Y-%m-%d\'))*1000))");
          PREPARE stmt FROM @sql;
          EXECUTE stmt;
          DEALLOCATE PREPARE stmt;

          /*营销活动，先删除该企业的营销*/
          SET @sql=CONCAT("DELETE FROM all_basic_data.g1_marketing_activity WHERE ent_id='",ent_id_cur,"';");
          PREPARE stmt FROM @sql;
          EXECUTE stmt;
          DEALLOCATE PREPARE stmt;

          /*营销活动*/
          SET @sql=CONCAT("INSERT INTO all_basic_data.g1_marketing_activity SELECT '",ent_id_cur,"',t.id,CASE WHEN tt.template_type=7 THEN 3 ELSE 1 END '1',t.use_start_time,t.use_end_time,tt.title FROM ",catalog_name_cur,".t_marketing_activity t , ",catalog_name_cur,".t_marketing_wap tt  WHERE tt.audit_status = 101 AND t.`marketingwap_id`=tt.id ;");
          PREPARE stmt FROM @sql;
          EXECUTE stmt;
          DEALLOCATE PREPARE stmt;

          /*返券活动*/
          SET @sql=CONCAT("INSERT INTO all_basic_data.g1_marketing_activity SELECT '",ent_id_cur,"',coupon_id,'2',create_time,deadline_date,coupon_content FROM ",catalog_name_cur,".t_return_coupon   WHERE audit_status = 101 and is_deleted=0  ;");
          PREPARE stmt FROM @sql;
          EXECUTE stmt;
          DEALLOCATE PREPARE stmt;

          /*预订数据*/
          SET @sql=CONCAT("INSERT INTO all_basic_data.g1_order SELECT '",ent_id_cur,"',linkman_tel,COUNT(*),create_time FROM ",catalog_name_cur,".t_order WHERE DATE_FORMAT(create_time,\'%Y-%m-%d\')=@statistic_time GROUP BY linkman_tel;");
          PREPARE stmt FROM @sql;
          EXECUTE stmt;
          DEALLOCATE PREPARE stmt;

          /*排队数据*/
          SET @sql=CONCAT("INSERT INTO all_basic_data.g1_queue SELECT '",ent_id_cur,"',phone_num,COUNT(*),create_time FROM ",catalog_name_cur,".t_queuers WHERE DATE_FORMAT(create_time,\'%Y-%m-%d\')=@statistic_time GROUP BY phone_num;");
          PREPARE stmt FROM @sql;
          EXECUTE stmt;
          DEALLOCATE PREPARE stmt;

          /*消费记录*/
          SET @sql=CONCAT("INSERT INTO all_basic_data.g1_consume SELECT '",ent_id_cur,"',linkman_tel,custom_type,SUM(pay_amount),SUM(custom_count),COUNT(*),@statistic_time FROM ",catalog_name_cur,".t_service WHERE  DATE_FORMAT(create_time,\'%Y-%m-%d\')=@statistic_time GROUP BY linkman_tel,custom_type");
          PREPARE stmt FROM @sql;
          EXECUTE stmt;
          DEALLOCATE PREPARE stmt;

          /*潜在联系人*/
          SET @sql=CONCAT("INSERT INTO all_basic_data.g1_potential_customers SELECT '",ent_id_cur,"',phone_number,reference_type,create_time FROM ",catalog_name_cur,".t_potential_customers_collect WHERE DATE_FORMAT(create_time,\'%Y-%m-%d\')=@statistic_time;");
          PREPARE stmt FROM @sql;
          EXECUTE stmt;
          DEALLOCATE PREPARE stmt;

        					IF e!='0' THEN
						INSERT INTO all_basic_data.db_upgrade_error_log VALUES(ent_id_cur,catalog_name_cur);
						UPDATE all_basic_data.db_upgrade_log SET error_time = NOW(),error_message= e ,end_time = NOW() WHERE ent_id = ent_id_cur;
					ELSE
						SET @LOG = CONCAT('update all_basic_data.db_upgrade_log set end_time = now() where ent_id = "',ent_id_cur,'";');
						PREPARE stmt FROM @LOG;
						EXECUTE stmt;
					END IF;

					SET e='0';

		END IF;
	UNTIL done END REPEAT;
	CLOSE curEnt;
	END;
	UPDATE all_basic_data.`data_sync_log` SET `sync_date`=@statistic_time ,over_time=NOW();
	END WHILE;
	UPDATE all_basic_data.`data_sync_log` SET `is_ok`='yes' ;
END$$

DELIMITER ;
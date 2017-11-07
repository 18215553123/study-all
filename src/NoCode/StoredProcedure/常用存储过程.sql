--计算分隔长度
DELIMITER $$ 
DROP FUNCTION IF EXISTS `func_split_TotalLength` $$ 
 CREATE  FUNCTION `func_split_TotalLength`(f_string VARCHAR(1000),f_delimiter VARCHAR(5)) RETURNS INT(11) 
  BEGIN 
      RETURN 1+(LENGTH(f_string) - LENGTH(REPLACE(f_string,f_delimiter,''))); 
  END$$ 
 DELIMITER; 
 
--获取当前分隔位置的值
 DELIMITER $$ 
 DROP FUNCTION IF EXISTS `func_split` $$ 
CREATE FUNCTION `func_split` (
  f_string VARCHAR (1000),
  f_delimiter VARCHAR (5),
  f_order INT
) RETURNS VARCHAR (255) CHARSET utf8 
BEGIN
  DECLARE result VARCHAR (255) DEFAULT '' ;
  SET result = REVERSE(
    SUBSTRING_INDEX(
      REVERSE(
        SUBSTRING_INDEX(f_string, f_delimiter, f_order)
      ),
      f_delimiter,
      1
    )
  ) ;
  RETURN result ;
END $$

 
 DELIMITER; 
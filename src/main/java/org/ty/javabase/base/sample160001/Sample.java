package org.ty.javabase.base.sample160001;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <dl>
 * <dt>study-all</dt>
 * <dd>Description: 面试基础</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: </dd>
 * <dd>CreateDate: 2017年11月07日</dd>
 * </dl>
 *
 * @author Tangyun
 */
public class Sample {

    public void breakFors(){
        int[] nums = new int[3];
        //type1
        ok:for (int i=0;i<nums.length;i++){
            for (int j=0;j<nums.length;j++){
                break ok;
            }
        }
        //type2
        boolean flag = false;
        for (int i=0;i<nums.length&&!flag;i++){
            for (int j=i;j<nums.length;j++){
                flag=true;
                break;
            }
        }
    }

    public static void main(String[] args) {
        String dateStr = "20160704145727";
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(dateStr);
            System.out.println(format1.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}

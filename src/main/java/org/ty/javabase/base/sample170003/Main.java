package org.ty.javabase.base.sample170003;

import java.util.List;

/**
 * <dl>
 * <dt>study-all</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: 成都微积分科技有限公司</dd>
 * <dd>CreateDate: 2017年11月09日</dd>
 * </dl>
 *
 * @author Tangyun
 */
public class Main {

    public static void main(String[] args) {
        //单引号
//        int x = 1 + '字符';编译无法通过
        int c = 1 + 'a';
        List<String> strs = null;
        for (String s : strs) {
            System.out.println(s);
        }
    }
}

package org.ty.javabase.base.sample170002;

/**
 * <dl>
 * <dt>study-all</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: 成都微积分科技有限公司</dd>
 * <dd>CreateDate: 2017年11月08日</dd>
 * </dl>
 *
 * @author Tangyun
 */
public abstract class AbstractDemo {

    abstract int get();

    abstract void get(String s);

    int get(String s, String i) {
        return s.indexOf(i);
    }
}

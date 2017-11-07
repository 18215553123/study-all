package org.ty.cglib.sample160001.service;

import net.sf.cglib.proxy.Enhancer;
import org.ty.cglib.sample160001.proxy.MyCglibProxy;

/**
 * <dl>
 * <dt>study-all</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: </dd>
 * <dd>CreateDate: 2017年11月07日</dd>
 * </dl>
 *
 * @author Tangyun
 */
public class Factory {

    private static BookServiceBean bookServiceBean = new BookServiceBean();

    private Factory(){

    }
    //    public static BookServiceBean getInstance(){
//        return bookServiceBean;
//    }
    public static BookServiceBean getProxyInstance(MyCglibProxy proxy){
        Enhancer en = new Enhancer();
        en.setSuperclass(BookServiceBean.class);
        en.setCallback(proxy);
        return (BookServiceBean) en.create();
    }
}

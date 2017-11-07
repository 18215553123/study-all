package org.ty.cglib.sample160001.client;

import org.ty.cglib.sample160001.proxy.MyCglibProxy;
import org.ty.cglib.sample160001.service.BookServiceBean;
import org.ty.cglib.sample160001.service.Factory;

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
public class Client {

    public static void main(String[] args) {
//        BookServiceBean bookServiceBean = Factory.getInstance();
//        doMethod(bookServiceBean);
        doMethod1();
    }

    public static void doMethod(BookServiceBean bookServiceBean){
        bookServiceBean.create();
    }

    public static void doMethod1(){
        BookServiceBean serviceBean = Factory.getProxyInstance(new MyCglibProxy("张三"));
        serviceBean.create();
        BookServiceBean serviceBean1 = Factory.getProxyInstance(new MyCglibProxy("11"));
        serviceBean1.create();
    }
}

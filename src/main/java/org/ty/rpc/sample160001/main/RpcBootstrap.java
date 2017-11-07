package org.ty.rpc.sample160001.main;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * <dl>
 * <dt>org.ty.MainAndTest</dt>
 * <dd>Description: 发布服务 主方法</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: </dd>
 * <dd>CreateDate: 2016/3/4</dd>
 * </dl>
 *
 * @author tangyun
 */
public class RpcBootstrap {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("ApplicationContext.xml");
    }
}

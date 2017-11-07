package org.ty.rpc.sample160001.main;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.ty.rpc.sample160001.server.RpcProxy;
import org.ty.rpc.sample160001.service.IHelloService;

/**
 * <dl>
 * <dt>org.ty.MainAndTest</dt>
 * <dd>Description: 测试类</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: </dd>
 * <dd>CreateDate: 2016/3/4</dd>
 * </dl>
 *
 * @author tangyun
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class HelloServiceTest {

    @Autowired
    private RpcProxy rpcProxy;

    @Test
    public void helloTest(){
        System.out.println("开始测试。。。");
        IHelloService helloService = rpcProxy.create(IHelloService.class);
        String result = helloService.hello("World");
        Assert.assertEquals("Hello! World",result);
    }
}

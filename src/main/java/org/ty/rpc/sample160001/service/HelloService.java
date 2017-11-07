package org.ty.rpc.sample160001.service;

import org.ty.rpc.sample160001.annotation.RpcService;

/**
 * <dl>
 * <dt>org.ty.rpc.service.impl</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: </dd>
 * <dd>CreateDate: 2016/3/3</dd>
 * </dl>
 *
 * @author tangyun
 */
@RpcService(IHelloService.class)
public class HelloService implements IHelloService {

    @Override
    public String hello(String name) {
        return "Hello "+name;
    }
}

package org.ty.cglib.sample160001.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

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
public class MyCglibProxy implements MethodInterceptor {

    private Logger logger = LoggerFactory.getLogger(MyCglibProxy.class);
    public Enhancer enhancer = new Enhancer();
    public String name;

    public MyCglibProxy(String name) {
        this.name = name;
    }

    /**
     * 根据class对象创建该对象的代理对象
     * 1、设置父类；2、设置回调
     * 本质：动态创建了一个class对象的子类
     *
     * @param cls
     * @return
     */
    public Object getDaoBean(Class cls){
        enhancer.setSuperclass(cls);
        enhancer.setCallback(this);
        return cls;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        logger.debug("调用的方法是："+method.getName());
        if (!"张三".equals(name)){
            System.out.println("你没有权限") ;
            return null;
        }
        Object result = methodProxy.invokeSuper(o,objects);
        return result;
    }
}

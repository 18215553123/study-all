package org.ty.rpc.sample160001.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <dl>
 * <dt>org.ty.rpc.annotation</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: </dd>
 * <dd>CreateDate: 2016/3/3</dd>
 * </dl>
 *
 * @author tangyun
 */
@Target(ElementType.TYPE)
/*
设定自定义的Annotation型态
SOURCE, //编译程序处理完Annotation信息后就完成任务
CLASS,  //编译程序将Annotation储存于class档中，缺省
RUNTIME //编译程序将Annotation储存于class檔中，可由VM读入(通过反射机制)。这个功能搭配反射是非常强大的。
 */
@Retention(RetentionPolicy.RUNTIME)
@Component // 表明可被 Spring 扫描
public @interface RpcService {

    Class <?> value();
}

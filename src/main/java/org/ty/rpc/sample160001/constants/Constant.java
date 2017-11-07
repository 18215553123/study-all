package org.ty.rpc.sample160001.constants;

/**
 * <dl>
 * <dt>org.ty.rpc.Constants</dt>
 * <dd>Description: 配置敞亮</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: </dd>
 * <dd>CreateDate: 2016/3/3</dd>
 * </dl>
 *
 * @author tangyun
 */
public interface Constant {

    int ZK_SESSION_TIMEOUT = 5000;

    String ZK_REGISTRY_PATH = "/registry";
    String ZK_DATA_PATH = ZK_REGISTRY_PATH + "/data";
}

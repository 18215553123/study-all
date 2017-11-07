package org.ty.rpc.sample160001.server;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ty.rpc.sample160001.constants.Constant;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * <dl>
 * <dt>org.ty.rpc.server</dt>
 * <dd>Description:Zookeeper 服务注册组件</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: </dd>
 * <dd>CreateDate: 2016/3/3</dd>
 * </dl>
 *
 * @author tangyun
 */
public class ServiceRegistry {
    private static final Logger logger = LoggerFactory.getLogger(ServiceRegistry.class);

    private CountDownLatch latch = new CountDownLatch(1);

    private String registryAddress;

    public ServiceRegistry(String registryAddress){
        this.registryAddress = registryAddress;
    }

    public void register(String data){
        if (data != null){
            ZooKeeper zk = connectServer();
            if (zk != null){
                createNode(zk,data);
            }
        }
    }

    /**
     * 链接服务
     * @return
     */
    private ZooKeeper connectServer(){
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(registryAddress, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected){
                        latch.countDown();
                    }
                }
            });
            latch.await();
        } catch (IOException e) {
            logger.error("",e);
        } catch (InterruptedException e) {
            logger.error("",e);
        }
        return zk;
    }

    /**
     * 创建节点
     */
    private void createNode(ZooKeeper zk,String data){
        byte[] bytes = data.getBytes();
        try {
            String path = zk.create(Constant.ZK_DATA_PATH,bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            logger.debug("create zookeeper node ({} => {})", path, data);
        } catch (KeeperException e) {
            logger.error("",e);
        } catch (InterruptedException e) {
            logger.error("",e);
        }
    }
}

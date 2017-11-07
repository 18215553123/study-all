package org.ty.rpc.sample160001.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.ty.rpc.sample160001.annotation.RpcService;
import org.ty.rpc.sample160001.base.RpcDecoder;
import org.ty.rpc.sample160001.handler.RpcHandler;
import org.ty.rpc.sample160001.po.RpcRequest;
import org.ty.rpc.sample160001.po.RpcResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * <dl>
 * <dt>org.ty.rpc.server</dt>
 * <dd>Description: RPC 服务器</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: </dd>
 * <dd>CreateDate: 2016/3/3</dd>
 * </dl>
 *
 * @author tangyun
 */
public class RpcServer implements ApplicationContextAware,InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    private String serverAddress;
    private ServiceRegistry serviceRegistry;

    private Map<String,Object> handlerMap = new HashMap<>();//存放接口名与服务对象之间的映射关系

    public RpcServer(String serverAddress,ServiceRegistry serviceRegistry){
        this.serverAddress = serverAddress;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String,Object> serverBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);// 获取所有带有 RpcService 注解的 Spring Bean
        if (MapUtils.isNotEmpty(serverBeanMap)){
            for (Object serviceBean : serverBeanMap.values()){
                String interfaceName = serviceBean.getClass().getAnnotation(RpcService.class).value().getName();
                handlerMap.put(interfaceName,serviceBean);
            }
        }
        logger.debug("handlerMap size : {}",handlerMap.size());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline()
                            .addLast(new RpcDecoder(RpcRequest.class))
                            .addLast(new RpcDecoder(RpcResponse.class))
                            .addLast(new RpcHandler(handlerMap));
                }
            }).option(ChannelOption.SO_BACKLOG,128).childOption(ChannelOption.SO_KEEPALIVE,true);

            String[] array = serverAddress.split(":");
            String host = array[0];
            int port = Integer.parseInt(array[1]);

            ChannelFuture future = bootstrap.bind(host,port).sync();
            logger.debug("server started on port {}", port);

            if (serviceRegistry != null){
                serviceRegistry.register(serverAddress);
            }
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}

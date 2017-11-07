package org.ty.netty.sample160001;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.ty.netty.sample160001.po.Commod;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * <dl>
 * <dt>org.ty.netty.one</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: </dd>
 * <dd>CreateDate: 2016/3/1</dd>
 * </dl>
 *
 * @author tangyun
 */
public class HelloServer {

    public static void main(String[] args) {
        // Server服务启动器
        ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        // 设置一个处理客户端消息和各种消息事件的类(Handler)
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new HelloServerHandler());
            }
        });
//        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
//            @Override
//            public ChannelPipeline getPipeline() throws Exception {
//                return Channels.pipeline(new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())),new ObjectServerHandler());
//            }
//        });
        bootstrap.bind(new InetSocketAddress(8000));
    }

    private static class HelloServerHandler extends SimpleChannelHandler{
        /**
         * 当有客户端绑定到服务端的时候触发，打印"Hello world, I'm server."
         *
         * @alia OneCoder
         */
        @Override
        public void channelConnected(
                ChannelHandlerContext ctx,
                ChannelStateEvent e) {
            System.out.println("Hello world, I'm server.");
        }
    }

    /**
     * 传递对象的handler
     */
    public static class ObjectServerHandler extends SimpleChannelHandler{
        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            super.messageReceived(ctx, e);
            Commod commod = (Commod) e.getMessage();
            System.out.println(commod.getKey());
        }
    }
}

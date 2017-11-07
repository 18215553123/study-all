package org.ty.netty.sample160001;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
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
public class HelloClient {
    public static void main(String[] args) {
        ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
                Executors.newCachedThreadPool(),Executors.newCachedThreadPool()));
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new HelloClientHandler());
            }
        });
//        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
//            @Override
//            public ChannelPipeline getPipeline() throws Exception {
//                return Channels.pipeline(new ObjectEncoder(),new ObjectClientHandler());
//            }
//        });
        bootstrap.connect(new InetSocketAddress("127.0.0.1",8000));
    }

    private static class HelloClientHandler extends SimpleChannelHandler {


        /**
         * 当绑定到服务端的时候触发，打印"Hello world, I'm client."
         *
         * @alia OneCoder
         */
        @Override
        public void channelConnected(ChannelHandlerContext ctx,
                                     ChannelStateEvent e) {
            System.out.println("Hello world, I'm client.");
        }
    }

    private static class ObjectClientHandler extends SimpleChannelHandler{
        @Override
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            sendObject(e.getChannel());
        }

        private void sendObject(Channel channel){
            Commod commod = new Commod();
            commod.setKey("this is key, you are right!");
            channel.write(commod);
        }
    }
}

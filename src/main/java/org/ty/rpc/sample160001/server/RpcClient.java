package org.ty.rpc.sample160001.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ty.rpc.sample160001.base.RpcDecoder;
import org.ty.rpc.sample160001.base.RpcEncoder;
import org.ty.rpc.sample160001.po.RpcRequest;
import org.ty.rpc.sample160001.po.RpcResponse;

import static io.netty.channel.ChannelOption.*;

/**
 * <dl>
 * <dt>org.ty.rpc.server</dt>
 * <dd>Description:Rpc 客户端</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: </dd>
 * <dd>CreateDate: 2016/3/4</dd>
 * </dl>
 *
 * @author tangyun
 */
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse>{

    private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);

    private String host;

    private int port;

    private final Object obj = new Object();

    private RpcResponse response;

    public RpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        this.response = response;
        synchronized (obj){
            obj.notifyAll();
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("client caught exception", cause);
        ctx.close();
    }

    public RpcResponse send(RpcRequest request) throws Exception{
        logger.debug("*start client.send() ,requestId:{}",request.getRequestId());
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline()
                            .addLast(new RpcEncoder(RpcRequest.class))//将rpc请求编码 --发送
                            .addLast(new RpcDecoder(RpcResponse.class))//将rpc相应解码 --处理
                            .addLast(RpcClient.this);//使用RpcClient发送Rpc请求
                }
            }).option(SO_KEEPALIVE,true);
            ChannelFuture future = bootstrap.connect(host,port).sync();
            future.channel().writeAndFlush(request).sync();

            synchronized (obj){
                obj.wait();
                logger.debug("obj is wait ...............");
            }
            if (response != null){
                future.channel().closeFuture().sync();
            }
            return response;
        } finally {
            group.shutdownGracefully();
        }
    }
}

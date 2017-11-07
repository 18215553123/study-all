package org.ty.rpc.sample160001.base;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.ty.rpc.sample160001.utils.SerializationUtil;

import java.util.List;

/**
 * <dl>
 * <dt>org.ty.rpc.base</dt>
 * <dd>Description:RPC 解码 扩展于netty</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: </dd>
 * <dd>CreateDate: 2016/3/4</dd>
 * </dl>
 *
 * @author tangyun
 */
public class RpcDecoder extends ByteToMessageDecoder {

    private Class<?> genericClass;

    public RpcDecoder(Class<?> genericClass){
        this.genericClass = genericClass;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4){ //buf中封装了4个字节的数据长度
            return;
        }
        in.markReaderIndex();
        int dataLength = in.readInt();
        if (dataLength < 0){
            ctx.close();
        }
        if (in.readableBytes() < dataLength){
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);

        Object obj = SerializationUtil.deserialize(data,genericClass);
        out.add(obj);
    }
}

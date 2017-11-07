package org.ty.netty.sample160002;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * <dl>
 * <dt>org.ty.NIO.one</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: </dd>
 * <dd>CreateDate: 2016/3/3</dd>
 * </dl>
 *
 * @author tangyun
 */
public class SocketChannelSample {
    public static void main(String[] args){
        try {
            SocketChannelSample socketChannelSample = new SocketChannelSample();
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress("baidu.com",80));
            while (!socketChannel.finishConnect()){
                System.out.println("等待链接。。。。。。");
            }
            String path = socketChannelSample.getClass().getResource("").getPath();
            File file = new File(path.substring(1,path.length())+"SocketChannelFile.txt");
            if (!file.exists()){
                file.mkdir();
            }
            FileOutputStream out = new FileOutputStream(file);
            FileChannel fcout = out.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(48);
            while (true){
                int readByte = socketChannel.read(buffer);
                if (readByte == -1){
                    break;
                }
                buffer.flip();
                fcout.write(buffer);
                buffer.clear();
            }
            fcout.close();
            out.close();
            socketChannel.close();
            System.out.println("处理完成。。。");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

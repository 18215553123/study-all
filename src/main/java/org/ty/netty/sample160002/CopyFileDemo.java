package org.ty.netty.sample160002;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * <dl>
 * <dt>org.ty.NIO.one</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: </dd>
 * <dd>CreateDate: 2016/3/2</dd>
 * </dl>
 *
 * @author tangyun
 */
public class CopyFileDemo {
    public static void main(String[] args) throws Exception {
        String inFile = "filePath";
        String outFile = "filePath";
        //流
        FileInputStream in = new FileInputStream(inFile);
        FileOutputStream out = new FileOutputStream(outFile);
        //通道
        FileChannel fcin = in.getChannel();
        FileChannel fcout = out.getChannel();
        //缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while(true){
            //重设缓冲区
            buffer.clear();
            //read
            int r = fcin.read(buffer);
            if (r == -1){
                break;
            }
            //flip 让缓冲区可以将新读入的数据写入另一个通道
            buffer.flip();
            fcout.write(buffer);
        }
    }
}

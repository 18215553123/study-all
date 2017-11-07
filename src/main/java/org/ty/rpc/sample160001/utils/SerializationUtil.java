package org.ty.rpc.sample160001.utils;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <dl>
 * <dt>org.ty.rpc.Utils</dt>
 * <dd>Description: 序列化工具类 :如需要替换其它序列化框架，只需修改SerializationUtil即可。当然，更好的实现方式是提供配置项来决定使用哪种序列化方式</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: </dd>
 * <dd>CreateDate: 2016/3/4</dd>
 * </dl>
 *
 * @author tangyun
 */
public class SerializationUtil {
    /**
     * ConcurrentHashMap说明：线程安全的Map,通过分段锁机制实现。在ConcurrentHashMap中会将Map分成了N个Segment，
     * put和get的时候，都是现根据key.hashCode()算出放到哪个Segment中，多线程操作相同Segment时会阻塞，不同Segment不会阻塞。
     */
    private static Map<Class<?>,Schema<?>> cachedSchema = new ConcurrentHashMap<>();

    /**
     * objenesis实例化特殊对象的工具类,比java反射更为强大
     */
    private static Objenesis objenesis = new ObjenesisStd(true);

    private SerializationUtil(){}

    private static <T> Schema<T> getSchema(Class<T> cls){
        Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
        if (schema == null){
            schema = RuntimeSchema.createFrom(cls);
            if (schema != null){
                cachedSchema.put(cls,schema);
            }
        }
        return schema;
    }

    public static <T> byte[] serialize(T obj){
        Class<T> cls = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema = getSchema(cls);
            return ProtostuffIOUtil.toByteArray(obj,schema,buffer);
        } catch (Exception e){
            throw new IllegalStateException(e.getMessage(),e);
        }
    }

    public static <T> T deserialize(byte[] data,Class<T> cls){
        try {
            T message = objenesis.newInstance(cls);
            Schema<T> schema = getSchema(cls);
            ProtostuffIOUtil.mergeFrom(data,message,schema);
            return message;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(),e);
        }
    }
}

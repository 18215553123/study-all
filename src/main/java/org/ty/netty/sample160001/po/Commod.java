package org.ty.netty.sample160001.po;

import java.io.Serializable;

/**
 * <dl>
 * <dt>org.ty.netty.POJO</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: </dd>
 * <dd>CreateDate: 2016/3/1</dd>
 * </dl>
 *
 * @author tangyun
 */
public class Commod implements Serializable {

    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

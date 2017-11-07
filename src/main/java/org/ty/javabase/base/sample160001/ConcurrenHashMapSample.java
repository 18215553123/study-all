package org.ty.javabase.base.sample160001;

import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * <dl>
 * <dt>study-all</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: </dd>
 * <dd>CreateDate: 2017年11月07日</dd>
 * </dl>
 *
 * @author Tangyun
 */
public class ConcurrenHashMapSample {

    private static ConcurrentHashMap<String,?> concurrentHashMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {

    }

    public boolean compile(String regex){
        Pattern pattern = Pattern.compile(regex);
        return false;
    }
}

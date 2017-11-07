package org.ty.javabase.synchronizeds.sample160001.main;


import org.ty.javabase.synchronizeds.sample160001.po.Book;

/**
 * <dl>
 * <dt>org.ty.synchronizeds.main</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: </dd>
 * <dd>CreateDate: 2016/5/11</dd>
 * </dl>
 *
 * @author tangyun
 */
public class DoSomeThing {

    private static DoSomeThing doSomeThing = new DoSomeThing();

    public static DoSomeThing getInstance(){
        return doSomeThing;
    }

    public void doSome(String user){
        Book book = new Book();
        book.setId(user);
        synchronized (book.getId()){
            System.out.println(user+":执行开始。。。。。。。。。。。。。");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

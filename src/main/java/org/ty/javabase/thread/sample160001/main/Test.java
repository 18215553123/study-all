package org.ty.javabase.thread.sample160001.main;


import org.ty.javabase.thread.sample160001.po.Book;

/**
 * <dl>
 * <dt>org.ty.Thread.main</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: </dd>
 * <dd>CreateDate: 2016/5/12</dd>
 * </dl>
 *
 * @author tangyun
 */
public class Test {
    public static void main(String[] args) {
        int num = 0;
        Book book = new Book();
        book.setNm(num);
        System.out.println(book.getNm());
    }
}

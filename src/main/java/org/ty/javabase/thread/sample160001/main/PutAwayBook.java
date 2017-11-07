package org.ty.javabase.thread.sample160001.main;


import org.ty.javabase.thread.sample160001.po.Book;
import org.ty.javabase.thread.sample160001.po.Library;

/**
 * <dl>
 * <dt>org.ty.Thread.main</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: </dd>
 * <dd>CreateDate: 2016/5/11</dd>
 * </dl>
 *
 * @author tangyun
 */
public class PutAwayBook {

    public void putAway(Book book){
        System.out.println("上架一本书："+book.getName());
        Library library = Library.getInstance();
        library.setBooks(library.getBooks()+1);
        library.getBook().add(book);
        library.setExistBooks(library.getExistBooks()+1);
        System.out.println("上架结束");
    }
}

package org.ty.javabase.thread.sample160001.main;


import org.ty.javabase.thread.sample160001.po.Book;

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
public class WorkLibrary {

    public static void main(String[] args) {
        int putNum = 6;
        PutAwayBook putAwayBook = new PutAwayBook();
        for (int i=0;i<putNum;i++){
            Book book = new Book();
            book.setId("book"+i);
            book.setName("bookName"+i);
            book.setNumber(1);
            putAwayBook.putAway(book);
        }
        Book book = new Book();
        book.setId("book1");
        CheckOutBook checkOutBook = new CheckOutBook("M1", book);
        CheckOutBook checkOutBook1 = new CheckOutBook("M2", book);
        ReturnBook returnBook = new ReturnBook("M1", book);
        new Thread(checkOutBook).start();
        new Thread(checkOutBook1).start();
        new Thread(returnBook).start();
    }
}

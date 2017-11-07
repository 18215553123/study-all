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
public class ReturnBook implements Runnable {
    private String user;
    private Book bookId;

    public ReturnBook(String user,Book bookId){
        this.user = user;
        this.bookId = bookId;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(5000);
            Library library = Library.getInstance();
            Book book ;
            for (int i=0;i<library.getBook().size();i++) {
                book = library.getBook().get(i);
                if (book.getId().equals(bookId.getId())){
                    book.setNumber(1);
                    for (String id : library.getWaitBook()){
                        if (id.equals(bookId.getId())){
                            System.out.println("书已归还，并有人等待借出。。。");
                            synchronized (bookId){
                                bookId.notify();
                            }
                        }
                    }
                    break;
                }

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

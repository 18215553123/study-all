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
public class CheckOutBook implements Runnable {

    private String user;
    private Book bookId;

    public CheckOutBook(String user,Book bookId){
        this.user = user;
        this.bookId = bookId;
    }

    @Override
    public void run() {
        Library library = Library.getInstance();
        try {
            Book book ;
            for (int i=0;i<library.getBook().size();i++) {
                book = library.getBook().get(i);
                if (book.getId().equals(bookId.getId())){
                    if (book.getNumber() == 0) {
                        System.out.println("书已被借，等待归还，等待者："+user);
                        library.getWaitBook().add(bookId.getId());
                        synchronized (bookId){
                            bookId.wait();
                        }
                    }
                    book.setNumber(0);
                    System.out.println("书已归还，并被+"+user+"借出");
                    break;
                }

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setBookId(Book bookId) {
        this.bookId = bookId;
    }

}

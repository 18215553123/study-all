package org.ty.javabase.thread.sample160001.po;

import java.util.ArrayList;
import java.util.List;

/**
 * <dl>
 * <dt>org.ty.Thread.PO</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: </dd>
 * <dd>CreateDate: 2016/5/11</dd>
 * </dl>
 *
 * @author tangyun
 */
public class Library {

    private static Library library;

    public static Library getInstance(){
        if (library == null){//单例模拟数据库
            byte[] o = new byte[0];
            synchronized (o){
                if (library == null){
                    library = new Library();
                    library.setBook(new ArrayList<Book>());
                    library.setWaitBook(new ArrayList<String>());
                }
            }
        }
        return library;
    }

    private String libName;//名字

    private List<Book> book;//藏书

    private int books;//书的总数

    private int existBooks;//现存的书总数

    private int checkOutBooks;//借出的书总数

    private List<String> waitBook;//当前书有等待

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    public String getLibName() {
        return libName;
    }

    public void setLibName(String libName) {
        this.libName = libName;
    }

    public List<Book> getBook() {
        return book;
    }

    public void setBook(List<Book> book) {
        this.book = book;
    }

    public int getBooks() {
        return books;
    }

    public void setBooks(int books) {
        this.books = books;
    }

    public int getExistBooks() {
        return existBooks;
    }

    public void setExistBooks(int existBooks) {
        this.existBooks = existBooks;
    }

    public int getCheckOutBooks() {
        return checkOutBooks;
    }

    public void setCheckOutBooks(int checkOutBooks) {
        this.checkOutBooks = checkOutBooks;
    }

    public List<String> getWaitBook() {
        return waitBook;
    }

    public void setWaitBook(List<String> waitBook) {
        this.waitBook = waitBook;
    }
}

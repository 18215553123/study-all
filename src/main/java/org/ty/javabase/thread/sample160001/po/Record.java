package org.ty.javabase.thread.sample160001.po;

import java.util.Date;

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
public class Record {

    private String recordId;//记录id

    private String user;//借书或还书人

    private String bookName;//书名

    private String bookId;//书的唯一标识

    private Date time;//操作时间

    private String type;//0-借书 1-还书 2-上架 3-下架

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

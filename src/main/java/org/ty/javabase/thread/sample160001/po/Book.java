package org.ty.javabase.thread.sample160001.po;

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
public class Book {

    private String id;//书的唯一标识

    private String name;

    private String price;

    private int number;

    private Integer Nm;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Integer getNm() {
        return Nm;
    }

    public void setNm(Integer nm) {
        Nm = nm;
    }
}

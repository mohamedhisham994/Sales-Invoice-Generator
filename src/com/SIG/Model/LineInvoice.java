
package com.SIG.Model;


public class LineInvoice {
    private int num;
    private String item;
    private int price;
    private int count;
    private HeaderInvoice invoice;

    public LineInvoice(String item, int price, int count, HeaderInvoice invoice) {

        this.item = item;
        this.price = price;
        this.count = count;
        this.invoice = invoice;
    }



    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }



    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public HeaderInvoice getInvoice() {
        return invoice;
    }

    public void setInvoice(HeaderInvoice invoice) {
        this.invoice = invoice;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTotal_line(){
        return price*count;
    }



}

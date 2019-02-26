package Util;

import java.io.Serializable;

public class Product implements Serializable{
    private int id;
    private int price;
    private String name;
    private String description;

    private byte tab[] ;

    public Product(int id, int price, String name, String description, byte[] tab) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.description = description;
        this.tab = tab;
    }

    public byte[] getTab() {
        return tab;
    }

    public void setTab(byte[] tab) {
        this.tab = tab;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    @Override
    public String toString() {
        return "Product : "+name+" costs : "+price+"$ description : "+description+" size "+tab.length;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

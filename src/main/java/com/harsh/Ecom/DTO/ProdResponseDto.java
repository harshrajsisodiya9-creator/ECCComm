package com.harsh.Ecom.DTO;

public class ProdResponseDto {
    private int price;
    private String name;
    private int id;

    public ProdResponseDto(){};

    public ProdResponseDto(int id,int price,String name){this.id = id;this.price = price; this.name = name;};

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getId(){return id;}
    public void setId(int id) {
        this.id = id;
    }
}

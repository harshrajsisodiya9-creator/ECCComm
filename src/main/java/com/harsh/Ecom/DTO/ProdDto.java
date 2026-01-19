package com.harsh.Ecom.DTO;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


public class ProdDto implements Serializable {
    private int id;
    private int price;
    private String name;

    private String imageName;
    private String imageType;
    private byte[] imageData;

    public ProdDto(){};

    public ProdDto(int price, String name,int id){
        this.name = name;
        this.price = price;
        this.id = id;
    }

    public int getId(){return id;}
    public void setId(int id){this.id = id;}

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public void setImageName(String originalFilename) {
        this.imageName = originalFilename;
    }

    public void setImageType(String fileType){
        this.imageType = fileType;
    }

    public void setImageData(byte[] imageData){
        this.imageData = imageData;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public String getImageType() {
        return imageType;
    }

    public String getImageName(){
        return imageName;
    }
}

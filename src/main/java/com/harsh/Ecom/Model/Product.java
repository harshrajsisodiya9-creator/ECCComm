package com.harsh.Ecom.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import org.springframework.stereotype.Component;

@Component
@Entity
public class Product {

    @Id
    private int prodId;

    private int prodPrice;
    private String prodName;

    private String imageName;
    private String imageType;

    @Lob
    private byte[] imageData;

    public Product(){};

    public Product(int prodId,int prodPrice, String prodName){
        this.prodId = prodId;
        this.prodName = prodName;
        this.prodPrice = prodPrice;
    }

    public int getProdId() { return prodId; }
    public void setProdId(int prodId) { this.prodId = prodId; }

    public int getProdPrice() { return prodPrice; }
    public void setProdPrice(int prodPrice) { this.prodPrice = prodPrice; }

    public String getProdName() { return prodName; }
    public void setProdName(String prodName) { this.prodName = prodName; }

    public void setImageName(String originalFilename) {
        this.imageName = originalFilename;
    }

    public void setImageType(String fileType){
        this.imageType = fileType;
    }

    public void setImageData(byte[] imageData){
            this.imageData = imageData;               // Note: this.imageData is pointing towards imageData; i.e arrays whether int or byt or char are just pointing towards object in the memory pool
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

package com.harsh.Ecom.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Entity
public class Product implements Serializable {

    @Id
    private int prodId;

    private int prodPrice;
    private String prodName;

    private String objectName;
    private Long objectSize;
    private String bucketName;

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

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public Long getObjectSize() {
        return objectSize;
    }

    public void setObjectSize(Long objectSize) {
        this.objectSize = objectSize;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
}

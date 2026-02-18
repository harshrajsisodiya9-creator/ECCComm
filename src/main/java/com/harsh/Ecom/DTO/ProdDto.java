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

    private String objectName;
    private Long objectSize;
    private String url;

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

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }
    public String getObjectName(){
        return objectName;
    }

    public Long getobjectSize() {
        return objectSize;
    }
    public void setobjectSize(Long objectSize) {
        this.objectSize = objectSize;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

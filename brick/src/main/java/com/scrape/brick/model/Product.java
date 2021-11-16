package com.scrape.brick.model;

import java.math.BigDecimal;

public class Product {
    private String name;
    private String description;
    private String imageLink;
    private String price;
    private int rating;
    private String storeName;
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    public String getDescription(){
        return description;
    }
    
    public void setDescription(String description){
        this.description = description;
    }

    public String getImageLink(){
        return imageLink;
    }
    
    public void setImageLink(String imageLink){
        this.imageLink = imageLink;
    }

    public String getStoreName(){
        return storeName;
    }
    
    public void setStoreName(String storeName){
        this.storeName = storeName;
    }

    public String getPrice(){
        return price;
    }
    
    public void setPrice(String price){
        this.price = price;
    }

    public int getRating(){
        return rating;
    }
    
    public void setRating(int rating){
        this.rating = rating;
    }
}

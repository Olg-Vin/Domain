package com.vinio.controllers;

public class ProductResponse {
    private int id;
    private String name;
    private double price;
    private String category;
    private int count;

    public ProductResponse() {
    }

    public ProductResponse(int id, String name, double price, String category, int count) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.count = count;
    }

    // Getters и Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

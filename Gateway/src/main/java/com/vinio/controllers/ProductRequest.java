package com.vinio.controllers;

public class ProductRequest {
    private String name;
    private double price;
    private String category;
    private int count;

    public ProductRequest() {
    }

    public ProductRequest(String name, double price, String category, int count) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.count = count;
    }

    // Getters и Setters
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

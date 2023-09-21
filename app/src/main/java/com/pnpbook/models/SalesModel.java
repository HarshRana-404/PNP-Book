package com.pnpbook.models;

public class SalesModel {
    public int foodIndex;
    public String foodName;
    public int foodQuantity;
    public int foodPrice;
    public int foodTotal;

    public SalesModel(int foodIndex, String foodName, int foodQuantity, int foodPrice, int foodTotal) {
        this.foodIndex = foodIndex;
        this.foodName = foodName;
        this.foodQuantity = foodQuantity;
        this.foodPrice = foodPrice;
        this.foodTotal = foodTotal;
    }
}

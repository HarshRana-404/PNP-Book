package com.pnpbook.models;

public class ExpandableSalesModel {
    public String foodIndex;
    public String foodName;
    public int foodQuantity;
    public int foodAmount;

    public ExpandableSalesModel(String foodIndex, String foodName, int foodQuantity, int foodAmount) {
        this.foodIndex = foodIndex;
        this.foodName = foodName;
        this.foodQuantity = foodQuantity;
        this.foodAmount = foodAmount;
    }
}

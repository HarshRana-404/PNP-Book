package com.pnpbook.models;

public class ExpandableSalesModel {
    public String foodIndex;
    public String foodName;
    public int foodQuantity;
    public int foodAmount;
    public String foodDate;

    public ExpandableSalesModel(String foodIndex, String foodName, int foodQuantity, int foodAmount, String foodDate) {
        this.foodIndex = foodIndex;
        this.foodName = foodName;
        this.foodQuantity = foodQuantity;
        this.foodAmount = foodAmount;
        this.foodDate = foodDate;

    }
}

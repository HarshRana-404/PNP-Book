package com.pnpbook.models;

import java.util.ArrayList;

public class SalesModel {
    public String saleDate;
    public String totalSale;
    public ArrayList<ExpandableSalesModel> alExpandableSale;

    public boolean isExpanded=false;

    public SalesModel(String saleDate, String totalSale, ArrayList<ExpandableSalesModel> alExpandableSale){
        this.saleDate = saleDate;
        this.totalSale = "₹"+totalSale+"/-";
        this.alExpandableSale = alExpandableSale;
    }

}

package com.pnpbook.database;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DBHelper extends SQLiteOpenHelper {
    Context context;
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, "pnp_db", factory, version, errorHandler);
        this.context = context;
        SQLiteDatabase db = getWritableDatabase();
        try{
            db.execSQL("CREATE TABLE food_items (food_index CHAR(2), food_name CHAR(100) PRIMARY KEY, food_price CHAR(10))");
            db.execSQL("CREATE TABLE sale (sale_date DATE, food_name CHAR(100), food_price CHAR(100), food_quantity CHAR(10))");
        }
        catch (Exception e) {}
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL("CREATE TABLE food_items (food_index CHAR(2), food_name CHAR(100) PRIMARY KEY, food_price CHAR(10))");
            db.execSQL("CREATE TABLE sale (sale_date DATE, food_name CHAR(100),  food_price CHAR(100), food_quantity CHAR(10))");
        }
        catch (Exception e) {}
    }
    public void addFoodItem(String foodIndex, String foodName, String foodPrice){
        try{
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("INSERT INTO food_items VALUES('1', '"+foodName+"', '"+foodPrice+"')");
        } catch (Exception e) {
//            Toast.makeText(context, e+"", Toast.LENGTH_SHORT).show();
        }
    }
    public void removeAll(){
        try{
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("DELETE FROM food_items");
            db.execSQL("DELETE FROM sale");
        } catch (Exception e) {
//            Toast.makeText(context, e+"", Toast.LENGTH_SHORT).show();
        }
    }
    public Boolean itemPresentInSale(String saleDate, String foodName){
        SQLiteDatabase db = getWritableDatabase();
        Cursor csr = db.rawQuery("SELECT food_quantity FROM sale WHERE food_name = '"+foodName+"' AND sale_date = '"+saleDate+"'", null);
        int itemCount=0;
        while (csr.moveToNext()){
            itemCount++;
        }
        if(itemCount==1){
            return true;
        }
        return false;
    }
    public Cursor getSaleByDate(String saleDate){
        SQLiteDatabase db = getWritableDatabase();
        Cursor csr = db.rawQuery("SELECT * FROM sale WHERE sale_date = '"+saleDate+"'", null);
        return csr;
    }
    public int getPriceByFoodName(String foodName){
        SQLiteDatabase db = getWritableDatabase();
        Cursor csr = db.rawQuery("SELECT food_price FROM food_items WHERE food_name = '"+foodName+"'", null);
        int pr=0;
        while (csr.moveToNext()){
            pr = Integer.parseInt(csr.getString(0));
        }
        return pr;
    }
    public void updateSale(String saleDate, String foodName, String foodQuantity){
        try{
            SQLiteDatabase db = getWritableDatabase();
            Cursor csr = db.rawQuery("SELECT food_quantity FROM sale WHERE food_name = '"+foodName+"' AND sale_date = '"+saleDate+"'", null);
            int qty=0;
            while (csr.moveToNext()){
                qty = Integer.parseInt(csr.getString(0));
            }
            qty = qty + Integer.parseInt(foodQuantity);
            int price = getPriceByFoodName(foodName);
            db.execSQL("UPDATE sale SET food_quantity = '"+qty+"', food_price = '"+price+"' WHERE food_name = '"+foodName+"' AND sale_date = '"+saleDate+"'");
        } catch (Exception e) {
//            Toast.makeText(context, e+"", Toast.LENGTH_SHORT).show();
        }
    }
    public void insertItem(String foodName, String foodQuantity){
        try{
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("INSERT INTO sale VALUES('"+getCurrentDate()+"', '"+foodName+"', '"+getPriceByFoodName(foodName)+"', '"+foodQuantity+"')");
        } catch (Exception e) {
//            Toast.makeText(context, e+"", Toast.LENGTH_SHORT).show();
        }
    }
    public void removeFoodItem(String foodName){
        try{
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("DELETE FROM food_items WHERE food_name = '"+foodName+"'");
        } catch (Exception e) {
//            Toast.makeText(context, e+"", Toast.LENGTH_SHORT).show();
        }
    }
    public void removeFoodFromSale(String foodName, String foodDate){
        try{
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("DELETE FROM sale WHERE food_name = '"+foodName+"' AND sale_date = '"+foodDate+"'");
        } catch (Exception e) {
//            Toast.makeText(context, e+"", Toast.LENGTH_SHORT).show();
        }
    }
    public void updateFoodInSale(String foodName, String foodQty, String foodDate){
        try{
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("UPDATE sale SET food_quantity = '"+foodQty+"' WHERE food_name = '"+foodName+"' AND sale_date = '"+foodDate+"'");
        } catch (Exception e) {
//            Toast.makeText(context, e+"", Toast.LENGTH_SHORT).show();
        }
    }
    public void updateFoodItem(String foodName, String foodNameNew, String foodPrice){
        try{
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("UPDATE food_items SET food_name = '"+foodNameNew+"', food_price = '"+foodPrice+"' WHERE food_name = '"+foodName+"'");
        } catch (Exception e) {
//            Toast.makeText(context, e+"", Toast.LENGTH_SHORT).show();
        }
    }
    public int getItemCount(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor csr = db.rawQuery("SELECT COUNT(food_index) FROM food_items", null);
        int itemCount=0;
        while (csr.moveToNext()){
            itemCount = csr.getInt(0);
        }
        return itemCount;
    }
    public Cursor getFoodItems(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor csr = db.rawQuery("SELECT * FROM food_items", null);
        return csr;
    }
    public int getFoodItemPrice(String foodName){
        int pr = 0;
        try{
            SQLiteDatabase db = getWritableDatabase();
            Cursor csr = db.rawQuery("SELECT food_price FROM food_items WHERE food_name = '"+foodName+"'", null);
            while(csr.moveToNext()){
                pr = Integer.parseInt(csr.getString(0));
            }
        } catch (Exception e) {}
        return pr;
    }
    public Cursor getDates(){
        Cursor csr=null;
        try{
            SQLiteDatabase db = getWritableDatabase();
            csr = db.rawQuery("SELECT sale_date FROM sale GROUP BY sale_date ORDER BY sale_date DESC", null);
        } catch (Exception e) {}
        return csr;
    }
    public Cursor getWholeData(){
        Cursor csr=null;
        try{
            SQLiteDatabase db = getWritableDatabase();
            csr = db.rawQuery("SELECT * FROM sale", null);
        } catch (Exception e) {}
        return csr;
    }
    public static String getCurrentDate(){
        Calendar cl = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = sdf.format(cl.getTime());
        return todayDate;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}

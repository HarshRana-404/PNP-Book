package com.pnpbook.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pnpbook.R;
import com.pnpbook.database.DBHelper;

import java.util.ArrayList;


public class StockFragment extends Fragment {

    public static DBHelper dbh;
    FloatingActionButton fabAddNewStock;
    public static Context context;
    ListView lvStock;

    ArrayList<String> alStock = new ArrayList<>();

    AppBarLayout ablStock;
    TextView tvProfitAndLoss;

    Cursor csr;

    ArrayList<String> alSpinnerDates = new ArrayList<>();

    public StockFragment() {
        // Required empty public constructor
    }
    public static StockFragment newInstance(String param1, String param2) {
        StockFragment fragment = new StockFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container.removeAllViews();
        View view = inflater.inflate(R.layout.fragment_stock, container, false);
        context = view.getContext();
        dbh = new DBHelper(getContext(), null, null,  1, null);
        ablStock = view.findViewById(R.id.abl_stock);
        tvProfitAndLoss = ablStock.findViewById(R.id.tv_abl_pl);

        try{
            fabAddNewStock = view.findViewById(R.id.fab_add_new_stock);
            lvStock = view.findViewById(R.id.lv_stock);

            tvProfitAndLoss.setOnClickListener(new View.OnClickListener() {
                @Override
                @SuppressLint("MissingInflatedId")
                public void onClick(View view) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                    View vProfitLoss = getLayoutInflater().inflate(R.layout.layout_show_profit_loss_ad, container.findViewById(R.id.ad_root_profit_loss));
                    DialogInterface di;

                    ImageView ivClose = vProfitLoss.findViewById((R.id.iv_close));
                    Spinner spPLSelectMonthYear = vProfitLoss.findViewById((R.id.sp_pl_select_month_year));
                    TextView tvPurchase = vProfitLoss.findViewById(R.id.tv_pl_purchase);
                    TextView tvSale = vProfitLoss.findViewById(R.id.tv_pl_sale);
                    TextView tvProfitAndLoss = vProfitLoss.findViewById(R.id.tv_pl_value);

                    csr = dbh.getDates();
                    String date[];
                    String mnYr="";
                    while (csr.moveToNext()){
                        date = csr.getString(0).split("-");
                        mnYr = date[0]+"-"+date[1];
                        alSpinnerDates.add(mnYr);
                    }
                    ArrayAdapter datesAdapter = new ArrayAdapter(getContext(), R.layout.spinner_item_ui, R.id.tv_spinner_item, alSpinnerDates);
                    spPLSelectMonthYear.setAdapter(datesAdapter);

                    spPLSelectMonthYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            try{
                                csr = dbh.getSaleDatesOfSpecifiedMonth(alSpinnerDates.get(i));
                                int monthlySale=0;
                                while(csr.moveToNext()){
                                    Cursor dateCsr = dbh.getSaleByDate(csr.getString(0));
                                    while(dateCsr.moveToNext()){
                                        int foodQty = Integer.parseInt(dateCsr.getString(2));
                                        int foodPrice = Integer.parseInt(dateCsr.getString(3));
                                        int foodTotal = foodQty*foodPrice;
                                        monthlySale+=foodTotal;
                                    }
                                }

                                csr = dbh.getStockDatesOfSpecifiedMonth(alSpinnerDates.get(i));
                                int monthlyPurchase=0;
                                while(csr.moveToNext()){
                                    Cursor dateCsr = dbh.getStockByDate(csr.getString(0));
                                    while(dateCsr.moveToNext()){
                                        int stockQty = Integer.parseInt(dateCsr.getString(2));
                                        int stockPrice = Integer.parseInt(dateCsr.getString(3));
                                        int stockTotal = stockQty*stockPrice;
                                        monthlyPurchase+=stockTotal;
                                    }
                                }
                                tvPurchase.setText(monthlyPurchase+"");
                                tvSale.setText(monthlySale+"");
                                int profitLoss = monthlySale-monthlyPurchase;
                                if(profitLoss>0){
                                    tvProfitAndLoss.setText(profitLoss+"");
                                    tvProfitAndLoss.setTextColor(getResources().getColor(R.color.main_color_bu));
                                }else{
                                    tvProfitAndLoss.setText(profitLoss+"");
                                    tvProfitAndLoss.setTextColor(getResources().getColor(R.color.main_color));
                                }
                            } catch (Exception e) {
                                Log.d("dalle", e+"");
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {}
                    });

                    adb.setView(vProfitLoss);
                    di = adb.show();
                    ivClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            di.dismiss();
                        }
                    });
                }
            });

            fabAddNewStock.setOnClickListener(new View.OnClickListener() {
                @SuppressLint({"SetTextI18n", "MissingInflatedId"})
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                    View vAddItem = getLayoutInflater().inflate(R.layout.layout_add_stock_ad, container.findViewById(R.id.ad_add_stock_root_layout));
                    DialogInterface di;

                    ImageView ivClose = vAddItem.findViewById((R.id.iv_close));
                    Button btnAddNewItem = vAddItem.findViewById((R.id.btn_add_new_item));
                    EditText etStockName = vAddItem.findViewById((R.id.et_stock_name));
                    EditText etStockQty = vAddItem.findViewById((R.id.et_stock_qty));
                    EditText etStockPrice = vAddItem.findViewById((R.id.et_stock_price));

                    adb.setView(vAddItem);
                    di = adb.show();
                    ivClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            di.dismiss();
                        }
                    });
                    btnAddNewItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String stockName = etStockName.getText().toString()+"";
                            String stockQty = etStockQty.getText().toString()+"";
                            String stockPrice = etStockPrice.getText().toString()+"";
                            try {
                                dbh.addStockItem(stockName, stockQty, stockPrice);
                                refreshLV();
                                di.dismiss();
                            } catch (Exception e) {}
                        }
                    });
                }
            });

            lvStock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String item = alStock.get(i);
                    String str[];
                    str = item.split(" x ");
                    String stockNameOG = str[0].trim();
                    str = str[1].split(" ₹ ");
                    String stockQty = str[0].trim();
                    String str2[] = str[1].split(" = ");
                    String stockPrice = str2[0].trim();
                    str2 = str[1].split(" : ");
                    String stockTotal = str2[0].trim();
                    String date[] = str2[1].trim().split("-");
                    String itemPurchaseDate = date[2]+"-"+date[1]+"-"+date[0];
                    AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                    View vAddItem = getLayoutInflater().inflate(R.layout.layout_add_stock_ad, container.findViewById(R.id.ad_add_stock_root_layout));
                    DialogInterface di;

                    ImageView ivClose = vAddItem.findViewById((R.id.iv_close));
                    Button btnAddNewItem = vAddItem.findViewById((R.id.btn_add_new_item));
                    EditText etStockName = vAddItem.findViewById((R.id.et_stock_name));
                    EditText etStockQty = vAddItem.findViewById((R.id.et_stock_qty));
                    EditText etStockPrice = vAddItem.findViewById((R.id.et_stock_price));
                    TextView tvADTitle = vAddItem.findViewById(R.id.tv_ad_title);
                    ivClose.setImageDrawable(getResources().getDrawable(R.drawable.delete_ic));
                    btnAddNewItem.setText("SAVE CHANGES");
                    tvADTitle.setText("Edit Stock Details");
                    Drawable icEdit = getResources().getDrawable(R.drawable.edit_ic);
                    icEdit.setTint(getResources().getColor(R.color.main_color));
                    icEdit.setBounds(0, 0, 60, 60);
                    tvADTitle.setCompoundDrawables(icEdit, null, null, null);
                    etStockName.setText(stockNameOG);
                    etStockQty.setText(stockQty);
                    etStockPrice.setText(stockPrice);

                    adb.setView(vAddItem);
                    di = adb.show();
                    ivClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dbh.removeStockItem(stockNameOG, stockQty, stockPrice, itemPurchaseDate);
                            refreshLV();
                            di.dismiss();
                        }
                    });
                    btnAddNewItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String stockName = etStockName.getText().toString()+"";
                            String stockQty = etStockQty.getText().toString()+"";
                            String stockPrice = etStockPrice.getText().toString()+"";
                            try {
                                dbh.updateStockItem(stockNameOG, stockName, stockQty, stockPrice, itemPurchaseDate);
                                refreshLV();
                                di.dismiss();
                            } catch (Exception e) {}
                        }
                    });
                }
            });
            refreshLV();
        } catch (Exception e) {
            Log.e("wow", e+"");
        }
        return view;
    }

    public void refreshLV(){
        Cursor csr = dbh.getStockAll();
        alStock.clear();
        int qty, price, total;
        while(csr.moveToNext()){
            String date[] = csr.getString(0).split("-");
            String dt = date[2]+"-"+date[1]+"-"+date[0];
            qty = Integer.parseInt(csr.getString(2));
            price = Integer.parseInt(csr.getString(3));
            total = qty*price;

            alStock.add(csr.getString(1)+" x "+csr.getString(2)+" ₹ "+csr.getString(3)+" = "+total+" : "+dt);
        }
        ArrayAdapter<String> ad = new ArrayAdapter<String>(context, R.layout.stock_list_item_ui, R.id.tv_stock_item, alStock);
        lvStock.setAdapter(ad);
    }

}
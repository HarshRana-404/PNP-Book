package com.pnpbook.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

        fabAddNewStock = view.findViewById(R.id.fab_add_new_stock);
        lvStock = view.findViewById(R.id.lv_stock);
        refreshLV();

        fabAddNewStock.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                View vAddItem = getLayoutInflater().inflate(R.layout.layout_add_item_ad, container.findViewById(R.id.ad_add_item_root_layout));
                DialogInterface di;

                ImageView ivClose = vAddItem.findViewById((R.id.iv_close));
                Button btnAddNewItem = vAddItem.findViewById((R.id.btn_add_new_item));
                EditText etItemName = vAddItem.findViewById((R.id.et_item_name));
                EditText etItemPrice = vAddItem.findViewById((R.id.et_item_price));
                TextView tvADTitle = vAddItem.findViewById(R.id.tv_ad_title);
                tvADTitle.setText("Add New Stock Item");
                etItemPrice.setHint("Quantity");

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
                        String itemName = etItemName.getText().toString()+"";
                        String itemQty = etItemPrice.getText().toString()+"";
                        try {
                            dbh.addStockItem(itemName, itemQty);
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
                String itemNameOG = str[0];
                str = str[1].split(" : ");
                String itemQty = str[0];
                String date[] = str[1].split("-");
                String itemPurchaseDate = date[2]+"-"+date[1]+"-"+date[0];
                AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                View vAddItem = getLayoutInflater().inflate(R.layout.layout_add_item_ad, container.findViewById(R.id.ad_add_item_root_layout));
                DialogInterface di;

                ImageView ivClose = vAddItem.findViewById((R.id.iv_close));
                Button btnAddNewItem = vAddItem.findViewById((R.id.btn_add_new_item));
                EditText etItemName = vAddItem.findViewById((R.id.et_item_name));
                EditText etItemPrice = vAddItem.findViewById((R.id.et_item_price));
                TextView tvADTitle = vAddItem.findViewById(R.id.tv_ad_title);
                ivClose.setImageDrawable(getResources().getDrawable(R.drawable.delete_ic));
                btnAddNewItem.setText("SAVE CHANGES");
                tvADTitle.setText("Edit Item");
                etItemPrice.setHint("Quantity");
                etItemName.setText(itemNameOG);
                etItemPrice.setText(itemQty);

                adb.setView(vAddItem);
                di = adb.show();
                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dbh.removeStockItem(itemNameOG, itemQty, itemPurchaseDate);
                        refreshLV();
                        di.dismiss();
                    }
                });
                btnAddNewItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String itemName = etItemName.getText().toString()+"";
                        String itemQty = etItemPrice.getText().toString()+"";
                        try {
                            dbh.updateStockItem(itemNameOG, itemName, itemQty, itemPurchaseDate);
                            refreshLV();
                            di.dismiss();
                        } catch (Exception e) {}
                    }
                });
            }
        });
        
        return view;
    }

    public void refreshLV(){
        Cursor csr = dbh.getStockAll();
        alStock.clear();
        while(csr.moveToNext()){
            String date[] = csr.getString(0).split("-");
            String dt = date[2]+"-"+date[1]+"-"+date[0];
            alStock.add(csr.getString(1)+" x "+csr.getString(2)+" : "+dt);
        }
        ArrayAdapter<String> ad = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,  alStock);
        lvStock.setAdapter(ad);
    }

}
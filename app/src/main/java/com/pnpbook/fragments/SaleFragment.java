package com.pnpbook.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pnpbook.R;
import com.pnpbook.adapters.ExpandableSalesAdapter;
import com.pnpbook.adapters.SaleAdapter;
import com.pnpbook.database.DBHelper;
import com.pnpbook.models.ExpandableSalesModel;
import com.pnpbook.models.SalesModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import kotlinx.coroutines.selects.WhileSelectKt;

public class SaleFragment extends Fragment {

    FloatingActionButton fabAddSaleItem;
    BottomSheetDialog bsAddSaleItem;
    public static DBHelper dbh;

    Spinner spFoodItem;
    EditText etFoodQuantity;
    public static TextView tvItemValue, tvTodaySale;

    public static RecyclerView rvSales;
    public static SaleAdapter saleAdapter;
    public static ExpandableSalesAdapter expandableSalesAdapter;
    public static ArrayList<SalesModel> alSales = new ArrayList<>();

    public static Cursor csr;

    public static Context con;

    public SaleFragment() {
        // Required empty public constructor
    }
    public static SaleFragment newInstance(String param1, String param2) {
        SaleFragment fragment = new SaleFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    @SuppressLint({"MissingInflatedId", "NotifyDataSetChanged"})
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale, container, false);

        dbh = new DBHelper(getContext(), null, null, 1, null);

        fabAddSaleItem = view.findViewById(R.id.fab_add_sale_item);

        tvTodaySale = view.findViewById(R.id.tv_sale_today_sale);

        contextGain(getContext());
        try {
            rvSales = view.findViewById(R.id.rv_sale);
            rvSales.setLayoutManager(new LinearLayoutManager(con));
            saleAdapter = new SaleAdapter(con, alSales);
            expandableSalesAdapter = new ExpandableSalesAdapter(container);
            rvSales.setAdapter(saleAdapter);
            refreshRV();
            refreshLayoutValues();
        } catch (Exception e) {
//            Toast.makeText(con, e+"", Toast.LENGTH_SHORT).show();
        }
        fabAddSaleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bsAddSaleItem = new BottomSheetDialog(getContext());
                View v = inflater.inflate(R.layout.add_sale_item_bottom_sheet, null);

                spFoodItem = v.findViewById(R.id.sp_food_items);
                etFoodQuantity = v.findViewById(R.id.et_food_quantity);
                tvItemValue = v.findViewById(R.id.tv_item_value);
                Button btnSaveSaleDetails = v.findViewById(R.id.btn_add_sale_details);
                ImageView ivClose = v.findViewById(R.id.iv_add_sale_item_close);
                ImageButton ibMinusQuantity = v.findViewById(R.id.ib_minus_quantity);
                ImageButton ibPlusQuantity = v.findViewById(R.id.ib_plus_quantity);

                refreshSpinnerValues();
                refreshLayoutValues();
                btnSaveSaleDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            String foodItem="";
                            String foodQuantity="";
                            // date, item_name, item_qty
                            foodItem = spFoodItem.getSelectedItem().toString()+"";
                            foodQuantity = etFoodQuantity.getText().toString()+"";
                            if(foodItem.equals("") || foodQuantity.equals("")){}
                            else{
                                if(dbh.itemPresentInSale(getCurrentDate(), foodItem)){
                                    dbh.updateSale(getCurrentDate(), foodItem, foodQuantity);
                                }else{
                                    dbh.insertItem(foodItem, foodQuantity);
                                }
                                bsAddSaleItem.dismiss();
                                refreshRV();
                            }
                        } catch (Exception e) {
                            Toast.makeText(con, e+"", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                ibMinusQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Integer in = Integer.parseInt(etFoodQuantity.getText().toString());
                        if(in>1){
                            in--;
                        }
                        etFoodQuantity.setText(in+"");
                        refreshLayoutValues();
                    }
                });
                ibPlusQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Integer in = Integer.parseInt(etFoodQuantity.getText().toString());
                        in++;
                        etFoodQuantity.setText(in+"");
                        refreshLayoutValues();
                    }
                });
                etFoodQuantity.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(etFoodQuantity.getText().toString().equals("")){
                            etFoodQuantity.setText("1");
                        }
                        refreshLayoutValues();
                    }
                    @Override
                    public void afterTextChanged(Editable editable) {}
                });
                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bsAddSaleItem.dismiss();
                    }
                });
                spFoodItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        refreshLayoutValues();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {}
                });

                bsAddSaleItem.setContentView(v);
                bsAddSaleItem.show();
            }
        });

        return view;
    }
    @SuppressLint("SetTextI18n")
    public void refreshLayoutValues(){
        try {
            String foodItem="";
            String foodQuantity="";
            foodItem = spFoodItem.getSelectedItem().toString()+"";
            foodQuantity = etFoodQuantity.getText().toString()+"";
            int pr = dbh.getFoodItemPrice(foodItem);
            int qt = Integer.parseInt(foodQuantity);
            int val = pr*qt;
            tvItemValue.setText("₹"+val+"/-");
        } catch (Exception e) {}
    }
    public void refreshSpinnerValues(){
        try {
            ArrayList<String> alFoodItems = new ArrayList<>();
            Cursor csr = dbh.getFoodItems();
            while (csr.moveToNext()){
                alFoodItems.add(csr.getString(1));
            }
            spFoodItem.setAdapter(new ArrayAdapter<String>(con, android.R.layout.simple_list_item_1, alFoodItems));
        } catch (Exception e) {
        }
    }
    public void contextGain(Context con){
        SaleFragment.con = con;
    }
    public static String getCurrentDate(){
        Calendar cl = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = sdf.format(cl.getTime());
        return todayDate;
    }
    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    public static void refreshRV(){
        try {
            ArrayList<String> alDates = getDates();
            alSales.clear();
            for (int i=0;i<alDates.size();i++) {
                Cursor csr = dbh.getSaleByDate(alDates.get(i));
                int itemCnt=1;
                int todaysTotal=0;
                ArrayList<ExpandableSalesModel> alExpandableSales = new ArrayList<>();
                while (csr.moveToNext()){
                    String fName = csr.getString(1);
                    int fQty = Integer.parseInt(csr.getString(3));
                    int fPrice = Integer.parseInt(csr.getString(2));
                    int fAmount = (fQty*fPrice);
                    todaysTotal = (fAmount+todaysTotal);
                    alExpandableSales.add(new ExpandableSalesModel(itemCnt+".", fName, fQty, fAmount, getCurrentDate()));
                    itemCnt++;
                }
                String date[] = alDates.get(i).split("-");
//                tvTodaySale.setText("Today's Sale: ₹"+(todaysTotal));
                alSales.add(new SalesModel(date[2]+"-"+date[1]+"-"+date[0], todaysTotal+"", alExpandableSales));
                saleAdapter.notifyDataSetChanged();
            }
            alDates.clear();
        } catch (Exception e) {
            Log.e("wow", e.toString());
        }
    }

    public static ArrayList<String> getDates(){
        ArrayList<String> alDates = new ArrayList<>();
        csr = dbh.getDates();
        while (csr.moveToNext()){
            alDates.add(csr.getString(0));
        }
        return alDates;
    }
}
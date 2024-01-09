package com.pnpbook.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pnpbook.R;
import com.pnpbook.database.DBHelper;

import java.util.ArrayList;

public class AnalysisFragment extends Fragment {

    Button btnStartDate, btnEndDate;

    FloatingActionButton fabAnalysisShare;
    TextView tvGrandTotal;
    ListView rvAnalysis;

    String startDate, endDate;

    String analysisStr="";
    DBHelper dbh = null;

    Context context;


    ArrayList<String> alAnalysis = new ArrayList<>();
    public AnalysisFragment() {
        // Required empty public constructor
    }
    public static AnalysisFragment newInstance(String param1, String param2) {
        AnalysisFragment fragment = new AnalysisFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container.removeAllViews();
        View view = inflater.inflate(R.layout.fragment_analysis, container, false);
        this.context = view.getContext();
        dbh = new DBHelper(getContext(), null, null,  1, null);
        rvAnalysis = view.findViewById(R.id.rv_analysis);
        fabAnalysisShare = view.findViewById(R.id.fab_analysis_share);
        fabAnalysisShare.setVisibility(View.GONE);
        btnStartDate = view.findViewById(R.id.btn_start_date);
        btnEndDate = view.findViewById(R.id.btn_end_date);
        tvGrandTotal = view.findViewById(R.id.tv_analysis_sale_till_date);

        String today[] = SaleFragment.getCurrentDate().split("-");

        try {
            btnStartDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePickerDialog dp = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            i1 = (i1+1);
                            startDate = i+"-"+i1+"-"+i2;
                            String[] d = startDate.split("-");
                            if(Integer.parseInt(d[0])<=9){
                                d[0] = "0"+d[0];
                            }
                            if(Integer.parseInt(d[1])<=9){
                                d[1] = "0"+d[1];
                            }
                            if(Integer.parseInt(d[2])<=9){
                                d[2] = "0"+d[2];
                            }
                            startDate = d[0]+"-"+d[1]+"-"+d[2];
                            btnStartDate.setText(i2+"-"+i1+"-"+i);
                            if(!btnStartDate.getText().toString().endsWith("e") && !btnEndDate.getText().toString().endsWith("e")){
                                loadAnalysisData();
                            }
                        }
                    }, Integer.parseInt(today[0]), Integer.parseInt((today[1]))-1, Integer.parseInt(today[2]));
                    dp.show();
                }
            });

            btnEndDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePickerDialog dp2 = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            i1 = (i1+1);
                            endDate = i+"-"+i1+"-"+i2;
                            String[] d = endDate.split("-");
                            if(Integer.parseInt(d[0])<=9){
                                d[0] = "0"+d[0];
                            }
                            if(Integer.parseInt(d[1])<=9){
                                d[1] = "0"+d[1];
                            }
                            if(Integer.parseInt(d[2])<=9){
                                d[2] = "0"+d[2];
                            }
                            endDate = d[0]+"-"+d[1]+"-"+d[2];
                            btnEndDate.setText(i2+"-"+i1+"-"+i);
                            if(!btnStartDate.getText().toString().endsWith("e") && !btnEndDate.getText().toString().endsWith("e")){
                                loadAnalysisData();
                            }
                        }
                    }, Integer.parseInt(today[0]), Integer.parseInt(today[1])-1, Integer.parseInt(today[2]));
                    dp2.show();
                }
            });
        } catch (Exception e) {
            Log.e("wow", e+"");
        }

        fabAnalysisShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inShare = new Intent(Intent.ACTION_SEND);
                inShare.setType("text/plain");
                inShare.putExtra(Intent.EXTRA_TEXT, analysisStr.trim());
                inShare.putExtra(Intent.EXTRA_SUBJECT, "Patnagar Panipuri Sale From *"+btnStartDate.getText().toString()+"* To *"+btnEndDate.getText().toString()+"*");
                context.startActivity(Intent.createChooser(inShare, "Share"));
            }
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    public void loadAnalysisData(){
        try {
            analysisStr="Patnagar Panipuri Sale From *"+btnStartDate.getText().toString()+"* To *"+btnEndDate.getText().toString()+"*\n\n";
            fabAnalysisShare.setVisibility(View.VISIBLE);
            int grandTotal = 0;
            alAnalysis.clear();
            Cursor csr = dbh.getFoodsBetweenDates(startDate, endDate);
            while(csr.moveToNext()){
                String foodName=csr.getString(0);
                int foodQty=0, foodPrice=0, foodTotal=0;
                Cursor csrQtyPr = dbh.getFoodQtyBetweenDates(foodName, startDate, endDate);
                while(csrQtyPr.moveToNext()){
                    foodQty = Integer.parseInt(csrQtyPr.getString(0));
                    foodPrice = Integer.parseInt(csrQtyPr.getString(1));
                }
                foodTotal = foodQty * foodPrice;
                grandTotal += foodTotal;
                analysisStr+=(foodName+" - " +foodQty + " x "+foodPrice + " = " + foodTotal+"\n\n");
                alAnalysis.add(foodName+" - " +foodQty + " x "+foodPrice + " = " + foodTotal);
            }
            if(grandTotal!=0){
                tvGrandTotal.setText("Total: " + grandTotal);
                analysisStr += "Grand Total: "+grandTotal;
                ArrayAdapter<String> ad = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,  alAnalysis);
                rvAnalysis.setAdapter(ad);
            }
        } catch (Exception e) {
            Log.e("wow", e+"");
        }
    }
}
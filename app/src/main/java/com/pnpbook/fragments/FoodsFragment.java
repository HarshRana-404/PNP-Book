package com.pnpbook.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pnpbook.R;
import com.pnpbook.adapters.FoodsAdapter;
import com.pnpbook.database.DBHelper;
import com.pnpbook.models.FoodsModel;

import java.util.ArrayList;

public class FoodsFragment extends Fragment {

    RecyclerView rvFoods;
    public static ArrayList<FoodsModel> alFoodsModel = new ArrayList<>();
    public static FoodsAdapter foodsAdapter;
    public static DBHelper dbh;

    TextView tvAddItem;

    public static Context context;

    public FoodsFragment() {
        // Required empty public constructor
    }

    public void contextGain(Context context){
        context = context;
    }

    public static FoodsFragment newInstance(String param1, String param2) {
        FoodsFragment fragment = new FoodsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container.removeAllViews();
        View view = inflater.inflate(R.layout.fragment_foods, container, false);
        dbh = new DBHelper(getContext(), null, null,  1, null);

        FoodsAdapter fa = new FoodsAdapter(container);

        rvFoods = view.findViewById(R.id.rv_foods);
        tvAddItem = view.findViewById(R.id.tv_tb_add_item);

        rvFoods.setLayoutManager(new LinearLayoutManager(getContext()));
        foodsAdapter = new FoodsAdapter(getContext(), alFoodsModel);
        rvFoods.setAdapter(foodsAdapter);

        refreshRV();

        tvAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                View vAddItem = getLayoutInflater().inflate(R.layout.layout_add_item_ad, container.findViewById(R.id.ad_add_item_root_layout));
                DialogInterface di;

                ImageView ivClose = vAddItem.findViewById((R.id.iv_close));
                Button btnAddNewItem = vAddItem.findViewById((R.id.btn_add_new_item));
                EditText etItemName = vAddItem.findViewById((R.id.et_item_name));
                EditText etItemPrice = vAddItem.findViewById((R.id.et_item_price));

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
                        String itemPrice = etItemPrice.getText().toString()+"";
                        try {
                            if(itemName.equals("") || itemPrice.equals("")){}
                            else{
                                int cnt = dbh.getItemCount();
                                dbh.addFoodItem((cnt+1)+"", itemName, itemPrice);
                                refreshRV();
                                di.dismiss();
                            }
                        } catch (Exception e) {}
                    }
                });
            }
        });
        return view;
    }
    @SuppressLint("NotifyDataSetChanged")
    public static void refreshRV(){
        try {
            SaleFragment sf = new SaleFragment();
            sf.contextGain(context);
            sf.refreshSpinnerValues();
            alFoodsModel.clear();
            int cnt=1;
            Cursor csr = dbh.getFoodItems();
            while (csr.moveToNext()){
                alFoodsModel.add(new FoodsModel(cnt, csr.getString(1), Integer.parseInt(csr.getString(2))));
                cnt++;
            }
            foodsAdapter.notifyDataSetChanged();
        } catch (Exception e) {}
    }
}
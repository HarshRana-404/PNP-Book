package com.pnpbook.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pnpbook.R;
import com.pnpbook.adapters.FoodsAdapter;
import com.pnpbook.database.DBHelper;
import com.pnpbook.models.FoodsModel;

import java.util.ArrayList;

public class FoodsFragment extends Fragment {

    RecyclerView rvFoods;
    ArrayList<FoodsModel> alFoodsModel = new ArrayList<>();
    FoodsAdapter foodsAdapter;
    DBHelper dbh;

    TextView tvAddItem;

    public FoodsFragment() {
        // Required empty public constructor
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

        rvFoods = view.findViewById(R.id.rv_foods);
        tvAddItem = view.findViewById(R.id.tv_tb_add_item);

        rvFoods.setLayoutManager(new LinearLayoutManager(getContext()));
        foodsAdapter = new FoodsAdapter(getContext(), alFoodsModel);
        rvFoods.setAdapter(foodsAdapter);

        alFoodsModel.add(new FoodsModel(1, "Dahi Puri", 50));
        alFoodsModel.add(new FoodsModel(2, "Sev Puri", 50));
        alFoodsModel.add(new FoodsModel(3, "Pani Puri", 20));
        alFoodsModel.add(new FoodsModel(4, "Friends Pack", 120));
        alFoodsModel.add(new FoodsModel(5, "Family Pack", 190));
        alFoodsModel.add(new FoodsModel(6, "Jeera Masala", 10));
        alFoodsModel.add(new FoodsModel(7, "Mango Juice", 20));
        alFoodsModel.add(new FoodsModel(8, "Limca", 20));
        alFoodsModel.add(new FoodsModel(9, "Kala Khatta", 20));
        alFoodsModel.add(new FoodsModel(10, "Lemon Soda", 20));
        foodsAdapter.notifyDataSetChanged();

        tvAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                View vAddItem = getLayoutInflater().inflate(R.layout.layout_add_item_ad, container.findViewById(R.id.ad_add_item_root_layout));
                DialogInterface di;
                ImageView ivClose = vAddItem.findViewById((R.id.iv_close));
                adb.setView(vAddItem);
                di = adb.show();
                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        di.dismiss();
                    }
                });
            }
        });
        return view;
    }
}
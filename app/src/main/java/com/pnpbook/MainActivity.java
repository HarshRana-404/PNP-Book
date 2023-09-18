package com.pnpbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pnpbook.adapters.FoodsAdapter;
import com.pnpbook.database.DBHelper;
import com.pnpbook.fragments.AnalysisFragment;
import com.pnpbook.fragments.FoodsFragment;
import com.pnpbook.fragments.SaleFragment;
import com.pnpbook.fragments.StockFragment;
import com.pnpbook.models.FoodsModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bnavMain;

    @SuppressLint({"NotifyDataSetChanged", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new SaleFragment());

        bnavMain = findViewById(R.id.b_nav_main);
        bnavMain.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.mi_sale){
                    loadFragment(new SaleFragment());
                }
                else if(item.getItemId()==R.id.mi_foods){
                    loadFragment(new FoodsFragment());
                }
                else if(item.getItemId()==R.id.mi_stock){
                    loadFragment(new StockFragment());
                }
                else if(item.getItemId()==R.id.mi_analysis){
                    loadFragment(new AnalysisFragment());
                }
                return true;
            }
        });
    }
    public void loadFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_main_container, fragment);
        fm.popBackStack();
        ft.commit();
    }
}
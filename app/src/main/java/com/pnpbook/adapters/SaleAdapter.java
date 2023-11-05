package com.pnpbook.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pnpbook.R;
import com.pnpbook.models.SalesModel;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class SaleAdapter extends RecyclerView.Adapter<SaleAdapter.ViewHolder> {

    Context context;
    ArrayList<SalesModel> alSales;
    ExpandableSalesAdapter expandableSalesAdapter;
    public SaleAdapter(Context context, ArrayList<SalesModel> alSales) {
        this.context = context;
        this.alSales = alSales;
    }

    @NonNull
    @Override
    public SaleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewSale = LayoutInflater.from(context).inflate(R.layout.layout_sale_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(viewSale);
        return viewHolder;
    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull SaleAdapter.ViewHolder holder, int position) {
        try{
            holder.tvSaleDate.setText(alSales.get(position).saleDate+"");
            holder.tvDateTotal.setText(alSales.get(position).totalSale+"");
            holder.rvExpFoods.setLayoutManager(new LinearLayoutManager(context));
            expandableSalesAdapter = new ExpandableSalesAdapter(context, alSales.get(position).alExpandableSale);
            holder.rvExpFoods.setAdapter(expandableSalesAdapter);
            expandableSalesAdapter.notifyDataSetChanged();

            holder.rlExpander.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(alSales.get(position).isExpanded){
                        holder.ivArrowDown.setRotation(0);
                        holder.rvExpFoods.setVisibility(View.GONE);
                    }else{
                        holder.ivArrowDown.setRotation(180);
                        holder.rvExpFoods.setVisibility(View.VISIBLE);
                    }
                    alSales.get(position).isExpanded = !alSales.get(position).isExpanded;
                }
            });

        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return alSales.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlExpander;

        TextView tvSaleDate, tvDateTotal;
        ImageView ivArrowDown;
        RecyclerView rvExpFoods;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rlExpander = itemView.findViewById(R.id.rl_expander);
            tvSaleDate = itemView.findViewById(R.id.tv_sale_date);
            tvDateTotal = itemView.findViewById(R.id.tv_date_total);
            ivArrowDown = itemView.findViewById(R.id.iv_arrow_down);
            rvExpFoods = itemView.findViewById(R.id.expandable_sales_rv);
            rvExpFoods.setVisibility(View.GONE);
        }
    }
}

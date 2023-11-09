package com.pnpbook.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pnpbook.R;
import com.pnpbook.database.DBHelper;
import com.pnpbook.models.ExpandableSalesModel;
import com.pnpbook.models.SalesModel;

import java.util.ArrayList;

public class SaleAdapter extends RecyclerView.Adapter<SaleAdapter.ViewHolder> {

    Context context;
    ArrayList<SalesModel> alSales;
    ExpandableSalesAdapter expandableSalesAdapter;
    DBHelper dbh;
    public SaleAdapter(Context context, ArrayList<SalesModel> alSales) {
        this.context = context;
        this.alSales = alSales;
    }

    @NonNull
    @Override
    public SaleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewSale = LayoutInflater.from(context).inflate(R.layout.layout_sale_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(viewSale);
        dbh = new DBHelper(context, null, null,  1, null);
        return viewHolder;
    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull SaleAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try{
            holder.tvSaleDate.setText(alSales.get(position).saleDate+"");
            holder.tvDateTotal.setText(alSales.get(position).totalSale+"");
            holder.rvExpFoods.setLayoutManager(new LinearLayoutManager(context));
            expandableSalesAdapter = new ExpandableSalesAdapter(context, alSales.get(position).alExpandableSale);
            holder.rvExpFoods.setAdapter(expandableSalesAdapter);
            expandableSalesAdapter.notifyDataSetChanged();

            holder.rlExpander.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    try{
                        String dateDB[] = alSales.get(position).saleDate.split("-");
                        String dateFinalDB = dateDB[2]+"-"+dateDB[1]+"-"+dateDB[0];
                        Cursor csr = dbh.getSaleByDate(dateFinalDB);
                        int todaysTotal=0;

                        String title = "*Patnagar Panipuri "+alSales.get(position).saleDate+"*\n\n";
                        String finalBill=title, item;
                        while (csr.moveToNext()){
                            String fName = csr.getString(1);
                            int fQty = Integer.parseInt(csr.getString(3));
                            int fPrice = Integer.parseInt(csr.getString(2));
                            int fAmount = (fQty*fPrice);
                            item = fName + "\n";
                            item += fQty + " x ";
                            item += fPrice + " = ";
                            item += fAmount + "\n\n";
                            todaysTotal = (fAmount+todaysTotal);
                            finalBill+=item;
                        }
                        finalBill+="Grand Total: *"+todaysTotal+"*";
                        Intent inShare = new Intent(Intent.ACTION_SEND);
                        inShare.setType("text/plain");
                        inShare.putExtra(Intent.EXTRA_TEXT, finalBill.trim());
                        inShare.putExtra(Intent.EXTRA_SUBJECT, alSales.get(position).saleDate);
                        context.startActivity(Intent.createChooser(inShare, "Share"));
                    } catch (Exception e) {
                        Toast.makeText(context, e+"", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });

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

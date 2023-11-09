package com.pnpbook.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.pnpbook.R;
import com.pnpbook.database.DBHelper;
import com.pnpbook.fragments.SaleFragment;
import com.pnpbook.models.ExpandableSalesModel;

import java.util.ArrayList;

public class ExpandableSalesAdapter extends RecyclerView.Adapter<ExpandableSalesAdapter.ViewHolder> {

    Context context;
    ArrayList<ExpandableSalesModel> alExpandableSalesModel;

    ViewGroup container=null;
    DBHelper dbh;

    public ExpandableSalesAdapter(Context context, ArrayList<ExpandableSalesModel> alExpandableSalesModel) {
        this.context = context;
        this.alExpandableSalesModel = alExpandableSalesModel;
        dbh = new DBHelper(context, null, null,  1, null);
    }

    public ExpandableSalesAdapter(ViewGroup container) {
        this.container = container;
    }

    @NonNull
    @Override
    public ExpandableSalesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.expandable_sale_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        dbh = new DBHelper(context, null, null,  1, null);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ExpandableSalesAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            holder.tvFoodIndex.setText(alExpandableSalesModel.get(position).foodIndex+"");
            holder.tvFoodName.setText(alExpandableSalesModel.get(position).foodName);
            holder.tvFoodQuantity.setText(alExpandableSalesModel.get(position).foodQuantity+"");
            holder.tvFoodAmount.setText("₹"+ alExpandableSalesModel.get(position).foodAmount);

            holder.llExpandable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(context);
                    View vAddItem = View.inflate(context, R.layout.layout_add_item_ad, container);
                    DialogInterface di;

                    TextView tvAddTitle = vAddItem.findViewById((R.id.tv_ad_title));
                    ImageView ivClose = vAddItem.findViewById((R.id.iv_close));
                    Button btnAddNewItem = vAddItem.findViewById((R.id.btn_add_new_item));
                    EditText etItemName = vAddItem.findViewById((R.id.et_item_name));
                    EditText etItemPrice = vAddItem.findViewById((R.id.et_item_price));

                    etItemName.setEnabled(false);
                    etItemPrice.setHint("Quantity");

                    String saleDate = alExpandableSalesModel.get(position).foodDate;
                    String foodName = alExpandableSalesModel.get(position).foodName;

                    try{
                        etItemName.setText(alExpandableSalesModel.get(position).foodName);
                        etItemPrice.setText(alExpandableSalesModel.get(position).foodQuantity+"");
                    } catch (Exception e) {
//                        Toast.makeText(context, e+"", Toast.LENGTH_SHORT).show();
                    }

                    adb.setView(vAddItem);
                    di = adb.show();

                    ivClose.setImageResource(R.drawable.delete_ic);
                    btnAddNewItem.setText("SAVE ITEM DETAILS");
                    tvAddTitle.setText("Edit Details");

                    etItemPrice.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if(etItemPrice.getText().toString().equals("0")){
                                etItemPrice.setText("1");
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                    btnAddNewItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(etItemPrice.getText().toString().equals("")){
                                Toast.makeText(context, "Quantity is required!", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, saleDate, Toast.LENGTH_SHORT).show();
                                dbh.updateFoodInSale(foodName, etItemPrice.getText().toString(), saleDate);
                                SaleFragment.refreshRV();
                                di.dismiss();
                            }
                        }
                    });
                    ivClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dbh.removeFoodFromSale(foodName, saleDate);
                            SaleFragment.refreshRV();
                            di.dismiss();
                        }
                    });

                }
            });

        } catch (Exception e) {
            Log.e("wow", e+"");
        }
//        holder.tvFoodIndex.setText(alFoodsModel.get(position).foodIndex+"");
//        holder.tvFoodName.setText(alFoodsModel.get(position).foodName);
//        holder.tvFoodPrice.setText("₹"+alFoodsModel.get(position).foodPrice+"/-");
//        holder.cvFoodItem.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("UseCompatLoadingForDrawables")
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder adb = new AlertDialog.Builder(context);
//                View vAddItem = View.inflate(context, R.layout.layout_add_item_ad, container);
//                DialogInterface di;
//
//                TextView tvAddTitle = vAddItem.findViewById((R.id.tv_ad_title));
//                ImageView ivClose = vAddItem.findViewById((R.id.iv_close));
//                Button btnAddNewItem = vAddItem.findViewById((R.id.btn_add_new_item));
//                EditText etItemName = vAddItem.findViewById((R.id.et_item_name));
//                EditText etItemPrice = vAddItem.findViewById((R.id.et_item_price));
//
//                adb.setView(vAddItem);
//                di = adb.show();
//
//                String foodToBeEdited = alFoodsModel.get(position).foodName;
//
//                ivClose.setImageResource(R.drawable.delete_ic);
//                btnAddNewItem.setText("SAVE ITEM DETAILS");
//                tvAddTitle.setText("Edit Details");
//
//                try{
//                    etItemName.setText(alFoodsModel.get(position).foodName);
//                    etItemPrice.setText(String.valueOf(alFoodsModel.get(position).foodPrice));
//                } catch (Exception e) {}
//
//                btnAddNewItem.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dbh.updateFoodItem(foodToBeEdited, etItemName.getText().toString(), etItemPrice.getText().toString());
//                        FoodsFragment.context = context;
//                        FoodsFragment.refreshRV();
//                        di.dismiss();
//                    }
//                });
//                ivClose.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        di.dismiss();
//                        dbh.removeFoodItem(alFoodsModel.get(position).foodName);
//                        SaleFragment sf = null;
//                        sf.contextGain(context);
//                        sf.refreshSpinnerValues();
//                        Toast.makeText(context, alFoodsModel.get(position).foodName + " deleted!", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return alExpandableSalesModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llSaleItem;

        LinearLayout llExpandable;
        TextView tvFoodIndex, tvFoodName, tvFoodQuantity, tvFoodAmount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            llSaleItem = itemView.findViewById(R.id.ll_sale_item);
            tvFoodIndex = itemView.findViewById(R.id.tv_exp_food_index);
            tvFoodName = itemView.findViewById(R.id.tv_exp_food_name);
            tvFoodQuantity = itemView.findViewById(R.id.tv_exp_food_qty);
            tvFoodAmount = itemView.findViewById(R.id.tv_exp_food_amt);
            llExpandable = itemView.findViewById(R.id.ll_exp_sale_item);
        }
    }
}

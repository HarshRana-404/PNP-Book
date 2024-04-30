package com.pnpbook.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.media.Image;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pnpbook.R;
import com.pnpbook.database.DBHelper;
import com.pnpbook.fragments.FoodsFragment;
import com.pnpbook.fragments.SaleFragment;
import com.pnpbook.models.FoodsModel;

import java.util.ArrayList;

public class FoodsAdapter extends RecyclerView.Adapter<FoodsAdapter.ViewHolder> {

    Context context;
    ArrayList<FoodsModel> alFoodsModel;

    ViewGroup container=null;
    DBHelper dbh;

    public FoodsAdapter(Context context, ArrayList<FoodsModel> alFoodsModel) {
        this.context = context;
        this.alFoodsModel = alFoodsModel;
    }
    public FoodsAdapter(ViewGroup container) {
        this.container = container;
    }

    @NonNull
    @Override
    public FoodsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(com.pnpbook.R.layout.layout_food_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        dbh = new DBHelper(context, null, null,  1, null);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FoodsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvFoodIndex.setText(alFoodsModel.get(position).foodIndex+"");
        holder.tvFoodName.setText(alFoodsModel.get(position).foodName);
        holder.tvFoodPrice.setText("₹"+alFoodsModel.get(position).foodPrice+"/-");
        holder.cvFoodItem.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
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
                Drawable icEdit = context.getResources().getDrawable(R.drawable.edit_ic);
                icEdit.setTint(context.getResources().getColor(R.color.main_color));
                icEdit.setBounds(0, 0, 60, 60);
                tvAddTitle.setCompoundDrawables(icEdit, null, null, null);

                adb.setView(vAddItem);
                di = adb.show();

                String foodToBeEdited = alFoodsModel.get(position).foodName;

                ivClose.setImageResource(R.drawable.delete_ic);
                btnAddNewItem.setText("SAVE ITEM DETAILS");
                tvAddTitle.setText("Edit Details");

                try{
                    etItemName.setText(alFoodsModel.get(position).foodName);
                    etItemPrice.setText(String.valueOf(alFoodsModel.get(position).foodPrice));
                } catch (Exception e) {}

                btnAddNewItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dbh.updateFoodItem(foodToBeEdited, etItemName.getText().toString(), etItemPrice.getText().toString());
                        FoodsFragment.context = context;
                        FoodsFragment.refreshRV();
                        di.dismiss();
                    }
                });
                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try{
                            di.dismiss();
                            dbh.removeFoodItem(alFoodsModel.get(position).foodName);
                            SaleFragment sf = new SaleFragment();
                            sf.contextGain(context);
                            sf.refreshSpinnerValues();
                            FoodsFragment ff = new FoodsFragment();
                            ff.contextGain(context);
                            FoodsFragment.refreshOnlyRV();
                            Toast.makeText(context, alFoodsModel.get(position).foodName + " deleted!", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
//                            Toast.makeText(context, e+"", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return alFoodsModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout cvFoodItem;
        TextView tvFoodIndex, tvFoodName, tvFoodPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cvFoodItem = itemView.findViewById(R.id.cv_food_item);
            tvFoodIndex = itemView.findViewById(R.id.tv_food_index);
            tvFoodName = itemView.findViewById(R.id.tv_food_name);
            tvFoodPrice = itemView.findViewById(R.id.tv_food_price);
        }
    }
}

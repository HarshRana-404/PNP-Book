package com.pnpbook.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pnpbook.R;
import com.pnpbook.models.FoodsModel;

import java.util.ArrayList;

public class FoodsAdapter extends RecyclerView.Adapter<FoodsAdapter.ViewHolder> {

    Context context;
    ArrayList<FoodsModel> alFoodsModel;

    public FoodsAdapter(Context context, ArrayList<FoodsModel> alFoodsModel) {
        this.context = context;
        this.alFoodsModel = alFoodsModel;
    }

    @NonNull
    @Override
    public FoodsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(com.pnpbook.R.layout.layout_food_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FoodsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvFoodIndex.setText(alFoodsModel.get(position).foodIndex+"");
        holder.tvFoodName.setText(alFoodsModel.get(position).foodName);
        holder.tvFoodPrice.setText("₹"+alFoodsModel.get(position).foodPrice+"/-");
        holder.cvFoodItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

package com.finalyearproject.fooddelivery.Adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.finalyearproject.fooddelivery.Interfaces.FoodItemClick;
import com.finalyearproject.fooddelivery.Models.Food;
import com.finalyearproject.fooddelivery.R;
import com.finalyearproject.fooddelivery.Singletones.FoodDelivery;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    private List<Food> foods;
    private FoodItemClick foodItemClick;
    private FoodDelivery foodDelivery;

    public FoodAdapter(List<Food> foods, FoodItemClick foodItemClick) {
        this.foods = foods;
        this.foodItemClick = foodItemClick;
        foodDelivery = FoodDelivery.getInstance();
    }

    public FoodAdapter(List<Food> foods) {
        this.foods = foods;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.name.setText(foods.get(position).getName());
        holder.price.setText(String.format("Price : %s", foods.get(position).getPrice()));

        try {
            if (foodDelivery.getUserType().equals("Customer"))
                holder.orderStatus.setText(foods.get(position).getOrderStatus());
            if (foods.get(position).getOrderStatus().equals("Out for Delivery")) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (foodItemClick != null)
                            foodItemClick.onClick("Out for Delivery", position);
                    }
                });
            } else if (foods.get(position).getOrderStatus().equals("Order Delivered")) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (foodItemClick != null)
                            foodItemClick.onClick("Order Delivered", position);
                    }
                });
            } else {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (foodItemClick != null)
                            foodItemClick.onClick(position);
                    }
                });
            }
        } catch (Exception e) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (foodItemClick != null)
                        foodItemClick.onClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView orderStatus;
        private TextView price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            orderStatus = itemView.findViewById(R.id.order_status);
        }
    }
}

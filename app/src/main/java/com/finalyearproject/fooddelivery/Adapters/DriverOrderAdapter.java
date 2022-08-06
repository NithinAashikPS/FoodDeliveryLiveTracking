package com.finalyearproject.fooddelivery.Adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.finalyearproject.fooddelivery.Activities.QRCodeActivity;
import com.finalyearproject.fooddelivery.Interfaces.FoodItemClick;
import com.finalyearproject.fooddelivery.Models.Food;
import com.finalyearproject.fooddelivery.R;
import com.finalyearproject.fooddelivery.Singletones.Tracker;

import java.util.List;

public class DriverOrderAdapter extends RecyclerView.Adapter<DriverOrderAdapter.ViewHolder> {

    private List<Food> foods;
    private FoodItemClick foodItemClick;
    private Intent intent;

    private Tracker tracker;

    public DriverOrderAdapter(List<Food> foods, FoodItemClick foodItemClick) {
        this.foods = foods;
        this.foodItemClick = foodItemClick;
        tracker = Tracker.getInstance();
    }

    public DriverOrderAdapter(List<Food> foods) {
        this.foods = foods;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_driver_item, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.optionButton);
        popupMenu.getMenuInflater().inflate(R.menu.driver_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                intent = new Intent(holder.itemView.getContext(), QRCodeActivity.class);
                switch (item.getItemId()) {
                    case R.id.pick_up:
                        intent.putExtra("type", "PICKUP");
                        break;
                    case R.id.deliver:
                        tracker.setCustomerId(foods.get(position).getCustomer());
                        tracker.setOrderId(foods.get(position).getOrderId());
                        intent.putExtra("type", "DELIVER");
                        break;
                }
                holder.itemView.getContext().startActivity(intent);
                return false;
            }
        });

        holder.optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu.show();
            }
        });
        holder.name.setText(foods.get(position).getName());
        holder.price.setText(String.format("Price : %s", foods.get(position).getPrice()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (foodItemClick != null)
                    foodItemClick.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private ImageButton optionButton;
        private TextView price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            optionButton = itemView.findViewById(R.id.option_button);
        }
    }
}

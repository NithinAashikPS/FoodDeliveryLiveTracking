package com.finalyearproject.fooddelivery.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.finalyearproject.fooddelivery.Adapters.DriverOrderAdapter;
import com.finalyearproject.fooddelivery.Adapters.FoodAdapter;
import com.finalyearproject.fooddelivery.Interfaces.FoodItemClick;
import com.finalyearproject.fooddelivery.Models.Food;
import com.finalyearproject.fooddelivery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DriverActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private DatabaseReference foodReference;

    private List<Food> foods;
    private DriverOrderAdapter driverOrderAdapter;
    private RecyclerView foodRecyclerView;

    private String uri;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        foods = new ArrayList<>();
        foodRecyclerView = findViewById(R.id.list_orders);
        foodReference = FirebaseDatabase.getInstance().getReference("Foods");
        driverOrderAdapter = new DriverOrderAdapter(foods, new FoodItemClick() {
            @Override
            public void onClick(int position) {
                foodReference.child(foods.get(position).getId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        if (foods.get(position).getOrderStatus().equals("Order Confirmed") && task.isSuccessful()) {
                            Food food = task.getResult().getValue(Food.class);
                            uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%s,%s", food.getLocation().get(0), food.getLocation().get(1));
                        } else
                            uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%s,%s", foods.get(position).getLocation().get(0), foods.get(position).getLocation().get(1));
                        try {
                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            intent.setPackage("com.google.android.apps.maps");
                        } catch (Exception e) {
                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        }
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onClick(String type, int position) {

            }
        });
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(DriverActivity.this, LinearLayoutManager.VERTICAL, false));
        foodRecyclerView.setAdapter(driverOrderAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Orders");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    foods.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Food food = dataSnapshot1.getValue(Food.class);
                        food.setCustomer(dataSnapshot.getKey());
                        food.setOrderId(dataSnapshot1.getKey());
                        if (!food.isDelivered())
                            foods.add(food);
                    }
                }
                driverOrderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
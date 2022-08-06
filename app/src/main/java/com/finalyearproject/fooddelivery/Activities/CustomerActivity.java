package com.finalyearproject.fooddelivery.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.easywaylocation.EasyWayLocation;
import com.example.easywaylocation.Listener;
import com.finalyearproject.fooddelivery.Adapters.FoodAdapter;
import com.finalyearproject.fooddelivery.Interfaces.FoodItemClick;
import com.finalyearproject.fooddelivery.Models.Food;
import com.finalyearproject.fooddelivery.R;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerActivity extends AppCompatActivity implements Listener {

    private List<Food> foods;
    private FoodAdapter foodAdapter;
    private RecyclerView foodRecyclerView;
    private Button myOrders;

    private DatabaseReference databaseReference;
    private DatabaseReference addOrders;
    private DatabaseReference supplier;

    private FirebaseAuth firebaseAuth;
    private EasyWayLocation easyWayLocation;
    private com.finalyearproject.fooddelivery.Singletones.Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        foodRecyclerView = findViewById(R.id.list_food);
        myOrders = findViewById(R.id.my_orders);
        location = com.finalyearproject.fooddelivery.Singletones.Location.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Foods");
        addOrders = FirebaseDatabase.getInstance().getReference("Orders");
        supplier = FirebaseDatabase.getInstance().getReference("Supplier");
        easyWayLocation = new EasyWayLocation(this, false,false,this);
        easyWayLocation.startLocation();

        foods = new ArrayList<>();
        foodAdapter = new FoodAdapter(foods, new FoodItemClick() {
            @Override
            public void onClick(int position) {
                Food food = foods.get(position);
                food.setOrderStatus("Order Received");
                food.setDelivered(false);
                food.setLocation(location.getLocationList());
                new AlertDialog.Builder(CustomerActivity.this)
                        .setTitle("Place Order")
                        .setMessage("Do you really want to buy this product?")
                        .setIcon(R.drawable.ic_baseline_fastfood_24)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                addOrders
                                        .child(firebaseAuth.getUid()).push().setValue(food)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    supplier.child(foods.get(position).getSupplier()).child("Orders").push().setValue(firebaseAuth.getUid());
                                                    Toast.makeText(CustomerActivity.this, "Successfully Ordered", Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                    Toast.makeText(CustomerActivity.this, "Order Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("No", null).show();
            }

            @Override
            public void onClick(String type, int position) {

            }
        });
        myOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerActivity.this, MyOrdersActivity.class));
            }
        });
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(CustomerActivity.this, LinearLayoutManager.VERTICAL, false));
        foodRecyclerView.setAdapter(foodAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foods.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Food food = dataSnapshot.getValue(Food.class);
                    food.setId(dataSnapshot.getKey());
                    foods.add(food);
                }
                foodAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void locationOn() {

    }

    @Override
    public void currentLocation(Location location) {
        this.location.setDeviceLocation(location);
    }

    @Override
    public void locationCancelled() {

    }
}
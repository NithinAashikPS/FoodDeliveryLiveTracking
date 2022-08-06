package com.finalyearproject.fooddelivery.Activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easywaylocation.EasyWayLocation;
import com.example.easywaylocation.Listener;
import com.finalyearproject.fooddelivery.Adapters.FoodAdapter;
import com.finalyearproject.fooddelivery.Models.Food;
import com.finalyearproject.fooddelivery.R;
import com.finalyearproject.fooddelivery.Singletones.FoodDelivery;
import com.google.android.gms.location.LocationRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SupplierActivity extends AppCompatActivity implements Listener {

    private FloatingActionButton addFood;
    private Food food;

    private List<Food> foods;
    private FoodAdapter foodAdapter;
    private RecyclerView foodRecyclerView;

    private DatabaseReference databaseReference;
    private FoodDelivery foodDelivery;
    private EasyWayLocation easyWayLocation;
    private com.finalyearproject.fooddelivery.Singletones.Location location;

    private FirebaseAuth firebaseAuth;
    private Button orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier);

        addFood = findViewById(R.id.add_food_btn);
        foodRecyclerView = findViewById(R.id.list_food);
        orders = findViewById(R.id.orders);

        location = com.finalyearproject.fooddelivery.Singletones.Location.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        easyWayLocation = new EasyWayLocation(this, false,false,this);
        easyWayLocation.startLocation();

        foodDelivery = FoodDelivery.getInstance();
        food = new Food();
        foods = new ArrayList<>();
        foodAdapter = new FoodAdapter(foods);
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(SupplierActivity.this, LinearLayoutManager.VERTICAL, false));
        foodRecyclerView.setAdapter(foodAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Foods");

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SupplierActivity.this, OrdersActivity.class));
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foods.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
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

        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(SupplierActivity.this, R.style.DialogTheme);
                mBuilder.setTitle("Add Food");
                mBuilder.setInverseBackgroundForced(true);
                mBuilder.setCancelable(false);
                final EditText subject = new EditText(SupplierActivity.this);
                final EditText department = new EditText(SupplierActivity.this);
                final EditText totalHours = new EditText(SupplierActivity.this);
                mBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        food.setName(subject.getText().toString());
                        food.setPrice(department.getText().toString());
                        food.setSupplier(firebaseAuth.getUid());
                        food.setLocation(location.getLocationList());
//                        food.setQuantity(Integer.parseInt(totalHours.getText().toString()));
                        databaseReference.push().setValue(food);
                    }
                });
                mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                final AlertDialog dialog = mBuilder.create();
                LinearLayout layout = new LinearLayout(SupplierActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(editTextConfig(subject, "Food Name"));
                layout.addView(editTextConfig(department, "Food Price"));
//                layout.addView(editTextConfig(totalHours, "Food Quantity"));
                layout.setPadding(50, 40, 50, 10);
                dialog.setView(layout);
                dialog.show();
            }
        });
    }

    private EditText editTextConfig(EditText editText, String hint) {

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 10, 0, 10);
        editText.setHint(hint);
        editText.setHintTextColor(Color.argb(100, 0,0,0));
        editText.setPadding(25,25,50,25);
        editText.setTextColor(Color.BLACK);
        editText.setLayoutParams(lp);
        editText.setBackground(getResources().getDrawable(R.drawable.edittext_background));
        return editText;
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
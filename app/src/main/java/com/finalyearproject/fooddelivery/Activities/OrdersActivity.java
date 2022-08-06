package com.finalyearproject.fooddelivery.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.finalyearproject.fooddelivery.Adapters.FoodAdapter;
import com.finalyearproject.fooddelivery.Interfaces.FoodItemClick;
import com.finalyearproject.fooddelivery.Models.Food;
import com.finalyearproject.fooddelivery.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private List<String> userOrder;

    private List<Food> foods;
    private FoodAdapter foodAdapter;
    private RecyclerView foodRecyclerView;

    private QRCodeWriter qrCodeWriter;
    private JSONObject qrCodeContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        firebaseAuth = FirebaseAuth.getInstance();
        userOrder = new ArrayList<>();
        foods = new ArrayList<>();
        qrCodeContent = new JSONObject();

        qrCodeWriter = new QRCodeWriter();

        foodRecyclerView = findViewById(R.id.list_orders);
        foodAdapter = new FoodAdapter(foods, new FoodItemClick() {
            @Override
            public void onClick(int position) {
                try {
                    qrCodeContent.put("Customer", foods.get(position).getCustomer());
                    qrCodeContent.put("OrderId", foods.get(position).getOrderId());
                    BitMatrix bitMatrix = qrCodeWriter.encode(String.valueOf(qrCodeContent), BarcodeFormat.QR_CODE, 512, 512);
                    int width = bitMatrix.getWidth();
                    int height = bitMatrix.getHeight();
                    Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {
                            bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                        }
                    }
                    showImage(bmp);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onClick(String type, int position) {

            }
        });
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(OrdersActivity.this, LinearLayoutManager.VERTICAL, false));
        foodRecyclerView.setAdapter(foodAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Supplier")
                .child(firebaseAuth.getUid())
                .child("Orders")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userOrder.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (userOrder.indexOf(dataSnapshot.getValue().toString()) == -1)
                                userOrder.add(dataSnapshot.getValue().toString());
                        }

                        for (String customer : userOrder) {
                            databaseReference.child("Orders")
                                    .child(customer).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            foods.clear();
                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                Food food = dataSnapshot.getValue(Food.class);
                                                food.setCustomer(customer);
                                                food.setOrderId(dataSnapshot.getKey());
                                                if (!food.isDelivered())
                                                    foods.add(food);
                                            }
                                            foodAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void showImage(Bitmap bmp) {
        Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(bmp);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }
}
package com.finalyearproject.fooddelivery.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.finalyearproject.fooddelivery.LiveLocationService;
import com.finalyearproject.fooddelivery.R;
import com.finalyearproject.fooddelivery.Singletones.Tracker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

public class QRCodeActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    private CodeScannerView scannerView;

    private String type;
    private JSONObject qrCodeData;

    private Tracker tracker;

    private DatabaseReference databaseReference;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        intent = new Intent(QRCodeActivity.this, LiveLocationService.class);
        scannerView = findViewById(R.id.scanner_view);
        type = getIntent().getExtras().getString("type");
        mCodeScanner = new CodeScanner(this, scannerView);
        databaseReference = FirebaseDatabase.getInstance().getReference("Orders");
        tracker = Tracker.getInstance();
        if (type.equals("DELIVER")) {
            databaseReference
                    .child(tracker.getCustomerId())
                    .child(tracker.getOrderId())
                    .child("orderStatus")
                    .setValue("Order Delivered");
        }
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                try {
                    qrCodeData = new JSONObject(result.getText());
                    tracker.setCustomerId(qrCodeData.getString("Customer"));
                    tracker.setOrderId(qrCodeData.getString("OrderId"));
                    switch (type) {
                        case "PICKUP":
                            databaseReference
                                    .child(qrCodeData.getString("Customer"))
                                    .child(qrCodeData.getString("OrderId"))
                                    .child("orderStatus")
                                    .setValue("Out for Delivery");
                            startForegroundService(intent);
                            break;
                        case "DELIVER":
                            databaseReference
                                    .child(qrCodeData.getString("Customer"))
                                    .child(qrCodeData.getString("OrderId"))
                                    .child("delivered")
                                    .setValue(true);
                            stopService(intent);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCodeScanner.releaseResources();
                        QRCodeActivity.this.onBackPressed();
                    }
                });
            }
        });
        mCodeScanner.startPreview();
    }
}
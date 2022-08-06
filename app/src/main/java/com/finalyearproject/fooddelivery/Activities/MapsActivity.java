package com.finalyearproject.fooddelivery.Activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.finalyearproject.fooddelivery.Models.Food;
import com.finalyearproject.fooddelivery.R;
import com.finalyearproject.fooddelivery.Singletones.Tracker;
import com.finalyearproject.fooddelivery.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private LatLng foodLocation;
    private BitmapDescriptor deliveryIcon;
    private LatLng customer;
    private BitmapDescriptor customerIcon;

    private Food food;
    private Tracker tracker;
    private DatabaseReference databaseReference;

    private Marker customerMarker;
    private Marker driverMarker;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        deliveryIcon = getMarkerIconFromDrawable(getResources().getDrawable(R.drawable.ic_delivery_truck));
        customerIcon = getMarkerIconFromDrawable(getResources().getDrawable(R.drawable.ic_maps_and_flags));
        setContentView(binding.getRoot());

        tracker = Tracker.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Orders");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        databaseReference.child(tracker.getCustomerId())
                .child(tracker.getOrderId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        food = snapshot.getValue(Food.class);
                        customer = new LatLng(food.getLocation().get(0), food.getLocation().get(1));
                        foodLocation = new LatLng(food.getLiveLocation().get(0), food.getLiveLocation().get(1));
                        if (customerMarker != null && driverMarker != null){
                            customerMarker.remove();
                            driverMarker.remove();
                        }
                        customerMarker = mMap.addMarker(new MarkerOptions().position(customer).title("You're Here.").icon(customerIcon));
                        driverMarker = mMap.addMarker(new MarkerOptions().position(foodLocation).title("Your Food.").icon(deliveryIcon));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(foodLocation, 16));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
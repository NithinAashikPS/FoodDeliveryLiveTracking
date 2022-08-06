package com.finalyearproject.fooddelivery;

import static com.finalyearproject.fooddelivery.App.CHANNEL_ID_1;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.easywaylocation.EasyWayLocation;
import com.example.easywaylocation.Listener;
import com.finalyearproject.fooddelivery.Activities.DriverActivity;
import com.finalyearproject.fooddelivery.Singletones.Tracker;
import com.google.android.gms.location.LocationRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class LiveLocationService extends Service implements Listener {

    private Notification notification;

    private LocationRequest locationRequest;
    private EasyWayLocation easyWayLocation;
    private NotificationManager notificationManager;

    private Tracker tracker;
    private DatabaseReference databaseReference;


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onCreate() {
        super.onCreate();

        locationRequest = new LocationRequest();
        locationRequest.setInterval(60000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        easyWayLocation = new EasyWayLocation(this, false,false,this);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        tracker = Tracker.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Orders");
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent notificationIntent = new Intent(this, DriverActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        notification = new NotificationCompat.Builder(this, CHANNEL_ID_1)
                .setContentTitle("Food Delivery")
                .setContentText("Your location is monitoring.")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setColor(getColor(R.color.purple_700))
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
        easyWayLocation.startLocation();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        easyWayLocation.endUpdates();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void locationOn() {

    }

    @Override
    public void currentLocation(Location location) {
        List<Double> list = new ArrayList<>();
        list.add(location.getLatitude());
        list.add(location.getLongitude());
        databaseReference
                .child(tracker.getCustomerId())
                .child(tracker.getOrderId())
                .child("liveLocation")
                .setValue(list);
    }

    @Override
    public void locationCancelled() {

    }
}

package com.finalyearproject.fooddelivery.Singletones;

import java.util.ArrayList;
import java.util.List;

public class Location {

    private static Location location = null;
    private static android.location.Location deviceLocation = null;

    public static Location getInstance() {
        if (location == null)
            location = new Location();
        return location;
    }

    public android.location.Location getDeviceLocation() {
        return deviceLocation;
    }

    public List<Double> getLocationList() {
        List<Double> list = new ArrayList<>();
        list.add(deviceLocation.getLatitude());
        list.add(deviceLocation.getLongitude());
        return list;
    }

    public void setDeviceLocation(android.location.Location deviceLocation) {
        Location.deviceLocation = deviceLocation;
    }
}

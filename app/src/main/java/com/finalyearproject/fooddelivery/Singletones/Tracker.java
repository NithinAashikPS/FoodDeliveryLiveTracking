package com.finalyearproject.fooddelivery.Singletones;

public class Tracker {

    private static Tracker tracker = null;
    private static String CustomerId;
    private static String OrderId;

    public static Tracker getInstance() {
        if (tracker == null)
            tracker = new Tracker();
        return tracker;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }
}

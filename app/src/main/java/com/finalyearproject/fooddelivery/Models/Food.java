package com.finalyearproject.fooddelivery.Models;

import java.util.List;

public class Food {

    private String name;
    private String price;
    private String supplier;
    private String customer;
    private String id;
    private String orderId;
    private String orderStatus;
    private List<String> order;
    private List<Double> location;
    private List<Double> liveLocation;
    private boolean delivered;

    public Food() {
    }

    public Food(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public Food(String name, String price, String id) {
        this.name = name;
        this.price = price;
        this.id = id;
    }

    public Food(String name, String price, String id, List<String> order) {
        this.name = name;
        this.price = price;
        this.id = id;
        this.order = order;
    }

    public Food(String name, String price, String supplier, String id, List<String> order) {
        this.name = name;
        this.price = price;
        this.supplier = supplier;
        this.id = id;
        this.order = order;
    }

    public Food(String name, String price, String supplier, String customer, String id, List<String> order) {
        this.name = name;
        this.price = price;
        this.supplier = supplier;
        this.customer = customer;
        this.id = id;
        this.order = order;
    }

    public Food(String name, String price, String supplier, String customer, String id, String orderId, List<String> order) {
        this.name = name;
        this.price = price;
        this.supplier = supplier;
        this.customer = customer;
        this.id = id;
        this.orderId = orderId;
        this.order = order;
    }

    public Food(String name, String price, String supplier, String customer, String id, String orderId, String orderStatus, List<String> order) {
        this.name = name;
        this.price = price;
        this.supplier = supplier;
        this.customer = customer;
        this.id = id;
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.order = order;
    }

    public Food(String name, String price, String supplier, String customer, String id, String orderId, String orderStatus, List<String> order, List<Double> location) {
        this.name = name;
        this.price = price;
        this.supplier = supplier;
        this.customer = customer;
        this.id = id;
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.order = order;
        this.location = location;
    }

    public Food(String name, String price, String supplier, String customer, String id, String orderId, String orderStatus, List<String> order, List<Double> location, List<Double> liveLocation) {
        this.name = name;
        this.price = price;
        this.supplier = supplier;
        this.customer = customer;
        this.id = id;
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.order = order;
        this.location = location;
        this.liveLocation = liveLocation;
    }

    public Food(String name, String price, String supplier, String customer, String id, String orderId, String orderStatus, List<String> order, List<Double> location, List<Double> liveLocation, boolean delivered) {
        this.name = name;
        this.price = price;
        this.supplier = supplier;
        this.customer = customer;
        this.id = id;
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.order = order;
        this.location = location;
        this.liveLocation = liveLocation;
        this.delivered = delivered;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public List<Double> getLiveLocation() {
        return liveLocation;
    }

    public void setLiveLocation(List<Double> liveLocation) {
        this.liveLocation = liveLocation;
    }

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public List<String> getOrder() {
        return order;
    }

    public void setOrder(List<String> order) {
        this.order = order;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}

package com.finalyearproject.fooddelivery.Singletones;

public class FoodDelivery {

    private static FoodDelivery foodDelivery;
    private static String userType;

    public static FoodDelivery getInstance() {
        if (foodDelivery == null)
            foodDelivery = new FoodDelivery();
        return foodDelivery;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        FoodDelivery.userType = userType;
    }
}

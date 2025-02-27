package com.example.finalproject;

public class CartItem {
    private String name;
    private float price;
    private String foodGroup;
    private int amount;
    private String note;

    public CartItem(String name, float price, String foodGroup, int amount, String note) {
        this.name = name;
        this.price = price;
        this.foodGroup = foodGroup;
        this.amount = amount;
        this.note=note;
    }

    public String getName() { return name; }
    public float getPrice() { return price; }
    public String getFoodGroup() { return foodGroup; }
    public int getAmount() { return amount; }
    public float getTotalPrice() { return price * amount; }

    public void setAmount(int amount) { this.amount = amount; }
    public String getNotes() { return note; }
    public void setNotes(String note){this.note=note;}
}

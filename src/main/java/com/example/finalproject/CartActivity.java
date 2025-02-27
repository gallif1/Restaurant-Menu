package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private SqlLiteManager dbManager;
    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems = new ArrayList<>();
    private TextView totalPriceTextView;
    private Button backButton;
    private Button checkoutButton;
    private FirebaseFirestore firestore;
    public static int tableCounter = 0; // מונה שולחנות

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        dbManager = new SqlLiteManager(this);
        firestore = FirebaseFirestore.getInstance();

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalPriceTextView = findViewById(R.id.totalPriceTextView);

        // טעינת tableCounter מ-SharedPreferences
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        tableCounter = prefs.getInt("tableCounter", 0);

        loadCartItems();

        cartAdapter = new CartAdapter(cartItems, this, this::updateTotalPrice);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartAdapter);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, FirstPage.class);
            startActivity(intent);
        });

        checkoutButton = findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
            } else {
                sendOrderToFirestore();
            }
        });
    }

    private void loadCartItems() {
        SQLiteDatabase db = dbManager.getReadableDatabase();

        if (!doesTableExist(db, "orderTable")) {
            dbManager.onCreate(db);
        }

        Cursor cursor = db.rawQuery("SELECT * FROM orderTable", null);
        cartItems.clear();

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                float price = cursor.getFloat(cursor.getColumnIndexOrThrow("price"));
                String foodGroup = cursor.getString(cursor.getColumnIndexOrThrow("foodGroup"));
                int amount = cursor.getInt(cursor.getColumnIndexOrThrow("amount"));
                String notes = cursor.getString(cursor.getColumnIndexOrThrow("details")); // קריאת ההערות

                cartItems.add(new CartItem(name, price, foodGroup, amount, notes)); // הוספת הערות לפריט
            } while (cursor.moveToNext());
        } else {
            Log.d("DB_DEBUG", "Cart is empty.");
        }

        cursor.close();
        db.close();

        updateTotalPrice();
    }

    private boolean doesTableExist(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{tableName});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    private void updateTotalPrice() {
        float total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        totalPriceTextView.setText(String.format("Total: $%.2f", total));
    }

    private void sendOrderToFirestore() {
        tableCounter++; // הגדלת המונה בכל הזמנה חדשה

        Map<String, Object> orderData = new HashMap<>();
        List<Map<String, Object>> itemsList = new ArrayList<>();
        float totalPrice = 0;

        for (CartItem item : cartItems) {
            Map<String, Object> itemData = new HashMap<>();
            itemData.put("name", item.getName());
            itemData.put("price", item.getPrice());
            itemData.put("amount", item.getAmount());
            itemData.put("totalPrice", item.getTotalPrice());
            itemData.put("details", item.getNotes());
            itemsList.add(itemData);
            totalPrice += item.getTotalPrice();
        }

        orderData.put("tableNum", tableCounter); // מונה ייחודי לכל הזמנה
        orderData.put("items", itemsList);
        orderData.put("totalPrice", totalPrice);
        orderData.put("orderDate", new Date());

        firestore.collection("orders")
                .add(orderData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Your order has been sent successfully! Table: " + tableCounter, Toast.LENGTH_SHORT).show();

                    // שמירת tableCounter ב-SharedPreferences
                    SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("tableCounter", tableCounter);
                    editor.apply();

                    dbManager.dropTable();

                    Intent intent = new Intent(CartActivity.this, FirstPage.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to send order: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("FIREBASE_ERROR", "Error sending order", e);
                });
    }
}

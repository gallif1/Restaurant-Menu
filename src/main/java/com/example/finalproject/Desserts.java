package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class Desserts extends AppCompatActivity {
    private LinearLayout malabi, iceCream, souffle, fruitSalad;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desserts);

        // אתחול כל הכפתורים מה-XML
        malabi = findViewById(R.id.malabi);
        iceCream = findViewById(R.id.iceCream);
        souffle = findViewById(R.id.souffle);
        fruitSalad = findViewById(R.id.fruitSalad);
        backButton = findViewById(R.id.backButton);

        // הפעלת דף מוצר לכל פריט
        malabi.setOnClickListener(v -> openProductPage("Malabi", R.drawable.malabi, "5.99"));
        iceCream.setOnClickListener(v -> openProductPage("Ice Cream", R.drawable.ice_cream, "5.99"));
        souffle.setOnClickListener(v -> openProductPage("Souffle", R.drawable.souffle, "22.99"));
        fruitSalad.setOnClickListener(v -> openProductPage("Fruit Salad", R.drawable.fruit_salad, "15.99"));

        // כפתור חזרה
        backButton.setOnClickListener(v -> finish());
    }

    // פונקציה לפתיחת דף המוצר עם הנתונים המתאימים
    private void openProductPage(String name, int imageRes, String price) {
        Intent intent = new Intent(Desserts.this, ProductPage.class);
        intent.putExtra("name", name);
        intent.putExtra("image", imageRes);
        intent.putExtra("price", price);
        startActivity(intent);
    }
}

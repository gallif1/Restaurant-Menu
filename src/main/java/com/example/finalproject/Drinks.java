//package com.example.finalproject;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.MotionEvent;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class Drinks extends AppCompatActivity {
//    private LinearLayout cola, sprite, water, fanta, beerCorona, beerMaccabi, beerGoldstar;
//    private Button backButton;
//    private SqlLiteManager dbManager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_drinks);
//
//        dbManager = new SqlLiteManager(getApplicationContext());
//
//        // אתחול כל הכפתורים מה-XML
//        cola = findViewById(R.id.cola);
//        sprite = findViewById(R.id.sprite);
//        water = findViewById(R.id.water);
//        fanta = findViewById(R.id.fanta);
//        beerCorona = findViewById(R.id.beerCorona);
//        beerMaccabi = findViewById(R.id.beerMaccabi);
//        beerGoldstar = findViewById(R.id.beerGoldstar);
//        backButton = findViewById(R.id.backButton);
//
//        // הוספת אפקט לכל כפתור + הוספה למסד הנתונים
//        setupButton(cola, "Coca Cola", 5.99F, "Drinks");
//        setupButton(sprite, "Sprite", 5.99F, "Drinks");
//        setupButton(water, "Water", 2.99F, "Drinks");
//        setupButton(fanta, "Fanta", 5.99F, "Drinks");
//        setupButton(beerCorona, "Corona Beer", 16.99F, "Drinks");
//        setupButton(beerMaccabi, "Maccabi Beer", 16.99F, "Drinks");
//        setupButton(beerGoldstar, "Goldstar Beer", 16.99F, "Drinks");
//
//        backButton.setOnClickListener(v -> {
//            Intent intent = new Intent(Drinks.this, FirstPage.class);
//            startActivity(intent);
//        });
//    }
//
//    // פונקציה שמוסיפה אפקטים לכל כפתור וגם מכניסה למסד
//    private void setupButton(LinearLayout button, String itemName, float price, String foodGroup) {
//        if (button == null) return;
//
//        // = בלחיצה - הוספה למסד נתונים
//        button.setOnClickListener(v -> {
//            dbManager.addDish(itemName, price, foodGroup); // הוספה למסד
//            Toast.makeText(this, itemName + " added to cart!", Toast.LENGTH_SHORT).show();
//            dbManager.printAllDishes();
//        });
//
//        // אפקט הדגשה בעת מעבר עכבר (אם יש תמיכה בעכבר)
//        button.setOnHoverListener((v, event) -> {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_HOVER_ENTER:
//                    v.animate().alpha(0.7f).setDuration(200).start();
//                    return true;
//                case MotionEvent.ACTION_HOVER_EXIT:
//                    v.animate().alpha(1.0f).setDuration(200).start();
//                    return true;
//            }
//            return false;
//        });
//    }
//}


package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class Drinks extends AppCompatActivity {
    private LinearLayout cola, sprite, water, fanta, beerCorona, beerMaccabi, beerGoldstar;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drinks);

        // אתחול כל הכפתורים מה-XML
        cola = findViewById(R.id.cola);
        sprite = findViewById(R.id.sprite);
        water = findViewById(R.id.water);
        fanta = findViewById(R.id.fanta);
        beerCorona = findViewById(R.id.beerCorona);
        beerMaccabi = findViewById(R.id.beerMaccabi);
        beerGoldstar = findViewById(R.id.beerGoldstar);
        backButton = findViewById(R.id.backButton);

        // הוספת לחיצה לפתיחת דף מוצר
        setupButton(cola, "Coca Cola", R.drawable.cola, "5.99");
        setupButton(sprite, "Sprite", R.drawable.sprite, "5.99");
        setupButton(water, "Water", R.drawable.water, "2.99");
        setupButton(fanta, "Fanta", R.drawable.fanta, "5.99");
        setupButton(beerCorona, "Corona Beer", R.drawable.beer_corona, "16.99");
        setupButton(beerMaccabi, "Maccabi Beer", R.drawable.beer_maccabi, "16.99");
        setupButton(beerGoldstar, "Goldstar Beer", R.drawable.beer_goldstar, "16.99");

        // כפתור חזרה
        backButton.setOnClickListener(v -> finish());
    }

    // פונקציה לפתיחת דף המוצר עם הנתונים המתאימים
    private void setupButton(LinearLayout button, String name, int imageRes, String price) {
        if (button == null) return;

        button.setOnClickListener(v -> {
            Intent intent = new Intent(Drinks.this, ProductPage.class);
            intent.putExtra("name", name);
            intent.putExtra("image", imageRes);
            intent.putExtra("price", price);
            intent.putExtra("foodGroup", "Drinks");
            startActivity(intent);
        });

        // אפקט הדגשה בעת מעבר עכבר (אם יש תמיכה בעכבר)
        button.setOnHoverListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_HOVER_ENTER:
                    v.animate().alpha(0.7f).setDuration(200).start(); // שקיפות חלקית
                    return true;
                case MotionEvent.ACTION_HOVER_EXIT:
                    v.animate().alpha(1.0f).setDuration(200).start(); // מחזיר שקיפות מלאה
                    return true;
            }
            return false;
        });
    }
}


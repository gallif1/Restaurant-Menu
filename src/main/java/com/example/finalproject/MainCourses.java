package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainCourses extends AppCompatActivity {
    private LinearLayout schnitzel, chicken, steak, kebab, chickenBreast;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_courses);

        schnitzel = findViewById(R.id.schnitzel);
        chicken = findViewById(R.id.chicken);
        steak = findViewById(R.id.steak);
        kebab = findViewById(R.id.kebab);
        chickenBreast = findViewById(R.id.chickenBreast);
        backButton = findViewById(R.id.backButton);

        setupButton(schnitzel, "Schnitzel", R.drawable.schnitzel, "25.99");
        setupButton(chicken, "Chicken", R.drawable.chicken, "16.99");
        setupButton(steak, "Steak", R.drawable.steak_secound, "36.99");
        setupButton(kebab, "Kebab", R.drawable.kebab, "16.99");
        setupButton(chickenBreast, "Chicken Breast", R.drawable.chicken_breast, "16.99");

        backButton.setOnClickListener(v -> finish());
    }

    private void setupButton(LinearLayout button, String name, int imageRes, String price) {
        if (button == null) return;

        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainCourses.this, ProductPage.class);
            intent.putExtra("name", name);
            intent.putExtra("image", imageRes);
            intent.putExtra("price", price);
            startActivity(intent);
        });

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

package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class FirstCoursesPage extends AppCompatActivity {
    private LinearLayout vegetableSalad, rice, mashedPotatoes, caesarSalad, coleslaw;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_courses_page);

        // אתחול כל הכפתורים מה-XML
        vegetableSalad = findViewById(R.id.vegetableSalad);
        rice = findViewById(R.id.rice);
        mashedPotatoes = findViewById(R.id.mashedPotatoes);
        caesarSalad = findViewById(R.id.caesarSalad);
        coleslaw = findViewById(R.id.coleslaw);
        backButton = findViewById(R.id.backButton);

        // הוספת לחיצה לפתיחת דף מוצר
        setupButton(vegetableSalad, "Vegetable Salad", R.drawable.vegetable_salad, "16.99");
        setupButton(rice, "Rice", R.drawable.rice, "5.99");
        setupButton(mashedPotatoes, "Mashed Potatoes", R.drawable.mashed_potatoes, "6.99");
        setupButton(caesarSalad, "Caesar Salad", R.drawable.caesar_salad, "29.99");
        setupButton(coleslaw, "Coleslaw", R.drawable.coleslaw, "4.99");

        // כפתור חזרה
        backButton.setOnClickListener(v -> finish());
    }

    // פונקציה לפתיחת דף המוצר עם הנתונים המתאימים
    private void setupButton(LinearLayout button, String name, int imageRes, String price) {
        if (button == null) return;

        button.setOnClickListener(v -> {
            Intent intent = new Intent(FirstCoursesPage.this, ProductPage.class);
            intent.putExtra("name", name);
            intent.putExtra("image", imageRes);
            intent.putExtra("price", price);
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

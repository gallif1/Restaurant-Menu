package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FirstPage extends AppCompatActivity {
    private Button firstCourseButton;
    private Button mainCourseButton;
    private Button drinksButton;
    private Button dessertButton;
    private Button cartButton;
    private Button ordersButton;
    private SqlLiteManager dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        dbManager = new SqlLiteManager(this);

        ordersButton = findViewById(R.id.ordersButton);
        firstCourseButton = findViewById(R.id.firstCourseButton);
        mainCourseButton = findViewById(R.id.mainCourseButton);
        drinksButton = findViewById(R.id.drinksButton);
        dessertButton = findViewById(R.id.dessertButton);
        cartButton = findViewById(R.id.cartButton);

        firstCourseButton.setOnClickListener(v -> {
            Intent intent = new Intent(FirstPage.this, FirstCoursesPage.class);
            startActivity(intent);
        });

        mainCourseButton.setOnClickListener(v -> {
            Intent intent = new Intent(FirstPage.this, MainCourses.class);
            startActivity(intent);
        });

        drinksButton.setOnClickListener(v -> {
            Intent intent = new Intent(FirstPage.this, Drinks.class);
            startActivity(intent);
        });

        dessertButton.setOnClickListener(v -> {
            Intent intent = new Intent(FirstPage.this, Desserts.class);
            startActivity(intent);
        });


        cartButton.setOnClickListener(v -> {
            Intent intent = new Intent(FirstPage.this, CartActivity.class);
            startActivity(intent);
        });


        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ordersButton.setOnClickListener(v->{
            Intent intent = new Intent(FirstPage.this, Orders.class);
            startActivity(intent);
        });
    }
}

package com.example.finalproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProductPage extends AppCompatActivity {
    private TextView productName, productPrice, quantityText;
    private ImageView productImage;
    private EditText notesInput;
    private Button addButton, backButton, increaseQuantity, decreaseQuantity;
    private int quantity = 1;
    private SqlLiteManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        dbManager = new SqlLiteManager(getApplicationContext());

        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productImage = findViewById(R.id.productImage);
        quantityText = findViewById(R.id.quantityText);
        notesInput = findViewById(R.id.notesInput);
        addButton = findViewById(R.id.addButton);
        backButton = findViewById(R.id.backButton);
        increaseQuantity = findViewById(R.id.increaseQuantity);
        decreaseQuantity = findViewById(R.id.decreaseQuantity);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        int imageRes = intent.getIntExtra("image", 0);
        String price = intent.getStringExtra("price");
        String foodGroup = intent.getStringExtra("foodGroup");

        productName.setText(name);
        productImage.setImageResource(imageRes);
        productPrice.setText("$" + price);
        quantityText.setText(String.valueOf(quantity));

        increaseQuantity.setOnClickListener(v -> {
            quantity++;
            quantityText.setText(String.valueOf(quantity));
        });

        decreaseQuantity.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                quantityText.setText(String.valueOf(quantity));
            }
        });

        backButton.setOnClickListener(v -> finish());

        addButton.setOnClickListener(v -> {
            String notes = notesInput.getText().toString().trim();
            float finalPrice = Float.parseFloat(price) * quantity;

            SQLiteDatabase db = dbManager.getReadableDatabase();

            if (!doesTableExist(db, "orderTable")) {
                dbManager.onCreate(db);
            }
            dbManager.addDish(name, finalPrice, foodGroup, notes, quantity);

            Toast.makeText(ProductPage.this, name + " x" + quantity + " נוספה להזמנה!", Toast.LENGTH_SHORT).show();

            finish();
        });
    }
    private boolean doesTableExist(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{tableName});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
}

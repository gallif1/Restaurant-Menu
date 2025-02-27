package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class SqlLiteManager extends SQLiteOpenHelper {
    // שם המסד וגרסה
    private static final String DATABASE_NAME = "MyDatabase.db";
    private static final int DATABASE_VERSION = 1;

    // בנאי (Constructor)
    public SqlLiteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // יצירת הטבלה במסד
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ORDER_TABLE = "CREATE TABLE IF NOT EXISTS orderTable (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "price REAL, " +
                "foodGroup TEXT, " +
                "amount INTEGER," +
                "details TEXT)";
        db.execSQL(CREATE_ORDER_TABLE);
    }

    // שדרוג המסד במקרה של שינוי גרסה
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS orderTable");
        onCreate(db);
    }

    //add dish
    public void addDish(String name, float price, String foodGroup, String details,int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();

         try {
            if (searchDish(name)) {
                db.execSQL("UPDATE orderTable SET amount = amount + ? WHERE name = ?", new String[]{String.valueOf(quantity), name});
                System.out.println("Updated existing dish: " + name);
            } else {
                ContentValues values = new ContentValues();
                values.put("name", name);
                values.put("price", price);
                values.put("foodGroup", foodGroup);
                values.put("amount", quantity);
                values.put("details",details);
                long result = db.insert("orderTable", null, values);

                if (result == -1) {
                    System.out.println("Failed to insert dish: " + name);
                } else {
                    System.out.println("Inserted new dish: " + name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }




    //search dish
    public boolean searchDish(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        // סופר כמה מנות עם השם הנתון קיימות
        long count = DatabaseUtils.queryNumEntries(db, "orderTable", "name = ?", new String[]{name});
        return count > 0;
    }


    public void updateDish(String name, int new_amount) {
        if(new_amount==0){
            deleteDish(name);
        }else{
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("UPDATE orderTable SET amount = ? WHERE name = ?", new Object[]{new_amount, name});
            db.close();
        }
    }


    public void deleteDish(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("orderTable", "name = ?", new String[]{name});
        db.close();
    }



    //delete the table
    public void dropTable() {
        SQLiteDatabase db = this.getWritableDatabase();

        // מחיקת הטבלה מהמסד
        db.execSQL("DROP TABLE IF EXISTS orderTable");

        db.close();
    }


    public void printAllDishes() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM orderTable", null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                float price = cursor.getFloat(cursor.getColumnIndexOrThrow("price"));
                String foodGroup = cursor.getString(cursor.getColumnIndexOrThrow("foodGroup"));
                int amount = cursor.getInt(cursor.getColumnIndexOrThrow("amount"));

                // הדפסת המידע ל-Logcat
                Log.d("DB_DEBUG", "Name: " + name + ", Price: " + price + ", FoodGroup: " + foodGroup + ", Amount: " + amount);

            } while (cursor.moveToNext());
        } else {
            Log.d("DB_DEBUG", "No data found in orderTable.");
        }

        cursor.close();
        db.close();
    }

    public void updateDishDetails(String name, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("details", notes);
        db.update("orderTable", values, "name = ?", new String[]{name});
        db.close();
    }






}

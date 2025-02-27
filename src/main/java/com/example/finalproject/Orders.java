package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.PropertyName;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Orders extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> orderList = new ArrayList<>();
    private FirebaseFirestore firestore;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        recyclerView = findViewById(R.id.ordersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderAdapter = new OrderAdapter(orderList);
        recyclerView.setAdapter(orderAdapter);

        backButton = findViewById(R.id.backButton);
        firestore = FirebaseFirestore.getInstance();

        fetchOrders();

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(Orders.this, FirstPage.class);
            startActivity(intent);
        });
    }

    private void fetchOrders() {
        firestore.collection("orders")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        orderList.clear();
                        for (DocumentSnapshot doc : task.getResult()) {
                            Log.d("FirestoreDoc", doc.getData().toString()); // הדפסה של כל המסמך

                            // קריאה ישירה לשדה tableNum לצורך בדיקה
                            Long tableNum = doc.getLong("tableNum");
                            Log.d("DirectFetch", "Table Number: " + (tableNum != null ? tableNum : "Not Found"));

                            Order order = doc.toObject(Order.class);
                            if (order != null) {
                                orderList.add(order);
                                Log.d("TableNumberCheck", "Table Number from Object: " + order.getTableNum());
                            }
                        }
                        orderAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Failed to load orders.", Toast.LENGTH_SHORT).show();
                        Log.e("FirestoreError", "Error fetching orders", task.getException());
                    }
                });
    }

    private class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

        private List<Order> orders;

        public OrderAdapter(List<Order> orders) {
            this.orders = orders;
        }

        class OrderViewHolder extends RecyclerView.ViewHolder {
            TextView orderDate, orderTotal, orderItems;

            public OrderViewHolder(View itemView) {
                super(itemView);
                orderDate = new TextView(itemView.getContext());
                orderTotal = new TextView(itemView.getContext());
                orderItems = new TextView(itemView.getContext());

                LinearLayout layout = new LinearLayout(itemView.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(16, 16, 16, 16);

                layout.addView(orderDate);
                layout.addView(orderTotal);
                layout.addView(orderItems);

                ((ViewGroup) itemView).addView(layout);
            }
        }

        @NonNull
        @Override
        public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout layout = new LinearLayout(parent.getContext());
            layout.setLayoutParams(new RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(8, 8, 8, 8);
            return new OrderViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
            Order order = orders.get(position);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

            holder.orderDate.setText("Date: " + sdf.format(order.getOrderDate()));
            holder.orderTotal.setText("Total Price: $" + String.format("%.2f", order.getTotalPrice()));

            StringBuilder itemsBuilder = new StringBuilder();
            for (Item item : order.getItems()) {
                itemsBuilder.append(item.getName())
                        .append(" x")
                        .append(item.getAmount())
                        .append(" - $")
                        .append(String.format("%.2f", item.getTotalPrice()))
                        .append(" - ")
                        .append(item.getDetails())
                        .append("\n");
            }

            // הצגת מספר השולחן
            itemsBuilder.append("Table Number: ").append(order.getTableNum()).append("\n");

            holder.orderItems.setText(itemsBuilder.toString());

            // הדפסת מספר השולחן ללוג לצורך בדיקות
            Log.d("OrderDebug", "Table Number in Adapter: " + order.getTableNum());
        }

        @Override
        public int getItemCount() {
            return orders.size();
        }
    }

    public static class Order {
        private List<Item> items;
        private double totalPrice;
        private Date orderDate;
        private int tableNum; // שינוי השדה ל-tableNum

        public Order() { }

        public List<Item> getItems() {
            return items;
        }

        public double getTotalPrice() {
            return totalPrice;
        }

        public Date getOrderDate() {
            return orderDate;
        }

        @PropertyName("tableNum") // תואם לשדה ב-Firestore
        public int getTableNum() {
            return tableNum;
        }
    }

    public static class Item {
        private String name;
        private double price;
        private int amount;
        private double totalPrice;
        private String details;

        public Item() { }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        public int getAmount() {
            return amount;
        }

        public double getTotalPrice() {
            return totalPrice;
        }

        public String getDetails() {
            return details;
        }
    }
}

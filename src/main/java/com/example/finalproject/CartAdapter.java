package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;
    private Context context;
    private OnCartChangeListener listener;
    private SqlLiteManager dbManager;

    // ממשק להאזנה לשינויים בעגלה
    public interface OnCartChangeListener {
        void onCartChanged();
    }

    // קונסטרקטור
    public CartAdapter(List<CartItem> cartItems, Context context, OnCartChangeListener listener) {
        this.cartItems = cartItems;
        this.context = context;
        this.listener = listener;
        this.dbManager = new SqlLiteManager(context);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        // הצגת פרטי הפריט
        holder.itemName.setText(item.getName());
        holder.itemPrice.setText("Price: $" + item.getPrice());
        holder.itemAmount.setText("Amount: " + item.getAmount());
        holder.itemTotal.setText("Total: $" + item.getTotalPrice());

        // הצגת ההערות מתוך ה-DB בתוך EditText
        holder.itemDetails.setText(item.getNotes());

        // כפתור להגדלת כמות
        holder.increaseButton.setOnClickListener(v -> {
            item.setAmount(item.getAmount() + 1);
            dbManager.updateDish(item.getName(), item.getAmount());
            notifyItemChanged(position);
            listener.onCartChanged();
        });

        // כפתור להקטנת כמות או הסרה מהעגלה
        holder.decreaseButton.setOnClickListener(v -> {
            if (item.getAmount() > 1) {
                item.setAmount(item.getAmount() - 1);
                dbManager.updateDish(item.getName(), item.getAmount());
                notifyItemChanged(position);
            } else {
                dbManager.deleteDish(item.getName());
                cartItems.remove(position);
                notifyItemRemoved(position);
            }
            listener.onCartChanged();
        });

        // כפתור לשמירת ההערות לאחר עריכה
        holder.saveDetailsButton.setOnClickListener(v -> {
            String updatedNotes = holder.itemDetails.getText().toString().trim();
            item.setNotes(updatedNotes); // עדכון בפריט
            dbManager.updateDishDetails(item.getName(), updatedNotes); // עדכון במסד הנתונים
            Toast.makeText(context, "Details saved", Toast.LENGTH_SHORT).show();
            listener.onCartChanged();
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    // ViewHolder שמכיל גם את ה-EditText ואת כפתור השמירה
    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPrice, itemAmount, itemTotal;
        EditText itemDetails; // 🟢 תיבת טקסט לעריכת ההערות
        Button increaseButton, decreaseButton, saveDetailsButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.cartItemName);
            itemPrice = itemView.findViewById(R.id.cartItemPrice);
            itemAmount = itemView.findViewById(R.id.cartItemAmount);
            itemTotal = itemView.findViewById(R.id.cartItemTotal);
            itemDetails = itemView.findViewById(R.id.cartItemDetails); // 🟢 אתחול EditText
            saveDetailsButton = itemView.findViewById(R.id.saveDetailsButton); // 🟢 כפתור לשמירה
            increaseButton = itemView.findViewById(R.id.increaseButton);
            decreaseButton = itemView.findViewById(R.id.decreaseButton);
        }
    }
}

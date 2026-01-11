package com.example.laundryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    Context context;
    ArrayList<OrderHelper> list;

    public OrderAdapter(Context context, ArrayList<OrderHelper> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderHelper order = list.get(position);
        holder.name.setText(order.name);
        holder.phone.setText(order.phone);
        holder.details.setText(order.details);
        holder.price.setText(order.price + " Tk");
        holder.status.setText("Status: " + order.status);

        holder.itemView.setOnLongClickListener(v -> {
            showStatusDialog(order);
            return true;
        });
    }

    private void showStatusDialog(OrderHelper order) {
        String[] options = {"Active", "Washing", "Ironing", "Ready", "Delivered"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update Status for " + order.name);
        builder.setItems(options, (dialog, which) -> {
            String newStatus = options[which];
            FirebaseDatabase.getInstance().getReference("orders")
                    .child(order.phone)
                    .child(order.orderId)
                    .child("status").setValue(newStatus);

            Toast.makeText(context, "Status Updated to " + newStatus, Toast.LENGTH_SHORT).show();
        });
        builder.show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone, details, price, status;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvOrderName);
            phone = itemView.findViewById(R.id.tvOrderPhone);
            details = itemView.findViewById(R.id.tvOrderDetails);
            price = itemView.findViewById(R.id.tvOrderPrice);
            status = itemView.findViewById(R.id.tvOrderStatus);
        }
    }
}
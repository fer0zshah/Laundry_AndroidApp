package com.example.laundryapp;

import android.content.Context;
import android.graphics.Color;
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

        String nameAndPhone = order.name + " (" + order.phone + ")";
        holder.tvName.setText(nameAndPhone);
        holder.tvDetails.setText(order.details);
        holder.tvPrice.setText(order.price + " Tk");

        if (order.date != null && !order.date.isEmpty()) {
            holder.tvDate.setText(order.date);
        } else {
            holder.tvDate.setText("No Date");
        }


        holder.tvStatus.setText(order.status);
        setStatusColor(holder.tvStatus, order.status);

        holder.itemView.setOnLongClickListener(v -> {
            showStatusDialog(order);
            return true;
        });
    }


    private void setStatusColor(TextView tv, String status) {
        switch (status) {
            case "Delivered":
                tv.setTextColor(Color.parseColor("#2ECC71"));
                break;
            case "Active":
                tv.setTextColor(Color.parseColor("#3498DB"));
                break;
            case "Washing":
                tv.setTextColor(Color.parseColor("#F1C40F"));
                break;
            case "Ironing":
                tv.setTextColor(Color.parseColor("#E67E22"));
                break;
            default:
                tv.setTextColor(Color.parseColor("#7F8C8D"));
                break;
        }
    }

    private void showStatusDialog(OrderHelper order) {
        String[] options = {"Active", "Washing", "Ironing", "Ready", "Delivered"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update Status");
        builder.setItems(options, (dialog, which) -> {
            String newStatus = options[which];
            FirebaseDatabase.getInstance().getReference("orders")
                    .child(order.phone) // Ensure this matches your DB structure
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

        TextView tvName, tvDetails, tvPrice, tvStatus, tvDate;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvOrderName);
            tvDetails = itemView.findViewById(R.id.tvOrderDetails);
            tvPrice = itemView.findViewById(R.id.tvOrderPrice);
            tvStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvDate = itemView.findViewById(R.id.tvOrderDate);
        }
    }
}
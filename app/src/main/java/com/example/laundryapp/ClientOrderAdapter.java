package com.example.laundryapp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ClientOrderAdapter extends RecyclerView.Adapter<ClientOrderAdapter.OrderViewHolder> {

    private List<OrderHelper> orderList;

    public ClientOrderAdapter(List<OrderHelper> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderHelper order = orderList.get(position);

        holder.tvName.setText("Order #" + order.orderId.substring(0,4)); // Showing ID instead of Name looks better in History
        holder.tvPhone.setText(order.phone);
        holder.tvPrice.setText(order.price);
        holder.tvDetails.setText(order.details);
        holder.tvStatus.setText("Status: " + order.status);


        if ("Ready".equalsIgnoreCase(order.status)) {
            holder.tvStatus.setTextColor(Color.parseColor("#27ae60")); // Green
        } else if ("Washing".equalsIgnoreCase(order.status)) {
            holder.tvStatus.setTextColor(Color.BLUE);
        } else {
            holder.tvStatus.setTextColor(Color.parseColor("#d35400")); // Orange/Red
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvPhone, tvPrice, tvDetails, tvStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvOrderName);
            tvPhone = itemView.findViewById(R.id.tvOrderPhone);
            tvPrice = itemView.findViewById(R.id.tvOrderPrice);
            tvDetails = itemView.findViewById(R.id.tvOrderDetails);
            tvStatus = itemView.findViewById(R.id.tvOrderStatus);
        }
    }
}
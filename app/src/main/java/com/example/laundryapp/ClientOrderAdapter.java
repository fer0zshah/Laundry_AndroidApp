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

        if (order.date != null && !order.date.isEmpty()) {
            holder.tvDate.setText(order.date);
        } else {
            holder.tvDate.setText("No Date");
        }

        if(order.orderId != null && order.orderId.length() >= 4) {
            holder.tvName.setText("Order #" + order.orderId.substring(0, 4));
        } else {
            holder.tvName.setText("Order #" + order.orderId);
        }

        holder.tvPrice.setText(order.price + " Tk");


        holder.tvDetails.setText(order.details);

        holder.tvStatus.setText(order.status);

        setStatusColor(holder.tvStatus, order.status);
    }

    private void setStatusColor(TextView tv, String status) {
        // Matches the colors used in your Admin Adapter
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

    @Override
    public int getItemCount() {
        return orderList.size();
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
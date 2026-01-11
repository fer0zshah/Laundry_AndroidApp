package com.example.laundryapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PickupRequest extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseOrders;
    PickupAdapter adapter;
    List<OrderHelper> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_request);

        recyclerView = findViewById(R.id.recyclerPickup);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseOrders = FirebaseDatabase.getInstance().getReference("orders");
        orderList = new ArrayList<>();

        // Load the data
        loadPendingOrders();
    }

    private void loadPendingOrders() {
        databaseOrders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot orderSnapshot : userSnapshot.getChildren()) {
                        OrderHelper order = orderSnapshot.getValue(OrderHelper.class);

                        if (order != null && "Pending".equals(order.getStatus())) {
                            orderList.add(order);
                        }
                    }
                }
                adapter = new PickupAdapter(orderList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    class PickupAdapter extends RecyclerView.Adapter<PickupAdapter.PickupViewHolder> {
        List<OrderHelper> list;

        public PickupAdapter(List<OrderHelper> list) { this.list = list; }

        @NonNull
        @Override
        public PickupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pickupitem, parent, false);
            return new PickupViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PickupViewHolder holder, int position) {
            OrderHelper order = list.get(position);
            holder.tvCustomer.setText("Customer: " + order.getUserName());
            holder.tvDetails.setText(order.getDetails());
            holder.tvPrice.setText("Total: " + order.getTotalPrice());
            holder.tvId.setText("Phone: " + order.getUserPhone());

            holder.btnAccept.setOnClickListener(v -> {
                updateOrderStatus(order.getUserPhone(), order.getOrderId(), "Active"); // "Active" means Accepted
            });

            holder.btnReject.setOnClickListener(v -> {
                updateOrderStatus(order.getUserPhone(), order.getOrderId(), "Rejected");
            });
        }

        @Override
        public int getItemCount() { return list.size(); }

        class PickupViewHolder extends RecyclerView.ViewHolder {
            TextView tvCustomer, tvDetails, tvPrice, tvId;
            Button btnAccept, btnReject;

            public PickupViewHolder(@NonNull View itemView) {
                super(itemView);
                tvCustomer = itemView.findViewById(R.id.tvCustomerName);
                tvDetails = itemView.findViewById(R.id.tvOrderDetails);
                tvPrice = itemView.findViewById(R.id.tvOrderTotal);
                tvId = itemView.findViewById(R.id.tvOrderId);
                btnAccept = itemView.findViewById(R.id.btnAccept);
                btnReject = itemView.findViewById(R.id.btnReject);
            }
        }
    }

    private void updateOrderStatus(String phone, String orderId, String newStatus) {
        databaseOrders.child(phone).child(orderId).child("status").setValue(newStatus)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Order " + newStatus, Toast.LENGTH_SHORT).show());
    }
}
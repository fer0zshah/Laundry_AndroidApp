package com.example.laundryapp;

import android.os.Bundle;
import android.widget.TextView;
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

public class RevenueActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView tvTotalRevenue;
    OrderAdapter adapter; // reusing your existing adapter
    ArrayList<OrderHelper> list;
    DatabaseReference database;
    int totalIncome = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue);

        tvTotalRevenue = findViewById(R.id.tvTotalRevenue);
        recyclerView = findViewById(R.id.recyclerRevenue);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        // We reuse your OrderAdapter because it already designs the rows nicely
        adapter = new OrderAdapter(this, list);
        recyclerView.setAdapter(adapter);

        database = FirebaseDatabase.getInstance().getReference("orders");
        loadRevenueData();
    }

    private void loadRevenueData() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                totalIncome = 0;

                // Loop through all Users
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    // Loop through all Orders for that user
                    for (DataSnapshot orderSnapshot : userSnapshot.getChildren()) {
                        OrderHelper order = orderSnapshot.getValue(OrderHelper.class);

                        // Check if order exists AND is Delivered
                        if (order != null && "Delivered".equals(order.getStatus())) {
                            list.add(order);

                            // Calculate Money: Convert "50 taka" to integer 50
                            int price = extractPrice(order.getTotalPrice());
                            totalIncome += price;
                        }
                    }
                }

                // Update UI
                adapter.notifyDataSetChanged();
                tvTotalRevenue.setText(totalIncome + " Tk");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    // Helper function to turn "15 taka" into 15
    private int extractPrice(String priceString) {
        if (priceString == null) return 0;
        // Replace everything that is NOT a number (0-9) with empty space
        String numberOnly = priceString.replaceAll("[^0-9]", "");
        if (numberOnly.isEmpty()) return 0;
        return Integer.parseInt(numberOnly);
    }
}
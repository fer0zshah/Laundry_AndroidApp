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
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;



public class RevenueActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView tvTotalRevenue,tvToday,tvMonth,tvPending;
    OrderAdapter adapter; // reusing your existing adapter
    ArrayList<OrderHelper> list;
    DatabaseReference database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue);
        tvToday = findViewById(R.id.tvTodayRevenue);
        tvMonth = findViewById(R.id.tvMonthRevenue);
        tvPending = findViewById(R.id.tvPendingRevenue);
        tvTotalRevenue = findViewById(R.id.tvTotalRevenue);
        recyclerView = findViewById(R.id.recyclerRevenue);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
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
                int incomeToday=0;
                int incomeMonth=0;
                int incomePending=0;
                int incomeTotal=0;
                String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                String thisMonth = todayDate.substring(0, 7);

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot orderSnapshot : userSnapshot.getChildren()) {
                        OrderHelper order = orderSnapshot.getValue(OrderHelper.class);

                        if (order != null) {
                            int price = extractPrice(order.getTotalPrice());
                            String orderDate = order.getDate();

                            if (!"Delivered".equals(order.getStatus())) {
                                incomePending += price;
                            }
                            else {
                                list.add(order);

                                incomeTotal += price;

                                if (orderDate != null) {

                                    if (orderDate.equals(todayDate)) {
                                        incomeToday += price;
                                    }
                                    if (orderDate.startsWith(thisMonth)) {
                                        incomeMonth += price;
                                    }
                                }
                            }
                        }
                    }
                }

                adapter.notifyDataSetChanged();
                tvTotalRevenue.setText(incomeTotal + " Tk");
                tvToday.setText(incomeToday + " Tk");
                tvMonth.setText(incomeMonth + " Tk");
                tvPending.setText(incomePending + " Tk");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private int extractPrice(String priceString) {
        if (priceString == null) return 0;
        String numberOnly = priceString.replaceAll("[^0-9]", "");
        if (numberOnly.isEmpty()) return 0;
        return Integer.parseInt(numberOnly);
    }
}
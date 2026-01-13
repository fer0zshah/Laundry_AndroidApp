package com.example.laundryapp;

import android.os.Bundle;
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

public class ClientHistory extends AppCompatActivity {

    RecyclerView recyclerView;
    ClientOrderAdapter adapter;
    List<OrderHelper> myOrderList;
    DatabaseReference databaseOrders;
    String userPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_history);

        userPhone = getIntent().getStringExtra("USER_PHONE");

        recyclerView = findViewById(R.id.recyclerViewHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myOrderList = new ArrayList<>();
        adapter = new ClientOrderAdapter(myOrderList);
        recyclerView.setAdapter(adapter);

        databaseOrders = FirebaseDatabase.getInstance().getReference("orders");

        loadMyOrders();
    }

    private void loadMyOrders() {
        DatabaseReference myOrdersRef = databaseOrders.child(userPhone);

        myOrdersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myOrderList.clear();

                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    OrderHelper order = orderSnapshot.getValue(OrderHelper.class);
                    if (order != null) {
                        myOrderList.add(order);
                    }
                }


                java.util.Collections.reverse(myOrderList);

                adapter.notifyDataSetChanged();

                if (myOrderList.isEmpty()) {
                    Toast.makeText(ClientHistory.this, "No orders found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ClientHistory.this, "Error loading data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
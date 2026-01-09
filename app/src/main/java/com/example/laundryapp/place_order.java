package com.example.laundryapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class place_order extends AppCompatActivity {

    Spinner spinnerClothType;
    EditText etQuantity;
    TextView tvTotalPrice;
    Button btnPlaceOrder;
    RadioButton rbWashIron, rbDryClean;

    DatabaseReference databaseOrders;
    String userPhone,userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        databaseOrders = FirebaseDatabase.getInstance().getReference("orders");

        userPhone = getIntent().getStringExtra("USER_PHONE");
        userName = getIntent().getStringExtra("USER_NAME");
        spinnerClothType = findViewById(R.id.spinnerClothType);
        etQuantity = findViewById(R.id.etQuantity);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        rbWashIron = findViewById(R.id.rbWashIron);
        rbDryClean = findViewById(R.id.rbDryClean);

        String[] items = new String[]{"T-Shirt", "Jeans", "Shirt", "Jacket", "Suit", "Bed Sheet"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinnerClothType.setAdapter(adapter);

        btnPlaceOrder.setOnClickListener(v -> submitOrder());
    }

    private void submitOrder() {
        String qtyStr = etQuantity.getText().toString().trim();
        if (qtyStr.isEmpty()) {
            Toast.makeText(this, "Please enter quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantity = Integer.parseInt(qtyStr);
        String selectedItem = spinnerClothType.getSelectedItem().toString();

        int unitPrice = 0;
        if (selectedItem.equals("Shirt")) unitPrice = 5;
        else if (selectedItem.equals("Jeans")) unitPrice = 10;
        else if (selectedItem.equals("Suit")) unitPrice = 50;
        else if (selectedItem.equals("Bed Sheet")) unitPrice = 10;
        else if (selectedItem.equals("Jacket")) unitPrice = 15;
        else if(selectedItem.equals("T-Shirt")) unitPrice = 5;


        if (rbDryClean.isChecked()) {
            unitPrice += 5;
        }

        int total = unitPrice * quantity;
        String totalStr = total+" taka";

        String serviceType = rbDryClean.isChecked() ? "Dry Clean" : "Wash & Iron";
        String details = quantity + " " + selectedItem + " (" + serviceType + ")";
        String status = "Active";

        String orderId = databaseOrders.push().getKey();

        OrderHelper newOrder = new OrderHelper(
                orderId,
                userName,
                userPhone,
                details,
                status,
                totalStr
        );

        if (orderId != null) {
            databaseOrders.child(orderId).setValue(newOrder);
            Toast.makeText(this, "Order Placed!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
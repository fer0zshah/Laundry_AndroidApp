package com.example.laundryapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
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
    String userPhone, userName;

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


        etQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                calculateTotal();
            }
        });

        spinnerClothType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calculateTotal(); // Run math when item changes
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        rbWashIron.setOnClickListener(v -> calculateTotal());
        rbDryClean.setOnClickListener(v -> calculateTotal());

        btnPlaceOrder.setOnClickListener(v -> submitOrder());
    }

    private void calculateTotal() {
        String qtyStr = etQuantity.getText().toString().trim();
        int quantity = 0;
        if (!qtyStr.isEmpty()) {
            try {
                quantity = Integer.parseInt(qtyStr);
            } catch (NumberFormatException e) {
                quantity = 0;
            }
        }

        int unitPrice = 0;
        String selectedItem = spinnerClothType.getSelectedItem().toString();

        if (selectedItem.equals("Shirt")) unitPrice = 5;
        else if (selectedItem.equals("Jeans")) unitPrice = 10;
        else if (selectedItem.equals("Suit")) unitPrice = 50;
        else if (selectedItem.equals("Bed Sheet")) unitPrice = 10;
        else if (selectedItem.equals("Jacket")) unitPrice = 15;
        else if (selectedItem.equals("T-Shirt")) unitPrice = 5;

        if (rbDryClean.isChecked()) {
            unitPrice += 5;
        }
        int total = unitPrice * quantity;

        tvTotalPrice.setText(total + " tk");
    }

    private void submitOrder() {
        String qtyStr = etQuantity.getText().toString().trim();
        if (qtyStr.isEmpty()) {
            Toast.makeText(this, "Please enter quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        String totalStr = tvTotalPrice.getText().toString();

        int quantity = Integer.parseInt(qtyStr);
        String selectedItem = spinnerClothType.getSelectedItem().toString();
        String serviceType = rbDryClean.isChecked() ? "Dry Clean" : "Wash & Iron";
        String details = quantity + " " + selectedItem + " (" + serviceType + ")";
        String status = "Pending";


        String orderId = databaseOrders.child(userPhone).push().getKey();

        OrderHelper newOrder = new OrderHelper(
                orderId,
                userName,
                userPhone,
                details,
                status,
                totalStr
        );

        if (orderId != null) {
            databaseOrders.child(userPhone).child(orderId).setValue(newOrder);
            Toast.makeText(this, "Order Placed!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
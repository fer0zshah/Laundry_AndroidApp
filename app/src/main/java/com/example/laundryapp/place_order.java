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
    TextView tvTotalPrice, tvCartList;
    Button btnPlaceOrder, btnAddItem;
    RadioButton rbWashIron, rbDryClean;

    DatabaseReference databaseOrders;
    String userPhone, userName;

    // Global variables to store the running order
    int grandTotal = 0;
    StringBuilder orderDetailsBuilder = new StringBuilder();
    boolean isCartEmpty = true;

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
        tvCartList = findViewById(R.id.tvCartList); // New View
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        btnAddItem = findViewById(R.id.btnAddItem); // New Button
        rbWashIron = findViewById(R.id.rbWashIron);
        rbDryClean = findViewById(R.id.rbDryClean);

        String[] items = new String[]{"T-Shirt", "Jeans", "Shirt", "Jacket", "Suit", "Bed Sheet"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinnerClothType.setAdapter(adapter);


        btnAddItem.setOnClickListener(v -> addToCart());


        btnPlaceOrder.setOnClickListener(v -> submitOrder());
    }

    private void addToCart() {
        String qtyStr = etQuantity.getText().toString().trim();
        if (qtyStr.isEmpty()) {
            Toast.makeText(this, "Enter quantity first", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantity = Integer.parseInt(qtyStr);
        if (quantity <= 0) {
            Toast.makeText(this, "Quantity must be > 0", Toast.LENGTH_SHORT).show();
            return;
        }


        int unitPrice = 0;
        String selectedItem = spinnerClothType.getSelectedItem().toString();

        if (selectedItem.equals("Shirt")) unitPrice = 5;
        else if (selectedItem.equals("Jeans")) unitPrice = 10;
        else if (selectedItem.equals("Suit")) unitPrice = 50;
        else if (selectedItem.equals("Bed Sheet")) unitPrice = 10;
        else if (selectedItem.equals("Jacket")) unitPrice = 15;
        else if (selectedItem.equals("T-Shirt")) unitPrice = 5;


        String serviceType;
        if (rbDryClean.isChecked()) {
            unitPrice += 5;
            serviceType = "Wash & Iron";

        } else {
            serviceType = "Wash Only";
        }

        int itemTotal = unitPrice * quantity;


        grandTotal += itemTotal;


        String lineItem = quantity + " x " + selectedItem + " (" + serviceType + ") = " + itemTotal + "tk\n";

        if (isCartEmpty) {
            orderDetailsBuilder.setLength(0);
            isCartEmpty = false;
        }
        orderDetailsBuilder.append(lineItem);


        tvCartList.setText(orderDetailsBuilder.toString());
        tvTotalPrice.setText(grandTotal + " tk");


        etQuantity.setText("");
        Toast.makeText(this, "Item Added!", Toast.LENGTH_SHORT).show();
    }

    private void submitOrder() {
        // If cart is empty, user might have forgotten to click "Add"
        if (isCartEmpty) {
            Toast.makeText(this, "Please add items to cart first (+)", Toast.LENGTH_SHORT).show();
            return;
        }

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        String currentDate = sdf.format(new java.util.Date());

        String status = "Pending";
        String orderId = databaseOrders.child(userPhone).push().getKey();


        OrderHelper newOrder = new OrderHelper(
                orderId,
                userName,
                userPhone,
                orderDetailsBuilder.toString(),
                status,
                String.valueOf(grandTotal),
                currentDate
        );

        if (orderId != null) {
            databaseOrders.child(userPhone).child(orderId).setValue(newOrder);
            Toast.makeText(this, "Order Placed Successfully!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
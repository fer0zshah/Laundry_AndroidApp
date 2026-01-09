package com.example.laundryapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Client extends AppCompatActivity {

    TextView tvWelcome;
    CardView cardNewOrder, cardHistory, cardPriceList, cardFeedback;
    Button btnLogout;
    String userPhone,userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);


        tvWelcome = findViewById(R.id.tvWelcome);
        cardNewOrder = findViewById(R.id.cardNewOrder);
        cardHistory = findViewById(R.id.cardHistory);
        cardPriceList = findViewById(R.id.cardPriceList);
        cardFeedback = findViewById(R.id.cardFeedback);
        btnLogout = findViewById(R.id.btnLogout);

        userPhone = getIntent().getStringExtra("USER_PHONE");
        userName = getIntent().getStringExtra("USER_NAME");
        if (userPhone != null) {
            tvWelcome.setText("Hello, " + userName);
        }

        cardNewOrder.setOnClickListener(v -> {
            Toast.makeText(this, "Opening Order Screen...", Toast.LENGTH_SHORT).show();
             Intent intent = new Intent(Client.this, place_order.class);
             intent.putExtra("USER_PHONE", userPhone);
            intent.putExtra("USER_NAME", userName);
             startActivity(intent);
        });

        cardHistory.setOnClickListener(v -> {
            Intent intent = new Intent(Client.this, ClientHistory.class);
            intent.putExtra("USER_PHONE", userPhone);

            startActivity(intent);
        });

        cardPriceList.setOnClickListener(v -> {
            showPriceListDialog();
        });


        cardFeedback.setOnClickListener(v -> {
            Intent intent = new Intent(Client.this, FeedbackActivity.class);
            intent.putExtra("USER_PHONE", userPhone);
            intent.putExtra("USER_NAME", userName);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(Client.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void showPriceListDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Laundry Price List");
        builder.setMessage(
                "T-Shirt: 5tk\n" +
                        "Jeans: 10tk\n" +
                        "Shirt: 10\n" +
                        "Jacket: 15\n" +
                        "Bed Sheet: 10\n" +
                        "Suit: 50\n"


        );
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}
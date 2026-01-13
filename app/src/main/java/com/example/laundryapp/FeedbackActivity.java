package com.example.laundryapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
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

public class FeedbackActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText etMessage;
    Button btnSubmit;

    DatabaseReference databaseFeedback;
    List<FeedbackHelper> feedbackList;
    FeedbackAdapter adapter;

    String userPhone;
    String userName;
    boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        userPhone = getIntent().getStringExtra("USER_PHONE");
        userName = getIntent().getStringExtra("USER_NAME");

        if(userName == null) userName = "Anonymous";


        if("Admin".equalsIgnoreCase(userPhone)) {
            isAdmin = true;
        }

        databaseFeedback = FirebaseDatabase.getInstance().getReference("feedback");

        recyclerView = findViewById(R.id.recyclerFeedback);
        etMessage = findViewById(R.id.etFeedbackMessage);
        btnSubmit = findViewById(R.id.btnSubmitFeedback);

        if (isAdmin) {
            etMessage.setVisibility(android.view.View.GONE);
            btnSubmit.setVisibility(android.view.View.GONE);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        feedbackList = new ArrayList<>();

        adapter = new FeedbackAdapter(feedbackList, item -> {
            if (isAdmin) showReplyDialog(item);
        });

        recyclerView.setAdapter(adapter);

        loadFeedback();

        btnSubmit.setOnClickListener(v -> postFeedback());
    }

    private void postFeedback() {
        String msg = etMessage.getText().toString().trim();
        if (!TextUtils.isEmpty(msg)) {
            String id = databaseFeedback.push().getKey();

            FeedbackHelper newFeedback = new FeedbackHelper(id, userName, userPhone, msg);

            databaseFeedback.child(id).setValue(newFeedback);
            etMessage.setText("");
        }
    }

    private void loadFeedback() {
        databaseFeedback.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                feedbackList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    FeedbackHelper item = data.getValue(FeedbackHelper.class);
                    feedbackList.add(item);
                }
                adapter.notifyDataSetChanged();
                if(!feedbackList.isEmpty()) {
                    recyclerView.scrollToPosition(feedbackList.size() - 1);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void showReplyDialog(FeedbackHelper item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reply to " + item.userName); // Show Name in title

        final EditText input = new EditText(this);
        input.setHint("Type reply here...");
        builder.setView(input);

        builder.setPositiveButton("Reply", (dialog, which) -> {
            String replyText = input.getText().toString();
            databaseFeedback.child(item.id).child("adminReply").setValue(replyText);
            Toast.makeText(this, "Reply Sent!", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}
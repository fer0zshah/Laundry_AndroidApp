package com.example.laundryapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {

    private List<FeedbackHelper> feedbackList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(FeedbackHelper feedback);
    }

    public FeedbackAdapter(List<FeedbackHelper> feedbackList, OnItemClickListener listener) {
        this.feedbackList = feedbackList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feedback, parent, false);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        FeedbackHelper item = feedbackList.get(position);

        holder.tvUserName.setText(item.userName);
        holder.tvUserPhone.setText(item.userPhone);

        holder.tvUserMessage.setText(item.message);

        if (item.adminReply != null && !item.adminReply.isEmpty()) {
            holder.layoutAdminReply.setVisibility(View.VISIBLE);
            holder.tvAdminReplyText.setText(item.adminReply);
        } else {
            holder.layoutAdminReply.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public static class FeedbackViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvUserPhone, tvUserMessage, tvAdminReplyText;
        LinearLayout layoutAdminReply;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName); // New ID
            tvUserPhone = itemView.findViewById(R.id.tvUserPhone);
            tvUserMessage = itemView.findViewById(R.id.tvUserMessage);
            tvAdminReplyText = itemView.findViewById(R.id.tvAdminReplyText);
            layoutAdminReply = itemView.findViewById(R.id.layoutAdminReply);
        }
    }
}
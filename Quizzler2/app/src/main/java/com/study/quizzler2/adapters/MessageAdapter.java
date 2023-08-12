package com.study.quizzler2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.study.quizzler2.R;
import com.study.quizzler2.helpers.chatGPT.LocalMessage;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    List<LocalMessage> messageList;

    public MessageAdapter(List<LocalMessage> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            // If the question is null or empty, return an empty view with 0 height
            View emptyView = new View(parent.getContext());
            emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
            return new MyViewHolder(emptyView);
        } else {
            View chatView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
            return new MyViewHolder(chatView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (getItemViewType(position) == 0) {
            // Don't bind anything for the empty view
            return;
        }

        LocalMessage message = messageList.get(position);
        if (message.getSentBy().equals(LocalMessage.SENT_BY_ME)) {
            holder.leftChatView.setVisibility(View.GONE);
            holder.rightChatView.setVisibility(View.VISIBLE);
            holder.rightTextView.setText(message.getMessage());
        } else {
            holder.rightChatView.setVisibility(View.GONE);
            holder.leftChatView.setVisibility(View.VISIBLE);
            holder.leftTextView.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        LocalMessage message = messageList.get(position);
        if (message.getSentBy().equals(LocalMessage.SENT_BY_ME)) {
            return 1;
        } else {
            if (isQuestionNullOrEmpty(position)) {
                return 0; // Use viewType 0 for the empty layout
            } else {
                return 2;
            }
        }
    }

    // Helper method to check if the question is null or empty
    private boolean isQuestionNullOrEmpty(int position) {
        return messageList == null || messageList.isEmpty() || messageList.get(position).getMessage() == null || messageList.get(position).getMessage().isEmpty();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftChatView, rightChatView;
        TextView leftTextView, rightTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            leftChatView = itemView.findViewById(R.id.left_chat_view);
            rightChatView = itemView.findViewById(R.id.right_chat_view);
            leftTextView = itemView.findViewById(R.id.left_chat_text_view);
            rightTextView = itemView.findViewById(R.id.right_chat_text_view);
        }
    }
}
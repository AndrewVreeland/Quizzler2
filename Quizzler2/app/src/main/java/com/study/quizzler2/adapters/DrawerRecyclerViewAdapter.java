package com.study.quizzler2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.study.quizzler2.R;
import com.study.quizzler2.utils.ConversationItem;

import java.util.List;

public class DrawerRecyclerViewAdapter extends RecyclerView.Adapter<DrawerRecyclerViewAdapter.ConversationItemViewHolder> {

    private List<ConversationItem> conversations;
    private OnConversationClickListener listener;

    public DrawerRecyclerViewAdapter(List<ConversationItem> conversations, OnConversationClickListener listener) {
        this.conversations = conversations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ConversationItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation_item_layout, parent, false);
        return new ConversationItemViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationItemViewHolder holder, int position) {
        holder.bind(conversations.get(position));
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    static class ConversationItemViewHolder extends RecyclerView.ViewHolder {
        TextView conversationSnippet;
        String conversationId;
        OnConversationClickListener listener;

        public ConversationItemViewHolder(@NonNull View itemView, OnConversationClickListener listener) {
            super(itemView);
            conversationSnippet = itemView.findViewById(R.id.conversation_snippet);
            this.listener = listener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onConversationClick(conversationId);
                    }
                }
            });
        }

        public void bind(ConversationItem conversationItem) {
            conversationSnippet.setText(conversationItem.getSnippet());
            conversationId = conversationItem.getId();
        }
    }

    public interface OnConversationClickListener {
        void onConversationClick(String conversationId);
    }

    public void updateData(List<ConversationItem> newConversations) {
        conversations.addAll(newConversations);
        notifyDataSetChanged();
    }
}
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

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {

    private static List<ConversationItem> conversationList;
    private OnConversationClickListener listener;

    public interface OnConversationClickListener {
        void onConversationClick(String conversationID);
    }

    public ConversationAdapter(List<ConversationItem> conversationList, OnConversationClickListener listener) {
        this.conversationList = conversationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation_item_layout, parent, false);
        return new ConversationViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        holder.bind(conversationList.get(position));
    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }

    public static class ConversationViewHolder extends RecyclerView.ViewHolder {

        private TextView snippetTextView;

        public ConversationViewHolder(@NonNull View itemView, OnConversationClickListener listener) {
            super(itemView);
            snippetTextView = itemView.findViewById(R.id.conversation_snippet);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onConversationClick(conversationList.get(position).getId());
                }
            });
        }

        public void bind(ConversationItem item) {
            snippetTextView.setText(item.getSnippet());
        }
    }
}
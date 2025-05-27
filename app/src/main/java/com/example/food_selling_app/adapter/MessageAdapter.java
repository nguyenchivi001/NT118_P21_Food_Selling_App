package com.example.food_selling_app.adapter;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_selling_app.R;
import com.example.food_selling_app.model.Message;

import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_LEFT = 0;
    private static final int TYPE_RIGHT = 1;
    private final List<Message> messageList;

    public MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        return message.isSentByUser() ? TYPE_RIGHT : TYPE_LEFT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_RIGHT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right, parent, false);
            return new RightViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left, parent, false);
            return new LeftViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        String text = message.getContent();

        if (holder instanceof RightViewHolder) {
            ((RightViewHolder) holder).tvMessage.setText(text);
        } else if (holder instanceof LeftViewHolder) {
            LeftViewHolder leftHolder = (LeftViewHolder) holder;
            leftHolder.tvMessage.setText(text);

            // Chỉ hiện nút loa nếu là tin nhắn của assistant
            if ("assistant".equalsIgnoreCase(message.getRole())) {
                leftHolder.btnSpeak.setVisibility(View.VISIBLE);
                leftHolder.btnSpeak.setOnClickListener(v -> {
                    leftHolder.tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                });
            } else {
                leftHolder.btnSpeak.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class RightViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;

        public RightViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
        }
    }

    static class LeftViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        ImageView btnSpeak;
        TextToSpeech tts;

        public LeftViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            btnSpeak = itemView.findViewById(R.id.btnSpeak);

            // Khởi tạo TTS cho từng holder
            tts = new TextToSpeech(itemView.getContext(), status -> {
                if (status == TextToSpeech.SUCCESS) {
                    tts.setLanguage(new Locale("vi", "VN"));
                }
            });
        }
    }
}

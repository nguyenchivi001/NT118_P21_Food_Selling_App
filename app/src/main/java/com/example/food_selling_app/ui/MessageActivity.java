package com.example.food_selling_app.ui;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_selling_app.R;
import com.example.food_selling_app.adapter.MessageAdapter;
import com.example.food_selling_app.model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends BaseActivity {

    private RecyclerView recyclerChat;
    private MessageAdapter adapter;
    private List<Message> messageList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    protected int getSelectedNavItem() {
        return R.id.nav_message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khởi tạo RecyclerView
        recyclerChat = findViewById(R.id.recyclerChat);
        recyclerChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerChat.setHasFixedSize(true);

        // Dữ liệu mẫu
        messageList = new ArrayList<>();
        messageList.add(new Message("Hi, how can I help you?", false));
        messageList.add(new Message("Hello, I ordered two fried chicken burgers. Can I know how much time it will get to arrive?", true));
        messageList.add(new Message("Ok, please let me check!", false));
        messageList.add(new Message("It’ll get 25 minutes to arrive to your address", false));
        messageList.add(new Message("Ok, thanks for your support", true));

        // Gắn adapter
        adapter = new MessageAdapter(messageList);
        recyclerChat.setAdapter(adapter);
    }
}

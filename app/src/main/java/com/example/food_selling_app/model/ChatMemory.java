package com.example.food_selling_app.model;

import com.example.food_selling_app.model.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatMemory {

    private static ChatMemory instance;
    private final List<Message> messageList;

    private ChatMemory() {
        messageList = new ArrayList<>();
        messageList.add(new Message("assistant", "Bạn cần tôi hỗ trợ điều gì?")); // Tin nhắn khởi tạo
    }

    public static ChatMemory getInstance() {
        if (instance == null) {
            instance = new ChatMemory();
        }
        return instance;
    }

    public List<Message> getMessages() {
        return messageList;
    }

    public void addMessage(Message message) {
        messageList.add(message);
    }

    public void clear() {
        messageList.clear();
        messageList.add(new Message("assistant", "Bạn cần tôi hỗ trợ điều gì?"));
    }
}

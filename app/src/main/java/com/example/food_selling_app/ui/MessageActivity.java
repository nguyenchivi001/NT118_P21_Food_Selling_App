package com.example.food_selling_app.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_selling_app.R;
import com.example.food_selling_app.adapter.MessageAdapter;
import com.example.food_selling_app.model.ChatRequest;
import com.example.food_selling_app.model.ChatResponse;
import com.example.food_selling_app.model.Message;
import com.example.food_selling_app.network.ConnectAI;
import com.example.food_selling_app.network.TogetherApi;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends BaseActivity {

    private RecyclerView recyclerChat;
    private MessageAdapter adapter;
    private List<Message> messageList;
    private EditText edtMessage;
    private ImageView btnSend;

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

        recyclerChat = findViewById(R.id.recyclerChat);
        edtMessage = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);

        recyclerChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerChat.setHasFixedSize(true);

        messageList = new ArrayList<>();
        adapter = new MessageAdapter(messageList);
        recyclerChat.setAdapter(adapter);

        // Lời chào đầu tiên từ assistant
        messageList.add(new Message("assistant", "Bạn cần tôi hỗ trợ điều gì?"));
        adapter.notifyItemInserted(messageList.size() - 1);

        // Xử lý nút gửi
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInput = edtMessage.getText().toString().trim();
                if (!userInput.isEmpty()) {
                    Message userMessage = new Message("user", userInput);
                    messageList.add(userMessage);
                    adapter.notifyItemInserted(messageList.size() - 1);
                    recyclerChat.scrollToPosition(messageList.size() - 1);
                    edtMessage.setText("");

                    sendToAI(userInput);
                }
            }
        });
    }

    private void sendToAI(String prompt) {
        List<Message> tempMessages = new ArrayList<>();

        // Tin nhắn hệ thống để hướng dẫn AI (luôn là tin đầu tiên)
        tempMessages.add(new Message("system", "Bạn là trợ lý AI hỗ khách hàng cho một App bán đồ ăn nhanh Online. Mặc định trả lời bằng tiếng Việt."));

        // Lấy tối đa 9 tin nhắn gần nhất (không tính tin system)
        int count = 0;
        for (int i = messageList.size() - 1; i >= 0 && count < 9; i--) {
            Message msg = messageList.get(i);
            if (!"system".equalsIgnoreCase(msg.getRole())) {
                tempMessages.add(1, msg); // Chèn vào sau system
                count++;
            }
        }

        ChatRequest request = new ChatRequest(
                "meta-llama/Llama-3.3-70B-Instruct-Turbo-Free",
                tempMessages
        );

        TogetherApi api = ConnectAI.getInstance();
        api.chatWithAI(request).enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                if (response.isSuccessful() && response.body() != null &&
                        response.body().getOutput().getChoices() != null &&
                        !response.body().getOutput().getChoices().isEmpty()) {

                    String reply = response.body().getOutput().getChoices().get(0).getText();
                    Message botMessage = new Message("assistant", reply);
                    messageList.add(botMessage);
                    adapter.notifyItemInserted(messageList.size() - 1);
                    recyclerChat.scrollToPosition(messageList.size() - 1);
                } else {
                    Toast.makeText(MessageActivity.this, "AI không phản hồi!", Toast.LENGTH_SHORT).show();
                    try {
                        if (response.errorBody() != null) {
                            String rawError = response.errorBody().string();
                            Log.e("AI_RAW_ERROR", rawError);
                        } else {
                            Log.e("AI_RAW_ERROR", "errorBody is null");
                        }
                    } catch (IOException e) {
                        Log.e("AI_RAW_ERROR", "Exception reading errorBody", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                Toast.makeText(MessageActivity.this, "Không kết nối được AI", Toast.LENGTH_SHORT).show();
                Log.e("AI_CHAT", "Lỗi gọi API: " + t.getMessage());
            }
        });
    }
}


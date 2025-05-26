package com.example.food_selling_app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_selling_app.R;
import com.example.food_selling_app.adapter.MessageAdapter;
import com.example.food_selling_app.model.ChatRequest;
import com.example.food_selling_app.model.ChatResponse;
import com.example.food_selling_app.model.ChatMemory;
import com.example.food_selling_app.model.Message;
import com.example.food_selling_app.network.ConnectAI;
import com.example.food_selling_app.network.TogetherApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends BaseActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1001;

    private RecyclerView recyclerChat;
    private MessageAdapter adapter;
    private List<Message> messageList;
    private EditText edtMessage;
    private ImageView btnSend;
    private ImageView btnMic;

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
        btnMic = findViewById(R.id.btnMic);

        recyclerChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerChat.setHasFixedSize(true);

        // Load messages from memory
        messageList = ChatMemory.getInstance().getMessages();
        adapter = new MessageAdapter(messageList);
        recyclerChat.setAdapter(adapter);
        recyclerChat.scrollToPosition(messageList.size() - 1);

        btnSend.setOnClickListener(v -> {
            String userInput = edtMessage.getText().toString().trim();
            if (!userInput.isEmpty()) {
                sendUserMessage(userInput);
            }
        });

        btnMic.setOnClickListener(v -> startSpeechToText());
    }

    private void sendUserMessage(String userInput) {
        Message userMessage = new Message("user", userInput);
        ChatMemory.getInstance().addMessage(userMessage);
        adapter.notifyItemInserted(messageList.size() - 1);
        recyclerChat.scrollToPosition(messageList.size() - 1);
        edtMessage.setText("");

        sendToAI(userInput);
    }

    private void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, new Locale("vi", "VN")); // tiếng Việt
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Nói nội dung bạn muốn...");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(this, "Thiết bị không hỗ trợ nhận diện giọng nói", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String spokenText = result.get(0);
                edtMessage.setText(spokenText);
                btnSend.performClick();
            }
        }
    }

    private void sendToAI(String prompt) {
        List<Message> tempMessages = new ArrayList<>();
        tempMessages.add(new Message("system", "Bạn là trợ lý AI hỗ trợ khách hàng cho một App bán đồ ăn nhanh Online. Mặc định trả lời bằng tiếng Việt."));

        int count = 0;
        List<Message> history = ChatMemory.getInstance().getMessages();
        for (int i = history.size() - 1; i >= 0 && count < 9; i--) {
            Message msg = history.get(i);
            if (!"system".equalsIgnoreCase(msg.getRole())) {
                tempMessages.add(1, msg);
                count++;
            }
        }

        Message typingIndicator = new Message("typing", "Đang phản hồi...");
        ChatMemory.getInstance().addMessage(typingIndicator);
        adapter.notifyItemInserted(messageList.size() - 1);
        recyclerChat.scrollToPosition(messageList.size() - 1);

        ChatRequest request = new ChatRequest(
                "meta-llama/Llama-3.3-70B-Instruct-Turbo-Free",
                tempMessages
        );

        TogetherApi api = ConnectAI.getInstance();
        api.chatWithAI(request).enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                removeTypingIndicator();

                if (response.isSuccessful() && response.body() != null &&
                        response.body().getOutput().getChoices() != null &&
                        !response.body().getOutput().getChoices().isEmpty()) {

                    String reply = response.body().getOutput().getChoices().get(0).getText();
                    Message botMessage = new Message("assistant", reply);
                    ChatMemory.getInstance().addMessage(botMessage);
                    adapter.notifyItemInserted(messageList.size() - 1);
                    recyclerChat.scrollToPosition(messageList.size() - 1);
                } else {
                    Toast.makeText(MessageActivity.this, "AI không phản hồi!", Toast.LENGTH_SHORT).show();
                    logError(response);
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                removeTypingIndicator();
                Toast.makeText(MessageActivity.this, "Không kết nối được AI", Toast.LENGTH_SHORT).show();
                Log.e("AI_CHAT", "Lỗi gọi API: " + t.getMessage());
            }
        });
    }

    private void removeTypingIndicator() {
        List<Message> messages = ChatMemory.getInstance().getMessages();
        for (int i = messages.size() - 1; i >= 0; i--) {
            if ("typing".equals(messages.get(i).getRole())) {
                messages.remove(i);
                adapter.notifyItemRemoved(i);
                break;
            }
        }
    }

    private void logError(Response<ChatResponse> response) {
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

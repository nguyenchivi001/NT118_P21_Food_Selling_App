package com.example.food_selling_app.utils;

import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;

public class JwtUtils {
    public static JSONArray getRolesFromToken(String token) {
        try {
            // Tách phần payload
            String[] parts = token.split("\\.");
            if (parts.length < 2) return null;

            String payload = parts[1];

            // Decode Base64 URL
            byte[] decodedBytes = Base64.decode(payload, Base64.URL_SAFE);
            String decodedPayload = new String(decodedBytes, "UTF-8");

            // Parse JSON
            JSONObject json = new JSONObject(decodedPayload);

            // Lấy role (nếu có)
            if (json.has("role")) {
                return json.getJSONArray("role");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
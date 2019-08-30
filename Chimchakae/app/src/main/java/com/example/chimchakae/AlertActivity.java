package com.example.chimchakae;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class AlertActivity extends AppCompatActivity {

    private Button carrierButton;
    private Button followerButton;

    private SharedPreferences auto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        carrierButton = findViewById(R.id.carrierBtn);
        followerButton = findViewById(R.id.followerBtn);
    }

    public void roleBtnClick(View v) {
        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url("ws://52.78.134.52:9000/")
                .build();

        final WebSocket webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
            }
        });
        String userId = auto.getString("inputId", null);
        Intent intent = null;
        if (v.getId() == R.id.carrierBtn) {
            webSocket.send("android: " + userId + ": C");
            intent = new Intent(AlertActivity.this, CarrierActivity.class);
        }
        if (v.getId() == R.id.followerBtn) {
            webSocket.send("android: " + userId + ": F");
            intent = new Intent(AlertActivity.this, FollowerActivity.class);
        }
        webSocket.close(1000, "Bye");
        startActivity(intent);
    }

    ;

}

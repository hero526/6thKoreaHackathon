package com.example.chimchakae;

import android.content.Intent;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

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

        carrierButton = findViewById(R.id.carrierBtn);
        followerButton = findViewById(R.id.followerBtn);

        carrierButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                webSocket.send("android: testID: C");
                //receive data
                webSocket.close(1000, "Bye");
                Intent intent = new Intent(AlertActivity.this, CarrierActivity.class);
                startActivity(intent);
            }
        });

        followerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                webSocket.send("android: testID: F");
                webSocket.close(1000, "Bye");
                Intent intent = new Intent(AlertActivity.this, FollowerActivity.class);
                startActivity(intent);
            }
        });
    }


}

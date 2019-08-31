package com.example.chimchakae;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
<<<<<<< HEAD

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
=======
>>>>>>> 43ec7ff6074798e6d93e8e72a0d807e11d3c6412

import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class AlertActivity extends AppCompatActivity {
<<<<<<< HEAD

    private CardView carrierButton;
    private CardView followerButton;

=======
>>>>>>> 43ec7ff6074798e6d93e8e72a0d807e11d3c6412
    private SharedPreferences auto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
<<<<<<< HEAD

        carrierButton = findViewById(R.id.carrierCard);
        followerButton = findViewById(R.id.followerCard);
=======
>>>>>>> 43ec7ff6074798e6d93e8e72a0d807e11d3c6412
    }

    public void roleBtnClick(View v) {
        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url("ws://52.78.134.52:9000/")
                .build();

        WebSocket webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
            }
        });
        String userId = auto.getString("inputId", null);
        String carNum = auto.getString("inputCarNum", null);
        Intent intent = null;
        if (v.getId() == R.id.carrierCard) {
            webSocket.send("android: " + userId + ": " + carNum  + ": C");
            intent = new Intent(AlertActivity.this, CarrierActivity.class);
        }
        if (v.getId() == R.id.followerCard) {
            webSocket.send("android: " + userId + ": " + carNum  + ": F");
            intent = new Intent(AlertActivity.this, FollowerActivity.class);
        }
        webSocket.close(1000, "Bye");
        startActivity(intent);
    }

    ;

}

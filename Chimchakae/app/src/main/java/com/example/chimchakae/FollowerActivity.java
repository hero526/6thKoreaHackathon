package com.example.chimchakae;

import android.os.Bundle;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class FollowerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);

        final TextView Msg = findViewById(R.id.followerWaitingMsg);

        Msg.setText("캐리어를 찾고 있습니다.");

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url("ws://52.78.134.52:9000/")
                .build();

        WebSocketListener webSocketListener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                if(text.equals("connected")) Msg.setText("캐리어가 연결 후 차량 이동을 시작했습니다.");
                else if (text.equals("arrived")) Msg.setText("캐리어가 차량을 안전지역에 주차 완료하였습니다.");
                else Msg.setText("알 수 없는 메세지.");
            }
        };

        client.newWebSocket(request, webSocketListener);
    }


}

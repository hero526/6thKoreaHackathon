package com.example.chimchakae;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class DrivingActivity extends AppCompatActivity {
    // auto login obj
    private SharedPreferences auto;
    String followerNum;
    String myNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving);

        Intent intent = getIntent();
        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        followerNum = intent.getExtras().getString("followerNum");
        myNum = auto.getString("inputCarNum", null);
    }

    public void StartBtnClick(View v) {
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
        webSocket.send("carrier: " + myNum + ": " + followerNum);
        Toast.makeText(DrivingActivity.this, "팔로워 인도 신청이 완료되었습니다. 운행 승인 알림이 뜨면 출발 해 주세요", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(DrivingActivity.this, CarryFinActivity.class);
        startActivity(intent);
        webSocket.close(1000, "Bye");
    }
}

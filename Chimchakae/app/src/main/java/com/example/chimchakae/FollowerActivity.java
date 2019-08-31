package com.example.chimchakae;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FollowerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);

        TextView Msg = findViewById(R.id.followerWaitingMsg);

        Msg.setText("팔로워 등록이 완료되었습니다.");
    }
}

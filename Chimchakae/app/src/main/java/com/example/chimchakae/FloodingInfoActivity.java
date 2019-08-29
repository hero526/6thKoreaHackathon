package com.example.chimchakae;

import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.chimchakae.Model.Request.Request;

import java.util.concurrent.ExecutionException;

public class FloodingInfoActivity extends FragmentActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flooding_info);

        TextView textView = findViewById(R.id.tv_json);
        String resultText = "값이 없음";

        try {
            resultText = new Request().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        textView.setText(resultText);
        System.out.println(resultText);

    }

}

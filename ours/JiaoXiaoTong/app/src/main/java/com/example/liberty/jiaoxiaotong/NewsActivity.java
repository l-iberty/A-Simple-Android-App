package com.example.liberty.jiaoxiaotong;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.liberty.jiaoxiaotong.global.SchoolPush;

public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        TextView titleView = (TextView) findViewById(R.id.textView_title);
        TextView contentView = (TextView) findViewById(R.id.textView_content);
        if (titleView != null && contentView != null) {
            titleView.setText(SchoolPush.title);
            contentView.setText(SchoolPush.content);
        }
    }
}

package com.example.liberty.jiaoxiaotong;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        // 监听按钮，根据用户的注册类型跳转到相应注册界面
        Button schoolRegBtn = (Button) findViewById(R.id.button_school);
        Button parentRegBtn = (Button) findViewById(R.id.button_parent);
        Button teachreRegBtn = (Button) findViewById(R.id.button_teacher);
        if (schoolRegBtn != null &&
                parentRegBtn != null &&
                teachreRegBtn != null) {

            // 学校注册
            schoolRegBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(RegistActivity.this, SchoolRegistActivity.class));
                }
            });

            // 家长注册
            parentRegBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(RegistActivity.this, ParentRegistActivity.class));
                }
            });

            // 教师注册
            teachreRegBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(RegistActivity.this, TeacherRegistActivity.class));
                }
            });
        }
    }
}

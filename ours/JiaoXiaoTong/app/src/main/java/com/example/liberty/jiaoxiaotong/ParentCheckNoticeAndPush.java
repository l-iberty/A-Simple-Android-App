package com.example.liberty.jiaoxiaotong;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ParentCheckNoticeAndPush extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_check_notice_and_push);

        // 查看学校发布的公告
        Button schoolPushBtn = (Button) findViewById(R.id.button_checkSchoolPush);
        if (schoolPushBtn != null) {
            schoolPushBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(ParentCheckNoticeAndPush.this, "学校推送", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ParentCheckNoticeAndPush.this,
                            ParentCheckSchoolPushActivity.class));
                }
            });
        }

        // 查看教师发布的活动
        Button teacherNotice = (Button) findViewById(R.id.button_checkTeacherNotice);
        if (teacherNotice != null) {
            teacherNotice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(ParentCheckNoticeAndPush.this, "查看教师发布活动", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ParentCheckNoticeAndPush.this,
                            ParentCheckNoticeActivity.class));
                }
            });
        }
    }
}

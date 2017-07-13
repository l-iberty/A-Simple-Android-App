package com.example.liberty.jiaoxiaotong;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by WX on 2017/7/9.
 */

public class StudInfoCheckActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_info_check);

        //成绩及年级排名
        Button rankBtn = (Button) findViewById(R.id.button_checkRank);
        if (rankBtn != null) {
            rankBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO 跳转到相关界面，实现查看 "年级排名" 功能
                    Toast.makeText(StudInfoCheckActivity.this, "年级排名", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(StudInfoCheckActivity.this, ParentCheckGradeRankActivity.class));
                }
            });
        }

        //作业查看
        Button homeworkBtn = (Button) findViewById(R.id.button_checkHomework);
        if (homeworkBtn != null) {
            homeworkBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO 跳转到相关界面，实现 "查看作业" 功能
                    Toast.makeText(StudInfoCheckActivity.this, "查看作业", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(StudInfoCheckActivity.this, ParentCheckHomeworkActivity.class));
                }
            });
        }

        //教师评语
        Button commentBtn = (Button) findViewById(R.id.button_checkComment);
        if (commentBtn != null) {
            commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO 跳转到相关界面，实现 "查看评语" 功能
                    Toast.makeText(StudInfoCheckActivity.this, "教师评语", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(StudInfoCheckActivity.this, ParentCheckCommentActivity.class));

                }
            });
        }
    }
}

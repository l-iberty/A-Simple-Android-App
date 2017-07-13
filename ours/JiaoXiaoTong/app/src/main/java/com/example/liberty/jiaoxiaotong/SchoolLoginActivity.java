package com.example.liberty.jiaoxiaotong;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liberty.jiaoxiaotong.global.CurrentUser;

public class SchoolLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_login);

        // 显示欢迎语
        showWelcome();

        // "发布成绩" 按钮
        Button gradeBtn = (Button) findViewById(R.id.button_issueGrades);
        if (gradeBtn != null) {
            gradeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO 跳转到相关界面，实现 "发布成绩" 功能
                    Toast.makeText(SchoolLoginActivity.this, "发布成绩", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SchoolLoginActivity.this, IssueGradeActivity.class));
                }
            });
        }


        // "发布公告" 按钮
        Button noticeBtn = (Button) findViewById(R.id.button_issueNotice);
        if (noticeBtn != null) {
            noticeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO 跳转到相关界面，实现 "发布公告" 功能
                    Toast.makeText(SchoolLoginActivity.this, "发布公告", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SchoolLoginActivity.this, SchoolPushActivity.class));
                }
            });
        }


        // "学生信息管理" 按钮
        Button studInfoBtn = (Button) findViewById(R.id.button_studInfoManage);
        if (studInfoBtn != null) {
            studInfoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO 跳转到相关界面，实现 "学生信息管理" 功能
                    //Toast.makeText(SchoolLoginActivity.this, "发布成绩", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SchoolLoginActivity.this, StudInfoManageActivity.class));
                }
            });
        }

        // "编辑微网站" 按钮
        Button editWebBtn = (Button) findViewById(R.id.button_editweb);
        if (editWebBtn != null) {
            editWebBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO 跳转到编辑微网站界面
                    startActivity(new Intent(SchoolLoginActivity.this,SchoolEditWebActivity.class));
                }
            });
        }
    }

    /**
     * 显示欢迎语
     */
    private void showWelcome() {
        TextView welcomeView = (TextView) findViewById(R.id.textView_welcome);
        if (welcomeView != null) {
            String welcome = "欢迎你, " + CurrentUser.USER_NAME;
            welcomeView.setText(welcome);
        }
    }
}

package com.example.liberty.jiaoxiaotong;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liberty.jiaoxiaotong.global.CurrentUser;

public class TeacherLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);

        // 显示欢迎语
        showWelcome();

        // "发布作业" 按钮
        Button homeworkBtn = (Button) findViewById(R.id.button_issueHomework);
        if (homeworkBtn != null) {
            homeworkBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO 跳转相关界面，实现 "发布作业" 功能
                    Toast.makeText(TeacherLoginActivity.this, "发布作业", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TeacherLoginActivity.this, TeacherHomeworkActivity.class));
                }
            });
        }

        // "评价学生" 按钮
        Button commentBtn = (Button) findViewById(R.id.button_commentStud);
        if (commentBtn != null) {
            commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO 跳转相关界面，实现 "评价学生" 功能
                    Toast.makeText(TeacherLoginActivity.this, "评价学生", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TeacherLoginActivity.this, TeacherCommentActivity.class));
                }
            });
        }

        // "联系家长" 按钮
        Button parentBtn = (Button) findViewById(R.id.button_talkwithParent);
        if (parentBtn != null) {
            parentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO 跳转相关界面，实现 "联系家长" 功能
                    Toast.makeText(TeacherLoginActivity.this, "联系家长", Toast.LENGTH_SHORT).show();
                    Uri uri = Uri.parse("https://leanmessage.leanapp.cn");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }

        // "学生奖罚" 按钮
        Button stduRPBtn = (Button) findViewById(R.id.button_studRewardPunish);
        if (stduRPBtn != null) {
            stduRPBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO 跳转相关界面，实现 "学生奖罚" 功能
                    Toast.makeText(TeacherLoginActivity.this, "学生奖罚", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TeacherLoginActivity.this, RewardPunishActivity.class));
                }
            });
        }

        // "发布活动" 按钮
        Button activityBtn = (Button) findViewById(R.id.button_issueAvtivities);
        if (activityBtn != null) {
            activityBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO 跳转相关界面，实现 "发布活动" 功能
                    Toast.makeText(TeacherLoginActivity.this, "发布活动", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TeacherLoginActivity.this, TeacherIssueActivity.class));
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

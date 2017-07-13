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

public class ParentLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login);

        // 显示欢迎语
        showWelcome();

        // "子女信息查询" 按钮
        Button studInfoBtn = (Button) findViewById(R.id.button_checkoutStudInfo);
        if (studInfoBtn != null) {
            studInfoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO 跳转到相关界面，实现 "子女信息查询" 功能
                    Toast.makeText(ParentLoginActivity.this, "子女信息查询", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ParentLoginActivity.this, StudInfoCheckActivity.class));
                }
            });
        }

        // "通知及推送浏览" 按钮
        Button noticeBtn = (Button) findViewById(R.id.button_checkoutNotice);
        if (noticeBtn != null) {
            noticeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO 跳转到相关界面，实现 "通知及推送浏览" 功能
                    Toast.makeText(ParentLoginActivity.this, "通知及推送浏览", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ParentLoginActivity.this,
                            ParentCheckNoticeAndPush.class));
                }
            });
        }

        // "联系教师" 按钮
        Button teacherBtn = (Button) findViewById(R.id.button_talkwithTeacher);
        if (teacherBtn != null) {
            teacherBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO 跳转到相关界面，实现 "联系教师" 功能

                    Toast.makeText(ParentLoginActivity.this, "联系教师", Toast.LENGTH_SHORT).show();
                    Uri uri = Uri.parse("https://leanmessage.leanapp.cn");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }

        // "奖惩记录" 按钮
        Button emergencyBtn = (Button) findViewById(R.id.button_rewardpunish);
        if (emergencyBtn != null) {
            emergencyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO 跳转到相关界面，实现 "奖惩记录" 功能
                    Toast.makeText(ParentLoginActivity.this, "奖惩记录", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ParentLoginActivity.this, ParentCheckRewardPunishActivity.class));
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

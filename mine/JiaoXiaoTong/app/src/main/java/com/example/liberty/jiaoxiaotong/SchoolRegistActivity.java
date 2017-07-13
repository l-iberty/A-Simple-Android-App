package com.example.liberty.jiaoxiaotong;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;

public class SchoolRegistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_regist);

        // 监听 "提交" 按钮
        Button submitBtn = (Button) findViewById(R.id.button_submit);
        if (submitBtn != null) {
            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 获取用户输入
                    final String schoolID = ((TextView) findViewById(R.id.editText_schooID)).getText().toString();
                    final String schoolName = ((TextView) findViewById(R.id.editText_schoolName)).getText().toString();
                    final String passwd = ((TextView) findViewById(R.id.editText_passwd)).getText().toString();
                    String passwd2 = ((TextView) findViewById(R.id.editText_passwdConfirm)).getText().toString();

                    if ((schoolID != null && !schoolID.trim().equals("")) &&
                            (schoolName != null && !schoolName.trim().equals("")) &&
                            (passwd != null && !passwd.trim().equals(""))) {

                        if (!passwd.equals(passwd2)) {
                            Toast.makeText(SchoolRegistActivity.this, "密码确认失败！", Toast.LENGTH_SHORT).show();
                        } else {
                            // 向 SchoolUser 和 LoginInfo 两个表添加记录
                            add_SchoolUserAndLoginInfo(
                                    schoolID,
                                    schoolName,
                                    passwd,
                                    MainActivity.SCHOOL_LOGIN
                            );
                        }
                    }
                }
            }); // "提交" 按钮事件响应结束
        }
    }

    private void add_SchoolUserAndLoginInfo(final String schoolID, final String schoolName,
                                            final String passwd, final int property) {

        // 向 SchoolUser 表录入数据
        AVObject schoolUser = new AVObject("SchoolUser");
        schoolUser.put("schoolID", schoolID);
        schoolUser.put("schoolName", schoolName);

        schoolUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    // 数据录入成功

                    // 向 LoginInfo 表录入数据
                    // 只有向 SchoolUser 和 LoginInfo 的数据录入均成功，本次注册才成功
                    add_LoginInfo(schoolID, schoolName, passwd, property);
                } else {
                    // 数据录入失败
                    Toast.makeText(SchoolRegistActivity.this, "(: 注册失败，请检查网络等", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void add_LoginInfo(String ID, String name, String passwd, int property) {
        AVObject loginInfo = new AVObject("LoginInfo");
        loginInfo.put("ID", ID);
        loginInfo.put("name", name);
        loginInfo.put("passwd", passwd);
        loginInfo.put("property", property);

        loginInfo.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    // 提交成功，返回登录界面重新登录
                    Toast.makeText(SchoolRegistActivity.this, "提交成功！请重新登录 ^_^", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SchoolRegistActivity.this, MainActivity.class));
                }
            }
        });
    }
}

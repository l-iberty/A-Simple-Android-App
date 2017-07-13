package com.example.liberty.jiaoxiaotong;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.example.liberty.jiaoxiaotong.global.CurrentUser;

public class SchoolPushActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_push);

        Button bt = (Button) findViewById(R.id.button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = ((EditText) findViewById(R.id.editText1)).getText().toString();
                String content = ((EditText) findViewById(R.id.editText2)).getText().toString();

                AVObject schoolpush = new AVObject("SchoolPush");
                schoolpush.put("title", title);
                schoolpush.put("content", content);
                schoolpush.put("schoolID", CurrentUser.USER_ID);
                schoolpush.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            // 发布成功
                            Toast.makeText(SchoolPushActivity.this, "成功发布！", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SchoolPushActivity.this, SchoolLoginActivity.class));
                        } else {
                            // 失败的话，请检查网络环境以及 SDK 配置是否正确
                            Toast.makeText(SchoolPushActivity.this, "(: 发布失败，请检查网络", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}

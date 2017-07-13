package com.example.liberty.jiaoxiaotong;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.example.liberty.jiaoxiaotong.global.CurrentUser;

/**
 * Created by WX on 2017/7/9.
 */

public class TeacherCommentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_comment);
        
        // 监听 "评价学生" 按钮
        Button commentBtn = (Button) (findViewById(R.id.comment_bt));
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String studentID = ((EditText) findViewById(R.id.comment_stuID)).getText().toString();
                String content = ((EditText) findViewById(R.id.comment_content)).getText().toString();
                String teacherID = CurrentUser.USER_ID;
                
                AVObject comment = new AVObject("Comment");
                comment.put("teacherID", teacherID);
                comment.put("studentID", studentID);
                comment.put("content", content);
                comment.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            // 存储成功
                            Toast.makeText(TeacherCommentActivity.this, "评价成功!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(TeacherCommentActivity.this, TeacherLoginActivity.class));
                        } else {
                            // 失败的话，请检查网络环境以及 SDK 配置是否正确
                            Toast.makeText(TeacherCommentActivity.this, "(: 评价失败！请检查网络连接...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}

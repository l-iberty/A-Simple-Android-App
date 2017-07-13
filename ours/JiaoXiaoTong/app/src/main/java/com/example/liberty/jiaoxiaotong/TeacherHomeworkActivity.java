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
import com.example.liberty.jiaoxiaotong.global.CurrentUser;

public class TeacherHomeworkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_homework);

        // "提交" 按钮
        Button submitBtn = (Button) findViewById(R.id.button_submit);
        if (submitBtn != null) {
            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String classID = ((TextView) findViewById(R.id.editText_classID)).getText().toString();
                    String content = ((TextView) findViewById(R.id.editText_homework)).getText().toString();

                    AVObject homework = new AVObject("Homework");
                    homework.put("teacherID", CurrentUser.USER_ID);
                    homework.put("classID", classID);
                    homework.put("content", content);
                    homework.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                Toast.makeText(TeacherHomeworkActivity.this, "提交成功!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(TeacherHomeworkActivity.this, TeacherLoginActivity.class));
                            } else {
                                Toast.makeText(TeacherHomeworkActivity.this, "(: 提交失败，请检查网络", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
    }
}

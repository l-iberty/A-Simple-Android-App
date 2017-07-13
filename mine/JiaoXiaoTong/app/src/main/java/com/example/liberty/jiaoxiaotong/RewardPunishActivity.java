package com.example.liberty.jiaoxiaotong;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.example.liberty.jiaoxiaotong.global.CurrentUser;

public class RewardPunishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_punish);

        // "提交" 按钮
        Button submitBtn = (Button) findViewById(R.id.button_submit);
        if (submitBtn != null) {
            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String studentID = ((TextView) findViewById(R.id.editText_studentID)).getText().toString();
                    String content = ((TextView) findViewById(R.id.editText_rewardpunish)).getText().toString();

                    AVObject rewardPunishment = new AVObject("RewardPunishment");
                    rewardPunishment.put("studentID", studentID);
                    rewardPunishment.put("teacherID", CurrentUser.USER_ID);
                    rewardPunishment.put("content", content);
                    rewardPunishment.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                Toast.makeText(RewardPunishActivity.this, "提交成功！", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RewardPunishActivity.this, TeacherLoginActivity.class));
                            } else {
                                Toast.makeText(RewardPunishActivity.this, "(: 提交失败，请检查网络", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
    }
}

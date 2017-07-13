package com.example.liberty.jiaoxiaotong;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.liberty.jiaoxiaotong.global.CurrentUser;

import java.util.List;

public class ParentCheckGradeRankActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_check_grade_rank);

        final TextView gradeRank = (TextView) findViewById(R.id.textView_gradeRank);
        if (gradeRank != null) {
            String parentID = CurrentUser.USER_ID;

            // 从 ParentUser 找到与 parentID 对应的 studentID, 从 Grade 找到
            // 该 studentID 对应的记录 gradeNo.
            // 根据我们的设计, Grade 表中的每个 studentID 对应一条记录, 学校应该
            // 及时上传学生的成绩排名.

            AVQuery<AVObject> parentQuery = new AVQuery<>("ParentUser");
            parentQuery.whereEqualTo("parentID", parentID);
            parentQuery.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if (e == null) {
                        if (!list.isEmpty()) {
                            AVObject parentUser = list.get(0);
                            String studentID = parentUser.getString("studentID");

                            AVQuery<AVObject> gradeQuery = new AVQuery<>("Grade");
                            gradeQuery.whereEqualTo("studentID", studentID);
                            gradeQuery.findInBackground(new FindCallback<AVObject>() {
                                @Override
                                public void done(List<AVObject> list, AVException e) {
                                    if (e == null) {
                                        if (!list.isEmpty()) {
                                            AVObject grade = list.get(0);
                                            String gradeNo = grade.getString("gradeNo");
                                            String show = "第 " + gradeNo + " 名";
                                            // 显示
                                            gradeRank.setText(show);
                                        } else {
                                            gradeRank.setText("(: 找不到您孩子的成绩信息");
                                        }
                                    } else {
                                        gradeRank.setText("(: 出错了");
                                    }
                                }
                            });
                        } else {
                            gradeRank.setText("(: 找不到您孩子的成绩信息");
                        }
                    } else {
                        gradeRank.setText("(: 出错了");
                    }
                }
            });
        }
    }
}

package com.example.liberty.jiaoxiaotong;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.liberty.jiaoxiaotong.global.CurrentUser;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class ParentCheckHomeworkActivity extends AppCompatActivity {
    private ListView homeworkListView = null;
    private ArrayAdapter<String> adapter = null;
    private List<String> homeworkList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_check_homework);

        homeworkListView = (ListView) findViewById(R.id.listView_homework);
        if (homeworkListView != null) {
            // 查询逻辑:
            // ParentUser.parentID -> ParentUser.studentID ->
            // 从 ParentUser.studentID 截取出 classID -> Homework.content
            // 在我们的设计中 Homework 中的数据在每天 00:00 被清空, 以此实时更新作业信息

            AVQuery<AVObject> parentQuery = new AVQuery<>("ParentUser");
            parentQuery.whereEqualTo("parentID", CurrentUser.USER_ID);
            parentQuery.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if (e == null) {
                        if (!list.isEmpty()) {
                            AVObject parentUser = list.get(0);
                            String studentID = parentUser.getString("studentID");
                            String classID = studentID.substring(0, 10);

                            AVQuery<AVObject> homeworkQuery = new AVQuery<>("Homework");
                            homeworkQuery.whereEqualTo("classID", classID);
                            homeworkQuery.findInBackground(new FindCallback<AVObject>() {
                                @Override
                                public void done(List<AVObject> list, AVException e) {
                                    if (e == null) {
                                        if (!list.isEmpty()) {
                                            AVObject homework = list.get(0);
                                            String content = homework.getString("content");
                                            homeworkList.add(content);
                                            // 显示到 ListView
                                            adapter = new ArrayAdapter<>(ParentCheckHomeworkActivity.this,
                                                    android.R.layout.simple_list_item_1,
                                                    homeworkList);
                                            homeworkListView.setAdapter(adapter);
                                        } else {
                                            Toast.makeText(ParentCheckHomeworkActivity.this, "老师今天没有布置作业", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(ParentCheckHomeworkActivity.this, "(: 出错了", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(ParentCheckHomeworkActivity.this, "您的账号信息出了偏差, 请联系校方", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ParentCheckHomeworkActivity.this, "(: 出错了", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}

package com.example.liberty.jiaoxiaotong;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.liberty.jiaoxiaotong.global.*;

public class ParentCheckSchoolPushActivity extends AppCompatActivity {
    private ListView titleListView = null;
    private ArrayAdapter<String> adapter = null;
    private List<String> titleList = new ArrayList<>();
    private List<String> contentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_check_school_push);

        titleListView = (ListView) findViewById(R.id.listView_title);
        if (titleListView != null) {
            // 查询逻辑
            // parentID -> ParentUser.schoolID -> SchoolPush.content

            AVQuery<AVObject> parentQuery = new AVQuery<>("ParentUser");
            parentQuery.whereEqualTo("parentID", CurrentUser.USER_ID);
            parentQuery.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if (e == null) {
                        if (!list.isEmpty()) {
                            AVObject parentUser = list.get(0);
                            String schoolID = parentUser.getString("schoolID");

                            AVQuery<AVObject> schoolPushQuery = new AVQuery<>("SchoolPush");
                            schoolPushQuery.whereEqualTo("schoolID", schoolID);
                            schoolPushQuery.findInBackground(new FindCallback<AVObject>() {
                                @Override
                                public void done(List<AVObject> list, AVException e) {
                                    if (e == null) {
                                        if (!list.isEmpty()) {
                                            for (AVObject schoolPush : list) {
                                                titleList.add(schoolPush.getString("title"));
                                                contentList.add(schoolPush.getString("content"));
                                            }
                                            adapter = new ArrayAdapter<>(ParentCheckSchoolPushActivity.this,
                                                    android.R.layout.simple_list_item_1,
                                                    titleList);
                                            titleListView.setAdapter(adapter);
                                        } else {
                                            Toast.makeText(ParentCheckSchoolPushActivity.this, "校方尚未发布公告新闻", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(ParentCheckSchoolPushActivity.this, "(: 出错了，请检查网络", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else
                            Toast.makeText(ParentCheckSchoolPushActivity.this, "您的账号信息出了偏差，请联系校方", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ParentCheckSchoolPushActivity.this, "(: 出错了，请检查网络", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // 监听 ListView
            titleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String title = adapter.getItem(i);
                    String content = contentList.get(i);
                    SchoolPush.title = title;
                    SchoolPush.content = content;
                    // 跳转
                    startActivity(new Intent(ParentCheckSchoolPushActivity.this,
                            NewsActivity.class));
                }
            });
        }
    }
}

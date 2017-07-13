package com.example.liberty.jiaoxiaotong;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.liberty.jiaoxiaotong.global.CurrentUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WX on 2017/7/9.
 */

public class ParentCheckCommentActivity extends AppCompatActivity {
    private ListView contentListView = null;
    private ArrayAdapter<String> adapter = null;
    private List<String> contentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_checkcomment);

        contentListView = (ListView) findViewById(R.id.listView_content);

        final String parentID1 = CurrentUser.USER_ID;

        // 家长ID -> 学生ID -> 评语
        AVQuery<AVObject> parentQuery = new AVQuery<>("ParentUser");
        parentQuery.whereEqualTo("parentID", parentID1);
        parentQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    for (final AVObject avObject : list) {
                        // 获取当前 ParentUser 对应的 studentID
                        String studentID = avObject.getString("studentID");

                        // 从 Comment 表检索出该 studentID 对应的评语内容 content
                        AVQuery<AVObject> query = new AVQuery<>("Comment");
                        query.whereEqualTo("studentID", studentID);
                        query.findInBackground(new FindCallback<AVObject>() {
                            @Override
                            public void done(List<AVObject> list, AVException e) {
                                if (e == null) {
                                    int count = 0;
                                    for (AVObject avObject : list) {
                                        if (count++ < 3) { // 最多获取3条记录
                                            String content = avObject.getString("content");
                                            contentList.add(content);
                                        } else break;
                                    }
                                    // 显示找到的所有评语
                                    adapter = new ArrayAdapter<>(ParentCheckCommentActivity.this,
                                            android.R.layout.simple_list_item_1,
                                            contentList);
                                    contentListView.setAdapter(adapter);
                                }
                            }
                        });
                    }
                }
            }
        });

    }
}

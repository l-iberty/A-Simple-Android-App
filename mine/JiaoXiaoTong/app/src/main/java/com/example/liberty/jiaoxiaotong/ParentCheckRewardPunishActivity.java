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
import java.util.List;

public class ParentCheckRewardPunishActivity extends AppCompatActivity {
    private ListView rpListView = null;
    private ArrayAdapter<String> adapter = null;
    private List<String> rpList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_check_reward_punish);

        rpListView = (ListView) findViewById(R.id.listView_rewardpunish);
        if (rpListView != null) {
            // 查询逻辑 ParentUser.parentID -> ParentUser.studentID -> RewardPunishment.content
            AVQuery<AVObject> parentQuery = new AVQuery<>("ParentUser");
            parentQuery.whereEqualTo("parentID", CurrentUser.USER_ID);
            parentQuery.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if (e == null) {
                        if (!list.isEmpty()) {
                            AVObject parentUser = list.get(0);
                            String studentID = parentUser.getString("studentID");

                            AVQuery<AVObject> rpQuery = new AVQuery<>("RewardPunishment");
                            rpQuery.whereEqualTo("studentID", studentID);
                            rpQuery.findInBackground(new FindCallback<AVObject>() {
                                @Override
                                public void done(List<AVObject> list, AVException e) {
                                    if (e == null) {
                                        if (!list.isEmpty()) {
                                            for (AVObject rp : list) {
                                                String content = rp.getString("content");
                                                rpList.add(content);
                                            }
                                            adapter = new ArrayAdapter<>(ParentCheckRewardPunishActivity.this,
                                                    android.R.layout.simple_list_item_1,
                                                    rpList);
                                            rpListView.setAdapter(adapter);
                                        } else {
                                            Toast.makeText(ParentCheckRewardPunishActivity.this, "您孩子的奖惩记录为空", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(ParentCheckRewardPunishActivity.this, "(: 出错了，请检查网络", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(ParentCheckRewardPunishActivity.this, "您的账号信息除了偏差，请联系校方", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ParentCheckRewardPunishActivity.this, "(: 出错了，请检查网络", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}

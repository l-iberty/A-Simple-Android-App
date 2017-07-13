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
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;

import java.util.Arrays;
import java.util.List;

public class ParentRegistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_regist);

        // 监听 "提交" 按钮
        Button submitBtn = (Button) findViewById(R.id.button_submit);
        if (submitBtn != null) {
            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 获取用户输入
                    final String parentName = ((TextView) findViewById(R.id.editText_parentName)).getText().toString();
                    final String parentID = ((TextView) findViewById(R.id.editText_parentID)).getText().toString();
                    final String schoolID = ((TextView) findViewById(R.id.editText_schoolID)).getText().toString();
                    final String studentID = ((TextView) findViewById(R.id.editText_studentID)).getText().toString();
                    final String passwd = ((TextView) findViewById(R.id.editText_passwd)).getText().toString();
                    final String passwd2 = ((TextView) findViewById(R.id.editText_passwdConfirm)).getText().toString();

                    if ((parentName != null && !parentName.trim().equals("")) &&
                            (parentID != null && !parentID.trim().equals("")) &&
                            (schoolID != null && !schoolID.trim().equals("")) &&
                            (studentID != null && !studentID.trim().equals("")) &&
                            (passwd != null && !passwd.trim().equals(""))) {

                        run(parentName, parentID, schoolID, studentID,
                                passwd, passwd2);
                    }
                }
            }); // "提交" 按钮事件响应结束
        }
    }


    /**
     * run() 完成以下工作:
     * 1. 将用户的输入 parentName, parentID, schoolID 和 studentID 与现有表 ParentUser 中的记录进行
     * 匹配，若匹配失败则说明用户输入的信息无效，注册失败；若匹配成功则进行第2步.
     * 2. 密码确认后，将 parentName, parentID, 用户属性(类型)和 passwd 录入 LoginInfo 表，录入成功后
     * 进行第3步；否则注册失败.
     * 3. 注册成功，跳转到登录界面，用户使用刚才注册的账号登录.
     * <p>
     * On more thing, 如果用户已经注册过账号，但是Ta也许忘记自己注册过，还是点击了 "注册" 按钮，将导致
     * LoginInfo 中存在两条仅创建日期不同的记录. 因此，向 LoginInfo 表录入信息之前需进行查重. 而且我们
     * 不允许用户使用不同的密码注册多个账号.
     *
     * @param parentName
     * @param parentID
     * @param schoolID
     * @param studentID
     * @param passwd
     * @param passwd2
     */
    void run(final String parentName, final String parentID,
             final String schoolID, final String studentID,
             final String passwd, final String passwd2) {

        // 创建查询
        AVQuery<AVObject> parentNameQuery = new AVQuery<>("ParentUser");
        AVQuery<AVObject> parentIDQuery = new AVQuery<>("ParentUser");
        AVQuery<AVObject> schoolIDQuery = new AVQuery<>("ParentUser");
        AVQuery<AVObject> studentIDQuery = new AVQuery<>("ParentUser");

        parentNameQuery.whereEqualTo("parentName", parentName);
        parentIDQuery.whereEqualTo("parentID", parentID);
        schoolIDQuery.whereEqualTo("schoolID", schoolID);
        studentIDQuery.whereEqualTo("studentID", studentID);

        AVQuery<AVObject> query =
                AVQuery.and(Arrays.asList(parentNameQuery, parentIDQuery, schoolIDQuery, studentIDQuery));
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (!list.isEmpty()) {
                        // 注册成功后将必要信息录入 LoginInfo 表，最后跳回登录界面，重新登录

                        // 先确认密码
                        if (!passwd.equals(passwd2)) {
                            Toast.makeText(ParentRegistActivity.this, "密码确认失败!", Toast.LENGTH_SHORT).show();
                        } else {
                            // 查重
                            AVQuery<AVObject> idQuery = new AVQuery<>("LoginInfo");
                            AVQuery<AVObject> nameQuery = new AVQuery<>("LoginInfo");
                            AVQuery<AVObject> propertyQuery = new AVQuery<>("LoginInfo");

                            idQuery.whereEqualTo("ID", parentID);
                            nameQuery.whereEqualTo("name", parentName);
                            propertyQuery.whereEqualTo("property", MainActivity.PARENT_LOGIN);

                            AVQuery<AVObject> query = AVQuery.and(
                                    Arrays.asList(idQuery, nameQuery, propertyQuery));
                            query.findInBackground(new FindCallback<AVObject>() {
                                @Override
                                public void done(List<AVObject> list, AVException e) {
                                    if (e == null) {
                                        if (list.isEmpty()) {
                                            AVObject loginInfo = new AVObject("LoginInfo");
                                            loginInfo.put("ID", parentID);
                                            loginInfo.put("name", parentName);
                                            loginInfo.put("property", MainActivity.PARENT_LOGIN);
                                            loginInfo.put("passwd", passwd);

                                            loginInfo.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(AVException e) {
                                                    if (e == null) {
                                                        // LoginInfo 表写入成功
                                                        Toast.makeText(ParentRegistActivity.this, "注册成功！请重新登录", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(ParentRegistActivity.this, MainActivity.class));
                                                    } else {
                                                        Toast.makeText(ParentRegistActivity.this, "(: 注册失败, 无法录入您的账号信息", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        } else {
                                            Toast.makeText(ParentRegistActivity.this, "您已注册过账号，不能重复注册！", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                        }
                    } else {
                        Toast.makeText(ParentRegistActivity.this, "(: 注册失败, 数据库中没有您的信息", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }); // 查询验证结束
    }
}

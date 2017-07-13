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

public class TeacherRegistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_regist);

        // 监听 "提交" 按钮
        Button submitBtn = (Button) findViewById(R.id.button_submit);
        if (submitBtn != null) {
            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 获取用户输入
                    final String teacherName = ((TextView) findViewById(R.id.editText_teacherName)).getText().toString();
                    final String teacherID = ((TextView) findViewById(R.id.editText_teacherID)).getText().toString();
                    final String schoolID = ((TextView) findViewById(R.id.editText_schoolID)).getText().toString();
                    final String classID = ((TextView) findViewById(R.id.editText_classID)).getText().toString();
                    final String passwd = ((TextView) findViewById(R.id.editText_passwd)).getText().toString();
                    final String passwd2 = ((TextView) findViewById(R.id.editText_passwdConfirm)).getText().toString();

                    if ((teacherName != null && !teacherName.trim().equals("")) &&
                            (teacherID != null && !teacherID.trim().equals("")) &&
                            (schoolID != null && !schoolID.trim().equals("")) &&
                            (classID != null && !classID.trim().equals("")) &&
                            (passwd != null && !passwd.trim().equals(""))) {

                        run(teacherName, teacherID, schoolID, classID,
                                passwd, passwd2);
                    }
                }
            }); // "提交" 按钮事件响应结束
        }
    }


    // run() 方法出现在 TeacherRegistActivity 和 ParentRegistActivity 两个类中, 视图把 run() 统一
    // 到 RegistActivity 中, 然后 TeacherRegistActivity 和 ParentRegistActivity 继承 RegistActivity,
    // 但 Java 是单继承，而且也不可能把 RegistActivity 定义为 interface, 所以暂且违背一下代码重用原则吧.

    /**
     * run() 完成以下工作:
     * 1. 将用户的输入 teacherName, teacherID, schoolID 与现有表 TeacherUser 中的记录进行
     * 匹配，若匹配失败则说明用户输入的信息无效，注册失败；若匹配成功则进行第2步.
     * 2. 密码确认后，将 teacherName, teacherID, 用户属性(类型)和 passwd 录入 LoginInfo 表，录入成功后
     * 进行第3步；否则注册失败.
     * 3. 注册成功，跳转到登录界面，用户使用刚才注册的账号登录.
     * <p>
     * On more thing, 如果用户已经注册过账号，但是Ta也许忘记自己注册过，还是点击了 "注册" 按钮，将导致
     * LoginInfo 中存在两条仅创建日期不同的记录. 因此，向 LoginInfo 表录入信息之前需进行查重. 而且我们
     * 不允许用户使用不同的密码注册多个账号.
     *
     * @param teacherName
     * @param teacherID
     * @param schoolID
     * @param classID
     * @param passwd
     * @param passwd2
     */
    void run(final String teacherName, final String teacherID,
             final String schoolID, final String classID,
             final String passwd, final String passwd2) {

        // 创建查询
        AVQuery<AVObject> teacherNameQuery = new AVQuery<>("TeacherUser");
        AVQuery<AVObject> teacherIDQuery = new AVQuery<>("TeacherUser");
        AVQuery<AVObject> schoolIDQuery = new AVQuery<>("TeacherUser");
        AVQuery<AVObject> classIDQuery = new AVQuery<>("TeacherUser");

        teacherNameQuery.whereEqualTo("teacherName", teacherName);
        teacherIDQuery.whereEqualTo("teacherID", teacherID);
        schoolIDQuery.whereEqualTo("schoolID", schoolID);
        classIDQuery.whereEqualTo("classID", classID);

        AVQuery<AVObject> query =
                AVQuery.and(Arrays.asList(teacherNameQuery, teacherIDQuery, schoolIDQuery, classIDQuery));
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (!list.isEmpty()) {
                        // 注册成功后将必要信息录入 LoginInfo 表，最后跳回登录界面，重新登录

                        // 先确认密码
                        if (!passwd.equals(passwd2)) {
                            Toast.makeText(TeacherRegistActivity.this, "密码确认失败!", Toast.LENGTH_SHORT).show();
                        } else {
                            // 查重
                            AVQuery<AVObject> idQuery = new AVQuery<>("LoginInfo");
                            AVQuery<AVObject> nameQuery = new AVQuery<>("LoginInfo");
                            AVQuery<AVObject> propertyQuery = new AVQuery<>("LoginInfo");

                            idQuery.whereEqualTo("ID", teacherID);
                            nameQuery.whereEqualTo("name", teacherName);
                            propertyQuery.whereEqualTo("property", MainActivity.TEACHER_LOGIN);

                            AVQuery<AVObject> query = AVQuery.and(
                                    Arrays.asList(idQuery, nameQuery, propertyQuery));
                            query.findInBackground(new FindCallback<AVObject>() {
                                @Override
                                public void done(List<AVObject> list, AVException e) {
                                    if (e == null) {
                                        if (list.isEmpty()) {
                                            AVObject loginInfo = new AVObject("LoginInfo");
                                            loginInfo.put("ID", teacherID);
                                            loginInfo.put("name", teacherName);
                                            loginInfo.put("property", MainActivity.TEACHER_LOGIN);
                                            loginInfo.put("passwd", passwd);

                                            loginInfo.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(AVException e) {
                                                    if (e == null) {
                                                        // LoginInfo 表写入成功
                                                        Toast.makeText(TeacherRegistActivity.this, "注册成功！请重新登录", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(TeacherRegistActivity.this, MainActivity.class));
                                                    } else {
                                                        Toast.makeText(TeacherRegistActivity.this, "(: 注册失败, 无法录入您的账号信息", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        } else {
                                            Toast.makeText(TeacherRegistActivity.this, "您已注册过账号，不能重复注册！", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                        }
                    } else {
                        Toast.makeText(TeacherRegistActivity.this, "(: 注册失败, 数据库中没有您的信息", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }); // 查询验证结束
    }
}

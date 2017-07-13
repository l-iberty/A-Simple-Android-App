package com.example.liberty.jiaoxiaotong;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.liberty.jiaoxiaotong.global.CurrentUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // 3种登录类型常量定义
    public final static int SCHOOL_LOGIN = 0;
    public final static int PARENT_LOGIN = 1;
    public final static int TEACHER_LOGIN = 2;

    // 用户输入的账号和密码
    private String account = "";
    private String passwd = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 登录类型单选按钮
        final RadioButton schoolRbtn = (RadioButton) findViewById(R.id.radioButton_school);
        final RadioButton parentRbtn = (RadioButton) findViewById(R.id.radioButton_parent);
        final RadioButton teacherRbtn = (RadioButton) findViewById(R.id.radioButton_teacher);

        // 监听 "登录" 按钮
        final Button loginBtn = (Button) findViewById(R.id.button_login);
        if (loginBtn != null) {
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 获取账号密码
                    account = ((TextView) findViewById(R.id.editText_account)).getText().toString();
                    passwd = ((TextView) findViewById(R.id.editText_passwd)).getText().toString();

                    // 识别登录类型
                    int loginType = -1;
                    if (schoolRbtn.isChecked()) {
                        loginType = SCHOOL_LOGIN;
                    } else if (parentRbtn.isChecked()) {
                        loginType = PARENT_LOGIN;
                    } else if (teacherRbtn.isChecked()) {
                        loginType = TEACHER_LOGIN;
                    }

                    if (loginType == -1) {
                        // 用户未选择注册类型
                        Toast.makeText(MainActivity.this, "请您选择登录类型 ^_^", Toast.LENGTH_SHORT).show();
                    } else {
                        // 账号验证，若验证成功则跳转到用户主页
                        checkAccount(account, passwd, loginType);
                    }
                }
            });
        }


        // 监听 "注册" 按钮
        Button regBtn = (Button) findViewById(R.id.button_regist);
        if (regBtn != null) {
            regBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 跳转到注册界面，选择注册类型
                    startActivity(new Intent(MainActivity.this, RegistActivity.class));
                }
            });
        }
    }

    /**
     * 根据登录类型跳转到相应的登录界面
     *
     * @param loginType 登录类型
     */
    private void login(int loginType) {
        switch (loginType) {
            case SCHOOL_LOGIN:
                //Toast.makeText(this, "school", Toast.LENGTH_SHORT).show();
                setWelcomeAndJump(account, SCHOOL_LOGIN, SchoolLoginActivity.class);
                break;
            case PARENT_LOGIN:
                //Toast.makeText(this, "parent", Toast.LENGTH_SHORT).show();
                setWelcomeAndJump(account, PARENT_LOGIN, ParentLoginActivity.class);
                break;
            case TEACHER_LOGIN:
                //Toast.makeText(this, "teacher", Toast.LENGTH_SHORT).show();
                setWelcomeAndJump(account, TEACHER_LOGIN, TeacherLoginActivity.class);
                break;
        }
    }


    /**
     * 账号验证，若验证成功则调用 login(loginType) 跳转到用户主页
     *
     * @param account
     * @param passwd
     * @param loginType
     */
    private void checkAccount(String account, String passwd, final int loginType) {
        // 创建查询
        AVQuery<AVObject> accountQuery = new AVQuery<>("LoginInfo");
        accountQuery.whereEqualTo("ID", account);

        AVQuery<AVObject> pwdQuery = new AVQuery<>("LoginInfo");
        pwdQuery.whereEqualTo("passwd", passwd);

        AVQuery<AVObject> typeQuery = new AVQuery<>("LoginInfo");
        typeQuery.whereEqualTo("property", loginType);

        AVQuery<AVObject> query = AVQuery.and(Arrays.asList(accountQuery, pwdQuery, typeQuery));
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (!list.isEmpty()) {
                        Toast.makeText(MainActivity.this, "成功登录！", Toast.LENGTH_SHORT).show();
                        login(loginType); // 登录到用户主页
                    } else {
                        Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * 将当前用户的登录信息记录到 CurrentUser 中, 并跳转到相应的 Activity.
     * 注意:
     * startActivity() 的调用被放到了回调函数里面，否则由于异步操作的特点，在跳转到
     * 用户主页前一时刻，CurrentUser.USER_NAME 尚未设置完成，将导致 setWelcome()
     * 显示这样的欢迎语: "欢迎你, null".
     *
     * @param userId         当前用户ID
     * @param loginType      当前用户的登录类型(属性)
     * @param targetActivity 需要跳转到的 Activity
     */
    void setWelcomeAndJump(String userId, int loginType, final Class targetActivity) {
        CurrentUser.USER_ID = userId;
        CurrentUser.LOGIN_TYPE = loginType;

        // 根据 userId, 从 LoginInfo 表中找到对应的 userName
        AVQuery<AVObject> query = new AVQuery<>("LoginInfo");
        query.whereEqualTo("ID", userId);

        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (!list.isEmpty()) {
                        // 将用户名记录到 CurrentUser
                        String userName = list.get(0).getString("name");
                        CurrentUser.USER_NAME = userName;

                        // 跳转
                        startActivity(new Intent(MainActivity.this, targetActivity));
                    }
                }
            }
        });
    }
}

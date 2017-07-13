package com.example.liberty.jiaoxiaotong;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.liberty.jiaoxiaotong.util.FileView;
import com.example.liberty.jiaoxiaotong.util.ImportUtilities;

import java.util.ArrayList;
import java.util.List;

public class IssueGradeActivity extends AppCompatActivity {
    private ListView filePathsListView = null;
    private ArrayAdapter<String> adapter = null;
    private List<String> filePaths = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_grade);

        // 显示 *.xls 文件路径的 ListView
        filePathsListView = (ListView) findViewById(R.id.listView_filePaths);
        if (filePathsListView != null) {
            filePathsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (adapter != null) {
                        // 获取选中的 *.xls 文件路径, 解析后将成绩数据上传到云平台
                        String xlsFile = adapter.getItem(i);
                        ImportUtilities iu = new ImportUtilities(xlsFile);
                        iu.uploadGradeInfo(iu.getGrades());
                    }
                }
            });
        }

        // "发布成绩" 按钮
        Button importBtn = (Button) findViewById(R.id.button_importGrade);
        if (importBtn != null) {
            importBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO 实现 "发布成绩" 功能

                    // 文件检索非常耗时，单独为其开一个线程
                    Thread _thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            FileView fileView = new FileView();
                            String SdPath = fileView.getSdPath(); // 获取内部存储卡路径
                            List<String> TfPaths = fileView.getTfPath(); // 获取扩展存储卡路径

                            // 将SD卡路径和TF卡路径整合到一个容器中
                            List<String> paths = new ArrayList<>(TfPaths);
                            paths.add(SdPath);

                            // 搜索 *.xls 文件
                            filePaths = fileView.getFilePaths(paths, ".xls");
                        }
                    });
                    _thread.start();

                    // TODO 添加进度提示

                    try {
                        _thread.join(); // 挂起主线程
                    } catch (InterruptedException e) {
                        // TODO handle the exception
                    }

                    if (filePathsListView != null) {
                        adapter = new ArrayAdapter<>(IssueGradeActivity.this,
                                android.R.layout.simple_list_item_1,
                                filePaths);
                        filePathsListView.setAdapter(adapter);
                    }
                }
            });
        }
    }
}

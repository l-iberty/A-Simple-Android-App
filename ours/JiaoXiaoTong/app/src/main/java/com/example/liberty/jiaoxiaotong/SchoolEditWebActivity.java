package com.example.liberty.jiaoxiaotong;

import android.content.Intent;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SchoolEditWebActivity extends AppCompatActivity {
    private ImageView mImageViewSelect;
    private byte[] mImageBytes = null;
    private Handler mHandler = new Handler();

//  private ProgressCallback mImageUploadProgressCallback = new ProgressCallback() {
//    @Override
//    public void done(Integer integer) {
//      final int mProgressStatus = integer;
//      mProgerss.setProgress(mProgressStatus);
//    }
//  };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_edit_web);

        mImageViewSelect = (ImageView) findViewById(R.id.imageButton);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Button mButtonSelect = (Button) findViewById(R.id.button5);
        mButtonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 42);
            }
        });

        final EditText mDiscriptionEdit = (EditText) findViewById(R.id.editText3);
        findViewById(R.id.button_submit_publish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("".equals(mDiscriptionEdit.getText().toString())) {
                    Toast.makeText(SchoolEditWebActivity.this, "请输入学校简介", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mImageBytes == null) {
                    Toast.makeText(SchoolEditWebActivity.this, "请选择一张照片", Toast.LENGTH_SHORT).show();
                    return;
                }

                AVObject product = new AVObject("SchoolPush");
                product.put("description", mDiscriptionEdit.getText().toString());
                product.put("image", new AVFile("productPic", mImageBytes));
                product.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            SchoolEditWebActivity.this.finish();
                            Toast.makeText(SchoolEditWebActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SchoolEditWebActivity.this, SchoolLoginActivity.class));
                        } else {

                            Toast.makeText(SchoolEditWebActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
//        }, mImageUploadProgressCallback);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 42 && resultCode == RESULT_OK) {
            try {
                mImageViewSelect.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData()));
                mImageBytes = getBytes(getContentResolver().openInputStream(data.getData()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}








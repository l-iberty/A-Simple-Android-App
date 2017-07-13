package com.example.liberty.jiaoxiaotong.util;

import android.os.Environment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liberty on 2017/7/8.
 */

public class FileView {
    // filePaths 用于 getFilePaths0() 的递归调用，由两个重载的 getFilePaths() 返回给调用者
    private List<String> filePaths = new ArrayList<>();

    /**
     * @return 内部存储卡(SD卡)的绝对路径
     */
    public String getSdPath() {
        String SdPath = null; // SD卡的绝对路径

        // 检测SD卡是否存在
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED
        )) {
            File SdFile = Environment.getExternalStorageDirectory();
            SdPath = SdFile.getAbsolutePath();
        }

        return SdPath;
    }


    // getTfPath() 的实现代码来自网络，其中原理我暂时不懂

    /**
     * @return 扩展存储卡(TF卡)的的绝对路径. 一个手机可以同时使用多个TF卡，需将结果放入 List 容器.
     */
    public List<String> getTfPath() {
        List<String> paths = new ArrayList<>();
        File extFile = Environment.getExternalStorageDirectory();

        try {
            // obtain executed result of command line code of 'mount', to judge
            // whether tfCard exists by the result
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("mount");
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            int mountPathIndex = 1;

            while ((line = br.readLine()) != null) {
                // format of sdcard file system: vfat/fuse
                if ((!line.contains("fat") && !line.contains("fuse") && !line
                        .contains("storage"))
                        || line.contains("secure")
                        || line.contains("asec")
                        || line.contains("firmware")
                        || line.contains("shell")
                        || line.contains("obb")
                        || line.contains("legacy") || line.contains("data")) {
                    continue;
                }

                String[] parts = line.split(" ");
                int length = parts.length;
                if (mountPathIndex >= length) {
                    continue;
                }

                String mountPath = parts[mountPathIndex];
                if (!mountPath.contains("/") || mountPath.contains("data")
                        || mountPath.contains("Data")) {
                    continue;
                }

                File mountRoot = new File(mountPath);
                if (!mountRoot.exists() || !mountRoot.isDirectory()
                        || !mountRoot.canWrite()) {
                    continue;
                }

                boolean equalsToPrimarySD = mountPath.equals(extFile
                        .getAbsolutePath());
                if (equalsToPrimarySD) {
                    continue;
                }

                // 扩展存储卡即TF卡路径
                paths.add(mountPath);
            }
        } catch (IOException e) {
            // TODO handle the exception
        }

        return paths;
    }


    /**
     * 获取特定扩展名的文件的绝对路径
     *
     * @param path 路径
     * @param ext  扩展名
     */
    private void getFilePaths0(String path, String ext) {
        File[] files = new File(path).listFiles();

        for (File file : files) {
            if (file.isDirectory()) {
                getFilePaths0(file.getAbsolutePath(), ext);
            } else {
                String filePath = file.getAbsolutePath();
                if (filePath.endsWith(ext)) {
                    filePaths.add(filePath);
                }
            }
        }
    }


    /**
     * 获取"path"路径下特定扩展名的文件的绝对路径
     *
     * @param path 路径
     * @param ext  扩展名
     * @return 路径"path"下所有具有扩展名"ext"的文件的绝对路径列表
     */
    private List<String> getFilePaths(String path, String ext) {
        getFilePaths0(path, ext);
        return filePaths;
    }


    /**
     * 获取"paths"路径列表下特定扩展名的文件的绝对路径
     *
     * @param paths 路径列表
     * @param ext   扩展名
     * @return 路径列表"path"下所有具有扩展名"ext"的文件的绝对路径列表
     */
    public List<String> getFilePaths(List<String> paths, String ext) {
        for (String path : paths) {
            getFilePaths(path, ext);
        }

        return filePaths;
    }
}

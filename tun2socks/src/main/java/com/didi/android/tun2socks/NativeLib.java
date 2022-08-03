package com.didi.android.tun2socks;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.List;

public class NativeLib {

    // Used to load the 'tun2socks' library on application startup.
    static {
        System.loadLibrary("tun2socks");
    }

    /**
     * A native method that is implemented by the 'tun2socks' native library,
     * which is packaged with this application.
     */

    public void test(Activity activity) {

        String findLibrary = "";
        try {
            ClassLoader loader = this.getClass().getClassLoader();
            Method library = ClassLoader.class.getDeclaredMethod("findLibrary", String.class);
            findLibrary = (String) library.invoke(loader, "tun2socks");
        } catch (Exception e) {
            e.printStackTrace();
        }

        File libFile = new File(findLibrary);
        Log.e("ACC", "findLibrary::" + findLibrary + ", " + libFile.exists() + ", " + libFile.canExecute());

        File nativeLibFile = new File(activity.getApplicationInfo().nativeLibraryDir, "libtun2socks.so");
        String nativeLib = nativeLibFile.getAbsolutePath();
        Log.e("ACC", "nativeLibFile::" + nativeLib + ", " + nativeLibFile.exists() + ", " + nativeLibFile.canExecute());

        File noBackupFile = activity.getApplication().getNoBackupFilesDir();
        String noBackup = noBackupFile.getAbsolutePath();
        Log.e("ACC", "noBackupFile::" + noBackup + ", " + noBackupFile.exists() + ", " + noBackupFile.canExecute());

        String dataDir = init(activity);
        File dataDirFile = new File(dataDir);
        Log.e("ACC", "dataDirFile::" + dataDir + ", " + dataDirFile.exists() + ", " + dataDirFile.canExecute());

        Tun2Socks.startDefault(activity);


    }

    public native String stringFromJNI(String dir);

    public String init(Activity activity) {
        String dataDir = "/data/data/" + activity.getPackageName() + "/lib/arm64/libtun2socks.so";
//        String dataDir = "/data/app/com.didi.android.tun2socks.app-XC6mZUT9gKKnK_ZTDQ-kew==/lib/arm64/libtun2socks.so";
        File file = new File(dataDir);
        if (!file.exists()) {
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            try {
                InputStream inputStream = activity.getResources().getAssets().open("libtun2socks.so");

                copySdcardFile(inputStream, dataDir);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ACC", "::exists " + dataDir);
        }
        Log.e("ACC", "::init() " + dataDir);
        file.setExecutable(true, false);
        file.setReadable(true, false);
        file.setWritable(true, false);

        return dataDir;
    }

    public void test() throws Exception {
        //创建相关流
        FileInputStream fileInputStream = new FileInputStream("resources/test.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("resources/test2.txt");
        //获取各个流对应的fileChannel
        FileChannel sourceCh = fileInputStream.getChannel();
        FileChannel destCh = fileOutputStream.getChannel();
        //从目标通道中复制数据到当前通道
        destCh.transferFrom(sourceCh, 0, sourceCh.size());
        //关闭相关通道和流
        sourceCh.close();
        destCh.close();
        fileInputStream.close();
        fileOutputStream.close();
    }


    public int copySdcardFile(InputStream fromFile, String toFile) {
        try {
            InputStream fosfrom = fromFile;
            FileOutputStream fosto = new FileOutputStream(toFile);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = fosfrom.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            // 从内存到写入到具体文件
            fosto.write(baos.toByteArray());
            // 关闭文件流
            baos.close();
            fosto.close();
            fosfrom.close();
            return 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }
}
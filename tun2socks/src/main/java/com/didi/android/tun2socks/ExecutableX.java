package com.didi.android.tun2socks;

import android.system.Os;
import android.system.OsConstants;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * didi Create on 2022/5/20 .
 * <p>
 * Copyright (c) 2022/5/20 by didiglobal.com.
 *
 * @author <a href="realonlyone@126.com">zhangjun</a>
 * @version 1.0
 * @Date 2022/5/20 3:24 下午
 * @Description 用一句话说明文件功能
 */

public class ExecutableX {

    private static final String REDSOCKS = "libredsocks.so";
    private static final String SS_LOCAL = "libsslocal.so";
    private static final String TUN2SOCKS = "libtun2socks.so";

    private static List<String> EXECUTABLES = Arrays.asList(SS_LOCAL, REDSOCKS, TUN2SOCKS);

    public static void killAll() {
        File dir = new File("/proc");
        for (File process : dir.listFiles()) {
            if (TextUtils.isDigitsOnly(process.getName())) {
                try {
                    File file = new File(process, "cmdline");
                    String text = redFile(file);
                    String exeName = text.split("" + Character.MIN_VALUE, 2)[0];
                    Log.i("ExecutableX", "cmdline ::" + text + ", " + text.getBytes().length + ", " + file.getAbsolutePath() + ", " + exeName);
                    File eexFile = new File(exeName);
                    if (EXECUTABLES.contains(eexFile.getName())) {
                        Os.kill(Integer.valueOf(process.getName()), OsConstants.SIGKILL);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static String redFile(File file) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            int size = inputStream.available();
            byte[] buffer = new byte[1024];
            int len = inputStream.read(buffer);
            String text = new String(buffer, 0, len);
            return text;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }
}

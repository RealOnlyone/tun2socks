package com.didi.android.tun2socks;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * didi Create on 2022/5/21 .
 * <p>
 * Copyright (c) 2022/5/21 by didiglobal.com.
 *
 * @author <a href="realonlyone@126.com">zhangjun</a>
 * @version 1.0
 * @Date 2022/5/21 12:23 上午
 * @Description 用一句话说明文件功能
 */

public class ProcessRuntime {

    private Process process;
    private ReadThread printReadThread;
    private ReadThread printErrorReadThread;


    public static ProcessRuntime start(List<String> cmd, File workDir) {
        ProcessRuntime runtime = new ProcessRuntime();
        boolean ok = runtime.run(cmd, workDir);
        if (ok) {
            return runtime;
        } else {
            runtime.shutDown();
            return null;
        }
    }

    public boolean run(List<String> cmd, String workDir) {
        return run(cmd, new File(workDir));
    }

    public boolean run(List<String> cmd, File workDir) {
        shutDown();
        try {
            process = new ProcessBuilder(cmd).directory(workDir).start();
            startPrint();
            startErrorPrint();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void startPrint() {
        printReadThread = new ReadThread("process-in-" + process.hashCode(), process, false);
        printReadThread.start();
    }

    private void startErrorPrint() {
        printErrorReadThread = new ReadThread("process-error-" + process.hashCode(), process, true);
        printErrorReadThread.start();
    }

    public void shutDown() {
        if (process != null) {
            process.destroy();
            process = null;
        }
        if (printReadThread != null) {
            printReadThread.stopNow();
            printReadThread = null;
        }
        if (printErrorReadThread != null) {
            printErrorReadThread.stopNow();
            printErrorReadThread = null;
        }
    }

    private static final class ReadThread extends Thread {
        private Process process;
        private boolean shutDown = false;
        private boolean error;

        public ReadThread(@NonNull String name, Process process, boolean error) {
            super(name);
            this.process = process;
            this.error = error;
        }

        public void stopNow() {
            shutDown = true;
        }

        private void println(String msg) {
            if (error) {
                Log.e(getName(), "::" + msg);
            } else {
                Log.i(getName(), "::" + msg);
            }
        }

        @Override
        public void run() {
            Log.i(getName(), "::run()");
            InputStream inputStream = null;
            if (error) {
                inputStream = process.getErrorStream();
            } else {
                inputStream = process.getInputStream();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            while (!shutDown) {
                try {
                    String text = reader.readLine();
                    while (text != null) {
                        if (text != null && text.length() > 0) {
                            println(text);
                        } else {
                            println("null");
                        }
                        text = reader.readLine();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        if (process.isAlive()) {
                            Thread.sleep(300);
                            continue;
                        }
                    }
                    String value = "null";
                    try {
                        value = "" + process.exitValue();
                    } catch (Exception e) {
                    }

                    if (TextUtils.isDigitsOnly(value)) {
                        println("::run() stopNow! exitValue=" + value);
                        stopNow();
                    } else {
                        println("::run() sleep! exitValue=" + value);
                        Thread.sleep(300);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.i(getName(), "::stop() ok!");
        }
    }


}

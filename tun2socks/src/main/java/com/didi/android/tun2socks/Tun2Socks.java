package com.didi.android.tun2socks;

import android.content.Context;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * didi Create on 2022/5/21 .
 * <p>
 * Copyright (c) 2022/5/21 by didiglobal.com.
 *
 * @author <a href="realonlyone@126.com">zhangjun</a>
 * @version 1.0
 * @Date 2022/5/21 2:17 上午
 * @Description 用一句话说明文件功能
 */

public class Tun2Socks {

    public static ProcessRuntime startDefault(Context context) {
        return start(context, Arrays.asList(
                "--netif-ipaddr", "172.0.0.1",
                "--socks-server-addr", "172.0.0.1:9090",
                "--tunmtu", "1500",
                "--sock-path", "sock_path",
                "--dnsgw", "127.0.0.1:5053",
                "--loglevel", "debug",
                "--enable-udprelay"
        ));
    }


    public static ProcessRuntime start(Context context, List<String> cmd) {
        String nativeLib = new File(context.getApplicationInfo().nativeLibraryDir, "libtun2socks.so").getAbsolutePath();
        File noBackupFile = context.getNoBackupFilesDir();

        LinkedList<String> cmdNew = new LinkedList<>(cmd);
        cmdNew.addFirst(nativeLib);

        ExecutableX.killAll();
        return ProcessRuntime.start(cmdNew, noBackupFile);
    }
}

package com.didi.android.native_test;

public class NativeLib {

    // Used to load the 'native_test' library on application startup.
    static {
        System.loadLibrary("native_test");
    }

    /**
     * A native method that is implemented by the 'native_test' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
#include <jni.h>
#include <string>
#include <tun2socks/tun2socks.h>

extern "C" JNIEXPORT jstring JNICALL
Java_com_didi_android_tun2socks_NativeLib_stringFromJNI(JNIEnv *env, jobject native, jstring text) {
    std::string hello = "Hello from C++";
    jclass jclass1 = env->GetObjectClass(native);
    const char *dirPath = env->GetStringUTFChars(text, (jboolean *)false);

    jsize size = env->GetStringUTFLength(text);
    char *e2 = new char[size];
    memcpy(e2, dirPath, strlen(dirPath));

    char **args = new char *[]{"", "--netif-ipaddr", "172.0.0.1",
                               "--socks-server-addr", "127.0.0.1:9090",
                               "--tunmtu", "1500",
                               "--sock-path", "sock_path",
                               "--dnsgw", "127.0.0.1:5053",
                               "--loglevel", "warning",
                               "--enable-udprelay"};
    args[0] = e2;
    char *sp = "/sock_path";
    char *xpath = new char[strlen(sp) + strlen(e2)];
    stpcpy(xpath, e2);
    strncat(xpath, sp, strlen(sp));

    args[8] = xpath;

    main(14, args);

    return env->NewStringUTF(hello.c_str());
}
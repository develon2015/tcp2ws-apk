#include <jni.h>
#include <string>
#include "rust.h"
#include "log.h"

extern "C" JNIEXPORT void JNICALL
Java_app_tcp2ws_Bridge_test(
        JNIEnv* env,
        jobject /* this */) {
    test();
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_app_tcp2ws_Bridge_start(JNIEnv *env, jobject thiz, jstring i_name,
                             jstring i_ws, jstring i_listen) {
    const char *name = env->GetStringUTFChars(i_name, 0);
    const char *ws = env->GetStringUTFChars(i_ws, 0);
    const char *listen = env->GetStringUTFChars(i_listen, 0);
    // 在此处使用本地字符串
    const bool ok = start(name, ws, listen);
    // 释放字符串内存
    env->ReleaseStringUTFChars(i_name, name);
    env->ReleaseStringUTFChars(i_ws, ws);
    env->ReleaseStringUTFChars(i_listen, listen);
    // 返回任务句柄
    return ok;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_app_tcp2ws_Bridge_stop(JNIEnv *env, jobject thiz, jstring i_name,
                             jstring i_ws, jstring i_listen) {
    const char *name = env->GetStringUTFChars(i_name, 0);
    const char *ws = env->GetStringUTFChars(i_ws, 0);
    const char *listen = env->GetStringUTFChars(i_listen, 0);
    // 在此处使用本地字符串
    const bool ok = stop(name, ws, listen);
    // 释放字符串内存
    env->ReleaseStringUTFChars(i_name, name);
    env->ReleaseStringUTFChars(i_ws, ws);
    env->ReleaseStringUTFChars(i_listen, listen);
    return ok;
}
#include <jni.h>
#include <stdio.h>
#include "xianzhan_misc_jni_JavaNativeInterface.h"

/*
 * Class:     xianzhan_misc_jni_JavaNativeInterface
 * Method:    hello
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_xianzhan_misc_jni_JavaNativeInterface_hello(JNIEnv *env, jclass jclazz, jstring jstr)
{
    const char *str = (*env)->GetStringUTFChars(env, jstr, 0);
    printf("%s", str);

    // need to release this string when done with it in order to
    // avoid memory leek
    (*env)->ReleaseStringUTFChars(env, jstr, str);
}
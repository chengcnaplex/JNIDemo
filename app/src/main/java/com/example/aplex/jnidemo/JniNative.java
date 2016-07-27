package com.example.aplex.jnidemo;

/**
 * Created by APLEX on 2016/7/27.
 */
public class JniNative {
    static {
        System.loadLibrary("aplex");
    }
    public static native void sayHelloToC(String hello);
    public static native String HelloFromC();
}

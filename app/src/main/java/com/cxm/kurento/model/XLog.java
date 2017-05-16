package com.cxm.kurento.model;

import android.util.Log;

/**
 * Created by chenxiemin on 6/10/2016.
 */
public class XLog {
    public static void v(String args) {
        String tag = getTagString();
        Log.d(tag, args);
    }

    public static void d(String args) {
        String tag = getTagString();
        Log.d(tag, args);
    }

    public static void i(String args) {
        String tag = getTagString();
        Log.d(tag, args);
    }

    public static void w(String args) {
        String tag = getTagString();
        Log.d(tag, args);
    }

    public static void e(String args) {
        String tag = getTagString();
        Log.d(tag, args);
    }

    private static String getTagString() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();

        return elements[4].getClassName() + ":" + elements[4].getLineNumber();
    }
}

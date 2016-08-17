package com.oneguy.live;

import android.content.Context;

public class DemoCache {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        DemoCache.context = context.getApplicationContext();
    }
}

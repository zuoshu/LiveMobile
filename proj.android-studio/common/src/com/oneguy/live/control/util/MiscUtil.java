package com.oneguy.live.control.util;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

/**
 * Created by neavo on 2014/8/7.
 */

public class MiscUtil {


    public static synchronized int getScreenWidth(Context ctx) {
        Point point = new Point();

        WindowManager manager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getSize(point);

        return point.x;
    }

    public static synchronized int getScreenHeight(Context ctx) {
        Point point = new Point();

        WindowManager manager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getSize(point);

        return point.y;
    }


}

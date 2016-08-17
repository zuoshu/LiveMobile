package com.oneguy.live.control.util;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by neavo on 2014/7/14.
 */

public class ViewUtil {

    public static synchronized void fixSize(View v, int width, int height) {
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        v.setLayoutParams(layoutParams);
    }

    public static synchronized void fixWidth(View v, int height, float ratio) {
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        layoutParams.height = (int) (height * ratio);
        v.setLayoutParams(layoutParams);
    }

    public static synchronized void fixHeight(View v, int width, float ratio) {
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        layoutParams.height = (int) (width / ratio);
        v.setLayoutParams(layoutParams);
    }

    public static synchronized void fixWidth(View v, int width) {
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        layoutParams.width = width;
        v.setLayoutParams(layoutParams);
    }
}

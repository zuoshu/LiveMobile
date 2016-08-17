package com.oneguy.live.module.live;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.netease.LSMediaCapture.lsSurfaceView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LiveSurfaceView extends lsSurfaceView {

    private int previewWidth;
    private int previewHeight;
    private float ratio;
    private int winWidth;
    private int winHeight;
    private Rect rect;
    private Context context;

    public LiveSurfaceView(Context context) {
        super(context);
        this.context = context;
    }

    public LiveSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public LiveSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public void setPreviewSize(int width, int height) {
        int screenW = getResources().getDisplayMetrics().widthPixels;
        int screenH = getResources().getDisplayMetrics().heightPixels;
        setScreenParam();
        if (screenW < screenH) {
            previewWidth = width < height ? width : height;
            previewHeight = width >= height ? width : height;
        } else {
            previewWidth = width > height ? width : height;
            previewHeight = width <= height ? width : height;
        }
        ratio = previewWidth / (float) previewHeight;
        previewWidth = screenW;
        previewHeight = screenH;
        requestLayout();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = 0;
        int measuredHeight = 0;

        if (previewWidth > 0 && previewHeight > 0) {
            float winRatio = (float) winWidth / winHeight;
            if (winRatio > ratio) {
                // 屏幕宽按照高来计算
                measuredWidth = (int) (ratio * (winHeight + rect.top));
                measuredHeight = winHeight;
            } else {
                // 屏幕窄按照宽来计算
                measuredWidth = winWidth;
                measuredHeight = (int) (winWidth / ratio);
            }

            setMeasuredDimension(measuredWidth, measuredHeight);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void setScreenParam() {
        rect = new Rect();
        this.getWindowVisibleDisplayFrame(rect);//获取状态栏高度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay(); //获取屏幕分辨率
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) { //new
            DisplayMetrics metrics = new DisplayMetrics();
            display.getRealMetrics(metrics);
            winWidth = metrics.widthPixels;
            winHeight = metrics.heightPixels - rect.top;
        } else { //old
            try {
                Method mRawWidth = Display.class.getMethod("getRawWidth");
                Method mRawHeight = Display.class.getMethod("getRawHeight");
                winWidth = (Integer) mRawWidth.invoke(display);
                winHeight = (Integer) mRawHeight.invoke(display) - rect.top;
            } catch (NoSuchMethodException e) {
                DisplayMetrics dm = context.getResources().getDisplayMetrics();
                winWidth = dm.widthPixels;
                winHeight = dm.heightPixels - rect.top;
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    protected int defineWidth(int previewW, int previewWMode) {
        int measuredWidth;
        if (previewWMode == View.MeasureSpec.UNSPECIFIED) {
            measuredWidth = previewWidth;
        } else if (previewWMode == View.MeasureSpec.EXACTLY) {
            measuredWidth = previewW;
        } else {
            measuredWidth = Math.min(previewW, previewWidth);
        }
        return measuredWidth;
    }

    protected int defineHeight(int previewH, int previewHMode) {
        int measuredHeight;
        if (previewHMode == View.MeasureSpec.UNSPECIFIED) {
            measuredHeight = previewHeight;
        } else if (previewHMode == View.MeasureSpec.EXACTLY) {
            measuredHeight = previewH;
        } else {
            measuredHeight = Math.min(previewH, previewHeight);
        }
        return measuredHeight;
    }

}

package com.oneguy.live.view.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.game.common.R;
import com.oneguy.live.control.util.ScreenUtil;


public class DrawSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

	private static final String tag2 = "DrawSV";
	private boolean runFlag = true;
	protected SurfaceHolder holder;
	private Bitmap rotateImg;
	private Thread myThread;

	public DrawSurfaceView(Context context, AttributeSet attrs) {
		//构造函数
		super(context, attrs);
		rotateImg = BitmapFactory.decodeResource(getResources(), R.drawable.ic_loading);
		rotateImg = Bitmap.createScaledBitmap(rotateImg, 100, 100, true);
		holder = this.getHolder();
		holder.addCallback(this);
		holder.setFormat(PixelFormat.TRANSPARENT);  //顶层绘制SurfaceView设成透明
		this.setZOrderOnTop(true);
		myThread = new Thread(new MyThread());
	}

	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		Log.v(tag2, "DrawSV:surfaceChanged...");
		
	}

	public void surfaceCreated(SurfaceHolder arg0) {
		Log.v(tag2, "DrawSV:surfaceCreated...");
		//启动自定义线程		
		myThread.start();
		
	}

	public void surfaceDestroyed(SurfaceHolder arg0) {
		Log.v(tag2, "pre DrawSV:surfaceDestroyed...");
		//终止自定义线程
		runFlag = false;
		myThread.interrupt();
		
	}

	/*自定义线程*/
	class MyThread implements Runnable{

		public void run() {
			Canvas canvas = null;
			int rotate = 0;
			while(runFlag){
				try {
					canvas = holder.lockCanvas(new Rect(0, 0, ScreenUtil.screenWidth, ScreenUtil.screenHeight)); //获取画布
					Paint paint = new Paint();
					//canvas.drawBitmap(rotateImg, 0, 0, paint); //绘制旋转的背景
					//创建矩阵控制图片旋转和平移
					Matrix matrix = new Matrix();
					//设置旋转角度
					matrix.postRotate((rotate += 48) % 360,
							rotateImg.getWidth() / 2, rotateImg.getHeight() / 2);
					//设置左边距和上边距
					matrix.postTranslate((ScreenUtil.screenWidth - rotateImg.getWidth()) / 2,
							(ScreenUtil.screenHeight - rotateImg.getHeight()) / 2);
					//绘制旋转图片
					canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
					canvas.drawBitmap(rotateImg, matrix, paint);
					holder.unlockCanvasAndPost(canvas);//解锁画布，提交画好的图像
					//休眠控制最大帧率为每秒3绘制30次
					Thread.sleep(80);
				} catch (Exception e) {
					Log.v(tag2, "DrawSurfaceView：绘制失败...");
				}	
				
				
				
			}
			
		}
		
	}

}
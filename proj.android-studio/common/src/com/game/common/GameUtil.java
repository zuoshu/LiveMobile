package com.game.common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Map;
import android.app.Activity;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.pingplusplus.android.PaymentActivity;
//import com.pingplusplus.android.PaymentActivity;
//import com.pingplusplus.android.PingppLog;
import com.umeng.social.CCUMSocialController;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.facebook.controller.UMFacebookHandler;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class GameUtil {
	public static final int QQ_LOGIN_TAG 					=	1;
	public static final int WX_LOGIN_TAG 					=	2;
	public static final int THIRD_PAY_TAG 					=	3;
	public static final int COPY_INFO_TAG 					=	4;
	public static final int PHONE_TAG 						=	5;	
	public static final int REQUEST_CODE_PAYMENT 			=	6;
	public static final int INIT_MODULE 					=	7;
	
	
	
	public static String wxAppID = null;
	public static String wxAppSecret = null;
	public static String qqAppID = null;
	public static String qqAppKey = null;
	public static String deviceID = null;
	
	
	public static UMSocialService loginUmSocialService = null;
	public static UMSocialService shareUmSocialService = null;
	public static UMWXHandler wxHandler = null;
	public static UMWXHandler wxCircleHandler = null;
	public static UMQQSsoHandler qqSsoHandler = null;
	public static QZoneSsoHandler qzoneSsoHandler = null;
	public static UMFacebookHandler mFacebookHandler = null;
	public static WeiXinShareContent weixinShareContent = null;
	public static CircleShareContent circleShareContent = null;
	public static QQShareContent qqShareContent = null;
	public static QZoneShareContent qzoneShareContent = null;
	public static String shareType = null;
	public static String shareContent = null;
	public static String shareImage = null;
	public static Activity mActivity = null;
	public static Context mContext = null;
	public static String[] known_emulator_serial= {"unknown", "nox"};
	public static native void thirdLogin(String account, String nickname);//登陆回调返回C++
	public static native void removeTipLayer();//移除tiplayer返回C++
	
	//handler主线程处理
	public static Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case QQ_LOGIN_TAG:
			{
				loginUmSocialService.doOauthVerify(mContext, SHARE_MEDIA.QQ, new UMAuthListener() {
				    @Override
				    public void onStart(SHARE_MEDIA platform) {
				        Toast.makeText(mContext, "授权开始", Toast.LENGTH_SHORT).show();
				    }
				    @Override
				    public void onError(SocializeException e, SHARE_MEDIA platform) {
				        Toast.makeText(mContext, "授权错误", Toast.LENGTH_SHORT).show();
				    }
				    @Override
				    public void onComplete(Bundle value, SHARE_MEDIA platform) {
				        Toast.makeText(mContext, "授权完成", Toast.LENGTH_SHORT).show();
				        final String openidStr = value.get("openid").toString();
				        //获取相关授权信息
				        loginUmSocialService.getPlatformInfo(mActivity, SHARE_MEDIA.QQ, new UMDataListener() {
				    @Override
				    public void onStart() {
				        //Toast.makeText(mAppActivity, "获取平台数据开始...", Toast.LENGTH_SHORT).show();
				    }                                              
				    @Override
				        public void onComplete(int status, Map<String, Object> info) {
				            if(status == 200 && info != null){
				                // StringBuilder sb = new StringBuilder();
				                // Set<String> keys = info.keySet();
				                // for(String key : keys){
				                //    sb.append(key+"="+info.get(key).toString()+"\r\n");
				                // }
				                // Log.d("TestData",sb.toString());
				                thirdLogin(openidStr, info.get("screen_name").toString());
				                //Toast.makeText(mAppActivity, sb.toString(), Toast.LENGTH_SHORT).show();
				            }else{
				               Log.d("TestData","发生错误："+status);
				           }
				        }
				});
				    }
				    @Override
				    public void onCancel(SHARE_MEDIA platform) {
				        Toast.makeText(mContext, "授权取消", Toast.LENGTH_SHORT).show();
				    }
				} );
			}
				break;
			case WX_LOGIN_TAG:
			{
				loginUmSocialService.doOauthVerify(mContext, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
				    @Override
				    public void onStart(SHARE_MEDIA platform) {
				        Toast.makeText(mContext, "授权开始", Toast.LENGTH_SHORT).show();
				    }
				    @Override
				    public void onError(SocializeException e, SHARE_MEDIA platform) {
				        Toast.makeText(mContext, "授权错误", Toast.LENGTH_SHORT).show();
				    }
				    @Override
				    public void onComplete(Bundle value, SHARE_MEDIA platform) {
				        Toast.makeText(mContext, "授权完成", Toast.LENGTH_SHORT).show();
				        //获取相关授权信息
				        loginUmSocialService.getPlatformInfo(mActivity, SHARE_MEDIA.WEIXIN, new UMDataListener() {
				    @Override
				    public void onStart() {
				        //Toast.makeText(mAppActivity, "获取平台数据开始...", Toast.LENGTH_SHORT).show();
				    }                                              
				    @Override
				        public void onComplete(int status, Map<String, Object> info) {
				            if(status == 200 && info != null){
				                // StringBuilder sb = new StringBuilder();
				                // Set<String> keys = info.keySet();
				                // for(String key : keys){
				                //    sb.append(key+"="+info.get(key).toString()+"\r\n");
				                // }
				                //Log.d("TestData",sb.toString());
				                thirdLogin(info.get("openid").toString(), info.get("nickname").toString());
				                //Toast.makeText(mAppActivity, sb.toString(), Toast.LENGTH_SHORT).show();
				            }else{
				               Log.d("TestData","发生错误："+status);
				           }
				        }
				});
				    }
				    @Override
				    public void onCancel(SHARE_MEDIA platform) {
				        Toast.makeText(mContext, "授权取消", Toast.LENGTH_SHORT).show();
				    }
				} );
			}
				break;
			case THIRD_PAY_TAG:
			{
				 final String _msg = msg.obj.toString();
//	        	 System.out.println("支付凭证：" + _msg);
	             Intent intent = new Intent();
	             String packageName = mActivity.getPackageName();
	             ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
	             intent.setComponent(componentName);
	             intent.putExtra(PaymentActivity.EXTRA_CHARGE, _msg);
	             mActivity.startActivityForResult(intent, REQUEST_CODE_PAYMENT);
			}
				break;
			case COPY_INFO_TAG:
			{
				final String _msg = msg.obj.toString();
				if (android.os.Build.VERSION.SDK_INT > 11) {
					android.content.ClipboardManager clipboard = (android.content.ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE); 
					clipboard.setPrimaryClip(ClipData.newPlainText(null, _msg));
				}else{
					android.text.ClipboardManager clipboard = (android.text.ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);	
					clipboard.setText(_msg);
				}
			}
				break;
				
			case PHONE_TAG:
			{
				String strPhoneNumber = msg.obj.toString();
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+ strPhoneNumber));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mActivity.startActivity(intent);
			}
				break;
				
			case INIT_MODULE:
			{
				initLoginModule();//初始化登录模块
				initShareModule();//初始化分享模块
		        initPayModule();//初始化支付模块
		        initPowerModule();//屏幕常亮
			}
				break;
				
			default:
				break;
			}
		}
	};
	
	//初始化
	public static void initGameModule(Activity activity, Context context) {
		mActivity = activity;
		mContext = context;
        System.out.println("初始化gamemodule");
	}
	
	//初始化appkey
	public static void initAppKey(String wxAppIDString, String wxAppSecretString, String qqAppIDString, String qqAppKeyString) {
		wxAppID = wxAppIDString;
		wxAppSecret = wxAppSecretString;
		qqAppID = qqAppIDString;
		qqAppKey = qqAppKeyString;
		Message msg = Message.obtain();
		msg.what = INIT_MODULE;
	    mHandler.sendMessage(msg);
		System.out.println("初始化appkey" +  wxAppID + "***" + wxAppSecret + "***" + qqAppID + "***" + qqAppKey);
	}

	//初始化支付模块
	public static void initPayModule() {
//		PingppLog.DEBUG = true;
	}

	//初始化友盟分享
	public static void initShareModule() {
		CCUMSocialController.initSocialSDK(mActivity, "com.umeng.social.share");
	}

	//初始化第三方登陆
	public static void initLoginModule() {
		if (null == wxAppID || null == wxAppSecret || null == qqAppID || null == qqAppKey || null == mActivity) {
			return;
		}
		//友盟登陆
        loginUmSocialService = UMServiceFactory.getUMSocialService("com.umeng.login");
        //添加微信平台
        wxHandler = new UMWXHandler(mActivity, wxAppID, wxAppSecret);
        wxHandler.addToSocialSDK();
        //支持微信朋友圈
        wxCircleHandler = new UMWXHandler(mActivity, wxAppID, wxAppSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
        //添加QQ平台
        qqSsoHandler = new UMQQSsoHandler(mActivity, qqAppID, qqAppKey);
        qqSsoHandler.addToSocialSDK();
        //QQ空间
        qzoneSsoHandler = new QZoneSsoHandler(mActivity, qqAppID, qqAppKey);
        qzoneSsoHandler.addToSocialSDK();
	}
	
	//初始化设备管理（电源、硬件ID）
	public static void initPowerModule() {
		mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		TelephonyManager tm = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
		deviceID = tm.getDeviceId();
		System.out.println("初始化设备信息");
	}
	
	//QQ登录
	public static void qqLogin() {
		Message msg = Message.obtain();
		msg.what = QQ_LOGIN_TAG;
	    mHandler.sendMessage(msg);
	    System.out.println("QQ登录");
	}
	
	//微信登录
	public static void wxLogin() {
		Message msg = Message.obtain();
		msg.what = WX_LOGIN_TAG;
	    mHandler.sendMessage(msg);
	    System.out.println("微信登录");
	}
	
	//是否安装了微信客户端
	public static String isInstallWechat() {
		if (null == wxHandler) {
			return "false";
		}
		System.out.println("检查微信客户端");
		if (wxHandler.isClientInstalled() == true) {
			System.out.println("微信客户端已安装");
			return "true";
		} else {
			System.out.println("微信客户端未安装");
			return "false";
		}
	}
		
	//是否安装了QQ客户端
	public static String isInstallQQ() {
		if (null == qqSsoHandler) {
			return "false";
		}
		System.out.println("检查QQ客户端");
		if (qqSsoHandler.isClientInstalled() == true) {
			System.out.println("qq客户端已安装");
			return "true";
		} else {
			System.out.println("qq客户端未安装");
			return "false";
		}
	}
	
	//检测wifi是否连接 
	public static boolean isWifiConnected() {
	     ConnectivityManager cm = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
	     if (cm != null) {
	         NetworkInfo networkInfo = cm.getActiveNetworkInfo();
	         if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
	        	 return true;
	     	}  
	     }  
	     return false;
	}
	
	//三方支付
	public static void thirdPartyPay(String charge) {
		 Message msg = Message.obtain();
		 msg.what = THIRD_PAY_TAG;
		 msg.obj = charge;
		 mHandler.sendMessage(msg);
		 System.out.println("三方支付");
	}
	
	//手机震动
	public static void phoneShake() {
		Vibrator vibrator = (Vibrator)mContext.getSystemService(Context.VIBRATOR_SERVICE);   
		vibrator.vibrate(500);
		System.out.println("手机振动");
	}
	
	//获取设备ID
	public static String getDeviceID() {
		System.out.println("获取设备ID");
		return deviceID;
	}
	
	//复制黏贴
	public static void CopyPersonalDetails(String strDetails) {
		Message msg = Message.obtain();
		msg.obj = strDetails;
		msg.what = COPY_INFO_TAG;
		mHandler.sendMessage(msg);
		System.out.println("复制黏贴");
	}
	
	//拨打电话
	public static void phone(String strPhoneNumber) {
		Message msg = Message.obtain();
		msg.obj = strPhoneNumber;
	    msg.what = PHONE_TAG;
	    mHandler.sendMessage(msg);
	}
	
	//检测是真机还是模拟器
	public static String checkEmulatorOrPhone() {
		String serial = android.os.Build.SERIAL;
		for (int i = 0; i < known_emulator_serial.length; i++) {
			if (serial.contains(known_emulator_serial[i])) {
				return "emulator";
			}
		}
		return "phone";
	}
	
	public static void showMsg(String title, String msg1, String msg2) {
		if (title.equals("cancel")) {
			Toast.makeText(mActivity, "取消支付！", Toast.LENGTH_SHORT).show();
		} else if (title.equals("fail")) {
			Toast.makeText(mActivity, "支付失败！", Toast.LENGTH_SHORT).show();
		} else if (title.equals("success")) {
			Toast.makeText(mActivity, "支付成功，请进入保险箱查看！", Toast.LENGTH_SHORT).show();
		} else if (title.equals("invalid")) {
			Toast.makeText(mActivity, "支付凭证非法！", Toast.LENGTH_SHORT).show();
		}
    }
	
	//结果处理
	public static void handleResult(int requestCode, int resultCode, Intent data) {
		//支付页面返回处理
        if (requestCode == GameUtil.REQUEST_CODE_PAYMENT) {
        	 removeTipLayer();
             if (resultCode == Activity.RESULT_OK) {
                 String result = data.getExtras().getString("pay_result");
                 /* 处理返回值
                  * "success" - payment succeed
                  * "fail"    - payment failed
                  * "cancel"  - user canceld
                  * "invalid" - payment plugin not installed
                  */
                 String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                 String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                 showMsg(result, errorMsg, extraMsg);
             }
        }
        //友盟分享
        CCUMSocialController.onActivityResult(requestCode, resultCode, data);
	}
}

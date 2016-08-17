package com.oneguy.live.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.game.common.R;
import com.game.common.R2;
import com.netease.livestreamingFilter.filter.Filters;
import com.netease.livestreamingFilter.view.CameraSurfaceView;
import com.oneguy.live.base.TActivity;
import com.oneguy.live.control.permission.MPermission;
import com.oneguy.live.control.permission.annotation.OnMPermissionDenied;
import com.oneguy.live.control.permission.annotation.OnMPermissionGranted;
import com.oneguy.live.control.permission.annotation.OnMPermissionNeverAskAgain;
import com.oneguy.live.control.permission.util.MPermissionUtil;
import com.oneguy.live.module.live.LivePlayer;
import com.oneguy.live.module.live.LiveSurfaceView;
import com.oneguy.live.view.widget.DrawSurfaceView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZuoShu on 16/7/24.
 */
public class LiveActivity extends TActivity implements LivePlayer.ActivityProxy {
    private static final String TAG = LiveActivity.class.getSimpleName();
    @BindView(R2.id.live_view)
    LiveSurfaceView liveView;

    // constant
    public static final String EXTRA_URL = "EXTRA_URL";
    private static final String EXTRA_MODE = "EXTRA_MODE";
    private final int LIVE_PERMISSION_REQUEST_CODE = 100;

    @BindView(R2.id.switch_image)
    ImageView switchImage;
    @BindView(R2.id.play_stop_image)
    ImageView playStopImage;
    @BindView(R2.id.filter_mode_image)
    ImageView filterModeImage;
    @BindView(R2.id.control_layout)
    RelativeLayout controlLayout;
    @BindView(R2.id.filter_mode_layout)
    RelativeLayout filterModeLayout;
    @BindView(R2.id.filter_surface_view)
    CameraSurfaceView filterSurfaceView;
    @BindView(R2.id.close_btn)
    ImageView closeBtn;

    // mode layout
//    @Bind(R.id.filter_image)
//    ImageView filterImage;
//    @Bind(R.id.filter_text)
//    TextView filterText;
//    @Bind(R.id.filter_layout)
//    RelativeLayout filterLayout;
//    @Bind(R.id.normal_image)
//    ImageView normalImage;
//    @Bind(R.id.normal_image_mode)
//    ImageView normalImageMode;
//    @Bind(R.id.normal_text)
//    TextView normalText;
//    @Bind(R.id.normal_layout)
//    RelativeLayout normalLayout;
//    @Bind(R.id.mode_choose_layout)
//    RelativeLayout modeChooseLayout;
//    @Bind(R.id.start_live_btn)
//    Button startLiveBtn;
//    @Bind(R.id.cover_layout)
//    RelativeLayout coverLayout;
    @BindView(R2.id.draw_view)
    DrawSurfaceView drawSurfaceView;
    @BindView(R2.id.filter_normal_mode)
    RelativeLayout filterNormalMode;
    @BindView(R2.id.filter_white_mode)
    RelativeLayout filterWhiteMode;
    @BindView(R2.id.filter_night_mode)
    RelativeLayout filterNightMode;
    @BindView(R2.id.filter_blur_mode)
    RelativeLayout filterBlurMode;
    @BindView(R2.id.filter_dark_mode)
    RelativeLayout filterDarkMode;
    @BindView(R2.id.filter_sunset_mode)
    RelativeLayout filterSunsetMode;
    @BindView(R2.id.loadingPanel)
    RelativeLayout loadingPanel;

    // data
    private boolean isFilterMode;
    // 推流地址
    private String url;
    // 推流参数
    private LivePlayer livePlayer;

    public static void startActivity(Context context, String url, boolean isFilterMode) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_MODE, isFilterMode);
        intent.setClass(context, LiveActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        ButterKnife.bind(this);

        parseIntent();
        updateBtnState(false);
        requestLivePermission(); // 请求权限

        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startLive();
            }
        }, 100);
    }

    /***********************
     * 录音摄像头权限申请
     *******************************/

    // 权限控制
    private static final String[] LIVE_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private void requestLivePermission() {
        MPermission.with(this)
                .addRequestCode(LIVE_PERMISSION_REQUEST_CODE)
                .permissions(LIVE_PERMISSIONS)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(LIVE_PERMISSION_REQUEST_CODE)
    public void onLivePermissionGranted() {
//        Toast.makeText(LiveActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
    }

    @OnMPermissionDenied(LIVE_PERMISSION_REQUEST_CODE)
    public void onLivePermissionDenied() {
        List<String> deniedPermissions = MPermission.getDeniedPermissions(this, LIVE_PERMISSIONS);
        String tip = "您拒绝了权限" + MPermissionUtil.toString(deniedPermissions) + "，无法开启直播";
        Toast.makeText(LiveActivity.this, tip, Toast.LENGTH_SHORT).show();
        finish();
    }

    @OnMPermissionNeverAskAgain(LIVE_PERMISSION_REQUEST_CODE)
    public void onLivePermissionDeniedAsNeverAskAgain() {
        List<String> deniedPermissions = MPermission.getDeniedPermissionsWithoutNeverAskAgain(this, LIVE_PERMISSIONS);
        List<String> neverAskAgainPermission = MPermission.getNeverAskAgainPermissions(this, LIVE_PERMISSIONS);
        StringBuilder sb = new StringBuilder();
        sb.append("无法开启直播，请到系统设置页面开启权限");
        sb.append(MPermissionUtil.toString(neverAskAgainPermission));
        if (deniedPermissions != null && !deniedPermissions.isEmpty()) {
            sb.append(",下次询问请授予权限");
            sb.append(MPermissionUtil.toString(deniedPermissions));
        }

        Toast.makeText(LiveActivity.this, sb.toString(), Toast.LENGTH_LONG).show();
    }

    private void clearColors() {
        filterNormalMode.setBackgroundColor(getResources().getColor(R.color.color_black_cc000000));
        filterWhiteMode.setBackgroundColor(getResources().getColor(R.color.color_black_cc000000));
        filterNightMode.setBackgroundColor(getResources().getColor(R.color.color_black_cc000000));
        filterBlurMode.setBackgroundColor(getResources().getColor(R.color.color_black_cc000000));
        filterDarkMode.setBackgroundColor(getResources().getColor(R.color.color_black_cc000000));
        filterSunsetMode.setBackgroundColor(getResources().getColor(R.color.color_black_cc000000));
    }

    private void showFilterMode(int type) {
        livePlayer.setFilterType(type);
        controlLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 恢复直播
        if (livePlayer != null) {
            livePlayer.onActivityResume();
        }
    }

    protected void onPause() {
        super.onPause();
        // 暂停直播
        if (livePlayer != null) {
            livePlayer.onActivityPause();
        }
    }

    @Override
    protected void onDestroy() {
        resetLivePlayer();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (livePlayer != null) {
            resetLivePlayer();
        } else {
            super.onBackPressed();
        }
    }

    private void parseIntent() {
        url = getIntent().getStringExtra(EXTRA_URL);
        isFilterMode = getIntent().getBooleanExtra(EXTRA_MODE, false);
    }

    private void updateBtnState(boolean register) {
        playStopImage.setEnabled(register);
        switchImage.setEnabled(register);
        filterModeImage.setEnabled(register);
    }

    private void initLivePlayer() {
//        isFilterMode = false;
        if (!isFilterMode) {
            livePlayer = new LivePlayer(liveView, url, this);
            liveView.setVisibility(View.VISIBLE);
            filterSurfaceView.setVisibility(View.GONE);
        } else {
            livePlayer = new LivePlayer(filterSurfaceView, url, this);
            filterSurfaceView.setVisibility(View.VISIBLE);
            liveView.setVisibility(View.GONE);
        }
    }

    private boolean checkVersion() {
        if (Build.VERSION.SDK_INT < 19) {
            return false;
        }
        return true;
    }

//    private void setMode(boolean isFilterMode) {
//        this.isFilterMode = isFilterMode;
//        if (isFilterMode) {
//            filterImage.setImageResource(R.drawable.ic_filter_pressed);
//            filterText.setTextColor(getResources().getColor(R.color.color_black_2e2625));
//            normalImageMode.setImageResource(R.drawable.ic_normal_mode);
//            normalText.setTextColor(getResources().getColor(R.color.color_black_CCffffff));
//            filterLayout.setBackgroundResource(R.drawable.ic_solid_round);
//            normalLayout.setBackgroundResource(R.drawable.ic_round_hole);
//        } else {
//            filterImage.setImageResource(R.drawable.ic_filter_normal);
//            filterText.setTextColor(getResources().getColor(R.color.color_black_CCffffff));
//            normalImageMode.setImageResource(R.drawable.ic_normal_mode_pressed);
//            normalText.setTextColor(getResources().getColor(R.color.color_black_2e2625));
//            filterLayout.setBackgroundResource(R.drawable.ic_round_hole);
//            normalLayout.setBackgroundResource(R.drawable.ic_solid_round);
//        }
//    }

    // 隐藏选择模式布局，开始推流
//    private void hideAndStartPlay() {
//        if (isFilterMode && !checkVersion()) {
//            Toast.makeText(LiveActivity.this, R.string.not_support_version, Toast.LENGTH_SHORT).show();
//            return;
//        }
//        coverLayout.setVisibility(View.GONE);
//        // 普通模式，不显示滤镜按钮
//        if (!isFilterMode) {
//            filterModeImage.setVisibility(View.GONE);
//        }
//        drawSurfaceView.setVisibility(View.VISIBLE);
//        playStopImage.setImageResource(R.drawable.ic_pause);
//        getHandler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startLive();
//            }
//        }, 100);
//    }

    private void startLive() {
        initLivePlayer();
        livePlayer.startStopLive();
    }

    private void resetLivePlayer() {
        loadingPanel.setVisibility(View.VISIBLE);
        // 释放资源
        if (livePlayer != null) {
            livePlayer.tryStop();
            livePlayer.resetLive();
        }
    }


    @Override
    public Activity getActivity() {
        return LiveActivity.this;
    }

    @Override
    public void onLiveStart() {
        if (drawSurfaceView.getParent() != null)
            ((ViewGroup) drawSurfaceView.getParent()).removeView(drawSurfaceView);
        updateBtnState(true);
        Toast.makeText(LiveActivity.this, R.string.live_starting, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInitFailed() {
        finish();
    }

    @Override
    public void onNetWorkBroken() {
        Toast.makeText(LiveActivity.this, R.string.net_broken, Toast.LENGTH_SHORT).show();
        resetLivePlayer();
    }

    @Override
    public void onFinished() {
        finish();
    }

    @OnClick(R2.id.close_btn)
    public void onCloseClick(View view){
        resetLivePlayer();
    }

    @OnClick(R2.id.switch_image)
    public void onSwitchClick(View view){
        livePlayer.switchCamera();
    }

    @OnClick(R2.id.play_stop_image)
    public void onStopClick(View view){
        if (livePlayer.isManualPause()) {
            playStopImage.setImageResource(R.drawable.ic_pause);
            livePlayer.setManualPause(false);
            livePlayer.restartLive();
        } else {
            playStopImage.setImageResource(R.drawable.ic_play);
            livePlayer.setManualPause(true);
            livePlayer.stopLive();
            Toast.makeText(LiveActivity.this, R.string.live_finished, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R2.id.filter_mode_image)
    public void onFilterModeImageClick(View view){
        // 点击滤镜图标，显示滤镜选择布局
        if (isFilterMode) {
            filterModeLayout.setVisibility(View.VISIBLE);
            controlLayout.setVisibility(View.GONE);
        }
    }

    @OnClick(R2.id.filter_mode_layout)
    public void onFilterModeLayoutClick(View view){
        // 点击空白处，隐藏滤镜选择布局
        if (isFilterMode) {
            filterModeLayout.setVisibility(View.GONE);
            controlLayout.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(LiveActivity.this, R.string.is_not_filter_mode, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R2.id.filter_normal_mode)
    public void onFilterNormalModeClick(View view){
        clearColors();
        filterNormalMode.setBackgroundColor(Color.GRAY);
        showFilterMode(Filters.FILTER_NONE);
    }

    @OnClick(R2.id.filter_white_mode)
    public void onFilterWhiteModeClick(View view){
        clearColors();
        filterWhiteMode.setBackgroundColor(Color.GRAY);
        showFilterMode(Filters.FILTER_WHITEN);
    }

    @OnClick(R2.id.filter_night_mode)
    public void onFilterNightModeClick(View view){
        clearColors();
        filterNightMode.setBackgroundColor(Color.GRAY);
        showFilterMode(Filters.FILTER_NIGHT);
    }


    @OnClick(R2.id.filter_blur_mode)
    public void onFilterBlurModeClick(View view){
        clearColors();
        filterBlurMode.setBackgroundColor(Color.GRAY);
        showFilterMode(Filters.FILTER_BLUR);
    }

    @OnClick(R2.id.filter_dark_mode)
    public void onFilterDarkModeClick(View view){
        clearColors();
        filterDarkMode.setBackgroundColor(Color.GRAY);
        showFilterMode(Filters.FILTER_BLACK_WHITE);
    }

    @OnClick(R2.id.filter_sunset_mode)
    public void onFilterSunsetModeClick(View view){
        clearColors();
        filterDarkMode.setBackgroundColor(Color.GRAY);
        showFilterMode(Filters.FILTER_SEPIA);
    }

}

package com.oneguy.live.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.game.common.R;
import com.game.common.R2;
import com.oneguy.live.base.TActivity;
import com.oneguy.live.module.video.NEVideoView;
import com.oneguy.live.module.video.VideoPlayer;
import com.oneguy.live.module.video.constant.VideoConstant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 拉流界面
 * Created by hzxuwen on 2016/5/22.
 */
public class AudienceActivity extends TActivity implements VideoPlayer.VideoPlayerProxy {

    private final static String EXTRA_URL = "EXTRA_URL";
    @BindView(R2.id.video_view)
    NEVideoView videoView;
//    @Bind(R.id.draw_view)
//    DrawSurfaceView drawView;
//    @Bind(R.id.start_stop_btn)
//    ImageView startStopBtn;
    @BindView(R2.id.close_btn)
    ImageView closeBtn;

    private VideoPlayer videoPlayer;
    private int bufferStrategy = 0; //0:直播低延时；1:点播抗抖动
    private String url; // 拉流地址

    public static void startActivity(Context context, String url) {
        Intent intent = new Intent();
        intent.setClass(context, AudienceActivity.class);
        intent.putExtra(EXTRA_URL, url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audience);
        ButterKnife.bind(this);

        this.url = getIntent().getStringExtra(EXTRA_URL);

        initAudienceParam();
    }

    private void initAudienceParam() {
//        drawView.setVisibility(View.VISIBLE);
        videoPlayer = new VideoPlayer(AudienceActivity.this, videoView, null, url,
                bufferStrategy, this, VideoConstant.VIDEO_SCALING_MODE_FIT);

        videoPlayer.openVideo();
//        startStopBtn.setEnabled(false);
//        startStopBtn.setImageResource(R.drawable.ic_pause);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 恢复播放
        if (videoPlayer != null) {
            videoPlayer.onActivityResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // 释放资源
        if (videoPlayer != null) {
            videoPlayer.resetVideo();
        }
        super.onDestroy();
    }

    //显示视频推流结束
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.tip);
        builder.setMessage(R.string.living_finished);
        builder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }
                });
        builder.show();
    }

    /****************************
     * 播放器状态回调
     *****************************/

    @Override
    public boolean isDisconnected() {
        return false;
    }

    @Override
    public void onError() {
        Toast.makeText(AudienceActivity.this, "直播失败", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onCompletion() {
        showDialog();
    }

    @Override
    public void onPrepared() {
//        if (drawView.getParent() != null) {
//            ((ViewGroup) drawView.getParent()).removeView(drawView);
//        }
//        startStopBtn.setEnabled(true);
    }

    @OnClick({R2.id.close_btn})
    public void onClick(View view) {
        finish();
    }

//    private void startStopVideo() {
//        if (videoPlayer == null) {
//            return;
//        }
//        if (videoPlayer.isPlaying()) {
//            videoPlayer.pauseVideo();
//            startStopBtn.setImageResource(R.drawable.ic_play);
//        } else {
//            videoPlayer.startVideo();
//            startStopBtn.setImageResource(R.drawable.ic_pause);
//        }
//    }

}

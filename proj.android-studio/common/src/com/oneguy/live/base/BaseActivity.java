package com.oneguy.live.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.game.common.R;
import com.game.common.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ZuoShu on 6/28/16.
 */
public class BaseActivity extends Activity {
    @BindView(android.R.id.content)
    public FrameLayout defaultContainer;

    @Nullable
    @BindView(R2.id.content)
    public FrameLayout customContainer;

    private LayoutHolder holder;

    protected final String TAG = getClass().getName();
    protected final Object VOLLEY_TAG = this;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(getActivity());
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ButterKnife.bind(getActivity());
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        ButterKnife.bind(getActivity());
    }

    protected void onInitialize(Bundle savedInstanceState) {
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        this.holder = LayoutHolder.newInstance(customContainer != null ? customContainer : defaultContainer);
        holder.setRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload();
            }
        });
        onInitialize(savedInstanceState);
    }

    protected void reload() {

    }

    protected Activity getActivity() {
        return this;
    }

    protected void showEmpty() {
        holder.showEmpty();
    }

    protected void showContent() {
        holder.showContent();
    }

    protected void showLoading() {
        holder.showLoading();
    }

    protected void setEmptyView(View v) {
        holder.setEmptyView(v);
    }

    protected void setLoadingView(View v) {
        holder.setLoadingView(v);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_from_right);
    }

    protected Context getContext(){
        return this;
    }

}

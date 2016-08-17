package com.oneguy.live.base;

import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.game.common.R;

import butterknife.ButterKnife;

public class LayoutHolder {

    private View empty;
    private View loading;
    private View content;
    private FrameLayout container;

    private ImageView loadingImage;
    private AnimationDrawable drawable;

    private View.OnClickListener retryListener;

    public static synchronized LayoutHolder newInstance(FrameLayout container) {
        LayoutHolder holder = new LayoutHolder();
        holder.container = container;

        if (container != null && !(container instanceof ScrollView) && container.getChildCount() == 1) {
            holder.content = container.getChildAt(0);

            holder.empty = LayoutInflater
                    .from(container.getContext())
                    .inflate(R.layout.widget_empty_holder, container, false);

            holder.loading = LayoutInflater
                    .from(container.getContext())
                    .inflate(R.layout.widget_loading_holder, container, false);

            holder.container.addView(holder.empty);
            holder.container.addView(holder.loading);

            holder.loadingImage = ButterKnife.findById(holder.loading, R.id.loading_image);
            holder.drawable = (AnimationDrawable) holder.loadingImage.getDrawable();
        }

        return holder;
    }

    public void showEmpty() {
        if (empty != null) {
            empty.setVisibility(View.VISIBLE);
        }

        if (loading != null) {
            drawable.stop();
            loading.setVisibility(View.GONE);
        }

        if (content != null) {
            content.setVisibility(View.GONE);
        }
    }

    public void showLoading() {
        if (empty != null) {
            empty.setVisibility(View.GONE);
        }

        if (loading != null && drawable != null) {
            drawable.start();
            loading.setVisibility(View.VISIBLE);
        }

        if (content != null) {
            content.setVisibility(View.GONE);
        }
    }

    public void showContent() {
        if (empty != null) {
            empty.setVisibility(View.GONE);
        }

        if (loading != null) {
            drawable.stop();
            loading.setVisibility(View.GONE);
        }

        if (content != null) {
            content.setVisibility(View.VISIBLE);
        }
    }

    public void setEmptyView(View v) {
        if (container != null && empty != null) {
            container.removeView(empty);
            empty = v;
            if (retryListener != null && empty.findViewById(R.id.retry) != null) {
                empty.findViewById(R.id.retry).setOnClickListener(retryListener);
            }
            container.addView(v);
        }
    }

    public void setLoadingView(View v) {
        if (container != null && loading != null) {
            container.removeView(loading);
            loading = v;
            container.addView(v);

            loadingImage = ButterKnife.findById(loading, R.id.loading_image);
            if (loadingImage != null) {
                drawable = (AnimationDrawable) loadingImage.getDrawable();
            }
        }
    }

    public void setRetryListener(View.OnClickListener retryListener) {
        this.retryListener = retryListener;
        if (retryListener != null
                && empty != null
                && empty.findViewById(R.id.retry) != null) {
            empty.findViewById(R.id.retry).setOnClickListener(retryListener);
        }
    }
}

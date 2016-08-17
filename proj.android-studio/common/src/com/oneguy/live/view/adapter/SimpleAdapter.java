package com.oneguy.live.view.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by ZuoShu on 15/9/21.
 */
public abstract class SimpleAdapter<T> extends BaseAdapter {
    protected Context context;
    protected List<T> data;

    public SimpleAdapter(Context context) {
        this.context = context;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public void clear() {
        if (data != null) {
            data.clear();
        }
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public T getItem(int position) {
        return data == null ? null : data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void remove(T item) {
        if (data != null) {
            synchronized (data) {
                data.remove(item);
            }
        }

    }

    public void insert(T item, int to) {
        if (data != null) {
            synchronized (data) {
                data.add(to, item);
            }
        }
    }
}

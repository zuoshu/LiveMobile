package com.oneguy.live.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.game.common.R;
import com.game.common.R2;
import com.oneguy.live.model.bean.Item;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by ZuoShu on 6/28/16.
 */
public class ProgrammeItemAdapter extends SimpleAdapter<Item> {
    public ProgrammeItemAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater
                    .from(context)
                    .inflate(R.layout.item_programme_item, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }

        Item item = getItem(position);
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.nameText.setText(item.getTime() + "    " + item.getName());
        holder.item = item;
        return convertView;
    }

    class ViewHolder {
        @BindView(R2.id.name)
        TextView nameText;

        Item item;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }

    }
}

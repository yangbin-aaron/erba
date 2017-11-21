package com.qp.app_new.adapters;

import android.view.View;
import android.widget.TextView;

import com.qp.app_new.App;
import com.qp.app_new.R;
import com.qp.app_new.utils.StringUtil;

import org.json.JSONObject;

/**
 * Created by Aaron on 2017/11/21.
 */

public class TrandCoinAdapter extends BaseMyAdapter<TrandCoinAdapter.ViewHolder> {

    @Override
    public int getContentView() {
        return R.layout.item_trand_coin;
    }

    @Override
    public void setConvertView(View convertView, boolean isEmtpy, int position) {
        ViewHolder holder;
        if (isEmtpy) {
            holder = new ViewHolder();
            holder.tv_no = (TextView) convertView.findViewById(R.id.tv_no);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_coin = (TextView) convertView.findViewById(R.id.tv_coin);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        JSONObject jsonObject = getItem(position);
        holder.tv_no.setText("No. " + jsonObject.optString("payNo"));
        holder.tv_time.setText(StringUtil.getShowTime14(jsonObject.optString("createTime")));
        int type = jsonObject.optInt("type");
        if (type == 1) {
            holder.tv_coin.setTextColor(App.mContext.getResources().getColor(R.color.orange_text));
            holder.tv_coin.setText("+" + jsonObject.optString("coin"));
        } else {
            holder.tv_coin.setTextColor(App.mContext.getResources().getColor(R.color.black_text));
            holder.tv_coin.setText("-" + jsonObject.optString("coin"));
        }
    }

    class ViewHolder {
        TextView tv_no, tv_time, tv_coin;
    }
}

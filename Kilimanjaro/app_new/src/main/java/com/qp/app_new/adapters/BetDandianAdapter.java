package com.qp.app_new.adapters;

import android.view.View;
import android.widget.TextView;

import com.qp.app_new.App;
import com.qp.app_new.R;

import org.json.JSONObject;

/**
 * Created by Aaron on 2017/11/20.
 */

public class BetDandianAdapter extends BaseMyAdapter<BetDandianAdapter.ViewHolder> {

    @Override
    public int getContentView() {
        return R.layout.item_bet_dandian_nums;
    }

    @Override
    public void setConvertView(View convertView, boolean isEmtpy, int position) {
        ViewHolder holder;
        if (isEmtpy) {
            holder = new ViewHolder();
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            holder.tv_amt = (TextView) convertView.findViewById(R.id.tv_amt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        JSONObject jsonObject = getItem(position);
        holder.tv_num.setText(jsonObject.optString("lotteryNumber"));
        long amt = jsonObject.optLong("amt");
        if (amt == 0) {
            holder.tv_num.setBackgroundResource(R.drawable.shape_circle_white_str1_grayx);
            holder.tv_num.setTextColor(App.mContext.getResources().getColor(R.color.gray_x));
            holder.tv_amt.setText(App.mContext.getString(R.string.click_input_amt));
            holder.tv_amt.setTextColor(App.mContext.getResources().getColor(R.color.gray_xx));
        } else {
            holder.tv_num.setBackgroundResource(R.drawable.shape_circle_main_str0);
            holder.tv_num.setTextColor(App.mContext.getResources().getColor(R.color.white));
            holder.tv_amt.setText(App.mContext.getString(R.string.bet_dd_amount, amt));
            holder.tv_amt.setTextColor(App.mContext.getResources().getColor(R.color.main_background));
        }
    }

    class ViewHolder {
        TextView tv_num, tv_amt;
    }
}

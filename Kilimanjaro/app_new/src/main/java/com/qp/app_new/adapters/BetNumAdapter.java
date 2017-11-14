package com.qp.app_new.adapters;

import android.view.View;
import android.widget.TextView;

import com.qp.app_new.App;
import com.qp.app_new.R;
import com.qp.app_new.contents.AppContent;

import org.json.JSONObject;

/**
 * Created by Aaron on 2017/11/14.
 */

public class BetNumAdapter extends BaseMyAdapter<BetNumAdapter.ViewHolder> {
    private boolean mCanClick;

    public void setCanClick(boolean canClick) {
        mCanClick = canClick;
        notifyDataSetChanged();
    }

    @Override
    public int getContentView() {
        return R.layout.item_bet_num;
    }

    @Override
    public void setConvertView(View convertView, boolean isEmtpy, final int position) {
        ViewHolder holder;
        if (isEmtpy) {
            holder = new ViewHolder();
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final JSONObject jsonObject = getItem(position);
        holder.tv_num.setText(jsonObject.optString("lotteryNumber"));
        boolean isSelected = jsonObject.optBoolean(AppContent.ISSELECTED, false);
        if (!isSelected) {
            holder.tv_num.setBackgroundResource(R.drawable.shape_circle_white_str1_grayx);
            holder.tv_num.setTextColor(App.mContext.getResources().getColor(mCanClick ? R.color.main_background : R.color.gray_x));
        } else {
            holder.tv_num.setBackgroundResource(R.drawable.shape_circle_main_str0);
            holder.tv_num.setTextColor(App.mContext.getResources().getColor(R.color.white));
        }

        holder.tv_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCanClick) {
                    mOnBetNumItemClickListener.onBetNumItemClickListener(position, jsonObject);
                }
            }
        });
    }

    class ViewHolder {
        TextView tv_num;
    }

    private OnBetNumItemClickListener mOnBetNumItemClickListener;

    public void setOnBetNumItemClickListener(OnBetNumItemClickListener onBetNumItemClickListener) {
        mOnBetNumItemClickListener = onBetNumItemClickListener;
    }

    public interface OnBetNumItemClickListener {
        void onBetNumItemClickListener(int position, JSONObject jsonObject);
    }
}

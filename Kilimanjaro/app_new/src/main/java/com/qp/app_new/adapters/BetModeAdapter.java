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

public class BetModeAdapter extends BaseMyAdapter<BetModeAdapter.ViewHolder> {

    @Override
    public int getContentView() {
        return R.layout.item_bet_mode;
    }

    @Override
    public void setConvertView(View convertView, boolean isEmtpy, final int position) {
        ViewHolder holder;
        if (isEmtpy) {
            holder = new ViewHolder();
            holder.tv_mode = (TextView) convertView.findViewById(R.id.tv_mode);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final JSONObject jsonObject = getItem(position);
        boolean isSelected = jsonObject.optBoolean(AppContent.ISSELECTED, false);
        holder.tv_mode.setText(jsonObject.optString("patternName"));
        holder.tv_mode.setBackgroundResource(isSelected ? R.drawable.shape_r5_main_str0 : R.drawable.shape_r5_white_str1_grayx);
        holder.tv_mode.setTextColor(
                App.mContext.getResources().getColor(isSelected ? R.color.white : R.color.black_text));
        holder.tv_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBetModeItemClickListener.onBetModeItemClickListener(position, jsonObject);
            }
        });
    }

    class ViewHolder {
        TextView tv_mode;
    }

    private OnBetModeItemClickListener mOnBetModeItemClickListener;

    public void setOnBetModeItemClickListener(OnBetModeItemClickListener onBetModeItemClickListener) {
        mOnBetModeItemClickListener = onBetModeItemClickListener;
    }

    public interface OnBetModeItemClickListener {
        void onBetModeItemClickListener(int position, JSONObject jsonObject);
    }
}

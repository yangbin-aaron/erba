package com.teb.kilimanjaro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teb.kilimanjaro.App;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.models.bean.BetMode;

import java.util.List;

/**
 * Created by yangbin on 16/7/15.
 */
public class BetJSModelGridViewAdapter extends BaseAdapter {

    private List<BetMode> mOddsDataList;
    private LayoutInflater mInflater;

    public BetJSModelGridViewAdapter(List<BetMode> oddsDataList) {
        mOddsDataList = oddsDataList;
        mInflater = (LayoutInflater) App.getAppContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mOddsDataList.size();
    }

    @Override
    public BetMode getItem(int position) {
        return mOddsDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_bet_grid, null);
            holder = new ViewHolder();
            holder.tv_mode = (TextView) convertView.findViewById(R.id.tv_mode);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final BetMode data = getItem(position);
        holder.tv_mode.setText(data.getModeName());
        holder.tv_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mOnBetModeItemClickListener.onBetModeItemClickListener(data.getModeCode(), data.getModeNums());
            }
        });
        return convertView;
    }

    private OnBetModeItemClickListener mOnBetModeItemClickListener;

    public void setOnBetModeItemClickListener(OnBetModeItemClickListener onBetModeItemClickListener) {
        mOnBetModeItemClickListener = onBetModeItemClickListener;
    }

    public interface OnBetModeItemClickListener {
        void onBetModeItemClickListener(int modeCode, String[] modeNums);
    }

    class ViewHolder {
        TextView tv_mode;
    }
}

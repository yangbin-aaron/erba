package com.teb.kilimanjaro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teb.kilimanjaro.App;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.models.entry.qq.BetPatternsModelQQ.BetPatternsData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangbin on 16/7/15.
 */
public class BetPatterGridViewAdapterQQ extends BaseAdapter {

    private List<BetPatternsData> mBettingModeDataList;
    private LayoutInflater mInflater;

    public BetPatterGridViewAdapterQQ() {
        mBettingModeDataList = new ArrayList<> ();
        mInflater = (LayoutInflater) App.getAppContext ().getSystemService (Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setList (List<BetPatternsData> bettingModeDataList) {
        mBettingModeDataList = (bettingModeDataList == null?new ArrayList<BetPatternsData>():bettingModeDataList);
        notifyDataSetChanged ();
    }

    @Override
    public int getCount () {
        return mBettingModeDataList.size ();
    }

    @Override
    public BetPatternsData getItem (int position) {
        return mBettingModeDataList.get (position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate (R.layout.adapter_bet_grid, null);
            holder = new ViewHolder ();
            holder.tv_mode = (TextView) convertView.findViewById (R.id.tv_mode);
            convertView.setTag (holder);
        } else {
            holder = (ViewHolder) convertView.getTag ();
        }

        final BetPatternsData data = getItem (position);
        holder.tv_mode.setText (data.getPatternName ());
        holder.tv_mode.setBackgroundResource (
                data.isSelect () ? R.drawable.radio_red_bg : R.drawable.radio_white_bg);
        holder.tv_mode.setTextColor (
                App.getAppContext ().getResources ().getColor (data.isSelect () ? R.color.white : R.color.black_text));
        holder.tv_mode.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {

                mOnBetModeItemClickListener.onBetModeItemClickListener (position);
            }
        });
        return convertView;
    }

    private OnBetModeItemClickListener mOnBetModeItemClickListener;

    public void setOnBetModeItemClickListener (OnBetModeItemClickListener onBetModeItemClickListener) {
        mOnBetModeItemClickListener = onBetModeItemClickListener;
    }

    public interface OnBetModeItemClickListener {
        void onBetModeItemClickListener (int position);
    }

    class ViewHolder {
        TextView tv_mode;
    }
}

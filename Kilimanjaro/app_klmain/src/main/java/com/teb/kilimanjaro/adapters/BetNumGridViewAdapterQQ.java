package com.teb.kilimanjaro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teb.kilimanjaro.App;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.models.entry.hall.OddsListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangbin on 16/7/15.
 */
public class BetNumGridViewAdapterQQ extends BaseAdapter {

    private List<OddsListModel.OddsData> mOddsDataList;
    private LayoutInflater mInflater;
    private boolean mCanClick;

    public BetNumGridViewAdapterQQ () {
        mCanClick = false;
        mOddsDataList = new ArrayList<> ();
        mInflater = (LayoutInflater) App.getAppContext ().getSystemService (Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setCanClick (boolean canClick) {
        mCanClick = canClick;
        notifyDataSetChanged ();
    }

    public void setList (List<OddsListModel.OddsData> oddsDataList) {
        mOddsDataList = (oddsDataList == null?new ArrayList<OddsListModel.OddsData>():oddsDataList);
        notifyDataSetChanged ();
    }

    @Override
    public int getCount () {
        return mOddsDataList.size ();
    }

    @Override
    public OddsListModel.OddsData getItem (int position) {
        return mOddsDataList.get (position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate (R.layout.adapter_js_bet_grid, null);
            holder = new ViewHolder ();
            holder.tv_num = (TextView) convertView.findViewById (R.id.tv_num);
            convertView.setTag (holder);
        } else {
            holder = (ViewHolder) convertView.getTag ();
        }

        OddsListModel.OddsData data = getItem (position);

        holder.tv_num.setText (data.getLotteryNumber () + "");
        if (!data.isSelected ()) {
            holder.tv_num.setBackgroundResource (R.drawable.circle_gray_white_bg);
            holder.tv_num.setTextColor (App.getAppContext ().getResources ().getColor (mCanClick ? R.color.main_background : R.color.gray_x));
        } else {
            holder.tv_num.setBackgroundResource (R.drawable.circle_red_bg);
            holder.tv_num.setTextColor (App.getAppContext ().getResources ().getColor (R.color.white));
        }

        holder.tv_num.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (mCanClick) {
                    mOnBetNumItemClickListener.onBetNumItemClickListener (position);
                }
            }
        });

        return convertView;
    }

    private OnBetNumItemClickListener mOnBetNumItemClickListener;

    public void setOnBetNumItemClickListener (OnBetNumItemClickListener onBetNumItemClickListener) {
        mOnBetNumItemClickListener = onBetNumItemClickListener;
    }

    public interface OnBetNumItemClickListener {
        void onBetNumItemClickListener (int position);
    }

    class ViewHolder {
        TextView tv_num;
    }
}

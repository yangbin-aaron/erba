package com.teb.kilimanjaro.adapters;

import android.content.Context;
import android.util.Log;
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
 * Created by yangbin on 16/7/13.
 */
public class LotteryDeMybetAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<OddsListModel.OddsData> mList;

    public LotteryDeMybetAdapter() {
        mInflater = (LayoutInflater) App.getAppContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mList = new ArrayList<>();
    }

    public void setList(List<OddsListModel.OddsData> list) {
        mList = (list == null ? new ArrayList<OddsListModel.OddsData>() : list);
        Log.e("aaa", mList.toString());
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public OddsListModel.OddsData getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_lottery_d_mybet, null);
            holder = new ViewHolder();
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            holder.tv_odds = (TextView) convertView.findViewById(R.id.tv_odds);
            holder.tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
            holder.tv_get_mdb = (TextView) convertView.findViewById(R.id.tv_get_mdb);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OddsListModel.OddsData data = getItem(position);
        holder.tv_num.setText(data.getLotteryNumber() + "");
        holder.tv_odds.setText(data.getOdds() + "");
        holder.tv_amount.setText(data.getBetCoin() > 0 ? data.getBetCoin() + "" : "-");
        holder.tv_get_mdb.setText(Double.parseDouble(data.getRevenue()) > 0 ? data.getRevenue() : "-");
        return convertView;
    }

    class ViewHolder {
        TextView tv_num, tv_odds, tv_amount, tv_get_mdb;
    }
}

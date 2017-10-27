package com.teb.kilimanjaro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teb.kilimanjaro.App;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.models.entry.hall.WinnersListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangbin on 16/7/13.
 */
public class LotteryDeRosterAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<WinnersListModel.WinnersListData> mList;

    public LotteryDeRosterAdapter() {
        mInflater = (LayoutInflater) App.getAppContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mList = new ArrayList<>();
    }

    public void setList(List<WinnersListModel.WinnersListData> list) {
        mList = (list == null ? new ArrayList<WinnersListModel.WinnersListData>() : list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public WinnersListModel.WinnersListData getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.adapter_lottery_d_roster, null);
            holder = new ViewHolder();
            holder.tv_userid = (TextView) convertView.findViewById(R.id.tv_userid);
            holder.tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
            holder.tv_get_mdb = (TextView) convertView.findViewById(R.id.tv_get_mdb);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        WinnersListModel.WinnersListData data = getItem(position);

        holder.tv_userid.setText(data.getUserName());
        holder.tv_amount.setText(data.getBetCoin() + "");
        holder.tv_get_mdb.setText(data.getRevenue());

        return convertView;
    }

    class ViewHolder {
        TextView tv_userid, tv_amount, tv_get_mdb;
    }
}

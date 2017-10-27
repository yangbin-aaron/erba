package com.teb.kilimanjaro.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.teb.kilimanjaro.App;
import com.teb.kilimanjaro.AppPrefs;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.configs.BroadcastConfig;
import com.teb.kilimanjaro.models.entry.hall.GameListModel;
import com.teb.kilimanjaro.views.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangbin on 16/7/5.
 */
public class DrawerGameListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<GameListModel.GameListData> mGameList;

    public DrawerGameListAdapter(List<GameListModel.GameListData> gameList) {
        mGameList = new ArrayList<>();
        for (GameListModel.GameListData data : gameList) {
            if (data.isVisible()) {
                mGameList.add(data);
            }
        }
        mInflater = (LayoutInflater) App.getAppContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mGameList.size();
    }

    @Override
    public GameListModel.GameListData getItem(int position) {
        return mGameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler hodler;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_drawer_gamelist, null);
            hodler = new ViewHodler();
            hodler.iconIV = (CircleImageView) convertView.findViewById(R.id.iv_icon);
            hodler.gameNameTV = (TextView) convertView.findViewById(R.id.tv_gamename);
            convertView.setTag(hodler);
        } else {
            hodler = (ViewHodler) convertView.getTag();
        }

        final GameListModel.GameListData data = getItem(position);

        GameListModel.GameListData selectedGame = AppPrefs.getInstance().getSelectedGame();
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPrefs.getInstance().saveSelectedGame(data.getId());
                App.getAppContext().sendBroadcast(new Intent(BroadcastConfig.ACTION_UPDATE_GAME));// 通知主界面改变数据
                notifyDataSetChanged();
            }
        });
        if (data.getId() == selectedGame.getId()) {
            convertView.setBackgroundResource(R.color.gamelist_btn_selected);
            convertView.setEnabled(false);
        } else {
            convertView.setBackgroundResource(R.drawable.btn_gameitem_selector_bg);
            convertView.setEnabled(true);
        }

        hodler.gameNameTV.setText(data.getGameName());
        ImageAware imageAware = new ImageViewAware(hodler.iconIV, false);
        hodler.iconIV.setTag(data.getIcon());
        ImageLoader.getInstance().displayImage(data.getIcon(), imageAware);
        return convertView;
    }

    class ViewHodler {
        CircleImageView iconIV;
        TextView gameNameTV;
    }
}

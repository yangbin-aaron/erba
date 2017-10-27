package com.teb.kilimanjaro.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teb.kilimanjaro.App;
import com.teb.kilimanjaro.AppPrefs;
import com.teb.kilimanjaro.R;
import com.teb.kilimanjaro.activitys.mine.LoginActivity;
import com.teb.kilimanjaro.configs.HandlerConfig;
import com.teb.kilimanjaro.models.bean.BetMode;
import com.teb.kilimanjaro.models.bean.ModeAutoList;
import com.teb.kilimanjaro.models.entry.hall.OddsListModel;
import com.teb.kilimanjaro.services.DownloadService;
import com.teb.kilimanjaro.views.dialogs.MyDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by yangbin on 16/7/14.
 * 自定义工具类
 */
public class MyUtil {

    /**
     * 启动Activity
     *
     * @param activity
     * @param intent
     */
    public static void startActivity(Activity activity, Intent intent) {
        activity.startActivity(intent);
        AnimUtil.actFromRightToLeft(activity);
    }

// ------------------------------处理投注的list-----------------------------

    /**
     * 处理各种模式选择的号码集合
     *
     * @param nums
     * @param oldList
     * @return
     */
    public static List<OddsListModel.OddsData> handlerOddsList(String[] nums, List<OddsListModel.OddsData> oldList) {
        int numsSize = nums.length;
        int jCount = nums.length;
        List<OddsListModel.OddsData> newList = new ArrayList<>();
        for (int i = 0; i < oldList.size(); i++) {
            OddsListModel.OddsData data = oldList.get(i);
            data.setSelected(false);// 默认为false
            if (data.isBeted()) {// 已经投过的就必须保持原样(提交过的)
                data.setSelected(true);
            } else {
                data.setBetCoin(0);
            }
            if (numsSize > 0) {// 还有 未匹配完的数字
                for (int j = 0; j < jCount; j++) {
                    if (data.getLotteryNumber() == Integer.parseInt(nums[j].trim())) {// 号码相同
                        data.setSelected(true);
                        data.setBetCoin(data.isBeted() ? data.getBetCoin() : data.getDefaultBet());
                        numsSize--;
                        break;
                    }
                }
            }

            newList.add(data);
        }
        return newList;
    }

    /**
     * 获取诺干个不重复的数字
     *
     * @param num
     * @param n
     * @return
     */
    public static String[] getRandomNum(int num, int n) {
        Set<Integer> sets = new HashSet<Integer>();
        Random random = new Random();
        while (sets.size() < n) {
            sets.add(random.nextInt(num));
        }
        Integer[] integers = sets.toArray(new Integer[n]);
        String nums[] = new String[integers.length];
        for (int i = 0; i < integers.length; i++) {
            nums[i] = integers[i].intValue() + "";
        }
        return nums;
    }

    /**
     * 计算投注的总数量
     *
     * @param list
     * @return 总金额   最少奖金    最大奖金
     */
    public static double[] getBetInfo(List<OddsListModel.OddsData> list) {
        int totalCoin = 0;
        double min = 0;// 最大奖金
        double max = 0;// 最小奖金
        for (OddsListModel.OddsData data : list) {
            if (data.isSelected()) {
                double temp = data.getBetCoin() * data.getOdds();
                min = min == 0 ? temp : (min > temp ? temp : min);// 取最小值
                max = max < temp ? temp : max;// 取最大值
                totalCoin += data.getBetCoin();
            }
        }
        double[] result = new double[3];
        result[0] = totalCoin;
        result[1] = min;
        result[2] = max;
        return result;
    }

    /**
     * 按一定倍数处理全部投注选项
     *
     * @param list
     * @param times
     * @return
     */
    public static List<OddsListModel.OddsData> handlerOddsListByTimes(List<OddsListModel.OddsData> list, float times) {
        for (int i = 0; i < list.size(); i++) {
            OddsListModel.OddsData data = list.get(i);
            if (!data.isBeted()) {
                data.setBetCoin(times == 0 ? 0 : (long) (data.getBetCoin() * times));
                data.setSelected(data.getBetCoin() > 0);
                list.set(i, data);
            }
        }
        return list;
    }

    /**
     * 反选
     *
     * @param list
     * @return
     */
    public static List<OddsListModel.OddsData> handlerOddsListByTurn(List<OddsListModel.OddsData> list) {
        for (int i = 0; i < list.size(); i++) {
            OddsListModel.OddsData data = list.get(i);
            if (!data.isBeted()) {
                data.setSelected(!data.isSelected());// 反选状态
                data.setBetCoin(data.isSelected() ? data.getDefaultBet() : 0);
                list.set(i, data);
            }
        }
        return list;
    }

    /**
     * 获取本次投注的号码个数以及总金额
     *
     * @param list
     * @return
     */
    public static long[] getBetCountAndTotalCoin(List<OddsListModel.OddsData> list) {
        // 计算出本次投注的总数
        long totalBet = 0;
        int count = 0;// 投注的号码个数
        for (OddsListModel.OddsData data : list) {
            if (!data.isBeted() && data.isSelected()) {
                totalBet += data.getBetCoin();
                count++;
            }
        }
        return new long[]{count, totalBet};
    }

    /**
     * 梭哈
     *
     * @param list
     * @param coin
     * @param statistics 包含本次投注的号码个数和总金额
     * @return
     */
    public static List<OddsListModel.OddsData> handlerOddsListByBetAll(List<OddsListModel.OddsData> list, long coin, long[] statistics) {
        int count = (int) statistics[0];
        long totalBet = statistics[1];
        float bili = (float) coin / (float) totalBet;// 算出比例
        Log.e("sh", coin + "/" + totalBet + "=" + bili + "------" + count + " ------");

        for (int i = 0; i < list.size(); i++) {
            OddsListModel.OddsData data = list.get(i);
            if (!data.isBeted() && data.isSelected()) {
                long bet;
                if (count == 1) {// 最后一个，因为四舍五入会和原始的coin有出入，所以在最后一项将未投入的全部投入
                    bet = coin;
                } else {
                    bet = (long) (data.getBetCoin() * bili);
                    coin -= bet;
                }
                data.setBetCoin(bet);
                list.set(i, data);
                count--;
            }
        }
        return list;
    }

    // ------------------------------初始化自定义的按钮集合-----------------------------

    /**
     * 初始化倍数按钮集合，并实现接口回调
     *
     * @param view
     * @param lisenter
     */
    public static void setOnTimesItemClickLisenter(View view, final OnTimesItemClickLisenter lisenter) {
        int[] ids = {R.id.tv_times_01, R.id.tv_times_05, R.id.tv_times_08, R.id.tv_times_12, R.id.tv_times_clear, R.id.tv_times_last,
                R.id.tv_times_15, R.id.tv_times_20, R.id.tv_times_100, R.id.tv_times_500, R.id.tv_times_turn, R.id.tv_times_betall};
        final float[] times = {0.1f, 0.5f, 0.8f, 1.2f, 0, -1, 1.5f, 2, 10, 50, -2, -3};
        TextView[] textViews = new TextView[ids.length];
        for (int i = 0; i < ids.length; i++) {
            final int position = i;
            textViews[i] = (TextView) view.findViewById(ids[i]);
            textViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lisenter.onTimesItemClickLisenter(times[position]);
                }
            });
        }
    }

    public interface OnTimesItemClickLisenter {
        /**
         * @param times 0 清空    -1 上期    -2 反选  -3 梭哈
         */
        void onTimesItemClickLisenter(float times);
    }


    /**
     * 初始化倍数按钮集合，并实现接口回调
     *
     * @param view
     * @param lisenter
     */
    public static void setOnKeyboardItemClickLisenter(View view, final OnKeyboardItemClickLisenter lisenter) {
        int[] ids = {R.id.key_0, R.id.key_1, R.id.key_2, R.id.key_3, R.id.key_4, R.id.key_5, R.id.key_6, R.id.key_7, R.id.key_8, R.id.key_9};
        TextView[] textViews = new TextView[ids.length];
        for (int i = 0; i < ids.length; i++) {
            final int position = i;
            textViews[i] = (TextView) view.findViewById(ids[i]);
            textViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lisenter.onKeyboardItemClickLisenter(position);
                }
            });
        }
        RelativeLayout hideIV = (RelativeLayout) view.findViewById(R.id.key_hide);
        hideIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lisenter.onKeyboardItemClickLisenter(10);
            }
        });
        RelativeLayout deleteIV = (RelativeLayout) view.findViewById(R.id.key_delete);
        deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lisenter.onKeyboardItemClickLisenter(11);
            }
        });
    }


    public interface OnKeyboardItemClickLisenter {

        /**
         * @param position 10 关闭键盘   11  删除
         */
        void onKeyboardItemClickLisenter(int position);
    }


    /**
     * 初始化普通投注界面其他模式按钮集合，并实现接口回调
     *
     * @param view
     * @param lisenter
     */
    public static void setOnOtherModeItemClickLisenter(View view, final OnOtherModeItemClickLisenter lisenter) {
        int[] ids = {R.id.tv_times_quan, R.id.tv_times_dan, R.id.tv_times_shuang, R.id.tv_times_zhong, R.id.tv_times_bian, R.id.tv_times_da, R.id.tv_times_xiao};
        final String[] nums = {"0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27", "1,3,5,7,9,11,13,15,17,19,21,23,25,27"
                , "0,2,4,6,8,10,12,14,16,18,20,22,24,26", "10,11,12,13,14,15,16,17", "0,1,2,3,4,5,6,7,8,9,18,19,20,21,22,23,24,25,26,27"
                , "14,15,16,17,18,19,20,21,22,23,24,25,26,27", "0,1,2,3,4,5,6,7,8,9,10,11,12,13"};
        TextView[] textViews = new TextView[ids.length];
        for (int i = 0; i < ids.length; i++) {
            final int position = i;
            textViews[i] = (TextView) view.findViewById(ids[i]);
            textViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lisenter.onOtherModeItemClickLisenter((nums[position]).split(","));
                }
            });
        }
    }


    public interface OnOtherModeItemClickLisenter {

        /**
         * @param modeNums
         */
        void onOtherModeItemClickLisenter(String[] modeNums);
    }

    // ------------------------------处理网络错误和返回错误-----------------------------

    /**
     * 处理网络错误和返回错误
     *
     * @param msg
     */
    public static void handMessage(final Activity act, Message msg, String tag) {
        Log.e("msg", tag + "-----Message异常");
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_FAIL:
                showErrorDialog(act, (String) msg.obj);
                break;
            case HandlerConfig.WHAT_NET_ERROR:
                showErrorDialog(act, act.getString(R.string.can_not_connect));
                break;
            case HandlerConfig.WHAT_DATA_WRONG:
                showErrorDialog(act, act.getString(R.string.have_not_service));
//                Log.e("kilimanjaro", act.getResources().getString(R.string.have_not_service));
                break;
            case HandlerConfig.WHAT_TOKEN_IS_UPDATED:
                if (AppPrefs.getInstance().getTokenState()) {
                    AppPrefs.getInstance().saveTokenState(false);// 不能够访问
                    new MyDialog(act)
                            .setRightText(act.getResources().getString(R.string.app_confirm))
                            .cancelable(false)
                            .setMessage(act.getResources().getString(R.string.have_login_wrong))
                            .listener(new MyDialog.OnDialogClickListener1() {
                                @Override
                                public void onRightClicked() {
                                    // 清空UserJson数据和GameListJson数据
                                    AppPrefs.getInstance().saveUserJson(null);
                                    AppPrefs.getInstance().saveGameListJson(null);
                                    Intent intent = new Intent(act, LoginActivity.class);
                                    act.finish();
                                    act.startActivity(intent);
                                }
                            })
                            .show();
                }
                break;
        }
    }

    public static void handMessage1(final Activity act, Message msg, String tag) {
        Log.e("msg", tag + "-----Message异常");
        switch (msg.what) {
            case HandlerConfig.WHAT_POST_FAIL:
                ToastUtil.showToast((String) msg.obj);
                break;
            case HandlerConfig.WHAT_NET_ERROR:
                ToastUtil.showToast(R.string.can_not_connect);
                break;
            case HandlerConfig.WHAT_DATA_WRONG:
                ToastUtil.showToast(R.string.have_not_service);
                break;
            case HandlerConfig.WHAT_TOKEN_IS_UPDATED:
                if (AppPrefs.getInstance().getTokenState()) {
                    AppPrefs.getInstance().saveTokenState(false);// 不能够访问
                    new MyDialog(act)
                            .setRightText(act.getResources().getString(R.string.app_confirm))
                            .cancelable(false)
                            .setMessage(act.getResources().getString(R.string.have_login_wrong))
                            .listener(new MyDialog.OnDialogClickListener1() {
                                @Override
                                public void onRightClicked() {
                                    // 清空UserJson数据和GameListJson数据
                                    AppPrefs.getInstance().saveUserJson(null);
                                    AppPrefs.getInstance().saveGameListJson(null);
                                    Intent intent = new Intent(act, LoginActivity.class);
                                    act.finish();
                                    act.startActivity(intent);
                                }
                            })
                            .show();
                }
                break;
        }
    }

    public static void showErrorDialog(Activity activity, String msgStr) {
        new MyDialog(activity)
                .setMessage(msgStr)
                .setRightText(activity.getString(R.string.app_confirm))
                .show();
    }

    public static void downLoadApk(String url) {
        Intent intent = new Intent(App.getAppContext(), DownloadService.class);
        intent.putExtra("apk_url", url);
        App.getAppContext().startService(intent);
    }

    /**
     * 解析mode列表
     *
     * @param json
     * @return
     */
    public static List<BetMode> getBetModeListFronJson(String json) {
        List<BetMode> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                int status = jsonObject.getInt("status");
                if (status == 1) {
                    BetMode betMode = new BetMode();
                    betMode.setStatus(status);
                    betMode.setModeCode(jsonObject.getInt("modeCode"));
                    betMode.setModeName(jsonObject.getString("modeName"));
                    betMode.setModeNums(jsonObject.getString("modeNums"));
                    list.add(betMode);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 是否存在同名的自动投注模式
     *
     * @param modeName
     * @return
     */
    public static ModeAutoList.ModeAutoData findModeAutoByName(String modeName) {
        ModeAutoList.ModeAutoData autoData = new ModeAutoList.ModeAutoData();
        int id = 0;
        String name = null;

        List<ModeAutoList.ModeAutoData> modeAutoDatas = AppPrefs.getInstance().getModeAutoList();
        for (ModeAutoList.ModeAutoData data : modeAutoDatas) {
            if (data.getId() > id) {
                id = data.getId();
            }
            if (data.getName().equals(modeName)) {
                name = modeName;
            }
        }
        autoData.setId(id + 1);// 创建一个新的id
        autoData.setName(name);
        return autoData;
    }


}

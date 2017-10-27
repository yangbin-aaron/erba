package com.teb.kilimanjaro;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.teb.kilimanjaro.models.bean.BetAutoModel;
import com.teb.kilimanjaro.models.bean.ModeAutoList;
import com.teb.kilimanjaro.models.entry.hall.GameListModel;
import com.teb.kilimanjaro.models.entry.hall.LotteryModel;
import com.teb.kilimanjaro.models.entry.hall.OddsListModel;
import com.teb.kilimanjaro.models.entry.mine.BasicInfoModel;
import com.teb.kilimanjaro.models.entry.qq.BetPatternsModelQQ;
import com.teb.kilimanjaro.models.entry.qq.BetPatternsModelQQ.BetPatternsData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangbin on 16/7/4.
 */
public class AppPrefs {

    private static final String SHARED_PREFERENCES_NAME = "kl_prefs";

    private interface Key {
        String USER_PHONE = "user_phone";// 登录账户
        String USER_JSON = "user_json";// 登录完成保持json数据
        String GAME_LIST_JSON = "game_list_json";// 游戏列表的json数据
        String GAME_SELECTED_ID = "game_selected_id";// 被选中的游戏ID
        String BET_MODE = "bet_mode";// 习惯的投注模式（极速还是普通）
        String TOKEN_WRONG = "token_wrong";// 是否自动下线
        String GAME_COIN = "game_coin";// 游戏金豆
        String GAME_SCORE = "game_score";// 游戏积分
        String LAST_BET = "last_bet";// 上次投注的记录
        String LAST_BET_QQ = "last_bet_qq";// 上次投注的记录
        String CURR_BET_INFO = "curr_bet_info";// 当前投注信息
        String NEW_BET_INFO = "new_bet_info";// 最新的开奖信息
        String START_ISSUE = "start_issue";// 开始期号
        String MODE_LIST = "mode_list";// 自动投注的模式列表
        String AFTER_WIN_CONTINUE = "after_win_continue";// 自动投注  赢了后继续
        String MODE_START = "mode_start";// 自动投注的模式列表
        //        String MODE_END = "mode_end";// 自动投注的模式列表
        String BET_AUTO_MAX_COIN = "bet_auto_max_coin";// 余额达到金额
        String BET_AUTO_MIN_COIN = "bet_auto_min_coin";// 余额不足金额
        String BET_AUTO_COUNT = "bet_auto_count";// 自动投注的期数
        String BET_AUTO_IS_STARTED = "bet_auto_is_started";// 自动投注是否开始
        String BASIC_INFO = "basic_info";// 客服信息
    }

    private static AppPrefs sInstance;

    private SharedPreferences mPrefs;

    public static AppPrefs getInstance() {
        if (sInstance == null) {
            sInstance = new AppPrefs();
        }
        return sInstance;
    }

    private AppPrefs() {
        mPrefs = App.getAppContext().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getEditor() {
        return mPrefs.edit();
    }

    public void saveUserPhone(String phone) {
        getEditor().putString(Key.USER_PHONE, phone).commit();
    }

    public String getUserPhone() {
        return mPrefs.getString(Key.USER_PHONE, null);
    }

    /**
     * /**
     * 保存登录返回的json数据
     *
     * @param userJson
     */
    public void saveUserJson(String userJson) {
        getEditor().putString(Key.USER_JSON, userJson).commit();
    }

    /**
     * 获取登录json
     *
     * @return
     */
    public String getUserJson() {
        return mPrefs.getString(Key.USER_JSON, null);
    }

    /**
     * 保存gameListJson数据
     *
     * @param gameListJson
     */
    public void saveGameListJson(String gameListJson) {
        getEditor().putString(Key.GAME_LIST_JSON, gameListJson).commit();
    }

    /**
     * 获取gameListJson数据
     *
     * @return
     */
    public String getGameListJson() {
        return mPrefs.getString(Key.GAME_LIST_JSON, null);
    }

    /**
     * 保存被选中的游戏对象
     *
     * @param gameId
     */
    public void saveSelectedGame(int gameId) {
        getEditor().putInt(Key.GAME_SELECTED_ID, gameId).commit();
    }

    /**
     * 获取被选中的游戏对象
     *
     * @return
     */
    public GameListModel.GameListData getSelectedGame() {
        String json = getGameListJson();
        if (json != null) {
            GameListModel model = new Gson().fromJson(json, GameListModel.class);
            for (GameListModel.GameListData data : model.getData()) {
                if (data.getId() == mPrefs.getInt(Key.GAME_SELECTED_ID, -1)) {
                    return data;
                }
            }
        }
        return null;
    }

    /**
     * 保存习惯投注模式
     *
     * @param mode
     */
    public void saveBetMode(int mode) {
        getEditor().putInt(Key.BET_MODE, mode).commit();
    }

    /**
     * 获取习惯投注模式
     *
     * @return
     */
    public int getBetMode() {
        return mPrefs.getInt(Key.BET_MODE, 0);
    }

    /**
     * 修改 自动下线的 状态
     *
     * @param tokenState
     */
    public void saveTokenState(boolean tokenState) {
        getEditor().putBoolean(Key.TOKEN_WRONG, tokenState).commit();
    }

    /**
     * 获取  自动下线的 状态
     *
     * @return
     */
    public boolean getTokenState() {
        return mPrefs.getBoolean(Key.TOKEN_WRONG, false);
    }

    /**
     * 积分
     *
     * @param coin
     */
    public void saveGameCoin(long coin, long score) {
        getEditor().putLong(Key.GAME_COIN, coin).commit();
        getEditor().putLong(Key.GAME_SCORE, score).commit();
    }

    /**
     * 积分
     *
     * @return
     */
    public long getGameCoinLong() {
        return mPrefs.getLong(Key.GAME_COIN, 0);
    }

    public long getGameScoreLong() {
        return mPrefs.getLong(Key.GAME_SCORE, 0);
    }

    /**
     * 根据是否是模拟场，返回积分
     *
     * @return
     */
    public long getGameLong() {
        if (AppPrefs.getInstance().getSelectedGame().isSimulate()) {
            return AppPrefs.getInstance().getGameScoreLong();
        } else {
            return AppPrefs.getInstance().getGameCoinLong();
        }
    }

    /**
     * 保存该次投注的记录
     *
     * @param list
     */
    public void saveBetList(List<OddsListModel.OddsData> list) {
        for (int i = 0; i < list.size(); i++) {
            OddsListModel.OddsData data = list.get(i);
            data.setBeted(false);
            list.set(i, data);
        }
        OddsListModel oddsListModel = new OddsListModel();
        oddsListModel.setData(list);
        String json = new Gson().toJson(oddsListModel);
        getEditor().putString(Key.LAST_BET, json).commit();
    }

    /**
     * 获取上次投注的记录
     *
     * @return
     */
    public List<OddsListModel.OddsData> getLastBetList() {
        String json = mPrefs.getString(Key.LAST_BET, null);
        List<OddsListModel.OddsData> oddsDatas = new ArrayList<>();
        if (json != null) {
            OddsListModel model = new Gson().fromJson(json, OddsListModel.class);
            oddsDatas = model.getData();
        }
        return oddsDatas;
    }

    /**
     * 保存自动投注模式
     *
     * @param list
     */
    public void saveModeList(List<ModeAutoList.ModeAutoData> list) {
        getEditor().putString(Key.MODE_LIST, new Gson().toJson(new ModeAutoList(list))).commit();
    }

    /**
     * 获取自动投注模式列表
     *
     * @return
     */
    public List<ModeAutoList.ModeAutoData> getModeAutoList() {
        List<ModeAutoList.ModeAutoData> list = new ArrayList<>();
        String json = mPrefs.getString(Key.MODE_LIST, null);
        if (json != null) {
            ModeAutoList modeAutoDatas = new Gson().fromJson(json, ModeAutoList.class);
            list = modeAutoDatas.getData();
        }
        return list;
    }

    /**
     * 保存开始参数  模式
     *
     * @param modeAutoData
     */
    public void saveModeStart(ModeAutoList.ModeAutoData modeAutoData) {
        getEditor().putString(Key.MODE_START, modeAutoData.getName()).commit();
    }

    /**
     * 获取开始参数  模式名称
     *
     * @return
     */
    public String getModeStartName() {
        return mPrefs.getString(Key.MODE_START, "");
    }

    /**
     * 保存 中奖后是否继续的状态
     *
     * @param isContinueAfterWin
     */
    public void saveIsContinueAfterWin(boolean isContinueAfterWin) {
        getEditor().putBoolean(Key.AFTER_WIN_CONTINUE, isContinueAfterWin).commit();
    }

    /**
     * 中奖后是否继续投注
     *
     * @return
     */
    public boolean isContinueAfterWin() {
        return mPrefs.getBoolean(Key.AFTER_WIN_CONTINUE, true);
    }

    /**
     * 保存最新的开奖信息
     *
     * @param newBetInfo
     */
    public void saveNewBetInfo(String newBetInfo) {
        getEditor().putString(Key.NEW_BET_INFO, newBetInfo).commit();
    }

    /**
     * 获取最新的开奖信息
     *
     * @return
     */
    public LotteryModel.LotteryData getNewBetInfo() {
        String text = mPrefs.getString(Key.NEW_BET_INFO, null);
        LotteryModel.LotteryData lotteryData = null;
        if (text != null) {
            lotteryData = new Gson().fromJson(text, LotteryModel.LotteryData.class);
        }
        return lotteryData;
    }


    /**
     * 保存当前期号的投注信息
     *
     * @param currentBetInfo
     */
    public void saveCurrentBetInfo(LotteryModel.LotteryData currentBetInfo) {
        String json = new Gson().toJson(currentBetInfo);
        getEditor().putString(Key.CURR_BET_INFO, json).commit();
    }

    /**
     * 获取当前期号投注信息
     *
     * @return
     */
    public LotteryModel.LotteryData getCurrentBetInfo() {
        String text = mPrefs.getString(Key.CURR_BET_INFO, null);
        LotteryModel.LotteryData lotteryData = null;
        if (text != null) {
            lotteryData = new Gson().fromJson(text, LotteryModel.LotteryData.class);
        }
        return lotteryData;
    }

    /**
     * 保存自动投注的参数对象
     *
     * @param model
     */
    public void saveBetAutoModel(BetAutoModel model) {
        getEditor().putInt(Key.START_ISSUE, model.getStartIssue()).commit();
        getEditor().putInt(Key.BET_AUTO_COUNT, model.getAutoCount()).commit();
        getEditor().putLong(Key.BET_AUTO_MAX_COIN, model.getMaxCoin()).commit();
        getEditor().putLong(Key.BET_AUTO_MIN_COIN, model.getMinCoin()).commit();
    }

    /**
     * 获取自动投注的参数对象
     *
     * @return
     */
    public BetAutoModel getBetAutoModel() {
        BetAutoModel autoModel = new BetAutoModel();
        List<ModeAutoList.ModeAutoData> list = getModeAutoList();
        ModeAutoList.ModeAutoData autoData = null;
        for (ModeAutoList.ModeAutoData data : list) {
            if (data.getName().equals(getModeStartName())) {
                autoData = data;
                break;
            }
        }
        autoModel.setModeAutoDataOfStart(autoData);
        autoModel.setStartIssue(mPrefs.getInt(Key.START_ISSUE, 1));
        autoModel.setAutoCount(mPrefs.getInt(Key.BET_AUTO_COUNT, 2));
        autoModel.setMaxCoin(mPrefs.getLong(Key.BET_AUTO_MAX_COIN, 0));
        autoModel.setMinCoin(mPrefs.getLong(Key.BET_AUTO_MIN_COIN, 0));
        autoModel.setContinueAfterWin(isContinueAfterWin());
        return autoModel;
    }

    /**
     * 保存自动投注开始状态
     *
     * @param flag
     */
    public void saveBetAutoStartState(boolean flag) {
        getEditor().putBoolean(Key.BET_AUTO_IS_STARTED, flag).commit();
    }

    /**
     * 获取自动投注开始状态
     *
     * @return
     */
    public boolean getBetAutoStartState() {
        return mPrefs.getBoolean(Key.BET_AUTO_IS_STARTED, false);
    }

    /**
     * 保存  QQ群玩法的上次投注
     *
     * @param patternsData
     */
    public void saveLastBetQQ(BetPatternsData patternsData) {
        int gameId = getSelectedGame().getId();
        patternsData.setGameId(gameId);
        BetPatternsModelQQ modelQQ = getLastBetListQQ();
        List<BetPatternsData> dataList;
        if (modelQQ != null) {
            dataList = modelQQ.getData();
            int index = -1;
            for (int i = 0; i < dataList.size(); i++) {
                if (gameId == dataList.get(i).getGameId()) {
                    index = i;
                    break;
                }
            }
            if (index == -1) {
                dataList.add(patternsData);
            } else {
                dataList.set(index, patternsData);
            }
        } else {
            modelQQ = new BetPatternsModelQQ();
            dataList = new ArrayList<>();
            dataList.add(patternsData);
        }
        modelQQ.setData(dataList);

        String json = new Gson().toJson(modelQQ);
        getEditor().putString(Key.LAST_BET_QQ, json).commit();
    }

    /**
     * 获取  QQ群玩法的上次投注(所有游戏列表)
     *
     * @return
     */
    public BetPatternsModelQQ getLastBetListQQ() {
        String json = mPrefs.getString(Key.LAST_BET_QQ, null);
        BetPatternsModelQQ data = null;
        if (json != null) {
            data = new Gson().fromJson(json, BetPatternsModelQQ.class);
        }
        return data;
    }

    /**
     * 获取  QQ群玩法的上次投注
     *
     * @return
     */
    public BetPatternsData getLastBetQQ() {
        int gameId = getSelectedGame().getId();
        BetPatternsModelQQ modelQQ = getLastBetListQQ();
        BetPatternsData data = null;
        if (modelQQ == null) {
            data = null;
        } else {
            List<BetPatternsData> dataList = modelQQ.getData();
            for (BetPatternsData patternsData : dataList) {
                if (patternsData.getGameId() == gameId) {
                    data = patternsData;
                    break;
                }
            }
        }
        return data;
    }

    /**
     * 保存客服信息
     *
     * @param json
     */
    public void saveBasicInfo(String json) {
        if (!TextUtils.isEmpty(json)) {
            getEditor().putString(Key.BASIC_INFO, json).commit();
        }
    }

    /**
     * 获取保存的客服信息
     *
     * @return
     */
    public BasicInfoModel getBasicInfoModel() {
        String json = mPrefs.getString(Key.BASIC_INFO, null);
        if (TextUtils.isEmpty(json)) {
            return null;
        } else {
            BasicInfoModel basicInfoModel = new Gson().fromJson(json, BasicInfoModel.class);
            return basicInfoModel;
        }
    }
}


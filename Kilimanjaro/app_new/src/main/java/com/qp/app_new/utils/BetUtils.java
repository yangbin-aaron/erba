package com.qp.app_new.utils;

import com.qp.app_new.contents.AppContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aaron on 2017/11/14.
 */

public class BetUtils {

    /**
     * 按一定倍数处理全部投注选项
     *
     * @param list
     * @param times
     * @return
     */
    public static JSONArray handlerOddsListByTimes(JSONArray list, float times) {
        try {
            for (int i = 0; i < list.length(); i++) {
                JSONObject jsonObject = list.optJSONObject(i);
                if (!jsonObject.optBoolean(AppContent.ISBETED)) {
                    jsonObject.put(AppContent.BETCOIN, times == 0 ? 0 : (long) (jsonObject.optInt(AppContent.BETCOIN) * times));
                    jsonObject.put(AppContent.ISSELECTED, jsonObject.optInt(AppContent.BETCOIN) > 0);
                    list.put(i, jsonObject);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 处理各种模式选择的号码集合
     *
     * @param nums
     * @param oldList
     * @return
     */
    public static JSONArray handlerOddsList(String[] nums, JSONArray oldList) {
        JSONArray newList = new JSONArray();
        try {
            int numsSize = nums.length;
            int jCount = nums.length;
            for (int i = 0; i < oldList.length(); i++) {
                JSONObject jsonObject = oldList.optJSONObject(i);
                jsonObject.put(AppContent.ISSELECTED, false);// 默认为false
                if (jsonObject.optBoolean(AppContent.ISBETED)) {// 已经投过的就必须保持原样(提交过的)
                    jsonObject.put(AppContent.ISSELECTED, true);
                } else {
                    jsonObject.put(AppContent.BETCOIN, 0);
                }
                if (numsSize > 0) {// 还有 未匹配完的数字
                    for (int j = 0; j < jCount; j++) {
                        if (jsonObject.optInt("lotteryNumber") == Integer.parseInt(nums[j].trim())) {// 号码相同
                            jsonObject.put(AppContent.ISSELECTED, true);
                            jsonObject.put(AppContent.BETCOIN, jsonObject.optBoolean(AppContent.ISBETED) ? jsonObject.optInt(AppContent.BETCOIN) : jsonObject.optInt("defaultBet"));
                            numsSize--;
                            break;
                        }
                    }
                }

                newList.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newList;
    }
}

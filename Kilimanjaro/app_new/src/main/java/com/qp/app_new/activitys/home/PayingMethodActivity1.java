package com.qp.app_new.activitys.home;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.qp.app_new.R;
import com.qp.app_new.activitys.BaseActivity;
import com.qp.app_new.httpnetworks.NetWorkManager;
import com.qp.app_new.interfaces.NetListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aaron on 17/11/26.
 */

public class PayingMethodActivity1 extends BaseActivity {
    @Override
    public int getContentView () {
        return R.layout.activity_paying_method1;
    }

    private JSONObject mGameJSONObject;

    @Override
    public void getIntentData () {
        super.getIntentData ();
        try {
            mGameJSONObject = new JSONObject (getIntent ().getStringExtra ("gameJsonObject"));
        } catch (JSONException e) {
            e.printStackTrace ();
        }
    }

    private TextView tv1, tv2, tv3, tv4, tv5;

    @Override
    public void initView () {
        initActionBar ();
        setLeftIV (R.drawable.ic_back_btn);
        setTitle (getString (R.string.qq_payingmethod_bar, mGameJSONObject.optString ("gameName")));

        tv1 = (TextView) findViewById (R.id.tv1);
        tv2 = (TextView) findViewById (R.id.tv2);
        tv3 = (TextView) findViewById (R.id.tv3);
        tv4 = (TextView) findViewById (R.id.tv4);
        tv5 = (TextView) findViewById (R.id.tv5);
        getBetModes ();
    }

    private void getBetModes () {
        NetWorkManager.getInstance ().getBetModeList (mLoadingDialog, new NetListener () {
            @Override
            public void onSuccessResponse (String msg, JSONArray jsonArray) {
                super.onSuccessResponse (msg, jsonArray);
                setValues (jsonArray);
            }
        });
    }

    private static final String LI = "<font color='#D44E4D'><b>例：</b></font> ";

    private void setValues (JSONArray jsonArray) {
        findViewById (R.id.ly_mothed).setVisibility (View.VISIBLE);
        String raferName = mGameJSONObject.optString ("referName");
        String str1 = mGameJSONObject.optString ("gameName") +
                "是一种简单有趣的数字竞彩游戏，总共有28个数字，从0依次到27。这个数字是由三个单独的个位数字（三个数字取自" +
                raferName
                + "）相加而得到的，您若押中了这个数字，即可得到相应的奖励。";
        tv1.setText (str1);

        String str2 = "游戏开奖结果取自" + raferName
                + "，保证公平公正！每一局，不管开奖任何号码，奖励都会如实返还给用户，输赢值再用户间产生，网站不参与或者接入游戏。";
        tv2.setText (str2);

        String str3 = "首先将当期开奖号码从小到大依次排序" +
                "<br>" + LI + raferName
                + "第1****期开奖号码从小到大排序后为："
                + "<font color='#FF7F24'>01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20</font>，"
                + "则本站第214***期（可以在历史开奖记录中查看" + raferName + "对应的期数进行验证）开奖号码取号方式为："
                + "<br>一区：<font color='#FF7F24'>02 05 08 11 14 17</font>，取尾数 <font color='#FF7F24'>7</font>"
                + "<br>二区：<font color='#FF7F24'>03 06 09 12 15 18</font>，取尾数 <font color='#FF7F24'>3</font>"
                + "<br>三区：<font color='#FF7F24'>04 07 10 13 16 19</font>，取尾数 <font color='#FF7F24'>9</font>"
                + "<br>那么，本站第214***期的开奖号码为<font color='#FF7F24'>7 + 3 + 9 = 19</font>";
        tv3.setText (Html.fromHtml (str3));

        String str4 = "开奖时间以实际开奖时间为准！";
        tv4.setText (str4);

        String str5 = "";
        for (int i = 0; i < jsonArray.length (); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject (i);
            if (i > 0) {
                str5 += "<br><br>";
            }
            str5 += "<font color='#FF7F24'>" + numToString (i) + "、压" + jsonObject.optString ("patternName") + "的计算方式：</font>";
            str5 += "<br><font color='#000000'>对应的号码：" + jsonObject.optString ("includeNum") + "</font>";
            long balanceCoin = jsonObject.optLong ("balanceCoin");
            String greaterSpecialOdds = jsonObject.optString ("greaterSpecialOdds");
            String odds = jsonObject.optString ("odds");
            String patternName = jsonObject.optString ("patternName");
            String tsnum = "";
            if (patternName.equals ("大") || patternName.equals ("小")
                    || patternName.equals ("单") || patternName.equals ("双")
                    || patternName.equals ("小单") || patternName.equals ("大双")) {
                tsnum = " 13 或 14 ";
            }
            if (balanceCoin > 0 && !TextUtils.isEmpty (tsnum)) {
                str5 += "<br>下单本金小于或等于 " + balanceCoin + " 时，则返还 该注本金 * " + odds
                        + "；下单本金大于 " + balanceCoin + " 时，如果开奖号码为" + tsnum + "，则返还 该注本金 * " + greaterSpecialOdds
                        + "，开奖号码为其他号码时返还 该注本金 * " + odds + "。";
            } else if (balanceCoin == 0 && !TextUtils.isEmpty (tsnum)) {
                str5 += "<br>如果开奖号码是" + tsnum + "，则返还 该注本金 * " + greaterSpecialOdds
                        + "，若开奖号码为其他号码，则返还 该注本金 * " + odds + "。";
            } else {
                str5 += "<br>若中奖，则返还 该注本金 * " + odds + "。";
            }

        }
        str5 += "<br><br><font color='#000000'><b>温馨提示：特殊号码奖金计算的本金都按照该期的总投注额！</b></font>";
        tv5.setText (Html.fromHtml (str5));
    }

    private String numToString (int i) {
        String str = "";
        switch (i) {
            case 0:
                str = "A";
                break;
            case 1:
                str = "B";
                break;
            case 2:
                str = "C";
                break;
            case 3:
                str = "D";
                break;
            case 4:
                str = "E";
                break;
            case 5:
                str = "F";
                break;
            case 6:
                str = "G";
                break;
            case 7:
                str = "H";
                break;
            case 8:
                str = "I";
                break;
            case 9:
                str = "J";
                break;
            case 10:
                str = "K";
                break;
        }
        return str;
    }
}

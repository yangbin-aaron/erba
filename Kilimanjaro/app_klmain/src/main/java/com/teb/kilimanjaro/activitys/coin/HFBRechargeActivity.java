package com.teb.kilimanjaro.activitys.coin;

import android.content.DialogInterface;

import com.teb.kilimanjaro.activitys.BaseActivity;

/**
 * Created by yangbin on 16/9/13.
 */
public class HFBRechargeActivity extends BaseActivity {

//    private static final int INIT_RESULT = 1001;
//    // 支付限额范围0.1-3000
//    private static final long MIN_RMB = 10;
//    private static final long MAX_RMB = 3000;
//
//    private EditText mInputRmbET;// 输入人民币金额
//
//    private RadioGroup rg_pay_way;
//    private RadioButton rb_wechat_pay, rb_alipay, rb_jcard;
//
//    private RadioButton rb_fast_pay, rb_phone_bank_pay, rb_credit_card_pay, rb_jcard_pay, rb_phone_card_pay,
//            rb_accout_pay, rb_else_card_pay, rb_all_card_pay;
//    private Bundle _payInfoBundle;
//    // 汇付宝安全支付服务Logo是否需要隐藏
//    private boolean isLogoHidden;
//
//    private TextView mOnPayTV;// 支付按钮
//
//    private PaymentInfo _paymentInfo;
//    private String _payType;
//    private String _agentBillNo;
//    private boolean isDebug = false;
//    private Dialog progressDialog;
//
//    private Handler handler = new Handler() {
//
//        @Override
//        public void handleMessage(Message message) {
//            switch (message.what) {
//                case INIT_RESULT:
//                    progressDialog.dismiss();
//                    PaymentInfo info = (PaymentInfo) message.obj;
//                    if (info.isHasError()) {
//                        Toast.makeText(HFBRechargeActivity.this, info.getMessage(), Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    if (_payType.equals("22") || _payType.equals("30")) {
//                        startHeepayServiceJar();
//                    } else {
//                        startHeepayService();
//                    }
//                    break;
//                case HeepaySDKConstant.PAYRESULT_OVER:
//                    // Log.v(SHOW_TAG, message.obj.toString());
//                    // 不允许被赋值为1/0/-1,因已有对应的支付状态
//                    int status = -2;
//                    try {
//                        String totalAmt = mInputRmbET.getText().toString().trim();
//                        if (TextUtils.isEmpty(mInputRmbET.getText().toString().trim())) {
//                            Toast.makeText(HFBRechargeActivity.this, "请输入金额", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        String payAmt = "0";
//                        JSONObject object = new JSONObject(message.obj.toString());
//                        // 订单金额
//                        if (object.has("total_amt")) {
//                            totalAmt = object.getString("total_amt");
//                        }
//                        // 实际支付金额
//                        if (object.has("pay_amt")) {
//                            payAmt = object.getString("pay_amt");
//                        }
//                        // 微信支付通知
//                        if (object.has("third_status")) {
//                            Toast.makeText(HFBRechargeActivity.this, object.getString("third_status"), Toast.LENGTH_SHORT)
//                                    .show();
//                        }
//                        status = object.getInt("bill_status");
//                        Log.v("Demo payAmt", "totalAmt=" + totalAmt + "\n" + "payAmt=" + payAmt);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    break;
//                case HeepaySDKConstant.CHECK_INSTALL:
//                    startHeepayService();
//                    break;
//                default:
//                    break;
//            }
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_recharge_hfb);
//        initViews();
//    }
//
//    private void initViews() {
//        // ******MyActionBar
//        MyActionBar actionBar = (MyActionBar) findViewById(R.id.actionBar);
//        actionBar.setBarBackGround(R.color.main_background);
//        actionBar.setBarLeftImage(R.drawable.ic_back_btn);
//        actionBar.setBarTitleText(R.string.recharge_bar);
//        actionBar.setOnActionBarClickListener(new MyActionBar.OnActionBarClickListener() {
//            @Override
//            public void onActionBarLeftClicked() {
//                finish();
//            }
//
//            @Override
//            public void onActionBarRightClicked() {
//
//            }
//        });
//
//        // ******输入金额
//        mInputRmbET = (EditText) findViewById(R.id.et_rmb);
//        mInputRmbET.setText(MIN_RMB + "");
//        mInputRmbET.setSelection(mInputRmbET.getText().toString().trim().length());
//
//        // ******initWidgets
//        initWidgets();
//    }
//
//    /**
//     * 控件初始化
//     */
//    public void initWidgets() {
//        rg_pay_way = (RadioGroup) findViewById(R.id.rg_pay_way);
//        rb_wechat_pay = (RadioButton) findViewById(R.id.rb_wechat_pay);
//        rb_jcard = (RadioButton) findViewById(R.id.rb_jcard);
//        rb_alipay = (RadioButton) findViewById(R.id.rb_alipay);
//        rb_fast_pay = (RadioButton) findViewById(R.id.rb_fast_pay);
//        rb_phone_bank_pay = (RadioButton) findViewById(R.id.rb_phone_bank_pay);
//        rb_credit_card_pay = (RadioButton) findViewById(R.id.rb_credit_card_pay);
//        rb_jcard_pay = (RadioButton) findViewById(R.id.rb_jcard_pay);
//        rb_phone_card_pay = (RadioButton) findViewById(R.id.rb_phone_card_pay);
//        rb_accout_pay = (RadioButton) findViewById(R.id.rb_accout_pay);
//        rb_else_card_pay = (RadioButton) findViewById(R.id.rb_else_card_pay);
//        rb_all_card_pay = (RadioButton) findViewById(R.id.rb_all_card_pay);
//
//        mOnPayTV = (TextView) findViewById(R.id.tv_onpay);
//        mOnPayTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                initPay();
//            }
//        });
//
//        initPayWayEvents();
//        _payType = "30";
//    }
//
//    /**
//     * 支付类型初始化
//     */
//    private void initPayWayEvents() {
//        rg_pay_way.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (checkedId == rb_wechat_pay.getId()) {// 微信
//                    _payType = "30";
//                }
//                if (checkedId == rb_alipay.getId()) {// 支付宝
//                    _payType = "22";
//                }
////                if (checkedId == rb_jcard.getId()) {// 骏卡支付？？？
////                    _payType = "10";
////                }
//
//                // ------------------------------------------
//                isLogoHidden = false;
//                if (checkedId == rb_fast_pay.getId()) {
//                    _payType = "18";
//                }
//                if (checkedId == rb_phone_bank_pay.getId()) {
//                    _payType = "19";
//                }
//                if (checkedId == rb_credit_card_pay.getId()) {
//                    _payType = "17";
//                }
//                if (checkedId == rb_jcard_pay.getId()) {
//                    _payType = "10";
//                }
//                if (checkedId == rb_phone_card_pay.getId()) {
//                    _payType = "12";
//                }
//                if (checkedId == rb_accout_pay.getId()) {
//                    _payType = "2";
//                }
//                if (checkedId == rb_else_card_pay.getId()) {
//                    _payType = "40";
//                }
//                if (checkedId == rb_all_card_pay.getId()) {
//                    _payType = "0";
//                }
//            }
//        });
//    }
//
//    public void initPay() {
//        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
//        String amt = mInputRmbET.getText().toString().trim();
//        if (TextUtils.isEmpty(amt)) {
//            ToastUtil.showToast(R.string.recharge_rmb_cannot_null);
//            return;
//        }
//        float amtInt = Float.parseFloat(amt);
//        if (amtInt < MIN_RMB) {
//            ToastUtil.showToast(getResources().getString(R.string.recharge_min, MIN_RMB));
//            return;
//        }
//
//        if (amtInt > MAX_RMB) {
//            ToastUtil.showToast(getResources().getString(R.string.recharge_dbxe, MAX_RMB));
//            return;
//        }
//        if (App.IS_RECHARGE_001) {
//            amt = "0.01";
//        }
//        pairs.add(new BasicNameValuePair("pay_amt", amt));
//        // Log.v(SHOW_TAG, "pay_amt=" + amtEt.getText().toString());
//        //要注意url转码
//        String goods_name = "金豆";
//        String good_note = "金豆" + amt + "元";
//        String remark = "无";
//        try {
//            pairs.add(new BasicNameValuePair("agent_id", URLEncoder.encode("1602809", "UTF-8")));// 1751412,1602809
//            pairs.add(new BasicNameValuePair("goods_name", URLEncoder.encode(goods_name, "UTF-8")));
//            pairs.add(new BasicNameValuePair("goods_note", URLEncoder.encode(good_note, "UTF-8")));
//            pairs.add(new BasicNameValuePair("remark", URLEncoder.encode(remark, "UTF-8")));
//            pairs.add(new BasicNameValuePair("user_identity", HfbUtils.generateUserIdentity(this)));
//            pairs.add(new BasicNameValuePair("goods_num", "1"));
//            pairs.add(new BasicNameValuePair("user_ip", "127.0.0.1"));
//            // pay_type:支付类型
//            pairs.add(new BasicNameValuePair("pay_type", _payType));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        //此地址为商户初始化地址，需要商户根据文档自行实现，请勿直接使用此地址进行初始化????????????????
//        String url;
//        if (isDebug) {
//            url = "http://192.168.2.95/DemoHeepay/SDK/SDKInit.aspx";
//        } else {
//            url = "http://211.103.157.45/DemoHeepayTest/SDK/SDKInit.aspx";
//        }
//        postInitData(url, pairs);
//    }
//
//    /**
//     * 初始化接口调用
//     *
//     * @param url   初始化url
//     * @param pairs 初始化参数
//     */
//    private void postInitData(final String url, final List<NameValuePair> pairs) {
//        progressDialog = ProgressDialog.show(this, "", "初始化...", false, true, cancelListener);
//        new Thread() {
//
//            public void run() {
//                try {
//                    HttpClient client = new DefaultHttpClient();
//                    client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 60000);
//                    client.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 60000);
//
//                    HttpPost mPost = new HttpPost(url);
//                    mPost.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
//                    HttpResponse response = client.execute(mPost);
//                    HttpEntity responseEntity = null;
//                    if (response.getStatusLine().getStatusCode() == 200) {
//                        responseEntity = response.getEntity();
//                        InputStream is = responseEntity.getContent();
//                        InputStreamReader br = new InputStreamReader(is);
//                        _paymentInfo = new PaymentInfo();
//                        _paymentInfo = ParseInitReturnData(br, _paymentInfo);
//                        Message msg = Message.obtain();
//                        msg.obj = _paymentInfo;
//                        msg.what = INIT_RESULT;
//                        handler.sendMessage(msg);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            ;
//        }.start();
//    }
//
//    /**
//     * 接收支付通知结果
//     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Constant.RESULTCODE) {
//            String respCode = data.getExtras().getString("respCode");
//            String respMessage = data.getExtras().getString("respMessage");
//            if (!TextUtils.isEmpty(respCode)) {
//                // 支付结果状态（01成功/00处理中/-1 失败）
//                if ("01".equals(respCode)) {
//                    Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_SHORT).show();
//                }
//                if ("00".equals(respCode)) {
//                    Toast.makeText(getApplicationContext(), "处理中...", Toast.LENGTH_SHORT).show();
//                }
//                if ("-1".equals(respCode)) {
//                    Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_SHORT).show();
//                }
//            }
//            // 除支付宝sdk支付respMessage均为null
//            if (!TextUtils.isEmpty(respMessage)) {
//                // 同步返回的结果必须放置到服务端进行验证, 建议商户依赖异步通知
//                PayResult result = new PayResult(respMessage);
//                // 同步返回需要验证的信息
//                String resultInfo = result.getResult();
//                String resultStatus = result.getResultStatus();
//                // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
//                if (TextUtils.equals(resultStatus, "9000")) {
//                    Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
//                } else {
//                    // 判断resultStatus 为非"9000"则代表可能支付失败
//                    // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
//                    if (TextUtils.equals(resultStatus, "8000")) {
//                        Toast.makeText(this, "支付结果确认中", Toast.LENGTH_SHORT).show();
//
//                    } else {
//                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
//                        Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//            }
//        }
//    }
//
//    /**
//     * 启动汇付宝安全支付服务
//     */
//    private void startHeepayServiceJar() {
//        HeepayPlugin.pay(this, _paymentInfo.getTokenID() + "," + _paymentInfo.getAgentId() + ","
//                + _paymentInfo.getBillNo() + "," + _payType);
//    }
//
//    /**
//     * 启动汇付宝安全支付服务
//     */
//    private void startHeepayService() {
//        if (_paymentInfo == null) {
//            Toast.makeText(this, "请先进行支付初始化", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        _payInfoBundle = new Bundle();
//        _payInfoBundle.putString("tid", _paymentInfo.getTokenID());
//        _payInfoBundle.putInt("aid", Integer.parseInt(_paymentInfo.getAgentId()));
//
//        _payInfoBundle.putString("bn", _paymentInfo.getBillNo());
//        // 微信支付需要为隐藏汇付宝Logo(true)
//        _payInfoBundle.putBoolean("hidden", isLogoHidden);
//        HeepayServiceHelper _heepayHelper = new HeepayServiceHelper(HFBRechargeActivity.this);
//        _heepayHelper.pay(_payInfoBundle, handler, HeepaySDKConstant.REQUEST_PAY);
//    }
//
//    /**
//     * 解析出初始化接口所需的参数
//     *
//     * @param br
//     * @param info
//     * @return
//     * @throws Exception
//     */
//    private PaymentInfo ParseInitReturnData(InputStreamReader br, PaymentInfo info) throws Exception {
//        XmlPullParser xmlParser = Xml.newPullParser();
//        xmlParser.setInput(br);
//        // 获得解析到的事件类别，这里有开始文档，结束文档，开始标签，结束标签，文本等等事件。
//        int evtType = xmlParser.getEventType();
//        StringBuilder sb = new StringBuilder();
//        while (evtType != XmlPullParser.END_DOCUMENT) {
//            if (evtType == XmlPullParser.START_TAG) {
//                String tag = xmlParser.getName();
//                sb.append("<" + tag + ">");
//                String strTextValue = "";
//                if (tag.equals("HasError")) {
//                    strTextValue = xmlParser.nextText();
//                    info.setHasError(!strTextValue.equalsIgnoreCase("false"));
//                } else if (tag.equals("Message")) {
//                    strTextValue = xmlParser.nextText();
//                    // Log.v(SHOW_TAG, "Message:" + strTextValue);
//                    info.setMessage(strTextValue);
//                } else if (tag.equals("TokenID")) {
//                    strTextValue = xmlParser.nextText();
//                    info.setTokenID(strTextValue);
//                    // Log.v(SHOW_TAG, "TokenID:" + info.getTokenID());
//                } else if (tag.equals("AgentID")) {
//                    strTextValue = xmlParser.nextText();
//                    info.setAgentId(strTextValue);
//                    // Log.v(SHOW_TAG, "AgentId:" + info.getAgentId());
//                } else if (tag.equals("AgentBillID")) {
//                    strTextValue = xmlParser.nextText();
//                    _agentBillNo = strTextValue;
//                    info.setBillNo(strTextValue);
//                    // Log.v(SHOW_TAG, "AgentBillID:" + info.getBillNo());
//                }
//                sb.append(strTextValue);
//
//            } else if (evtType == XmlPullParser.END_TAG) {
//                String tag = xmlParser.getName();
//                sb.append("<" + tag + "/>");
//
//            }
//            // 如果xml没有结束，则导航到下一个river节点
//            evtType = xmlParser.next();
//        }
//        System.out.println(sb.toString());
//        return info;
//    }

    /**
     * Dialog监听器
     */
    protected DialogInterface.OnCancelListener cancelListener = new DialogInterface.OnCancelListener() {
        public void onCancel(DialogInterface dlg) {
            dlg.dismiss();
        }
    };

}

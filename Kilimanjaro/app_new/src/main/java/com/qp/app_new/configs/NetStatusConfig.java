package com.qp.app_new.configs;

/**
 * Created by Aaron on 2017/11/8.
 */

public class NetStatusConfig {
    // ******访问网络相关STATUS
    public static final int STATUS_POST_SUCCESS = 200;// 访问网络成功
    public static final int STATUS_POST_FAIL = 400;// 访问网络失败
    public static final int STATUS_TOKEN_IS_UPDATED = 403;// 登录状态被修改
    public static final int STATUS_DATA_WRONG = 100;// 数据解析错误
    public static final int STATUS_NET_ERROR = 404;// 网络连接出错
    public static final int STATUS_HAVE_NO_DATA = 405;// 没有数据
}

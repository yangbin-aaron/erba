package com.qp.app_new.configs;

/**
 * Created by yangbin on 16/7/4.
 * 消息机制 常量管理类
 */
public class HandlerConfig {

    // ******访问网络相关what
    public static final int WHAT_POST_SUCCESS = 200;// 访问网络成功
    public static final int WHAT_POST_FAIL = 400;// 访问网络失败
    public static final int WHAT_TOKEN_IS_UPDATED = 403;// 登录状态被修改
    public static final int WHAT_DATA_WRONG = 100;// 数据解析错误
    public static final int WHAT_NET_ERROR = 404;// 网络连接出错
}

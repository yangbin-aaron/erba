package com.qp.app_new;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by Aaron on 17/11/7.
 */

public class App extends Application {// 签名密码 123456p
    public static Context mContext;
    public static final boolean IS_DEV = false;// 是否是测试环境
    public boolean haveNewVersion = false;
    public static JSONObject currentGameJsonObject;// 当前进入的游戏
    public static boolean openDandianActivity = true;// 是否开启自定义下注的功能

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        initUmeng(this);
        initImageLoad(this);
    }

    private void initUmeng(Context context) {
        MobclickAgent.setScenarioType(context, MobclickAgent.EScenarioType.E_UM_NORMAL);
        UMConfigure.init(context, UMConfigure.DEVICE_TYPE_PHONE, "");// 第三个参数为 推送时使用
    }

    private static void initImageLoad(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "TEImage/Cache");
        initImageLoader(context, cacheDir);
        AppPrefs.getInstance().saveTokenState(true);// 默认能够访问网络
    }

    /**
     * 初始化ImageLoader
     *
     * @param context
     */
    private static void initImageLoader(Context context, File cacheDir) {
        // 初始化ImageLoader
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.mipmap.ic_launcher)
                // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.ic_launcher) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.ic_launcher) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
//				.displayer(new FadeInBitmapDisplayer(500))
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                .imageScaleType(ImageScaleType.EXACTLY)
                // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).
                defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .defaultDisplayImageOptions(options)
                .discCacheFileCount(100)// 缓存文件数量
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCache(new UnlimitedDiskCache(cacheDir)) // 自定义缓存路径
                .threadPoolSize(3)
                .build();
        ImageLoader.getInstance().init(config);
    }
}

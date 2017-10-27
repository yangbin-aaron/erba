package com.teb.kilimanjaro;

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
import com.wo.main.WP_App;

import java.io.File;

/**
 * Created by yangbin on 16/7/1.
 */
public class App extends Application {

    private static Context mContext;

    public static Context getAppContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        initImageLoad(mContext);
        AppPrefs.getInstance().saveTokenState(true);// 默认能够访问网络
        AppPrefs.getInstance().saveBetAutoStartState(false);// 默认服务未开启

        // 实例化支付SDK
        WP_App.on_AppInit(getApplicationContext());

        // 友盟
//        Log.e("",);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);// 普通统计场景类型
//        MobclickAgent.setSessionContinueMillis(30 * 1000);//当应用在后台运行超过30秒（默认）再回到前端，将被认为是两个独立的session(启动)，例如用户回到home，或进入其他程序，经过一段时间后再返回之前的应用。
        MobclickAgent.enableEncrypt(true);// 日志加密,加密模式可以有效防止网络攻击，提高数据安全性。
        MobclickAgent.setDebugMode(true);
    }


    public static void initImageLoad(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "TEImage/Cache");
        initImageLoader(context, cacheDir);
    }

    /**
     * 初始化ImageLoader
     *
     * @param context
     */
    public static void initImageLoader(Context context, File cacheDir) {
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

    public boolean haveNewVersion = false;

    public static final boolean IS_DEV = true;// 是否是测试环境
    public static final boolean IS_RECHARGE_001 = false;// 是否只支付1分钱
}

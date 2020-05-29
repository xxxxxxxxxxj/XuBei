package com.xubei.shop;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.os.Environment;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.sdk.android.httpdns.HttpDns;
import com.alibaba.sdk.android.httpdns.HttpDnsService;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.mob.MobSDK;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.beta.Beta;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.xubei.shop.data.db.help.UserBeanHelp;
import com.xubei.shop.di.DaggerAppComponent;
import com.xubei.shop.ui.module.main.MainActivity;
import com.xubei.shop.ui.module.setting.SettingActivity;
import com.xubei.shop.ui.views.LottieHeader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import javax.inject.Inject;

import androidx.multidex.MultiDexApplication;
import cn.jpush.android.api.JPushInterface;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasBroadcastReceiverInjector;

/**
 * app初始化
 * date：2017/2/14 18:03
 * author：Seraph
 **/
public class AppApplication extends MultiDexApplication implements HasActivityInjector, HasBroadcastReceiverInjector {


    static {
        //下拉刷新头部动画
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> new LottieHeader(context).setAnimationViewJson("dino_dance.json"));
    }


    @Inject
    DispatchingAndroidInjector<Activity> dispatchingActivityInjector;

    @Inject
    DispatchingAndroidInjector<BroadcastReceiver> dispatchingBroadcastReceiverInjector;


    @Inject
    UserBeanHelp userBeanHelp;

    @Override
    public void onCreate() {
        super.onCreate();
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);

        //初始化dagger2
        DaggerAppComponent.builder().application(this).build().inject(this);
        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        if (AppConfig.DEBUG) {
            // 打印日志
            ARouter.openLog();
            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug();
        }
        ARouter.init(this);
        //初始化其它第三方工具
        initOther();
        //注册activity回调
        registerActivityLifecycleCallbacks(new AppActivityCallbacks());
    }

    private void initOther() {
        //初始化dns
        initDNS();
        //工具
        Utils.init(this);
        ToastUtils.setBgColor(0xFE000000);
        ToastUtils.setMsgColor(0xFFFFFFFF);
        //友盟统计
        //上下文，AppKey，渠道，
        UMConfigure.init(this, AppConfig.UM_APP_KEY, AppConfig.getChannelValue(), UMConfigure.DEVICE_TYPE_PHONE, null);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        //sharesdk分享
        MobSDK.init(this);
        //bugly升级
        appUpData();
        //初始化极光推送
        initJPush();
    }

    //初始化极光
    private void initJPush() {
        JPushInterface.init(this);
        //设置渠道
        JPushInterface.setChannel(this, AppConfig.getChannelValue());
        //读取是否打开推送消息
        //  SPUtils spUtils = SPUtils.getInstance(AppConstants.SPAction.SP_NAME);
        //  boolean isOpen = spUtils.getBoolean(AppConstants.SPAction.IS_OPEN_PUSH, true);
//        if (!isOpen && !JPushInterface.isPushStopped(this)) {
//            //如果关闭推送并且推送没有停止，则停止
//            JPushInterface.stopPush(this);
//        } else if (isOpen && JPushInterface.isPushStopped(this)) {
//            //如果推送打开并且推送关闭，则重新打开推送
//            JPushInterface.resumePush(this);
//        }
        if (JPushInterface.isPushStopped(this)) {
            //如果推送关闭，则重新打开推送
            JPushInterface.resumePush(this);
        }
        //设置别名（如果有用户登录）
        addAndDeleteJPushAlias();
    }

    /**
     * 设置推送别名和标签(有用户就绑定，没有用户就删除绑定)
     */
    public void addAndDeleteJPushAlias() {
        if (userBeanHelp.isLogin(false)) {
            String alias = userBeanHelp.getUserBean().getUserid();
            if (TextUtils.isEmpty(alias)) {
                return;
            }
            //设置别名
            JPushInterface.setAlias(this, 1, AppConfig.JPUSH_ALIAS + alias);
            //设置标签
            HashSet<String> tagSet = new HashSet<>();
            tagSet.add(AppConfig.JPUSH_TAG_LOGIN);//所有用户标签
            JPushInterface.setTags(this, 2, tagSet);
        } else {
            JPushInterface.deleteAlias(this, 3);
            JPushInterface.cleanTags(this, 4);
        }
    }

    //预先dns解析
    private void initDNS() {
        // 初始化httpdns
        HttpDnsService httpdns = HttpDns.getService(this, AppConfig.ACCOUNT_ID);
        ArrayList<String> hostList = new ArrayList<>(Arrays.asList(
                "api8.xubei.com",
                "user-server.xubei.com",
                "passport-server.xubei.com",
                "good-api.xubei.com",
                "rest-api.xubei.com",
                "pc-client.xubei.com"
        ));
        httpdns.setPreResolveHosts(hostList);
        // 允许过期IP以实现懒加载策略
        httpdns.setExpiredIPEnabled(true);
    }


    //app的升级与bug收集配置
    private void appUpData() {
        //初始化升级模块
        Beta.autoInit = true;
        //初始化自动升级检查
        Beta.autoCheckUpgrade = true;
        //设置升级检查周期（默认检查周期为0s），5秒内SDK不重复向后台请求策略
        Beta.upgradeCheckPeriod = 5 * 1000;
        //设置启动延时为1s（默认延时3秒），APP启动1s后初始化SDK，避免影响APP的启动速度
        Beta.initDelay = 2 * 1000;
        //设置通知栏大图标，项目中的图片资源id
        Beta.defaultBannerId = R.mipmap.ic_action_name;
        // 设置状态栏小图标，smallIconId为项目中的图片资源Id;
        Beta.smallIconId = R.mipmap.ic_action_name;
        //是否显示包信息
        Beta.canShowApkInfo = true;
        //设置是否显示消息通知
        Beta.enableNotification = true;
        //是否开启热更新功能
        Beta.enableHotfix = false;

        //设置更新弹窗默认展示的banner，defaultBannerId为项目中的图片资源Id;
        //当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading“;

        Beta.defaultBannerId = R.mipmap.ic_action_name;

        //设置sd卡的Download为更新资源保存目录;后续更新资源会保存在此目录，
        // 需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        //点击过确认的弹窗在APP下次启动自动检查更新时会再次显示;
        Beta.showInterruptedStrategy = true;

        //只允许在xxxActivity上显示更新弹窗，其他activity上不显示弹窗;
        //不设置会默认所有activity都可以显示弹窗;
        Beta.canShowUpgradeActs.add(MainActivity.class);
        Beta.canShowUpgradeActs.add(SettingActivity.class);
        //自定义升级对话框布局
        Beta.upgradeDialogLayoutId = R.layout.act_upgrade_dialog;

        BuglyStrategy buglyStrategy = new BuglyStrategy();
        //设置渠道号
        buglyStrategy.setAppChannel(AppConfig.getChannelValue());
        //统一初始化Bugly产品，包含Beta  c475f0a560
        Bugly.init(getApplicationContext(), AppConfig.BUGLY_ID, false, buglyStrategy);
    }


    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingActivityInjector;
    }


    @Override
    public AndroidInjector<BroadcastReceiver> broadcastReceiverInjector() {
        return dispatchingBroadcastReceiverInjector;
    }
}

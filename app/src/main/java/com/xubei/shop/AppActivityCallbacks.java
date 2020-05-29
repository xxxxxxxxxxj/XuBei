package com.xubei.shop;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.xubei.shop.ui.module.base.ABaseActivity;
import com.xubei.shop.ui.module.login.LoginActivity;
import com.xubei.shop.ui.module.login.LoginTypeSelectActivity;
import com.xubei.shop.ui.module.login.PhoneBindActivity;
import com.xubei.shop.ui.module.login.RegisteredActivity;
import com.xubei.shop.ui.module.main.MainActivity;
import com.xubei.shop.ui.module.welcome.WelcomeActivity;
import com.hwangjr.rxbus.RxBus;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;
import com.umeng.analytics.MobclickAgent;

/**
 * App中每个Activity的回调,处理部分公共的问题
 * 在{@link AppApplication#onCreate()}中注册。
 * 注意: ActivityLifecycleCallbacks 中所有方法的调用时机都是在 Activity 对应生命周期的 Super 方法中进行的。
 * date：2017/7/31 11:51
 * author：Seraph
 **/
public class AppActivityCallbacks implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(final Activity activity, Bundle savedInstanceState) {
        View rootView = activity.findViewById(android.R.id.content);
        //设置通用背景颜色
        //如果是欢迎页则不设置
        if (!(activity instanceof WelcomeActivity) && !(activity instanceof com.tencent.bugly.beta.ui.BetaActivity)) {
            rootView.setBackgroundResource(R.color.act_bg_color);
        }
        //设置字体，需要在设置布局之后
        //FontUtils.injectFont(rootView);
        //注册事件总线
        RxBus.get().register(activity);
        //todo 可以进行其他公共设置（例如布局，切换动画。）...
        //例如：如果有toolbar。则初始化部分公共设置，如果部分界面不需要，自己进行清除
        View appbar = activity.findViewById(R.id.appbar);
        if (appbar != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appbar.setOutlineProvider(null);
        }
        View view = activity.findViewById(R.id.toolbar);
        if (view instanceof Toolbar && activity instanceof ABaseActivity) {
            Toolbar toolbar = ((Toolbar) view);
            ((ABaseActivity) activity).setSupportActionBar(toolbar);
            //主页特殊处理。不需要进行返回键的操作
            if (activity instanceof MainActivity) {
                return;
            }
            //符合条件的布局设置统一的返回按键和监听
            if (activity instanceof LoginTypeSelectActivity || activity instanceof LoginActivity || activity instanceof RegisteredActivity || activity instanceof PhoneBindActivity) {
                toolbar.setNavigationIcon(R.mipmap.common_icon_close);
            } else {
                toolbar.setNavigationIcon(R.mipmap.common_title_back);
            }
            RxToolbar.navigationClicks(toolbar).subscribe(o -> activity.onBackPressed());
        }
        //如果是进入登录界面，则使用从下叠加到页面上的动画
        if (activity instanceof LoginTypeSelectActivity || activity instanceof LoginActivity) {
            activity.overridePendingTransition(R.anim.anim_slide_in_from_bottom, R.anim.anim_null);
        }
    }


    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        //友盟统计
        MobclickAgent.onResume(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        //友盟统计
        MobclickAgent.onPause(activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        RxBus.get().unregister(activity);
    }


}

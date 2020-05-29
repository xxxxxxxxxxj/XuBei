package com.xubei.shop.di.module.activity;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;

import com.xubei.shop.di.scoped.ActivityScoped;
import com.xubei.shop.ui.module.order.OrderSuccessActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * 订单支付成功
 * date：2017/5/5 15:18
 * author：Seraph
 *
 **/
@Module(includes = ActivityModule.class)
public abstract class OrderSuccessModule {

    @ActivityScoped
    @Binds
    abstract Activity bindActivity(OrderSuccessActivity activity);

    @Provides
    @ActivityScoped
    static String provideOrderId(Activity activity) {
        return activity.getIntent().getStringExtra("orderNo");
    }

    @Provides
    @ActivityScoped
    static ClipboardManager provideClipboardManager(Activity activity) {
        return (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
    }

}

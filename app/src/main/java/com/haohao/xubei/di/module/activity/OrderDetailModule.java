package com.haohao.xubei.di.module.activity;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;

import com.haohao.xubei.di.scoped.ActivityScoped;
import com.haohao.xubei.ui.module.order.OrderDetailActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * 订单详情
 * date：2017/5/5 15:18
 * author：Seraph
 *
 **/
@Module(includes = ActivityModule.class)
public abstract class OrderDetailModule {

    @ActivityScoped
    @Binds
    abstract Activity bindActivity(OrderDetailActivity activity);

    @Provides
    @ActivityScoped
    static String provideStringOrderNo(Activity activity) {
        return activity.getIntent().getStringExtra("orderNo");
    }

    @Provides
    @ActivityScoped
    static ClipboardManager provideClipboardManager(Activity activity) {
        return (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
    }

}

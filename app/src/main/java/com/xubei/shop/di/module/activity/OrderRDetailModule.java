package com.xubei.shop.di.module.activity;

import android.app.Activity;

import com.xubei.shop.di.scoped.ActivityScoped;
import com.xubei.shop.ui.module.order.OrderRDetailActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * 发布订单详情
 * date：2017/5/5 15:18
 * author：Seraph
 *
 **/
@Module(includes = ActivityModule.class)
public abstract class OrderRDetailModule {

    @ActivityScoped
    @Binds
    abstract Activity bindActivity(OrderRDetailActivity activity);

    @Provides
    @ActivityScoped
    static String provideStringOrderNo(Activity activity) {
        return activity.getIntent().getStringExtra("orderNo");
    }


}
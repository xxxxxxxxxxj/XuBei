package com.xubei.shop.di.module.activity;

import android.app.Activity;

import com.xubei.shop.di.scoped.ActivityScoped;
import com.xubei.shop.ui.module.order.OrderSearchActivity;

import dagger.Binds;
import dagger.Module;

/**
 * 订单搜索界面
 * date：2017/5/5 15:18
 * author：Seraph
 *
 **/
@Module(includes = ActivityModule.class)
public abstract class OrderSearchModule {

    @ActivityScoped
    @Binds
    abstract Activity bindActivity(OrderSearchActivity activity);


}
package com.haohao.xubei.di.module.activity;

import android.app.Activity;
import android.view.LayoutInflater;

import com.haohao.xubei.di.scoped.ActivityScoped;
import com.haohao.xubei.ui.module.account.model.OutGoodsDetailBean;
import com.haohao.xubei.ui.module.order.OrderCreateActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * 创建订单和支付订单
 * date：2017/5/5 15:18
 * author：Seraph
 *
 **/
@Module(includes = ActivityModule.class)
public abstract class OrderCreateModule {

    @ActivityScoped
    @Binds
    abstract Activity bindActivity(OrderCreateActivity activity);

    @Provides
    @ActivityScoped
    static OutGoodsDetailBean provideOutGoodsDetailBean(Activity activity) {
        return (OutGoodsDetailBean) activity.getIntent().getSerializableExtra("bean");
    }

    @Provides
    @ActivityScoped
    static LayoutInflater provideLayoutInflater(Activity activity) {
        return LayoutInflater.from(activity);
    }

}

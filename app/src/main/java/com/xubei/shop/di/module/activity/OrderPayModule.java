package com.xubei.shop.di.module.activity;

import android.app.Activity;
import android.view.LayoutInflater;

import com.xubei.shop.di.scoped.ActivityScoped;
import com.xubei.shop.ui.module.order.OrderPayActivity;
import com.xubei.shop.ui.module.order.model.OutOrderBean;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * 订单选择支付
 * date：2017/5/5 15:18
 * author：Seraph
 **/
@Module(includes = ActivityModule.class)
public abstract class OrderPayModule {

    @ActivityScoped
    @Binds
    abstract Activity bindActivity(OrderPayActivity activity);

    @Provides
    @ActivityScoped
    static OutOrderBean provideOrderBean(Activity activity) {
        return (OutOrderBean) activity.getIntent().getSerializableExtra("orderBean");
    }

    @Provides
    @ActivityScoped
    static LayoutInflater provideLayoutInflater(Activity activity) {
        return LayoutInflater.from(activity);
    }

}

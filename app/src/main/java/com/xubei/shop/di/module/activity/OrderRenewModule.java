package com.xubei.shop.di.module.activity;

import android.app.Activity;
import android.view.LayoutInflater;

import com.xubei.shop.di.scoped.ActivityScoped;
import com.xubei.shop.ui.module.order.OrderRenewActivity;
import com.xubei.shop.ui.module.order.model.OutOrderBean;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * 订单续租界面
 * date：2017/5/5 15:18
 * author：Seraph
 *
 **/
@Module(includes = ActivityModule.class)
public abstract class OrderRenewModule {

    @ActivityScoped
    @Binds
    abstract Activity bindActivity(OrderRenewActivity activity);

    @Provides
    @ActivityScoped
    static OutOrderBean provideOutOrderBean(Activity activity) {
        return (OutOrderBean) activity.getIntent().getSerializableExtra("bean");
    }

    @Provides
    @ActivityScoped
    static LayoutInflater provideLayoutInflater(Activity activity) {
        return LayoutInflater.from(activity);
    }

}

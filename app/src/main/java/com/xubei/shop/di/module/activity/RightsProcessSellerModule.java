package com.xubei.shop.di.module.activity;

import android.app.Activity;

import com.xubei.shop.di.scoped.ActivityScoped;
import com.xubei.shop.ui.module.rights.RightsProcessSellerActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * 维权处理
 * date：2017/5/5 15:18
 * author：Seraph
 *
 **/
@Module(includes = ActivityModule.class)
public abstract class RightsProcessSellerModule {

    @Binds
    @ActivityScoped
    abstract Activity bindActivity(RightsProcessSellerActivity activity);


    @Provides
    @ActivityScoped
    static String provideStrOrderNo(Activity activity) {
        return activity.getIntent().getStringExtra("orderNo");
    }
}

package com.xubei.shop.di.module.activity;

import android.app.Activity;

import com.xubei.shop.di.scoped.ActivityScoped;
import com.xubei.shop.ui.module.rights.RightsDetailSellerActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * 维权详情
 * date：2017/5/5 15:18
 * author：Seraph
 *
 **/
@Module(includes = ActivityModule.class)
public abstract class RightsDetailSellerModule {

    @Binds
    @ActivityScoped
    abstract Activity bindActivity(RightsDetailSellerActivity activity);


    @Provides
    @ActivityScoped
    static String provideStrOrderNo(Activity activity) {
        return activity.getIntent().getStringExtra("orderNo");
    }


}

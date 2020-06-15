package com.haohao.xubei.di.module.activity;

import android.app.Activity;

import com.haohao.xubei.di.scoped.ActivityScoped;
import com.haohao.xubei.ui.module.rights.RightsApplySellerActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * 申请维权
 * date：2017/5/5 15:18
 * author：Seraph
 *
 **/
@Module(includes = ActivityModule.class)
public abstract class RightsApplySellerModule {

    @Binds
    @ActivityScoped
    abstract Activity bindActivity(RightsApplySellerActivity activity);

    @Provides
    @ActivityScoped
    static String provideStrOrderNo(Activity activity) {
        return activity.getIntent().getStringExtra("orderNo");
    }

}

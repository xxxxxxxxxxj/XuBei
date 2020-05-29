package com.xubei.shop.di.module.activity;

import android.app.Activity;

import com.xubei.shop.di.QualifierType;
import com.xubei.shop.di.scoped.ActivityScoped;
import com.xubei.shop.ui.module.account.AccLeaseAllTypeActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * 账号租赁类型
 * date：2017/5/5 15:18
 * author：Seraph
 **/
@Module(includes = ActivityModule.class)
public abstract class AccLeaseAllTypeModule {

    @Binds
    @ActivityScoped
    abstract Activity bindActivity(AccLeaseAllTypeActivity activity);

    @QualifierType("type")
    @Provides
    @ActivityScoped
    static Integer provideStrType(Activity activity) {
        return activity.getIntent().getIntExtra("type", 0);
    }

}

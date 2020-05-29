package com.xubei.shop.di.module.activity;

import android.app.Activity;
import android.view.LayoutInflater;

import com.xubei.shop.di.scoped.ActivityScoped;
import com.xubei.shop.ui.module.account.AccREditActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * 账号发布编辑
 * date：2017/5/5 15:18
 * author：Seraph
 *
 **/
@Module(includes = ActivityModule.class)
public abstract class AccREditModule {

    @ActivityScoped
    @Binds
    abstract Activity bindActivity(AccREditActivity activity);


    @Provides
    @ActivityScoped
    static String provideStringId(Activity activity) {
        return activity.getIntent().getStringExtra("id");

    }

    @Provides
    @ActivityScoped
    static LayoutInflater provideLayoutInflater(Activity activity) {
        return LayoutInflater.from(activity);
    }

}

package com.xubei.shop.di.module.activity;

import android.app.Activity;

import com.xubei.shop.di.scoped.ActivityScoped;
import com.xubei.shop.ui.module.account.AccRSuccessActivity;

import dagger.Binds;
import dagger.Module;

/**
 * 账号发布完成
 * date：2017/5/5 15:18
 * author：Seraph
 *
 **/
@Module(includes = ActivityModule.class)
public abstract class AccRSuccessModule {

    @ActivityScoped
    @Binds
    abstract Activity bindActivity(AccRSuccessActivity activity);


}

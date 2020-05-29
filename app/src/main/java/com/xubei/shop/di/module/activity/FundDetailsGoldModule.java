package com.xubei.shop.di.module.activity;

import android.app.Activity;

import com.xubei.shop.di.scoped.ActivityScoped;
import com.xubei.shop.ui.module.user.FundDetailsGoldActivity;

import dagger.Binds;
import dagger.Module;

/**
 * date：2017/5/5 15:18
 * author：Seraph
 **/
@Module(includes = ActivityModule.class)
public abstract class FundDetailsGoldModule {

    @Binds
    @ActivityScoped
    abstract Activity bindActivity(FundDetailsGoldActivity activity);


}

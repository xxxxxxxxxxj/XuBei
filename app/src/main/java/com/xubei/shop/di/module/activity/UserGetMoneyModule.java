package com.xubei.shop.di.module.activity;

import android.app.Activity;

import com.xubei.shop.di.scoped.ActivityScoped;
import com.xubei.shop.ui.module.user.UserGetMoneyActivity;

import dagger.Binds;
import dagger.Module;

/**
 * 提现
 * date：2017/5/5 15:18
 * author：Seraph
 *
 **/
@Module(includes = ActivityModule.class)
public abstract class UserGetMoneyModule {

    @ActivityScoped
    @Binds
    abstract Activity bindActivity(UserGetMoneyActivity activity);

}

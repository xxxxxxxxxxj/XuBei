package com.xubei.shop.di.module.activity;

import android.app.Activity;

import com.xubei.shop.di.scoped.ActivityScoped;
import com.xubei.shop.ui.module.login.ResetPayPwActivity;

import dagger.Binds;
import dagger.Module;

/**
 * 重置支付密码
 * date：2017/5/5 15:18
 * author：Seraph
 *
 **/
@Module(includes = ActivityModule.class)
public abstract class ResetPayPwModule {

    @ActivityScoped
    @Binds
    abstract Activity bindActivity(ResetPayPwActivity activity);


}

package com.haohao.xubei.di.module.activity;

import android.app.Activity;

import com.haohao.xubei.di.scoped.ActivityScoped;
import com.haohao.xubei.ui.module.login.ResetPayPwActivity;

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

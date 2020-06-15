package com.haohao.xubei.di.module.activity;

import android.app.Activity;

import com.haohao.xubei.di.scoped.ActivityScoped;
import com.haohao.xubei.ui.module.account.AccRSuccessActivity;

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

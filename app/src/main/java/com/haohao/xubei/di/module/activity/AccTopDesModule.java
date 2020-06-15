package com.haohao.xubei.di.module.activity;

import android.app.Activity;

import com.haohao.xubei.di.scoped.ActivityScoped;
import com.haohao.xubei.ui.module.account.AccTopDesActivity;

import dagger.Binds;
import dagger.Module;

/**
 * 置顶说明
 * date：2017/5/5 15:18
 * author：Seraph
 **/
@Module(includes = ActivityModule.class)
public abstract class AccTopDesModule {

    @Binds
    @ActivityScoped
    abstract Activity bindActivity(AccTopDesActivity activity);


}

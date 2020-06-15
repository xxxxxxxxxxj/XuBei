package com.haohao.xubei.di.module.activity;

import android.app.Activity;

import com.haohao.xubei.di.scoped.ActivityScoped;
import com.haohao.xubei.ui.module.login.RegisteredActivity;

import dagger.Binds;
import dagger.Module;

/**
 * 注册模型
 * date：2017/5/5 15:18
 * author：Seraph
 **/
@Module(includes = ActivityModule.class)
public abstract class RegisteredModule {

    @ActivityScoped
    @Binds
    abstract Activity bindActivity(RegisteredActivity activity);


}

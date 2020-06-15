package com.haohao.xubei.di.module.activity;

import android.app.Activity;

import com.haohao.xubei.di.scoped.ActivityScoped;
import com.haohao.xubei.ui.module.user.UserInfoActivity;

import dagger.Binds;
import dagger.Module;

/**
 * 个人中心
 * date：2017/5/5 15:18
 * author：Seraph
 *
 **/
@Module(includes = ActivityModule.class)
public abstract class UserInfoModule {

    @ActivityScoped
    @Binds
    abstract Activity bindActivity(UserInfoActivity activity);
}

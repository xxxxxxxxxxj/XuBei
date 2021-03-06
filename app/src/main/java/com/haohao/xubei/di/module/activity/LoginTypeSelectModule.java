package com.haohao.xubei.di.module.activity;

import android.app.Activity;

import com.haohao.xubei.di.scoped.ActivityScoped;
import com.haohao.xubei.ui.module.login.LoginTypeSelectActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * 登录选择模型
 * date：2017/5/5 15:18
 * author：Seraph
 *
 **/
@Module(includes = ActivityModule.class)
public abstract class LoginTypeSelectModule {


    @ActivityScoped
    @Binds
    abstract Activity bindActivity(LoginTypeSelectActivity activity);

    @Provides
    @ActivityScoped
    static Boolean provideStrIsCleanUser(Activity activity) {
        return activity.getIntent().getBooleanExtra("isCleanUser", false);
    }
}

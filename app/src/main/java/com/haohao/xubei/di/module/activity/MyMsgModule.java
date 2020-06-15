package com.haohao.xubei.di.module.activity;

import android.app.Activity;

import com.haohao.xubei.di.scoped.ActivityScoped;
import com.haohao.xubei.ui.module.user.MyMsgActivity;

import dagger.Binds;
import dagger.Module;

/**
 * 我的消息
 * date：2017/5/5 15:18
 * author：Seraph
 **/
@Module(includes = ActivityModule.class)
public abstract class MyMsgModule {

    @Binds
    @ActivityScoped
    abstract Activity bindActivity(MyMsgActivity activity);


}

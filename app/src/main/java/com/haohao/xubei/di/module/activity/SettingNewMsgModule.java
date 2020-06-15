package com.haohao.xubei.di.module.activity;

import android.app.Activity;

import com.haohao.xubei.di.scoped.ActivityScoped;
import com.haohao.xubei.ui.module.setting.SettingNewMsgActivity;

import dagger.Binds;
import dagger.Module;

/**
 * 设置新消息
 * date：2017/5/5 15:18
 * author：Seraph
 **/
@Module(includes = ActivityModule.class)
public abstract class SettingNewMsgModule {

    @Binds
    @ActivityScoped
    abstract Activity bindActivity(SettingNewMsgActivity activity);


}

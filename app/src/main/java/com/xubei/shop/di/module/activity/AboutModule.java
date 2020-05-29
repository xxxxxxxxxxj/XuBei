package com.xubei.shop.di.module.activity;

import android.app.Activity;

import com.xubei.shop.di.scoped.ActivityScoped;
import com.xubei.shop.ui.module.setting.AboutActivity;

import dagger.Binds;
import dagger.Module;

/**
 * 关于
 * date：2017/5/5 15:18
 * author：Seraph
 **/
@Module(includes = ActivityModule.class)
public abstract class AboutModule {

    @Binds
    @ActivityScoped
    abstract Activity bindActivity(AboutActivity activity);


}

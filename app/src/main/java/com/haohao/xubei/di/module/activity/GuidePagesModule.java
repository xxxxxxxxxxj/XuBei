package com.haohao.xubei.di.module.activity;

import android.app.Activity;

import com.haohao.xubei.di.scoped.ActivityScoped;
import com.haohao.xubei.ui.module.welcome.GuidePagesActivity;

import dagger.Binds;
import dagger.Module;

/**
 * 引导页
 * date：2017/5/5 15:18
 * author：Seraph
 *
 **/
@Module(includes = ActivityModule.class)
public abstract class GuidePagesModule {

    @ActivityScoped
    @Binds
    abstract Activity bindContext(GuidePagesActivity activity);

}

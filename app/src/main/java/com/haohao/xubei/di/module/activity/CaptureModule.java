package com.haohao.xubei.di.module.activity;

import android.app.Activity;

import com.haohao.xubei.di.scoped.ActivityScoped;
import com.haohao.xubei.ui.module.common.scan.CaptureActivity;

import dagger.Binds;
import dagger.Module;

/**
 * 扫一扫
 * date：2017/5/5 15:18
 * author：Seraph
 *
 **/
@Module(includes = ActivityModule.class)
public abstract class CaptureModule {

    @ActivityScoped
    @Binds
    abstract Activity bindActivity(CaptureActivity activity);


}

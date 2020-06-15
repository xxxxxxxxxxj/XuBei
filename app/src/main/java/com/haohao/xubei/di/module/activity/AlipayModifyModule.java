package com.haohao.xubei.di.module.activity;

import android.app.Activity;

import com.haohao.xubei.di.scoped.ActivityScoped;
import com.haohao.xubei.ui.module.user.AlipayModifyActivity;

import dagger.Binds;
import dagger.Module;

/**
 * 修改支付宝界面
 * date：2017/5/5 15:18
 * author：Seraph
 *
 **/
@Module(includes = ActivityModule.class)
public abstract class AlipayModifyModule {

    @ActivityScoped
    @Binds
    abstract Activity bindActivity(AlipayModifyActivity activity);
}

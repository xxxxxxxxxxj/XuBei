package com.haohao.xubei.di.module.activity;

import android.app.Activity;

import com.haohao.xubei.di.scoped.ActivityScoped;
import com.haohao.xubei.ui.module.account.AccAreaSelectActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * 账号大区选择
 * date：2017/5/5 15:18
 * author：Seraph
 **/
@Module(includes = ActivityModule.class)
public abstract class AccAreaSelectModule {

    @ActivityScoped
    @Binds
    abstract Activity bindActivity(AccAreaSelectActivity activity);



    @Provides
    @ActivityScoped
    static String provideStringId(Activity activity) {
        return activity.getIntent().getStringExtra("id");
    }

}

package com.xubei.shop.di.module.activity;

import android.app.Activity;

import com.xubei.shop.di.QualifierType;
import com.xubei.shop.di.scoped.ActivityScoped;
import com.xubei.shop.ui.module.common.web.CommonAgentWebActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * 通用web界面
 * date：2017/5/5 15:18
 * author：Seraph
 *
 **/
@Module(includes = ActivityModule.class)
public abstract class CommonAgentWebModule {

    @Binds
    @ActivityScoped
    abstract Activity bindActivity(CommonAgentWebActivity activity);


    @QualifierType("webUrl")
    @Provides
    @ActivityScoped
    static String provideStrWebContent(Activity activity){
        return activity.getIntent().getStringExtra("webUrl");
    }

    @QualifierType("title")
    @Provides
    @ActivityScoped
    static String provideStrTitle(Activity activity){
        return activity.getIntent().getStringExtra("title");
    }
}

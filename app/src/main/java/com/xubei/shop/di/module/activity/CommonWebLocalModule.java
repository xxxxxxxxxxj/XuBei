package com.xubei.shop.di.module.activity;

import android.app.Activity;

import com.xubei.shop.di.QualifierType;
import com.xubei.shop.di.scoped.ActivityScoped;
import com.xubei.shop.ui.module.common.web.CommonWebLocalActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * 通用web界面(显示本地Str)
 * date：2017/5/5 15:18
 * author：Seraph
 **/
@Module(includes = ActivityModule.class)
public abstract class CommonWebLocalModule {

    @Binds
    @ActivityScoped
    abstract Activity bindActivity(CommonWebLocalActivity activity);


    @QualifierType("webContent")
    @Provides
    @ActivityScoped
    static String provideStrWebContent(Activity activity){
        return activity.getIntent().getStringExtra("webContent");
    }

    @QualifierType("title")
    @Provides
    @ActivityScoped
    static String provideStrTitle(Activity activity){
        return activity.getIntent().getStringExtra("title");
    }
}

package com.haohao.xubei.di.module.activity;

import android.app.Activity;
import android.view.LayoutInflater;

import com.haohao.xubei.di.scoped.ActivityScoped;
import com.haohao.xubei.ui.module.account.AccSearchActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * 商品搜索
 * date：2017/5/5 15:18
 * author：Seraph
 *
 **/
@Module(includes = ActivityModule.class)
public abstract class AccSearchModule {

    @ActivityScoped
    @Binds
    abstract Activity bindActivity(AccSearchActivity activity);

    @Provides
    @ActivityScoped
    static LayoutInflater provideLayoutInflater(Activity activity){
        return LayoutInflater.from(activity);
    }

}

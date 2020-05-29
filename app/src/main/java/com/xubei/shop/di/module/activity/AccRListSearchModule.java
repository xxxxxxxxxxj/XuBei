package com.xubei.shop.di.module.activity;

import android.app.Activity;
import android.view.LayoutInflater;

import com.xubei.shop.di.scoped.ActivityScoped;
import com.xubei.shop.ui.module.account.AccRListSearchActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * 商品搜索
 * date：2017/5/5 15:18
 * author：Seraph
 **/
@Module(includes = ActivityModule.class)
public abstract class AccRListSearchModule {

    @ActivityScoped
    @Binds
    abstract Activity bindActivity(AccRListSearchActivity activity);

    @Provides
    @ActivityScoped
    static LayoutInflater provideLayoutInflater(Activity activity) {
        return LayoutInflater.from(activity);
    }

}

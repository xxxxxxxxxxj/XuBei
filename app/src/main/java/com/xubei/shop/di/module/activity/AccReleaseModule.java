package com.xubei.shop.di.module.activity;

import android.app.Activity;

import com.xubei.shop.di.scoped.ActivityScoped;
import com.xubei.shop.ui.module.account.AccReleaseActivity;
import com.xubei.shop.ui.module.account.model.GameBean;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * 账号发布
 * date：2017/5/5 15:18
 * author：Seraph
 *
 **/
@Module(includes = ActivityModule.class)
public abstract class AccReleaseModule {

    @ActivityScoped
    @Binds
    abstract Activity bindActivity(AccReleaseActivity activity);

    @Provides
    @ActivityScoped
    static GameBean provideGameBean(Activity activity) {
        return (GameBean) activity.getIntent().getSerializableExtra("bean");
    }

}

package com.haohao.xubei.di.module.activity;

import android.app.Activity;

import com.haohao.xubei.di.QualifierType;
import com.haohao.xubei.di.scoped.ActivityScoped;
import com.haohao.xubei.ui.module.login.PhoneBindActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * 手机绑定
 * date：2017/5/5 15:18
 * author：Seraph
 **/
@Module(includes = ActivityModule.class)
public abstract class PhoneBindModule {

    @Binds
    @ActivityScoped
    abstract Activity bindActivity(PhoneBindActivity activity);

    @QualifierType("openId")
    @Provides
    @ActivityScoped
    static String providerStrOpenId(Activity activity) {
        return activity.getIntent().getStringExtra("openId");
    }

    @QualifierType("unionId")
    @Provides
    @ActivityScoped
    static String providerStrUnionId(Activity activity) {
        return activity.getIntent().getStringExtra("unionId");
    }

    @QualifierType("source")
    @Provides
    @ActivityScoped
    static String providerStrSource(Activity activity) {
        return activity.getIntent().getStringExtra("source");
    }
}

package com.xubei.shop.di.module.activity;

import android.app.Activity;

import com.xubei.shop.di.QualifierType;
import com.xubei.shop.di.scoped.ActivityScoped;
import com.xubei.shop.ui.module.login.PhoneBindActivity;

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

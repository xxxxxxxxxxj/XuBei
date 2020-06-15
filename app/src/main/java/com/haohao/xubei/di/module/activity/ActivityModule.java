package com.haohao.xubei.di.module.activity;

import android.app.Activity;
import android.content.Context;
import androidx.fragment.app.FragmentActivity;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.haohao.xubei.di.scoped.ActivityScoped;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Activity公共的模型
 * date：2017/4/6 09:25
 * author：Seraph
 *
 **/
@Module
public abstract class ActivityModule {

    @ActivityScoped
    @Binds
    abstract Context bindContext(Activity activity);

    @ActivityScoped
    @Provides
    static RxPermissions providesRxPermissions(Activity activity) {
        return new RxPermissions((FragmentActivity) activity);
    }


}
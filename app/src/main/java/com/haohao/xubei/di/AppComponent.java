package com.haohao.xubei.di;

import android.app.Application;

import com.haohao.xubei.AppApplication;
import com.haohao.xubei.di.module.ActivityBindingModule;
import com.haohao.xubei.di.module.ApiServiceModule;
import com.haohao.xubei.di.module.AppModule;
import com.haohao.xubei.di.module.BroadcastReceiverBindingModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * 全局的Component
 * date：2017/4/5 15:36
 * author：Seraph
 **/
@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        ActivityBindingModule.class,
        BroadcastReceiverBindingModule.class,
        AppModule.class,
        ApiServiceModule.class,
})
public interface AppComponent extends AndroidInjector<AppApplication> {


    //@BindsInstance使得component可以在构建时绑定实例Application
    @Component.Builder
    interface Builder {

        @BindsInstance
        AppComponent.Builder application(Application application);

        AppComponent build();
    }
}
package com.haohao.xubei.di.module;


import com.haohao.xubei.ui.module.receiver.JPushReceiver;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * 负责所有BroadcastReceiverBindingModule实例的管理
 * date：2018/7/20 16:00
 * author：Seraph
 **/
@Module
public abstract class BroadcastReceiverBindingModule {

    //极光推送接收
    @ContributesAndroidInjector()
    abstract JPushReceiver contributeJPushReceiverInjector();

}

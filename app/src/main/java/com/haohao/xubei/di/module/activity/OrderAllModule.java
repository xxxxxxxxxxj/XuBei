package com.haohao.xubei.di.module.activity;

import android.app.Activity;
import android.os.Bundle;

import com.haohao.xubei.di.QualifierType;
import com.haohao.xubei.di.scoped.ActivityScoped;
import com.haohao.xubei.di.scoped.FragmentScoped;
import com.haohao.xubei.ui.module.order.OrderAllActivity;
import com.haohao.xubei.ui.module.order.OrderAllFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

/**
 * 全部订单
 * date：2017/5/5 15:18
 * author：Seraph
 **/
@Module(includes = ActivityModule.class)
public abstract class OrderAllModule {

    @ActivityScoped
    @Binds
    abstract Activity bindActivity(OrderAllActivity activity);


    @QualifierType("type")
    @Provides
    @ActivityScoped
    static Integer provideIntegerType(Activity activity) {
        return activity.getIntent().getIntExtra("type", 0);
    }

    @Provides
    @ActivityScoped
    static FragmentManager provideFragmentManager(Activity activity) {
        return ((FragmentActivity) activity).getSupportFragmentManager();
    }

    @Provides
    @ActivityScoped
    static String[] provideStringsOrderType() {
        return new String[]{"全部", "租赁中", "待付款", "已完成", "售后维权"};
    }


    @FragmentScoped
    @ContributesAndroidInjector
    abstract OrderAllFragment contributeOrderAllFragmentInjector();


    @Provides
    @ActivityScoped
    static List<OrderAllFragment> provideListOrderAllFragment(String[] orderType) {
        List<OrderAllFragment> allFragments = new ArrayList<>();
        for (int i = 0; i < orderType.length; i++) {
            OrderAllFragment fragment = new OrderAllFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", i);
            fragment.setArguments(bundle);
            allFragments.add(fragment);
        }
        return allFragments;
    }

}

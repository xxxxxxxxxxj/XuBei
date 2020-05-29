package com.xubei.shop.di.module.activity;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.xubei.shop.di.QualifierType;
import com.xubei.shop.di.scoped.ActivityScoped;
import com.xubei.shop.di.scoped.FragmentScoped;
import com.xubei.shop.ui.module.order.OrderAllSellerActivity;
import com.xubei.shop.ui.module.order.OrderAllSellerFragment;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

/**
 * 我发布的全部订单
 * date：2017/5/5 15:18
 * author：Seraph
 **/
@Module(includes = ActivityModule.class)
public abstract class OrderAllSellerModule {

    @ActivityScoped
    @Binds
    abstract Activity bindActivity(OrderAllSellerActivity activity);


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
        return new String[]{"全部", "已取消", "待付款", "租赁中", "已完成", "维权中"};
    }

    @FragmentScoped
    @ContributesAndroidInjector
    abstract OrderAllSellerFragment contributeOrderAllFragmentInjector();

    @Provides
    @ActivityScoped
    static List<OrderAllSellerFragment> provideOrderAllSellerFragment(String[] orderType) {
        List<OrderAllSellerFragment> allFragments = new ArrayList<>();
        for (int i = 0; i < orderType.length; i++) {
            OrderAllSellerFragment fragment = new OrderAllSellerFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", i);
            fragment.setArguments(bundle);
            allFragments.add(fragment);
        }
        return allFragments;
    }

}

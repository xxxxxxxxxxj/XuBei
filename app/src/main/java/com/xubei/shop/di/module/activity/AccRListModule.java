package com.xubei.shop.di.module.activity;

import android.app.Activity;
import android.os.Bundle;

import com.xubei.shop.di.QualifierType;
import com.xubei.shop.di.scoped.ActivityScoped;
import com.xubei.shop.di.scoped.FragmentScoped;
import com.xubei.shop.ui.module.account.AccRListActivity;
import com.xubei.shop.ui.module.account.AccRListFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

/**
 * 卖家发布的账号列表
 * date：2017/5/5 15:18
 * author：Seraph
 **/
@Module(includes = ActivityModule.class)
public abstract class AccRListModule {

    @ActivityScoped
    @Binds
    abstract Activity bindActivity(AccRListActivity activity);


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
        return new String[]{"全部", "审核中", "展示中", "仓库中", "出租中", "已置顶"};
    }

    @FragmentScoped
    @ContributesAndroidInjector
    abstract AccRListFragment contributeAccountRListFragmentInjector();


    @Provides
    @ActivityScoped
    static List<AccRListFragment> provideListAccountRListFragment(String[] orderType) {
        List<AccRListFragment> allFragments = new ArrayList<>();
        for (int i = 0; i < orderType.length; i++) {
            AccRListFragment fragment = new AccRListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", i);
            fragment.setArguments(bundle);
            allFragments.add(fragment);
        }
        return allFragments;
    }

}

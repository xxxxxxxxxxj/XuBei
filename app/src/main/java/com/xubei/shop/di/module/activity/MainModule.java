package com.xubei.shop.di.module.activity;

import android.app.Activity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.xubei.shop.di.QualifierType;
import com.xubei.shop.di.scoped.ActivityScoped;
import com.xubei.shop.di.scoped.FragmentScoped;
import com.xubei.shop.ui.module.main.MainActivity;
import com.xubei.shop.ui.module.main.MainHome;
import com.xubei.shop.ui.module.main.MainMe;
import com.xubei.shop.ui.module.main.MainMeBuy;
import com.xubei.shop.ui.module.main.MainMeSell;
import com.xubei.shop.ui.module.main.MainRelease;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

/**
 * 主界面
 * date：2017/5/5 15:18
 * author：Seraph
 *
 **/
@Module(includes = ActivityModule.class)
public abstract class MainModule {

    @Binds
    @ActivityScoped
    abstract Activity bindActivity(MainActivity activity);

    @Provides
    @ActivityScoped
    static FragmentManager provideFragmentManager(Activity activity) {
        return ((FragmentActivity) activity).getSupportFragmentManager();
    }

    @FragmentScoped
    @ContributesAndroidInjector
    abstract MainHome contributeMainHomeInjector();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract MainRelease contributeMainReleaseInjector();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract MainMe contributeMainMeInjector();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract MainMeBuy contributeMainMeBuyInjector();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract MainMeSell contributeMainMeSellInjector();

    @QualifierType("main")
    @ActivityScoped
    @Provides
    static List<Fragment> provideListFragment(MainHome home, MainRelease release, MainMe mainMe) {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(home);
        fragments.add(release);
        fragments.add(mainMe);
        return fragments;
    }


    @QualifierType("me")
    @ActivityScoped
    @Provides
    static List<Fragment> provideMeFragment(MainMeBuy mainMeBuy, MainMeSell mainMeSell) {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(mainMeBuy);
        fragments.add(mainMeSell);
        return fragments;
    }


}

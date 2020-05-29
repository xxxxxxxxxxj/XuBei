package com.xubei.shop.di.module.activity;

import android.app.Activity;

import com.xubei.shop.di.QualifierType;
import com.xubei.shop.di.scoped.ActivityScoped;
import com.xubei.shop.ui.module.account.AccListActivity;
import com.xubei.shop.ui.module.account.model.GameBean;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * 账号列表
 * date：2017/5/5 15:18
 * author：Seraph
 **/
@Module(includes = ActivityModule.class)
public abstract class AccListModule {

    @ActivityScoped
    @Binds
    abstract Activity bindActivity(AccListActivity activity);

    @Provides
    @ActivityScoped
    static GameBean provideGameBean(Activity activity) {
        return (GameBean) activity.getIntent().getSerializableExtra("bean");
    }

    @Provides
    @ActivityScoped
    @QualifierType("sort")
    static List<String> provideListStringSort() {
        List<String> sortList = new ArrayList<>();
        sortList.add("默认排序");
        sortList.add("价格由高到低");
        sortList.add("价格由低到高");
        sortList.add("发布时间由远到近");
        sortList.add("发布时间由近到远");
        return sortList;
    }

}

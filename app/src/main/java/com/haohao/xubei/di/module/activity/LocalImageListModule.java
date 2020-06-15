package com.haohao.xubei.di.module.activity;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.GridLayoutManager;

import com.haohao.xubei.di.scoped.ActivityScoped;
import com.haohao.xubei.ui.module.common.photolist.LocalImageListActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * 显示并且选择本地图片
 * date：2017/5/5 15:18
 * author：Seraph
 *
 **/
@Module(includes = ActivityModule.class)
public abstract class LocalImageListModule {

    @ActivityScoped
    @Binds
    abstract Activity bindActivity(LocalImageListActivity activity);

    @ActivityScoped
    @Provides
    static GridLayoutManager provideGridLayoutManager(Context context) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 4);
        gridLayoutManager.setSmoothScrollbarEnabled(true);
        return gridLayoutManager;
    }

}

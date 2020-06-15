package com.haohao.xubei.di.module.activity;

import android.app.Activity;

import com.haohao.xubei.di.scoped.ActivityScoped;
import com.haohao.xubei.ui.module.setting.HelpListActivity;
import com.haohao.xubei.ui.module.setting.model.ProblemBean;

import androidx.recyclerview.widget.DividerItemDecoration;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * 帮助列表
 * date：2017/5/5 15:18
 * author：Seraph
 *
 **/
@Module(includes = ActivityModule.class)
public abstract class HelpListModule {

    @Binds
    @ActivityScoped
    abstract Activity bindActivity(HelpListActivity activity);

    @Provides
    @ActivityScoped
    static ProblemBean provideProblemBean(Activity activity) {
        return (ProblemBean) activity.getIntent().getSerializableExtra("bean");
    }


    @Provides
    @ActivityScoped
    static DividerItemDecoration provideDividerItemDecoration(Activity activity) {
        return new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL);
    }
}

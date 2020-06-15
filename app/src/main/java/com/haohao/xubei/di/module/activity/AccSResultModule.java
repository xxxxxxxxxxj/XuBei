package com.haohao.xubei.di.module.activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.haohao.xubei.R;
import com.haohao.xubei.di.scoped.ActivityScoped;
import com.haohao.xubei.ui.module.account.AccSResultActivity;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * 账号搜索结果
 * date：2017/5/5 15:18
 * author：Seraph
 **/
@Module(includes = ActivityModule.class)
public abstract class AccSResultModule {

    @ActivityScoped
    @Binds
    abstract Activity bindActivity(AccSResultActivity activity);

    @Provides
    @ActivityScoped
    static DividerItemDecoration provideDividerItemDecoration(Activity activity) {
        DividerItemDecoration decoration = new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(activity, R.drawable.act_acc_list_divider);
        if (drawable != null) {
            decoration.setDrawable(drawable);
        }
        return decoration;
    }

    @Provides
    @ActivityScoped
    static String provideSearchStr(Activity activity) {
        return activity.getIntent().getStringExtra("key");
    }

}

package com.haohao.xubei.di.module.activity;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import androidx.recyclerview.widget.GridLayoutManager;

import com.haohao.xubei.R;
import com.haohao.xubei.di.scoped.ActivityScoped;
import com.haohao.xubei.ui.module.setting.HelpCenterActivity;
import com.haohao.xubei.ui.module.setting.model.ProblemBean;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * 帮助中心
 * date：2017/5/5 15:18
 * author：Seraph
 *
 **/
@Module(includes = ActivityModule.class)
public abstract class HelpCenterModule {

    @Binds
    @ActivityScoped
    abstract Activity bindActivity(HelpCenterActivity activity);

    @Provides
    @ActivityScoped
    static ClipboardManager provideClipboardManager(Activity activity) {
        return (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @Provides
    @ActivityScoped
    static List<ProblemBean> provideProblemList() {
        List<ProblemBean> list = new ArrayList<>();
        list.add(new ProblemBean("买家帮助", R.mipmap.act_setting_help_other1, "1"));
        list.add(new ProblemBean("卖家帮助", R.mipmap.act_setting_help_other2, "2"));
        list.add(new ProblemBean("账号与资金", R.mipmap.act_setting_help_other3, "3"));
        list.add(new ProblemBean("虚贝百宝箱", R.mipmap.act_setting_help_other4, "4"));
        list.add(new ProblemBean("条款协议", R.mipmap.act_setting_help_other5, "5"));
        return list;
    }

    @Provides
    @ActivityScoped
    static GridLayoutManager providesGridLayoutManager(Activity activity) {
        return new GridLayoutManager(activity, 4);
    }


}

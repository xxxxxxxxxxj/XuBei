package com.haohao.xubei.di.module.activity;

import android.app.Activity;

import com.haohao.xubei.di.scoped.ActivityScoped;
import com.haohao.xubei.ui.module.user.RedemptionCenterActivity;
import com.haohao.xubei.ui.module.user.model.RedemptionGameBean;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * 兑换中心
 * date：2017/5/5 15:18
 * author：Seraph
 **/
@Module(includes = ActivityModule.class)
public abstract class RedemptionCenterModule {

    @Binds
    @ActivityScoped
    abstract Activity bindActivity(RedemptionCenterActivity activity);

    @Provides
    @ActivityScoped
    static List<RedemptionGameBean> provideListRedemptionGame() {
        List<RedemptionGameBean> list = new ArrayList<>();
        list.add(new RedemptionGameBean("302", "穿越火线", "随机领取10-150V账号\n账号价值8800元-132000元"));
        list.add(new RedemptionGameBean("245", "英雄联盟", "随机领取50-800皮肤账号\n账号价值4950-87200元"));
        list.add(new RedemptionGameBean("1494", "绝地求生", "随机领取50-800皮肤账号\n账号价值4950-87200元"));
        list.add(new RedemptionGameBean("592", "QQ飞车", "随机领取t1-t3账号\n账号价值2500-35500元"));
        list.add(new RedemptionGameBean("588", "逆战", "随机领取创世天神套装号\n账号价值2200-45000元"));
        return list;
    }
}

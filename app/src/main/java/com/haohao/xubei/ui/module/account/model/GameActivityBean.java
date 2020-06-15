package com.haohao.xubei.ui.module.account.model;

import java.io.Serializable;

/**
 * 游戏活动Bean
 * date：2017/11/28 13:59
 * author：Seraph
 * mail：417753393@qq.com
 **/
public class GameActivityBean implements Serializable {

    public String gameName;//"王者荣耀",
    public String gameId;//1109,
    public String id;//1,
    public String activityRule;//"5,3",
    public String activityRuleName;//"租五送三"

    @Override
    public String toString() {
        return activityRuleName;
    }

    public GameActivityBean(String id, String activityRuleName) {
        this.id = id;
        this.activityRuleName = activityRuleName;
    }
}

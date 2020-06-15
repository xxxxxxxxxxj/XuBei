package com.haohao.xubei.ui.module.user.model;

import java.io.Serializable;

/**
 * 可兑换的游戏bean
 * date：2018/12/10 16:06
 * author：xiongj
 **/
public class RedemptionGameBean implements Serializable {

    public String gameId;

    public String title;

    public String description;

    public boolean isSelect = false;

    public RedemptionGameBean(String gameId, String title, String description) {
        this(gameId, title, description, false);
    }

    public RedemptionGameBean(String gameId, String title, String description, boolean isSelect) {
        this.gameId = gameId;
        this.title = title;
        this.description = description;
        this.isSelect = isSelect;
    }


}

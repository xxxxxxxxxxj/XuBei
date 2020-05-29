package com.xubei.shop.ui.module.account.model;

import java.io.Serializable;

/**
 * 游戏区服bean
 * date：2017/11/28 13:59
 * author：Seraph
 *
 **/
public class GameAreaBean implements Serializable {

    public String allName;//"英雄联盟-电信",
    public long createTime;//null,
    public String createUserId;//131,
    public String gameName;//"电信",
    public Integer grade;//2,
    public int id = -1;//246,
    public int parentId = -1;//245,
    public String remark;//null,
    public Integer state;//0


    public GameAreaBean(String gameName) {
        this.gameName = gameName;
    }

    public GameAreaBean(String allName, int id) {
        this.allName = allName;
        this.id = id;
    }
}

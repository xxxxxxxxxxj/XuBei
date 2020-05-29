package com.xubei.shop.ui.module.account.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 游戏发布获取的所以配置，活动，1级大区
 * date：2017/12/15 16:22
 * author：Seraph
 * mail：417753393@qq.com
 **/
public class GameReleaseAllBean implements Serializable {

    //游戏配置模型1（基本信息）
    public List<GameConfigBean> gameConfigList1 = new ArrayList<>();

    //游戏配置模型2（出租方式价格）
    public List<GameConfigBean> gameConfigList2 = new ArrayList<>();

    //游戏配置模型3（账号补充信息）
    public List<GameConfigBean> gameConfigList3 = new ArrayList<>();

    //游戏区服 1级
    public List<GameAreaBean> gameAreaList;

    //游戏活动
    public List<GameActivityBean> gameActivityList;

}

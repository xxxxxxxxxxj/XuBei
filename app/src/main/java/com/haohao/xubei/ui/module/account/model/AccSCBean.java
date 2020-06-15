package com.haohao.xubei.ui.module.account.model;

import java.io.Serializable;

/**
 * 账号收藏bean
 * date：2018/6/6 16:23
 * author：Seraph
 * mail：417753393@qq.com
 **/
public class AccSCBean implements Serializable {
    public String id;//443592,

    public String bigGameName;//"英雄联盟",游戏名称

    public Double foregift;// 押金

    public String goodsTitle;//"全英雄丨248皮肤丨美猴王丨源计划全套丨恶霸鳄鱼丨万圣节丨铁血狙击手丨克格汪大嘴丨大量皮肤",

    public String hotValue;//336,热度值

    public String leasePrice;//2,租赁价格 小时

    public int shortLease;//3最短租赁时间

    public String signSeller;//是否是签约卖家 0是否 1是

    public String url;//"//xu-game.xubei.com/game/ZKJHnSeK4Q.png"游戏图片地址

    public String actity;//活动名字

    public int goodsStatus;//商品状态

    public String gameAllName;//游戏区服全名

    public String goodsCode;//商品编号

    public String serverName;//商品类型

    //本地管理，是否收藏
    public boolean isSC = true;

    public String deadLineOnLine;

    //1 仓库中 2 待审核 3 展示中 5 已出售 4 出租中
    public String goodsStatusStr() {
        switch (goodsStatus) {
            case 1:
                return "仓库中";
            case 2:
                return "待审核";
            case 3:
                return "展示中";
            case 4://出租
            case 7://锁定
                return "出租中";
            case 5:
                return "已出售";
            default:
                return "未知状态";
        }
    }

}

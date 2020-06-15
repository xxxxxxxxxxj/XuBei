package com.haohao.xubei.ui.module.account.model;

import java.io.Serializable;

/**
 * 商品管理bean
 * date：2017/12/26 10:11
 * author：Seraph
 **/
public class OutGoodsBean implements Serializable {

    public Double foregift;//number, optional): 押金

    public String gameAccount;//string, optional): 游戏账号

    public String gameAllName;//string, optional): 游戏名称，格式：游戏+区服，例如：英雄联盟-电信-征服之海

    public String gameId;//integer, optional): 游戏ID

    public String goodActivity;//string, optional): 参与活动：比如买三送一就是 3,1

    public String goodCode;//string, optional): 商品编号

    public String goodRoleName;//string, optional): 商品角色名称

    public String goodTitle;//string, optional): 商品标题

    public String goodsId;//integer, optional): 商品ID

    public int goodsStatus;//integer, optional): 商品状态 1 仓库中 2 待审核 3 展示中 4 出租中 7 锁单中

    public String imagePath;//string, optional): 订单列表页展示的图片地址

    public int isPhone;//integer, optional): 是否为手游：0是端游1是手游

    public Double leasePrice;//number, optional): 时租

    public Double price;//number, optional): 商品价格

    public String serverName;//string, optional): 商品类型，如：账号出租、账号出售

    public Double dayHours;//天租

    public Double tenHours;//10小时租

    public int isStick; // 置顶状态  0：没有置顶，1：排队中(即将进入置顶状态中)，2：置顶中

    public String endTime; // 置顶结束时间

    public Double stickPrice; // 置顶价格

    public String stickTime; // 置顶时间

    public String hotValue; //热度值


    public String goodsStatusText() {
        switch (goodsStatus) {
            case 1:
                return "仓库中";
            case 2:
                return "待审核";
            case 3:
                return "展示中";
            case 4:
                return "出租中";
            case 7:
                return "锁单中";
            default:
                return "未知状态";
        }
    }


    //商品置顶使用的额外字段

    public boolean isSelect = false; //是否选中

}

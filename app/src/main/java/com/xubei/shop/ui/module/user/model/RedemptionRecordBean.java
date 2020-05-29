package com.xubei.shop.ui.module.user.model;

import java.io.Serializable;

/**
 * 兑换记录bean
 * date：2018/12/5 18:24
 * author：xiongj
 **/
public class RedemptionRecordBean extends RedemptionCenterBean implements Serializable {
    public String goodTitle; //标题
    public String gameAccount;// (string, optional): 账号 ,
    public String gameBeginTime;//  (string, optional): 试玩开始时间 ,
    public String gameEndTime;//  (string, optional): 试玩结束时间 ,
    public String gamePwd;//  (string, optional): 密码 ,
    public String loginGameMode;//  (integer, optional): 上号方式,1.上号器上号，2.账号密码上号 ,
    public String isPhone;// (integer, optional): 0是端游1是手游 ,
    public String gameName;// (string, optional): 游戏名称

    public String goodImg = ""; //logo
}

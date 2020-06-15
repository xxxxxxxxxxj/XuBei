package com.haohao.xubei.ui.module.user.model;

import java.io.Serializable;

/**
 * 兑换中心bean
 * date：2018/12/5 18:24
 * author：xiongj
 **/
public class RedemptionCenterBean implements Serializable {

    public String exchangeGameId;// (integer, optional): 兑换的游戏ID ,

    public String exchangeGoodCode;// (string, optional): 兑换的商品编号 ,

    public String exchangeOrderNo;//(string, optional):兑换的订单号 ,

    public String exchangeTime;//(string, optional):兑换时间 ,

    public String exchangeType;//(string, optional):兑换类型 ,

    public String id;//(integer, optional):id ,

    public String mobile;//(string, optional):用户手机号 ,

    public String receiveTime;//(string, optional):领取时间 ,

    public int status;//(integer, optional):状态，1.已领取2.已使用，3.已失效 ,

    public String termOfValidity;//(string, optional):兑换有效期 ,

    public int userSource;//(integer, optional):用户来源，1.游戏试玩，2.视频账号 ,

    public String userType;//(integer, optional):用户类型：1.新用户，2.老用户


}

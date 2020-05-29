package com.xubei.shop.ui.module.order.model;

import java.io.Serializable;

/**
 * 查询订单bean
 * date：2018/9/19 10:31
 * author：Seraph
 *
 **/
public class SelectOrderPayBean implements Serializable {

    //共用
    public String balanceNo;// "2018091912xnpad90t5ji"

    //充值
    public String id;// 75248,
    public String orderNo;// "12xnpad8vtcum",
    public String userId;// "610193",
    public String thirdStreamNo;// null,
    public String thirdPayType;// null,
    public String orderType;// 1,
    public int orderStatus;// 1,
    public String remark;// null,
    public String amount;// 1,
    public String createTime;// "2018-09-19 10:29:22:000",
    public String updateTime;// "2018-09-19 10:29:22:000",
    public String alipayAccount;// null,
    public String withdrawNo;// null,
    public String name;// null,
    public String timestamp;// null,


    //租赁/续租
    public String actualAmount;// 0,
    public String count;// 0,
    public String endTime;// "2018-09-19T02:22:00.207Z",
    public String foregiftAmount;// 0,
    public int gameOrderStatus;// 0,
    public String jsqOrderStatus;// 0,
    public String leaseType;// "2",
    public String orderGameAmount;// 0,
    public String orderGameNo;// "string",
    public String orderJsqAmount;// 0,
    public String orderJsqNo;// "string",
    public String relet;// true
}

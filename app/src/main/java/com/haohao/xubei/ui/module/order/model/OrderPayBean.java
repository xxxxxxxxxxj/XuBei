package com.haohao.xubei.ui.module.order.model;

import java.io.Serializable;

/**
 * 订单支付bean
 * date：2018/9/14 17:17
 * author：Seraph
 *
 **/
public class OrderPayBean implements Serializable {

    public String orderGameNo;// "上号单号"
    public String orderBalanceNo;// "业务订单结算流水号，用于支付"
    public String gameAllName;// "游戏命名"
    public String payAmount;// 支付金额
    public String foregiftAmount;//  押金
    public String endTime;//游戏结束时间


    //业务订单结算流水号，用于支付 (续租用)
    public String balanceNo;

}

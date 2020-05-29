package com.xubei.shop.ui.module.user.model;

import java.io.Serializable;

/**
 * 资金明细模型
 * date：2018/10/9 10:29
 * author：xiongj
 **/
public class FundDetailBean implements Serializable {
    public long createTime;
    public String type;//类型
    public int cashflow;    //资金流向  0 是进，1是出
    public String detail;//":"押金返还",明细
    public String orderNo;//":"12xhkgxp4lr7c",相关交易订单 ,
    public Double preAvaAmt;//":274.91,变动前可用余额
    public Double changeAmt;//":1,变动金额
    public Double afterAvaAmt;//":275.91,变动后可用余额
}

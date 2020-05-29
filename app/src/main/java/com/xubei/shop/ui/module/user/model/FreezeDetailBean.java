package com.xubei.shop.ui.module.user.model;

import java.io.Serializable;

/**
 * 冻结资金明细模型
 * date：2018/10/9 10:29
 * author：xiongj
 **/
public class FreezeDetailBean implements Serializable {
    public long time;
    public String remarks;//备注
    public String orderNo;//相关交易订单 ,
    public Double freezeAmount;//冻结金额
}

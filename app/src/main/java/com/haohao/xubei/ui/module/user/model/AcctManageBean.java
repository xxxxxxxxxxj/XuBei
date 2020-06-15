package com.haohao.xubei.ui.module.user.model;

import java.io.Serializable;

/**
 * 账户金额
 * date：2017/12/11 14:33
 * author：Seraph
 **/
public class AcctManageBean implements Serializable {

    public Double acctAmt;  //账号总金额

    public Double aviableAmt; //账户可用余额

    public Double freezeAmt;     //不可以金额

    public String alipayAcct;//支付宝账号

    public boolean isBindQQ;//是否绑定QQ

    public boolean isPayPwd;//是否有支付密码

    public boolean isCertificate;//是否实名认证

    public String mobile;//绑定手机

    public String qq;//QQ

    public String realInfo;// 实名认证

    public String sellerType;//账户类型(0 普通卖家 1金牌签约卖家 2为美女卖家)

    public int icoinAmount; //金币数量
}

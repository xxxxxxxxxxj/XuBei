package com.xubei.shop.ui.module.rights.model;

import com.xubei.shop.ui.module.order.model.OutOrderBean;

import java.io.Serializable;
import java.util.List;

/**
 * 维权记录bean
 * date：2018/8/21 16:13
 * author：Seraph
 * mail：417753393@qq.com
 **/
public class LeaseeArbBean implements Serializable {

    //订单信息
    public OutOrderBean outOrder;

    //维权信息列表
    public List<OutOrderLeaseRight> outOrderLeaseRightList;

    //维权金额
    public String rightAmt;

    //维权信息bean
    public class OutOrderLeaseRight implements Serializable {
        public String businessNo;//"string",商户号
        public String buyerId;//0,买家ID
        public String buyerPhone;//"string",买家手机号
        public String buyerQq;//"string",买家QQ
        public String createTime;//"2018-08-21T01:43:20.751Z",创建时间
        public String dealDescription;//"string",客服处理描述
        public String dealResult;//0,客服处理结果,0-不退款,1-退款
        public String dealUserId;//"string", 处理维权用户的Id
        public List<DetailBean> detailList;// 维权明细
        public String endTime;//"2018-08-21T01:43:20.751Z",结束时间
        public String foregiftAmount;//0,订单押金
        public String gameAllName;//"string",游戏名称，格式：游戏+区服，例如：英雄联盟-电信-征服之海
        public String gameId;//0,游戏ID
        public String id;//0,主键
        public String imgId;//"string", 凭证ID
        public List<String> imgUrls;// 凭证图片地址
        public String isAgreeArb;//0,是否显示同意维权按钮
        public String isCustomerHand;//0,是否需要客服处理 :0不需要客服处理，1：需要客服处理
        public String isNoAgreeArb;//0,是否显示不同意维权按钮(添加凭证)
        public String isProcess;//0,对方是否处理，0-否，1-是
        public String isRelet;//0,是否续租，0-支付，1-续租
        public String leaseRightNo;//"string",维权单号
        public String leaseType;//0,维权类型 0：普通维权，1，极速维权
        public String orderAmount;//0,订单总金额，不包含押金
        public String orderEndTime;//"2018-08-21T01:43:20.752Z",订单结束时间
        public String orderGameNo;//"string",订单号
        public String orderPayTime;//"2018-08-21T01:43:20.752Z",订单支付时间
        public String realRefundAmount;//0,实际退款金额
        public String rightDeal;//0, 维权处理方(0买方,1卖方,2系统,3客服)
        public String rightExplain;//"string",维权说明
        public int rightSource;//0,维权发起方(0买方,1卖方,2系统) ,
        public int rightStatus;//0, 维权状态：0-仲裁中，1-不退款，2-退款 3-撤销) ,
        public int rightType;//0,维权原因：0-其他问题，1-账号资料错误/遗漏，无法修改，2-账号描述与实际物品不符，3-账号被找回， 4-账号被封，5-QQ账号财付通未注销，6-账号存在找回风险，7-密码错误导致无法登陆，8-账号在线或顶号导致无法正常游戏，9-账号被封无法登录，10-异地或锁定导致无法登陆，11-账号信息与描述不符，12-违规使用账号，违反出租方明确禁止内容，13-使用第三方插件、软件导致账号被封停，14-网站原因：资料验证失败，申请退款20-恶意挂机，不参与游戏，21-消极比赛，辱骂其他玩家，22-使用外挂等影响游戏平衡的软件，23-在游戏中发布虚假信息， 24-发表涉及政治、法令等信息，25-冒充腾讯官方人员进行诈骗，26-虚贝百宝箱异常，30-分销转租，卖家自己关闭订单 ,
        public String rightReasonDesc;//维权原因文本
        public String sellerId;//0,卖家ID
        public String sellerPhone;//"string",卖家手机号
        public String sellerQq;//"string",卖家QQ
        public String systemLeaseReason;//"string",系统维权原因，包含校验规则
        public String updateTime;//"2018-08-21T01:43:20.752Z", 更新时间
        public String wantRefundAmount;//0希望退款金额

        public String rightSource() {
            switch (rightSource) {
                case 0:
                    return "租方维权";
                case 1:
                    return "出租方维权";
                case 2:
                    return "系统维权";
                default:
                    return "未知维权";
            }
        }

        //维权详情bean
        public class DetailBean implements Serializable {
            public String agree;//"string",表示是否同意退款1.同意 0.不同意
            public String createTime;//"2018-08-21T01:43:20.751Z",创建时间
            public String explains;//"string",说明
            public String id;//0,
            public String imgId;//"string",凭证ID
            public List<String> imgUrls;//图片地址集
            public String leaseRightNo;//"string", 单号
            public String userId;//0,用户ID
            public String userType;//0客户类型(0买方，1卖方，2客服)
        }

        //维权状态文本
        public String rightStatusText() {
            switch (rightStatus) {
                case 0:
                    return "仲裁中";
                case 1:
                    return "维权失败";
                case 2:
                    return "维权成功";
                case 3:
                    return "撤销维权";
                default:
                    return "未知状态";
            }
        }
    }
}

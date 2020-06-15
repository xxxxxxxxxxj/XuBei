package com.haohao.xubei.ui.module.order.model;

import com.haohao.xubei.ui.module.account.model.OutGoodsDetailBean;

import java.io.Serializable;
import java.util.List;

/**
 * 我的出租订单Bean
 * date：2017/12/6 15:02
 * author：Seraph
 *
 **/
public class OutOrderBean implements Serializable {

    public String gameNo;//订单号
    public Integer orderStatus;// 订单状态，0为未支付，1为支付中，2为支付成功，3为支付失败，4维权中，5位退款成功，6位退款失败,7取消订单 ,
    public Long createTime;//1534813885000,创建时间
    public Long beginTime;//1534813891000, 游戏开始时间
    public Long endTime;//1534850491000, 游戏结束时间
    public String gameId;//3245,游戏ID
    public String goodId;//2834687,商品ID
    public String goodCode;//"xe843zt7hi12q", 商品编号
    public String goodTitle;//"bbbbbbbbbbbbbb",商品标题
    public String goodRoleName;//"azazazazazaz",商品角色名称
    public String isPhone;//0,是否为手游：0是端游1是手游
    public String imagePath;//"http://files.xubei.com/demon/6161f19603f347dfa96629296808f5e5.jpg",订单列表页展示的图片地址
    public String goodActivity;//null,参与活动：比如买三送一就是 3,1
    public String gameAllName;//"测试游戏-电信-守望之海",游戏名称，格式：游戏+区服
    public String remark;//null,备注
    public Long updateTime;//1534818846000,更新时间
    public String isFinished;//0,判断订单是否最终完成，完成之后不能进行其他操作
    public String orderAllAmount;//140,订单总金额
    public String orderForegiftAmount;//40,订单押金
    public String bigGameName;//"测试游戏",游戏名,不带区服
    public String goodCount;//10,租赁时长
    public String other;//null,其它服务
    public String isLeaseRight;//0,是否有维权
    public String leaseRightCreateTime;//null,维权创建时间
    public String leaseRightSource;//null,维权发起方(0买方,1卖方,2系统)
    public String hasJsq;//0,是否有加速器
    public String jsqAmount;//null,加速器金额
    public String goodsPrice;//10, 商品价格,默认时租价格
    public String goodsType;//1, 商品类型，1-按小时，2-按小时，3.天租 ,4-按周(168小时)，5-通宵畅玩(10小时)
    public String isNewOrder;//1,是否是新订单，1:新订单，0:老订单，如果是老订单，则不支持维权，也不能查看维权
    public String buyerId;//1733172, 买家ID
    public String sellerId;//610193,卖家ID
    public String operationOrderRole;//1, 当前操作该订单的用户角色.0买方,1卖方
    public OutGoodsDetailBean outGoodsDetail;//商品详情
    public int isCanArb;//1, 是否能申请维权
    public int isShowArb;//0,是否能查看维权
    public int isCancelArb;//0,是否能撤销维权
    public int isDealArb;//0,是否能处理维权
    public String lastArbApplyRole;//null,最近一笔维权申请方 0买方,1卖方,2系统
    public String isShowBeginGame;//1, 是否显示开始游戏按钮
    public int isShowPay;//0,是否显示支付按钮，对于未支付的订单，如果当前操作订单角色是买家，那么就显示支付按钮
    public List<String> rightStats;//null该订单下所有的维权状态，按维权创建时间顺序排列,0买方维权中,1卖方维权中,2买方维权失败,3卖方维权失败,4系统维权失败,5买方维权成功,6卖方维权成功,7系统维权成功,8买方撤销维权,9卖方撤销维权 ,


    public String getOrderStatusText() {
        if (orderStatus != null) {
            switch (orderStatus) {
                case 0:
                    return "未支付";
                case 1:
                    return "支付中";
                case 2:
                    return "支付成功";
                case 3:
                    return "支付失败";
                case 4:
                    return "维权中";
                case 5:
                    return "维权成功";
                case 6:
                    return "维权失败";
                case 7:
                    return "已取消";
                case 100:
                    return "交易完成";
                default:
                    return "未知状态";
            }
        } else {
            return "未知状态";
        }
    }


}

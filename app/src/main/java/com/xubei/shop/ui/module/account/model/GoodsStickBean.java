package com.xubei.shop.ui.module.account.model;

import java.io.Serializable;
import java.util.List;

/**
 * 商品置顶bean
 * date：2018/12/7 16:05
 * author：xiongj
 **/
public class GoodsStickBean implements Serializable {

    public String succNum;      // 置顶成功数量
    public String msg;          //错误信息
    public String orderAmount;     //置顶需要金额
    public String userAvaliableAmount;  // 用户可用余额
    public String actAmt;              //置顶实际用了的金额
    public List<MlsBean> mls;           //置顶详情

    public class MlsBean implements Serializable {

        public String goodsCode;    //商品编码
        public String goodsId;         //商品id
        public String time;            //置顶时间
        public String price;           //置顶金额
        public String msg;          //置顶说明
        public String code;         //返回码

    }


}

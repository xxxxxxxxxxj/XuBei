package com.xubei.shop.ui.module.order.contract;

import com.xubei.shop.ui.module.account.model.OutGoodsDetailBean;
import com.xubei.shop.ui.module.base.IABaseContract;

/**
 * 订单创建
 * date：2017/11/28 11:15
 * author：Seraph
 * mail：417753393@qq.com
 **/
public interface OrderCreateContract extends IABaseContract {

    interface View extends IBaseView {

        void setUIShow(OutGoodsDetailBean outGoodsDetailBean);

        void setAccountMoney(Double tempAccountMoney);

        void onShowPayPw();

        void setPayMoney(Double tempShowRent, Double tempShowPayPrice);

        void selectPayType(int payType);

        void selectTimeType(int timeType);

        void onSetPasswordShow(String tempPassword);

        void onCloseInput();

        void setNoDataView(int type);
    }

    abstract class Presenter extends ABasePresenter<View> {


    }

}

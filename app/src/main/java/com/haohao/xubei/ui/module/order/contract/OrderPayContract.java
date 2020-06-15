package com.haohao.xubei.ui.module.order.contract;

import com.haohao.xubei.ui.module.base.IABaseContract;
import com.haohao.xubei.ui.module.order.model.OutOrderBean;

/**
 * 订单支付
 * date：2018/3/15 15:33
 * author：Seraph
 * mail：417753393@qq.com
 **/
public interface OrderPayContract extends IABaseContract {

    interface View extends IBaseView {

        void setUIShow(OutOrderBean orderBean);

        void onSetPasswordShow(String tempPassword);

        void onCloseInput();

        void onShowPayPw();

        void setAccountMoney(Double tempAccountMoney);

        void selectPayType(int payType);

    }

    abstract class Presenter extends ABasePresenter<View> {


    }

}

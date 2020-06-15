package com.haohao.xubei.ui.module.order.contract;

import com.haohao.xubei.ui.module.base.IABaseContract;
import com.haohao.xubei.ui.module.order.model.OutOrderBean;

/**
 * 订单续租
 * date：2017/12/25 15:22
 * author：Seraph
 * mail：417753393@qq.com
 **/
public interface OrderRenewContract extends IABaseContract {

    interface View extends IBaseView {

        void setOrderInfo(OutOrderBean orderBean);

        void setAllPrice(double allPrice);

        void setMinNumber(int count);

        void setAccountMoney(Double tempAccountMoney);

        void onShowPayPw();

        void onSetPasswordShow(String tempPassword);

        void onCloseInput();

    }

    abstract class Presenter extends ABasePresenter<View> {


    }

}

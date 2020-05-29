package com.xubei.shop.ui.module.order.contract;

import com.xubei.shop.ui.module.base.IABaseContract;
import com.xubei.shop.ui.module.order.model.OutOrderBean;

/**
 * 订单支付成功(pc端)
 * date：2017/12/4 14:58
 * author：Seraph
 *
 **/
public interface OrderSuccessContract extends IABaseContract {

    interface View extends IBaseView {

        void doShowOrder(OutOrderBean orderBean);

    }

    abstract class Presenter extends ABasePresenter<View> {



    }

}

package com.haohao.xubei.ui.module.order.contract;

import com.haohao.xubei.ui.module.base.IABaseContract;
import com.haohao.xubei.ui.module.order.model.OutOrderBean;

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

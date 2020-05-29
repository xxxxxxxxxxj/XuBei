package com.xubei.shop.ui.module.user.contract;

import com.xubei.shop.ui.module.base.IABaseContract;

import java.util.ArrayList;

/**
 * 订单支付
 * date：2017/12/4 14:58
 * author：Seraph
 *
 **/
public interface UserPayContract extends IABaseContract {

    interface View extends IBaseView {

        void selectPayType(String payType);

        void initView(ArrayList<String> amountList);
    }

    abstract class Presenter extends ABasePresenter<View> {

    }

}

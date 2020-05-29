package com.xubei.shop.ui.module.user.contract;

import com.xubei.shop.ui.module.base.IABaseContract;

/**
 * 添加支付宝
 * date：2017/12/4 14:58
 * author：Seraph
 *
 **/
public interface AlipayAddContract extends IABaseContract {

    interface View extends IBaseView {

        void setCountdownText(long time);

        void setUserPhone(String phone);

    }

    abstract class Presenter extends ABasePresenter<View> {

    }

}

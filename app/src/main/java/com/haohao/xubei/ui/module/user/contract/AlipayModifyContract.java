package com.haohao.xubei.ui.module.user.contract;

import com.haohao.xubei.ui.module.base.IABaseContract;

/**
 * 修改支付宝
 * date：2017/12/4 14:58
 * author：Seraph
 *
 **/
public interface AlipayModifyContract extends IABaseContract {

    interface View extends IBaseView {

        void setCountdownText(long time);

        void setUserPhone(String phone);

    }

    abstract class Presenter extends ABasePresenter<View> {

    }

}

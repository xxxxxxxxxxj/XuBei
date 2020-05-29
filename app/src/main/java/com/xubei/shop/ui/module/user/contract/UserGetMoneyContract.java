package com.xubei.shop.ui.module.user.contract;

import com.xubei.shop.ui.module.base.IABaseContract;
import com.xubei.shop.ui.module.user.model.AcctManageBean;

/**
 * 提现
 * date：2017/12/22 11:52
 * author：Seraph
 *
 **/
public interface UserGetMoneyContract extends IABaseContract {

    interface View extends IBaseView {

        void initLayout(AcctManageBean acctManageBean);
    }

    abstract class Presenter extends ABasePresenter<View> {


    }

}

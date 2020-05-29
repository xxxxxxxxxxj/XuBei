package com.xubei.shop.ui.module.user.contract;

import com.xubei.shop.ui.module.base.IABaseContract;
import com.xubei.shop.ui.module.user.model.AcctManageBean;

/**
 * 我的钱包
 * date：2017/12/4 14:58
 * author：Seraph
 *
 **/
public interface PurseContract extends IABaseContract {

    interface View extends IBaseView {

        void setAccountMoney(AcctManageBean acctManageBean);

    }

    abstract class Presenter extends ABasePresenter<View> {

    }

}

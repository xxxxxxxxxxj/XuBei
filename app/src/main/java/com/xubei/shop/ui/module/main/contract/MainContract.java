package com.xubei.shop.ui.module.main.contract;

import com.xubei.shop.ui.module.base.IABaseContract;

/**
 * main契约类
 * date：2017/4/6 15:11
 * author：Seraph
 **/
public interface MainContract extends IABaseContract {

    interface View extends IBaseView {

        void showWelfareDialog();

//        void showRedAmountDialog();
//
//        void showRedAmountDialogValue(double amount);
    }

    abstract class Presenter extends ABasePresenter<View> {

    }

}

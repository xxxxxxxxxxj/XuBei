package com.xubei.shop.ui.module.welcome;

import com.xubei.shop.ui.module.base.IABaseContract;

/**
 * 欢迎页
 * date：2017/5/3 10:10
 * author：Seraph
 *
 **/
interface GuidePagesActivityContract extends IABaseContract {

    interface View extends IBaseView {

        void jumpNextActivity();

        void setImageList(Integer[] images);
    }

    abstract class Presenter extends ABasePresenter<View> {


    }

}

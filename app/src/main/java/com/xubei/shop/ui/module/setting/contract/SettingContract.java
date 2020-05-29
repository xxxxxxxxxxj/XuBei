package com.xubei.shop.ui.module.setting.contract;

import com.xubei.shop.ui.module.base.IABaseContract;

/**
 * 设置
 * date：2017/12/4 14:35
 * author：Seraph
 **/
public interface SettingContract extends IABaseContract {

    interface View extends IBaseView {

        void setAppCode(String appVersionName, String businessNo);

    }

    abstract class Presenter extends ABasePresenter<View> {

    }

}

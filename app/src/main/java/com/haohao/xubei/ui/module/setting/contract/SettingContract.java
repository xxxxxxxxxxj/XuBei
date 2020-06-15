package com.haohao.xubei.ui.module.setting.contract;

import com.haohao.xubei.ui.module.base.IABaseContract;

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

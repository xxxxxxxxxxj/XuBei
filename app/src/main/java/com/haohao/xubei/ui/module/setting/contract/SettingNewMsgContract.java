package com.haohao.xubei.ui.module.setting.contract;

import com.haohao.xubei.ui.module.base.IABaseContract;
import com.haohao.xubei.ui.module.user.model.MessageConfigBean;

import java.util.List;

/**
 * 设置
 * date：2017/12/4 14:35
 * author：Seraph
 **/
public interface SettingNewMsgContract extends IABaseContract {

    interface View extends IBaseView {

        void showMessageConfig(List<MessageConfigBean> list);

    }

    abstract class Presenter extends ABasePresenter<View> {

    }

}

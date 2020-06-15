package com.haohao.xubei.ui.module.account.contract;

import com.haohao.xubei.ui.module.base.IABaseContract;
import com.haohao.xubei.ui.module.account.model.GameAreaBean;

import java.util.List;

/**
 * 账号区域选择
 * date：2017/11/28 11:15
 * author：Seraph
 * mail：417753393@qq.com
 **/
public interface AccASContract extends IABaseContract {

    interface View extends IBaseView {

        void setListDate(List<GameAreaBean> gameBeans);

        void setSelectItem(GameAreaBean selectBean);
    }

    abstract class Presenter extends ABasePresenter<View> {

    }

}

package com.xubei.shop.ui.module.account.contract;

import com.xubei.shop.ui.module.account.model.AccBean;
import com.xubei.shop.ui.module.base.IABaseContract;

import java.util.List;

/**
 * 搜索账号列表
 * date：2017/11/28 11:15
 * author：Seraph
 * mail：417753393@qq.com
 **/
public interface AccSResultContract extends IABaseContract {

    interface View extends IBaseView {

        void notifyItemRangeChanged(int positionStart, int itemCount);

        void setNoDataView(int type);

        void setTitle(String name);

        void initLayout(List<AccBean> list);
    }

    abstract class Presenter extends ABasePresenter<View> {

    }

}

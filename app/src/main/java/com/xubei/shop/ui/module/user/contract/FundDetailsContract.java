package com.xubei.shop.ui.module.user.contract;

import com.xubei.shop.ui.module.base.IABaseContract;
import com.xubei.shop.ui.module.user.model.FundDetailBean;

import java.util.List;

/**
 * 资金明细
 * date：2017/12/4 14:58
 * author：Seraph
 **/
public interface FundDetailsContract extends IABaseContract {

    interface View extends IBaseView {

        void initLayout(List<FundDetailBean> list);

        void setNoDataView(int type);

        void notifyItemRangeChanged(int i, int size);

        void onAutoRefresh();
    }

    abstract class Presenter extends ABasePresenter<View> {

    }

}

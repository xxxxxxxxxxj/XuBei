package com.haohao.xubei.ui.module.account.contract;

import com.haohao.xubei.ui.module.account.model.OutGoodsBean;
import com.haohao.xubei.ui.module.base.IABaseContract;

import java.util.List;

/**
 * 账号管理
 * date：2017/11/28 11:15
 * author：Seraph
 * mail：417753393@qq.com
 **/
public interface AccRListContract extends IABaseContract {

    interface View extends IBaseView {

        void initLayout();

        void notifyItemRangeChanged(int positionStart, int itemCount);

        void onAutoRefreshList();

        void initInflateView(List<OutGoodsBean> list);

        void setNoDataView(int type);

    }

    abstract class Presenter extends ABasePresenter<View> {

    }

}

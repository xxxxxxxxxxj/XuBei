package com.haohao.xubei.ui.module.account.contract;

import com.haohao.xubei.ui.module.account.model.OutGoodsBean;
import com.haohao.xubei.ui.module.base.IABaseContract;

import java.util.List;

/**
 * 搜索账号列表
 * date：2017/11/28 11:15
 * author：Seraph
 * mail：417753393@qq.com
 **/
public interface AccRListSResultContract extends IABaseContract {

    interface View extends IBaseView {

        void notifyItemRangeChanged(int positionStart, int itemCount);

        void setNoDataView(int type);

        void setTitle(String name);

        void initLayout(List<OutGoodsBean> list);

        void onAutoRefreshList();

    }

    abstract class Presenter extends ABasePresenter<View> {

    }

}

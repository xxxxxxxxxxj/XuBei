package com.xubei.shop.ui.module.account.contract;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xubei.shop.ui.module.account.model.AccBean;
import com.xubei.shop.ui.module.account.model.GameAllAreaBean;
import com.xubei.shop.ui.module.account.model.GameSearchRelationBean;
import com.xubei.shop.ui.module.base.IABaseContract;
import com.xubei.shop.ui.views.NoDataView;

import java.util.List;

/**
 * 账号列表
 * date：2017/11/28 11:15
 * author：Seraph
 * mail：417753393@qq.com
 **/
public interface AccListContract extends IABaseContract {

    interface View extends IBaseView {

        void setTitle(String name);

        void onAutoRefresh();

        void notifyItemRangeChanged(int positionStart, int itemCount);

        void setFilterPlatform(String platform);

        void onShowPopFilter(List<String> list, String selectItem, int type);

        void showAreaPop(List<GameAllAreaBean> children, int p);

        void onCloseAreaPop();

        void onCloseOtherFilter();

        void initLayout(List<AccBean> list, NoDataView noDataView,boolean isFree);

        void doUpdataMoreView(List<GameSearchRelationBean> relationList);

        SmartRefreshLayout getSrl();

    }

    abstract class Presenter extends ABasePresenter<View> {

    }

}

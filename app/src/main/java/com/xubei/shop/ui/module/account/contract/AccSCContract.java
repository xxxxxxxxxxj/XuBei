package com.xubei.shop.ui.module.account.contract;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xubei.shop.ui.module.account.model.AccSCBean;
import com.xubei.shop.ui.module.base.IABaseContract;
import com.xubei.shop.ui.views.NoDataView;

import java.util.List;

/**
 * 账号收藏
 * date：2017/11/28 11:15
 * author：Seraph
 * mail：417753393@qq.com
 **/
public interface AccSCContract extends IABaseContract {

    interface View extends IBaseView {

        void initLayout(List<AccSCBean> list,NoDataView noDataView);

        void notifyDataSetChanged(int positionStart, int itemCount);

        SmartRefreshLayout getSrl();
    }

    abstract class Presenter extends ABasePresenter<View> {

    }

}

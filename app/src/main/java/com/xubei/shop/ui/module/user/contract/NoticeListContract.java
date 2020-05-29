package com.xubei.shop.ui.module.user.contract;

import com.xubei.shop.ui.module.base.IABaseContract;
import com.xubei.shop.ui.module.main.model.NoticeBean;

import java.util.List;

/**
 * 通知契约类
 * date：2017/4/6 15:11
 * author：Seraph
 *
 **/
public interface NoticeListContract extends IABaseContract {

    interface View extends IBaseView {

        void initLayout(List<NoticeBean> list);

        void setNoDataView(int type);

        void notifyItemRangeChanged(int p, int i);
    }

    abstract class Presenter extends ABasePresenter<View> {

    }

}

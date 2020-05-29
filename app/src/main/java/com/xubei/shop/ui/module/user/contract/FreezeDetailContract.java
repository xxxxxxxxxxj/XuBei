package com.xubei.shop.ui.module.user.contract;

import com.xubei.shop.ui.module.base.IABaseContract;
import com.xubei.shop.ui.module.user.model.FreezeDetailBean;

import java.util.List;

/**
 * 冻结明细
 * date：2018/10/24 15:33
 * author：xiongj
 **/
public interface FreezeDetailContract extends IABaseContract {

    interface View extends IBaseView {

        void initLayout(List<FreezeDetailBean> list);

        void setNoDataView(int type);

        void notifyItemRangeChanged(int i, int size);

    }

    abstract class Presenter extends ABasePresenter<View> {

    }

}

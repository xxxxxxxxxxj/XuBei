package com.haohao.xubei.ui.module.user.contract;

import com.haohao.xubei.ui.module.base.IABaseContract;
import com.haohao.xubei.ui.module.user.model.RedemptionRecordBean;

import java.util.List;

/**
 * 兑换记录
 * date：2018/11/14 17:15
 * author：xiongj
 **/
public interface RedemptionRecordContract extends IABaseContract {

    interface View extends IBaseView {

        void initLayout(List<RedemptionRecordBean> list);

        void setNoDataView(int type);

        void notifyDataSetChanged();

        void onScan();

    }

    abstract class Presenter extends ABasePresenter<View> {

    }


}

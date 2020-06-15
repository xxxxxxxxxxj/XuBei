package com.haohao.xubei.ui.module.user.contract;

import com.haohao.xubei.ui.module.base.IABaseContract;
import com.haohao.xubei.ui.module.main.model.NoticeBean;

import java.util.List;

/**
 * 我的消息
 * date：2018/11/14 17:15
 * author：xiongj
 **/
public interface MyMsgContract extends IABaseContract {

    interface View extends IBaseView {

        void initLayout(List<NoticeBean> list);

        void onAutoRefresh();

        void setNoDataView(int type);

        void notifyItemRangeChanged(int positionStart, int itemCount);

        void startTypeActivity(NoticeBean noticeBean);
    }

    abstract class Presenter extends ABasePresenter<View> {

    }


}

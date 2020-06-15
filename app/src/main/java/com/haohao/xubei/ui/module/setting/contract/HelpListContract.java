package com.haohao.xubei.ui.module.setting.contract;

import com.haohao.xubei.ui.module.base.IABaseContract;
import com.haohao.xubei.ui.module.setting.model.QuestionBean;

import java.util.List;

/**
 * 帮助详情
 * date：2017/12/4 14:35
 * author：Seraph
 *
 **/
public interface HelpListContract extends IABaseContract {

    interface View extends IBaseView {

        void setHelpTypeTitle(String title);

        void setQuestionList(List<QuestionBean> list);

        void setNoDataView(int type);
    }

    abstract class Presenter extends ABasePresenter<View> {

    }

}

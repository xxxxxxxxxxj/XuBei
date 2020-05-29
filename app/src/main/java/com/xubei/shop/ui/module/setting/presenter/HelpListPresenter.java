package com.xubei.shop.ui.module.setting.presenter;

import com.blankj.utilcode.util.ToastUtils;
import com.xubei.shop.data.network.rx.RxSchedulers;
import com.xubei.shop.data.network.service.Api8Service;
import com.xubei.shop.ui.module.base.ABaseSubscriber;
import com.xubei.shop.ui.module.setting.contract.HelpListContract;
import com.xubei.shop.ui.module.setting.model.ProblemBean;
import com.xubei.shop.ui.module.setting.model.QuestionBean;
import com.xubei.shop.ui.views.NoDataView;

import java.util.List;

import javax.inject.Inject;

/**
 * 帮助类型列表
 * date：2017/12/4 14:38
 * author：Seraph
 **/
public class HelpListPresenter extends HelpListContract.Presenter {


    private ProblemBean problemBean;

    private Api8Service api8Service;

    @Inject
    HelpListPresenter(Api8Service api8Service, ProblemBean problemBean) {
        this.problemBean = problemBean;
        this.api8Service = api8Service;
    }

    @Override
    public void start() {
        mView.setHelpTypeTitle(problemBean.menuName);
        //获取列表
        getArticleList();
    }


    public void getArticleList() {
        api8Service.getArticleByMenuId(problemBean.menuId)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.setNoDataView(NoDataView.LOADING))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<List<QuestionBean>>() {
                    @Override
                    public void onSuccess(List<QuestionBean> list) {
                        mView.setQuestionList(list);
                        mView.setNoDataView(NoDataView.LOADING_OK);
                    }

                    @Override
                    public void onError(String errStr) {
                        mView.setNoDataView(NoDataView.NET_ERR);
                        ToastUtils.showShort(errStr);
                    }
                });

    }
}

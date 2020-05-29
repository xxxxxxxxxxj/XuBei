package com.xubei.shop.ui.module.user.presenter;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.xubei.shop.AppConfig;
import com.xubei.shop.AppConstants;
import com.xubei.shop.data.network.rx.RxSchedulers;
import com.xubei.shop.data.network.service.Api8Service;
import com.xubei.shop.ui.module.base.ABaseSubscriber;
import com.xubei.shop.ui.module.base.BaseData;
import com.xubei.shop.ui.module.main.model.NoticeBean;
import com.xubei.shop.ui.module.user.contract.NoticeListContract;
import com.xubei.shop.ui.views.NoDataView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 消息列表逻辑
 * date：2018/5/29 18:03
 * author：Seraph
 **/
public class NoticeListPresenter extends NoticeListContract.Presenter {


    private Api8Service api8Service;

    @Inject
    public NoticeListPresenter(Api8Service api8Service) {
        this.api8Service = api8Service;
    }


    private List<NoticeBean> list = new ArrayList<>();

    private int pageNo = 0;

    @Override
    public void start() {
        mView.initLayout(list);
        onRefresh();
    }


    public void onItemClick(int position) {
        NoticeBean noticeBean = list.get(position);
        //如果是url则跳转网页加载
        if ("1".equals(noticeBean.dataType)) {
            //url
            ARouter.getInstance().build(AppConstants.PagePath.COMM_AGENTWEB)
                    .withString("title", noticeBean.title)
                    .withString("webUrl", noticeBean.dataValue)
                    .navigation();
        } else {
            ARouter.getInstance().build(AppConstants.PagePath.COMM_WEBLOCAL)
                    .withString("title", noticeBean.title)
                    .withString("webContent", noticeBean.secondTitle)
                    .navigation();
        }
    }

    public void onRefresh() {
        getUserMessage(1);
    }

    public void onNextPage() {
        getUserMessage(pageNo + 1);
    }


    //获取活动公告
    private void getUserMessage(int tempPageNo) {
        api8Service.getUserMessage(null, "1", tempPageNo, AppConfig.PAGE_SIZE)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.setNoDataView(NoDataView.LOADING))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<BaseData<NoticeBean>>() {
                    @Override
                    public void onSuccess(BaseData<NoticeBean> baseData) {
                        if (tempPageNo == 1) {
                            list.clear();
                        }
                        //如果请求的页面和返回的页面不一致
                        if (baseData.currentPage == tempPageNo || baseData.currentPage == 0) {
                            list.addAll(baseData.data);
                            mView.notifyItemRangeChanged((tempPageNo - 1) * AppConfig.PAGE_SIZE, baseData.data.size());
                        } else {
                            ToastUtils.showShort("没有更多数据");
                        }
                        if (list.size() == 0) {
                            mView.setNoDataView(NoDataView.NO_DATA);
                        } else {
                            mView.setNoDataView(NoDataView.LOADING_OK);
                        }
                        pageNo = baseData.currentPage;
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                        mView.setNoDataView(NoDataView.NET_ERR);
                    }
                });

    }


}

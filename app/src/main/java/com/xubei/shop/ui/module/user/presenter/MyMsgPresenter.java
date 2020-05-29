package com.xubei.shop.ui.module.user.presenter;

import com.blankj.utilcode.util.ToastUtils;
import com.xubei.shop.AppConfig;
import com.xubei.shop.data.db.help.UserBeanHelp;
import com.xubei.shop.data.network.rx.RxSchedulers;
import com.xubei.shop.data.network.service.Api8Service;
import com.xubei.shop.ui.module.base.ABaseSubscriber;
import com.xubei.shop.ui.module.base.BaseData;
import com.xubei.shop.ui.module.main.model.NoticeBean;
import com.xubei.shop.ui.module.user.contract.MyMsgContract;
import com.xubei.shop.ui.views.NoDataView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 我的消息
 * date：2018/11/14 17:19
 * author：xiongj
 **/
public class MyMsgPresenter extends MyMsgContract.Presenter {


    private UserBeanHelp userBeanHelp;

    private Api8Service api8Service;

    @Inject
    public MyMsgPresenter(Api8Service api8Service, UserBeanHelp userBeanHelp) {
        this.userBeanHelp = userBeanHelp;
        this.api8Service = api8Service;
    }


    private List<NoticeBean> list = new ArrayList<>();

    private int pageNo = 0;


    //消息类型 2是系统，3是个人
    private String type = "2";

    @Override
    public void start() {
        mView.initLayout(list);
        onRefresh();
    }

    //设置类型
    public void setTabSelectd(int position) {
        switch (position) {
            case 0://系统
                type = "2";
                break;
            case 1://个人
                type = "3";
                break;
        }
        mView.onAutoRefresh();
    }


    //点击
    public void onItemClick(int position) {
        if (position >= list.size()) {
            return;
        }
        NoticeBean noticeBean = list.get(position);
        if (noticeBean.status != null && noticeBean.status == 0) {
            updateStatus(noticeBean.messageId, position);
        }
      mView.startTypeActivity(noticeBean);

    }


    public void onRefresh() {
        getUserMessage(1);
    }


    public void onNextPage() {
        getUserMessage(pageNo + 1);
    }


    //获取我的消息
    private void getUserMessage(int tempPageNo) {
        api8Service.getUserMessage(userBeanHelp.getAuthorization(), type, tempPageNo, AppConfig.PAGE_SIZE)
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


    //更新消息已读
    private void updateStatus(Long messageId, int position) {
        api8Service.updateStatus(userBeanHelp.getAuthorization(), messageId)
                .compose(RxSchedulers.io_main_business())
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        list.get(position).status = 1;
                        mView.notifyItemRangeChanged(position, 1);
                    }

                    @Override
                    protected void onError(String errStr) {

                    }
                });
    }

}

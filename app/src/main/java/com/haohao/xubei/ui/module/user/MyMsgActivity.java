package com.haohao.xubei.ui.module.user;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.androidkun.xtablayout.XTabLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.databinding.ActUserMyMsgBinding;
import com.haohao.xubei.ui.module.base.ABaseActivity;
import com.haohao.xubei.ui.module.main.model.NoticeBean;
import com.haohao.xubei.ui.module.user.adapter.NoticeListAdapter;
import com.haohao.xubei.ui.module.user.contract.MyMsgContract;
import com.haohao.xubei.ui.module.user.presenter.MyMsgPresenter;
import com.haohao.xubei.utlis.LinearLayoutManager2;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 我的消息
 * date：2018/11/14 17:14
 * author：xiongj
 **/
@Route(path = AppConstants.PagePath.USER_MYMSG, extras = AppConstants.ARAction.LOGIN)
public class MyMsgActivity extends ABaseActivity<MyMsgContract.Presenter> implements MyMsgContract.View {

    private ActUserMyMsgBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_user_my_msg);
    }

    @Inject
    MyMsgPresenter presenter;

    @Override
    protected MyMsgContract.Presenter getMVPPresenter() {
        return presenter;
    }

    @Inject
    LinearLayoutManager2 linearLayoutManager;

    private NoticeListAdapter adapter;

    @Override
    protected void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("我的消息");
        presenter.start();
    }


    @Override
    public void initLayout(List<NoticeBean> list) {
        adapter = new NoticeListAdapter(list);
        binding.cll.recyclerview.setLayoutManager(linearLayoutManager);
        binding.cll.recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> presenter.onItemClick(position));
        binding.xtlMsg.addOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                //选中
                presenter.setTabSelectd(tab.getPosition());
            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {
                //再次点击
                onAutoRefresh();
            }
        });
        binding.cll.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                presenter.onNextPage();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                presenter.onRefresh();
            }
        });
        binding.cll.ndv.setOnClickListener(v -> presenter.onRefresh());

    }

    @Override
    public void setNoDataView(int type) {
        binding.cll.refreshLayout.finishRefresh();
        binding.cll.refreshLayout.finishLoadMore();
        binding.cll.ndv.setType(type);
    }

    @Override
    public void onAutoRefresh() {
        binding.cll.recyclerview.scrollToPosition(0);
        binding.cll.refreshLayout.autoRefresh();
    }

    @Override
    public void notifyItemRangeChanged(int positionStart, int itemCount) {
        if (positionStart >= 0) {
            adapter.notifyItemRangeChanged(positionStart, itemCount);
        }
    }

    public void startTypeActivity(NoticeBean noticeBean) {
        switch (noticeBean.msgType) {
            case 1://我的账号管理页面（仓库中）
                ARouter.getInstance().build(AppConstants.PagePath.ACC_R_LIST).withInt("type", 3).navigation();
                break;
            case 2://我的出租订单（维权中）
                ARouter.getInstance().build(AppConstants.PagePath.ORDER_SELLER_ALL).withInt("type", 5).navigation();
                break;
            case 3://我的账号管理
            case 4:
                ARouter.getInstance().build(AppConstants.PagePath.ACC_R_LIST).withInt("type", 0).navigation();
                break;
            case 5://系统消息
                if ("1".equals(noticeBean.dataType)) {
                    //url
                    ARouter.getInstance().build(AppConstants.PagePath.COMM_AGENTWEB)
                            .withString("title", noticeBean.title)
                            .withString("webUrl", noticeBean.dataValue)
                            .navigation();
                } else {
                    String webContent;
                    if (noticeBean.secondTitle.equals(noticeBean.dataValue)) {
                        webContent = noticeBean.secondTitle;
                    } else {
                        webContent = noticeBean.secondTitle + "<br/>" + noticeBean.dataValue;
                    }
                    ARouter.getInstance().build(AppConstants.PagePath.COMM_WEBLOCAL)
                            .withString("title", noticeBean.title)
                            .withString("webContent", webContent)
                            .navigation();
                }
                break;

        }
    }

}

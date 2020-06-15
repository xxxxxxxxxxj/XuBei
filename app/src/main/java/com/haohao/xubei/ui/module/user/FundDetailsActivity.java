package com.haohao.xubei.ui.module.user;

import android.os.Bundle;
import android.view.Menu;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.androidkun.xtablayout.XTabLayout;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.databinding.ActUserFundDetailsBinding;
import com.haohao.xubei.ui.module.base.ABaseActivity;
import com.haohao.xubei.ui.module.user.adapter.FundDetailsAdapter;
import com.haohao.xubei.ui.module.user.contract.FundDetailsContract;
import com.haohao.xubei.ui.module.user.model.FundDetailBean;
import com.haohao.xubei.ui.module.user.presenter.FundDetailsPresenter;
import com.haohao.xubei.utlis.LinearLayoutManager2;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 资金明细
 * date：2018/10/9 09:53
 * author：xiongj
 **/
@Route(path = AppConstants.PagePath.USER_FUNDDETAILS,extras = AppConstants.ARAction.LOGIN)
public class FundDetailsActivity extends ABaseActivity<FundDetailsContract.Presenter> implements FundDetailsContract.View {


    private ActUserFundDetailsBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_user_fund_details);
    }


    @Inject
    FundDetailsPresenter presenter;

    @Override
    protected FundDetailsContract.Presenter getMVPPresenter() {
        return presenter;
    }

    @Inject
    LinearLayoutManager2 linearLayoutManager;

    private FundDetailsAdapter adapter;

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("资金明细");
        RxToolbar.itemClicks(binding.appbar.toolbar)
                .as(bindLifecycle())
                .subscribe(menuItem ->
                        ARouter.getInstance().build(AppConstants.PagePath.USER_FREEZEDETAILS).navigation());
        presenter.start();
    }

    @Override
    public void initLayout(List<FundDetailBean> list) {
        adapter = new FundDetailsAdapter(list);
        binding.ccl.recyclerview.setLayoutManager(linearLayoutManager);
        binding.ccl.recyclerview.setAdapter(adapter);
        binding.xtl.addOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
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
        binding.ccl.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                presenter.nextPage();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                presenter.doRefresh();
            }
        });
        binding.ccl.ndv.setOnClickListener(v -> presenter.doRefresh());
    }

    @Override
    public void setNoDataView(int type) {
        binding.ccl.refreshLayout.finishRefresh();
        binding.ccl.refreshLayout.finishLoadMore();
        binding.ccl.ndv.setType(type);
    }

    @Override
    public void onAutoRefresh() {
        binding.ccl.recyclerview.scrollToPosition(0);
        binding.ccl.refreshLayout.autoRefresh();
    }

    @Override
    public void notifyItemRangeChanged(int positionStart, int itemCount) {
        if (positionStart >= 0) {
            adapter.notifyItemRangeChanged(positionStart, itemCount);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_freeze_details, menu);
        return super.onCreateOptionsMenu(menu);
    }
}

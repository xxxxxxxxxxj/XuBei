package com.xubei.shop.ui.module.user;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.databinding.ActUserFreezeDetailsBinding;
import com.xubei.shop.ui.module.base.ABaseActivity;
import com.xubei.shop.ui.module.user.adapter.FreezeDetailAdapter;
import com.xubei.shop.ui.module.user.contract.FreezeDetailContract;
import com.xubei.shop.ui.module.user.model.FreezeDetailBean;
import com.xubei.shop.ui.module.user.presenter.FreezeDetailPresenter;
import com.xubei.shop.utlis.LinearLayoutManager2;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 冻结资金明细
 * date：2018/10/24 15:32
 * author：xiongj
 **/
@Route(path = AppConstants.PagePath.USER_FREEZEDETAILS)
public class FreezeDetailsActivity extends ABaseActivity<FreezeDetailContract.Presenter> implements FreezeDetailContract.View {

    private ActUserFreezeDetailsBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_user_freeze_details);
    }

    @Inject
    FreezeDetailPresenter presenter;

    @Override
    protected FreezeDetailContract.Presenter getMVPPresenter() {
        return presenter;
    }

    @Inject
    LinearLayoutManager2 linearLayoutManager;

    private FreezeDetailAdapter adapter;

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("冻结明细");
        presenter.start();
    }

    @Override
    public void initLayout(List<FreezeDetailBean> list) {
        adapter = new FreezeDetailAdapter(list);
        binding.cll.recyclerview.setLayoutManager(linearLayoutManager);
        binding.cll.recyclerview.setAdapter(adapter);
        binding.cll.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                presenter.nextPage();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                presenter.doRefresh();
            }
        });
        binding.cll.ndv.setOnClickListener(v -> presenter.doRefresh());
    }

    @Override
    public void setNoDataView(int type) {
        binding.cll.refreshLayout.finishRefresh();
        binding.cll.refreshLayout.finishLoadMore();
        binding.cll.ndv.setType(type);
    }

    @Override
    public void notifyItemRangeChanged(int positionStart, int itemCount) {
        if (positionStart >= 0) {
            adapter.notifyItemRangeChanged(positionStart, itemCount);
        }
    }

}

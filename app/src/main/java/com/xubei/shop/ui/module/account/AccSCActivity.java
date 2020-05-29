package com.xubei.shop.ui.module.account;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.databinding.ActAccScBinding;
import com.xubei.shop.ui.module.account.adapter.AccSCAdapter;
import com.xubei.shop.ui.module.account.contract.AccSCContract;
import com.xubei.shop.ui.module.account.model.AccSCBean;
import com.xubei.shop.ui.module.account.presenter.AccSCPresenter;
import com.xubei.shop.ui.module.base.ABaseActivity;
import com.xubei.shop.ui.views.NoDataView;
import com.xubei.shop.utlis.LinearLayoutManager2;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 账号收藏
 * date：2018/6/5 09:47
 * author：Seraph
 * mail：417753393@qq.com
 **/
@Route(path = AppConstants.PagePath.ACC_SC,extras = AppConstants.ARAction.LOGIN)
public class AccSCActivity extends ABaseActivity<AccSCContract.Presenter> implements AccSCContract.View {

    private ActAccScBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_acc_sc);
    }

    @Inject
    AccSCPresenter presenter;

    @Override
    protected AccSCContract.Presenter getMVPPresenter() {
        return presenter;
    }


    private AccSCAdapter adapter;

    @Inject
    LinearLayoutManager2 linearLayoutManager;

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("我的收藏");
        presenter.start();
    }

    //初始化布局
    public void initLayout(List<AccSCBean> list, NoDataView noDataView) {
        adapter = new AccSCAdapter(list);
        adapter.setEmptyView(noDataView);
        adapter.setOnItemClickListener((adapter, view, position) -> presenter.onItemClick(position));
        adapter.setOnItemChildClickListener((adapter, view, position) -> presenter.onItemSCClick(position));
        binding.itemList.srl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                presenter.doRefresh();
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                presenter.doLoadMore();
            }

        });
        binding.itemList.rv.setLayoutManager(linearLayoutManager);
        binding.itemList.rv.setAdapter(adapter);
    }

    @Override
    public void notifyDataSetChanged(int positionStart, int itemCount) {
        if (positionStart == 0 && itemCount == 0) {
            adapter.notifyDataSetChanged();
        } else {
            adapter.notifyItemRangeChanged(positionStart, itemCount);
        }
    }

    @Override
    public SmartRefreshLayout getSrl() {
        return binding.itemList.srl;
    }

}

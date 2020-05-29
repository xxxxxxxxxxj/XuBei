package com.xubei.shop.ui.module.account;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.databinding.ActUserFreeBinding;
import com.xubei.shop.ui.module.account.adapter.AccUserFreeAdapter;
import com.xubei.shop.ui.module.account.contract.AccUserFreeContract;
import com.xubei.shop.ui.module.account.model.UserAccFreeBean;
import com.xubei.shop.ui.module.account.presenter.AccUserFreePresenter;
import com.xubei.shop.ui.module.base.ABaseActivity;
import com.xubei.shop.ui.views.NoDataView;
import com.xubei.shop.utlis.AlertDialogUtils;
import com.xubei.shop.utlis.LinearLayoutManager2;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 用户个人的免费试玩列表
 * date：2017/11/30 11:47
 * author：Seraph
 **/
@Route(path = AppConstants.PagePath.ACC_USER_FREE, extras = AppConstants.ARAction.LOGIN)
public class AccUserFreeActivity extends ABaseActivity<AccUserFreeContract.Presenter> implements AccUserFreeContract.View {

    private ActUserFreeBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_user_free);
    }

    @Inject
    AccUserFreePresenter presenter;

    @Override
    protected AccUserFreeContract.Presenter getMVPPresenter() {
        return presenter;
    }

    @Inject
    AlertDialogUtils dialogUtils;

    private AccUserFreeAdapter adapter;

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("免费租号");
        presenter.start();
    }


    @Override
    public void initView(List<UserAccFreeBean> list, NoDataView noDataView) {
        adapter = new AccUserFreeAdapter(list);
        adapter.setEmptyView(noDataView);
        adapter.setOnItemChildClickListener((adapter, view, position) ->
                dialogUtils.createShareDialog(binding.llRoot, platform -> presenter.onItemChildClick(position, platform))
        );
        adapter.setOnItemClickListener((adapter, view, position) -> presenter.onItemClick(position));
        binding.itemList.srl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                presenter.nextPage();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                presenter.doRefresh();
            }
        });
        binding.itemList.rv.setLayoutManager(new LinearLayoutManager2(this));
        binding.itemList.rv.setAdapter(adapter);
    }

    @Override
    public SmartRefreshLayout getSrl() {
        return binding.itemList.srl;
    }

    @Override
    public void notifyItemRangeChanged(int start, int size) {
        if (start == 0 && size == 0) {
            adapter.notifyDataSetChanged();
        } else {
            adapter.notifyItemRangeChanged(start, size);
        }
    }
}

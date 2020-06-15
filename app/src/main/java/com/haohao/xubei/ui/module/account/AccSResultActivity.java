package com.haohao.xubei.ui.module.account;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.databinding.ActAccSResultBinding;
import com.haohao.xubei.ui.module.account.adapter.AccAdapter;
import com.haohao.xubei.ui.module.account.contract.AccSResultContract;
import com.haohao.xubei.ui.module.account.model.AccBean;
import com.haohao.xubei.ui.module.account.presenter.AccSResultPresenter;
import com.haohao.xubei.ui.module.base.ABaseActivity;
import com.haohao.xubei.utlis.LinearLayoutManager2;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;

/**
 * 账号搜索结果
 * date：2017/12/21 09:54
 * author：Seraph
 **/
@Route(path = AppConstants.PagePath.ACC_SRESULT)
public class AccSResultActivity extends ABaseActivity<AccSResultContract.Presenter> implements AccSResultContract.View {

    private ActAccSResultBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_acc_s_result);
    }

    @Inject
    AccSResultPresenter presenter;

    @Override
    protected AccSResultContract.Presenter getMVPPresenter() {
        return presenter;
    }

    private AccAdapter adapter;

    @Inject
    LinearLayoutManager2 linearLayoutManager;

    @Inject
    DividerItemDecoration itemDecoration;

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        presenter.start();
    }

    @Override
    public void setTitle(String name) {
        binding.appbar.toolbar.setTitle(name);
    }

    @Override
    public void initLayout(List<AccBean> list) {
        adapter = new AccAdapter(list,false);
        binding.cll.recyclerview.setLayoutManager(linearLayoutManager);
        binding.cll.recyclerview.addItemDecoration(itemDecoration);
        binding.cll.recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> presenter.onItemClick(position));
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
    public void notifyItemRangeChanged(int positionStart, int itemCount) {
        if (positionStart >=0) {
            adapter.notifyItemRangeChanged(positionStart, itemCount);
        }
    }

    @Override
    public void setNoDataView(int type) {
        binding.cll.refreshLayout.finishRefresh();
        binding.cll.refreshLayout.finishLoadMore();
        binding.cll.ndv.setType(type);
    }


}

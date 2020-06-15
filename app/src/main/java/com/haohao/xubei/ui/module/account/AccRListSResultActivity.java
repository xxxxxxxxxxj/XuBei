package com.haohao.xubei.ui.module.account;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.databinding.ActAccRListSResultBinding;
import com.haohao.xubei.ui.module.account.adapter.AccRListAdapter;
import com.haohao.xubei.ui.module.account.contract.AccRListSResultContract;
import com.haohao.xubei.ui.module.account.model.OutGoodsBean;
import com.haohao.xubei.ui.module.account.presenter.AccRListSResultPresenter;
import com.haohao.xubei.ui.module.base.ABaseActivity;
import com.haohao.xubei.utlis.LinearLayoutManager2;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 我的账号管理搜索结果
 * date：2017/12/21 09:54
 * author：Seraph
 **/
@Route(path = AppConstants.PagePath.ACC_R_LISTSRESULT)
public class AccRListSResultActivity extends ABaseActivity<AccRListSResultContract.Presenter> implements AccRListSResultContract.View {

    private ActAccRListSResultBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_acc_r_list_s_result);
    }

    @Inject
    AccRListSResultPresenter presenter;

    @Override
    protected AccRListSResultContract.Presenter getMVPPresenter() {
        return presenter;
    }

    private AccRListAdapter adapter;

    @Inject
    LinearLayoutManager2 linearLayoutManager;

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        presenter.start();
    }

    @Override
    public void setTitle(String name) {
        binding.appbar.toolbar.setTitle(name);
    }

    @Override
    public void initLayout(List<OutGoodsBean> list) {
        adapter = new AccRListAdapter(list);
        binding.cll.recyclerview.setLayoutManager(linearLayoutManager);
        binding.cll.recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> presenter.doItemDetailClick(position));
        adapter.setOnItemChildClickListener((adapter, v, position) -> {
            switch (v.getId()) {
                case R.id.tv_goods_title:
                case R.id.iv_image://详情
                    presenter.doItemDetailClick(position);
                    break;
                case R.id.tv_delete: //删除
                    presenter.doItemDeleteClick(position);
                    break;
                case R.id.tv_edit://编辑
                    presenter.doItemEditClick(position);
                    break;
                case R.id.tv_modify://修改密码
                    presenter.doItemModifyClick(position);
                    break;
                case R.id.tv_shelf://上架商品
                    presenter.doItemShelfClick(position);
                    break;
                case R.id.tv_obtained://下架商品
                    presenter.doItemObtainedClick(position);
                    break;
            }
        });
        binding.cll.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                presenter.doNextPage();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                presenter.doRefresh();
            }
        });
        binding.cll.ndv.setNoDataMsg("没有搜索到相关数据");
        binding.cll.ndv.setOnClickListener(v -> presenter.doRefresh());
    }


    @Override
    public void onAutoRefreshList() {
        binding.cll.recyclerview.scrollToPosition(0);
        binding.cll.refreshLayout.autoRefresh();
    }

    @Override
    public void notifyItemRangeChanged(int positionStart, int itemCount) {
        if (positionStart >= 0) {
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

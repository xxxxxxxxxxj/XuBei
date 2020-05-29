package com.xubei.shop.ui.module.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.databinding.ActAccRFagBinding;
import com.xubei.shop.databinding.CommonListLayoutBinding;
import com.xubei.shop.ui.module.account.adapter.AccRListAdapter;
import com.xubei.shop.ui.module.account.contract.AccRListContract;
import com.xubei.shop.ui.module.account.model.OutGoodsBean;
import com.xubei.shop.ui.module.account.presenter.AccRListPresenter;
import com.xubei.shop.ui.module.base.ABaseFragment;
import com.xubei.shop.utlis.LinearLayoutManager2;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 我的账号发布列表
 * date：2017/11/20 18:00
 * author：Seraph
 **/
public class AccRListFragment extends ABaseFragment<AccRListContract.Presenter> implements AccRListContract.View {


    @Inject
    public AccRListFragment() {
    }

    private ActAccRFagBinding binding;

    private CommonListLayoutBinding listBinding;

    @Override
    protected View initDataBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.act_acc_r_fag, container, false);
        return binding.getRoot();
    }

    @Inject
    AccRListPresenter presenter;

    private AccRListAdapter adapter;


    @Override
    protected AccRListPresenter getMVPPresenter() {
        return presenter;
    }


    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        //当前跳转的位置
        presenter.setPosition(getArguments() != null ? getArguments().getInt("position", 0) : 0);
        initLayout();
    }

    @Override
    public void initLayout() {
        View view = binding.vs.getViewStub().inflate();
        listBinding = DataBindingUtil.bind(view);
        presenter.start();
    }

    //初始化惰性加载后的view
    public void initInflateView(List<OutGoodsBean> list) {
        adapter = new AccRListAdapter(list);
        listBinding.ndv.setNoDataMsg("暂无商品");
        listBinding.ndv.setOnClickListener(v -> presenter.doRefresh());
        listBinding.recyclerview.setLayoutManager(new LinearLayoutManager2(getContext()));
        listBinding.recyclerview.setAdapter(adapter);
        listBinding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                presenter.doNextPage();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                presenter.doRefresh();
            }
        });
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
                case R.id.tv_update_price://一键改价
                    presenter.doItemUpdatePriceClick(position);
                    break;
            }
        });
    }


    @Override
    public void notifyItemRangeChanged(int positionStart, int itemCount) {
        if (positionStart >= 0) {
            adapter.notifyItemRangeChanged(positionStart, itemCount);
        }
    }

    @Override
    public void onAutoRefreshList() {
        listBinding.recyclerview.scrollToPosition(0);
        listBinding.refreshLayout.autoRefresh();
    }

    @Override
    public void setNoDataView(int type) {
        listBinding.refreshLayout.finishRefresh();
        listBinding.refreshLayout.finishLoadMore();
        listBinding.ndv.setType(type);
    }


    @Subscribe(tags = {@Tag(AppConstants.RxBusAction.TAG_ACCOUNT_SELLER_LIST)})
    public void autoRefresh(Boolean isRefresh) {
        onAutoRefreshList();
    }


}

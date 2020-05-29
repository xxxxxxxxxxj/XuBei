package com.xubei.shop.ui.module.order;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.KeyboardUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.databinding.ActOrderSearchBinding;
import com.xubei.shop.ui.module.base.ABaseActivity;
import com.xubei.shop.ui.module.order.adapter.OrderAllAdapter;
import com.xubei.shop.ui.module.order.contract.OrderSearchContract;
import com.xubei.shop.ui.module.order.model.OutOrderBean;
import com.xubei.shop.ui.module.order.presenter.OrderSearchPresenter;
import com.xubei.shop.utlis.LinearLayoutManager2;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 订单搜索界面
 * date：2018/3/15 14:16
 * author：Seraph
 **/
@Route(path = AppConstants.PagePath.ORDER_SEARCH)
public class OrderSearchActivity extends ABaseActivity<OrderSearchContract.Presenter> implements OrderSearchContract.View {


    private ActOrderSearchBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_order_search);
    }

    @Inject
    OrderSearchPresenter presenter;

    @Override
    protected OrderSearchPresenter getMVPPresenter() {
        return presenter;
    }

    private OrderAllAdapter orderAllAdapter;

    @Inject
    LinearLayoutManager2 linearLayoutManager;

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.etSearch.setHint("请输入搜索关键字");
        binding.appbar.ivBack.setOnClickListener(v -> onBackPressed());
        binding.appbar.tvSearch.setOnClickListener(v -> {
            presenter.doSearch(binding.appbar.etSearch.getText().toString().trim());
            KeyboardUtils.hideSoftInput(this);
        });
        presenter.start();
    }

    public void initLayout(List<OutOrderBean> list) {
        orderAllAdapter = new OrderAllAdapter(list);
        binding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                presenter.doNextPage();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                presenter.doRefresh();
            }
        });

        binding.recyclerview.setLayoutManager(linearLayoutManager);
        orderAllAdapter.setOnItemClickListener((adapter, view, position) -> presenter.doItemClick(position));
        binding.recyclerview.setAdapter(orderAllAdapter);
        orderAllAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.tv_wq:    //申请维权
                    presenter.doStartRights(position);
                    break;
                case R.id.tv_xz:    //续租
                    presenter.doCheckOrderIsPay(position);
                    break;
                case R.id.tv_gopay://去支付
                    presenter.doPayClick(position);
                    break;
                case R.id.tv_r_zy://再次租用
                    presenter.doAccClick(position);
                    break;
                case R.id.tv_wqjl://查看维权记录
                    presenter.doStartRightsDetail(position);
                    break;
                case R.id.tv_cx_wq://撤销维权
                    presenter.doCxRights(position);
                    break;
            }
        });
    }


    @Override
    public void notifyItemRangeChanged(int positionStart, int itemCount) {
        binding.refreshLayout.finishRefresh();
        binding.refreshLayout.finishLoadMore();
        if (positionStart >= 0) {
            orderAllAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }
    }

}

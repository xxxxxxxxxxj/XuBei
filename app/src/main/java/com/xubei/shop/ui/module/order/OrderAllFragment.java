package com.xubei.shop.ui.module.order;

import android.graphics.drawable.Drawable;
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
import com.xubei.shop.databinding.ActOrderAllFagBinding;
import com.xubei.shop.databinding.CommonListLayoutBinding;
import com.xubei.shop.ui.module.base.ABaseFragment;
import com.xubei.shop.ui.module.order.adapter.OrderAllAdapter;
import com.xubei.shop.ui.module.order.contract.OrderAllContract;
import com.xubei.shop.ui.module.order.model.OutOrderBean;
import com.xubei.shop.ui.module.order.presenter.OrderAllPresenter;
import com.xubei.shop.utlis.LinearLayoutManager2;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;

/**
 * 我的订单分类
 * date：2017/11/20 18:00
 * author：Seraph
 **/

public class OrderAllFragment extends ABaseFragment<OrderAllContract.Presenter> implements OrderAllContract.View {

    @Inject
    public OrderAllFragment() {
    }

    private ActOrderAllFagBinding binding;

    private CommonListLayoutBinding listBinding;

    @Override
    protected View initDataBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.act_order_all_fag, container, false);
        return binding.getRoot();
    }

    @Inject
    OrderAllPresenter presenter;


    @Override
    protected OrderAllPresenter getMVPPresenter() {
        return presenter;
    }

    private OrderAllAdapter orderAllAdapter;

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        presenter.setPosition(getArguments() != null ? getArguments().getInt("position", 0) : 0);
        initLayout();
    }

    @Override
    public void initLayout() {
        View view = binding.vsList.getViewStub().inflate();
        listBinding = DataBindingUtil.bind(view);
        presenter.start();
    }

    //初始化惰性加载后的view
    public void initInflateView(List<OutOrderBean> list) {
        orderAllAdapter = new OrderAllAdapter(list);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.act_acc_list_divider);
        if (drawable != null) {
            decoration.setDrawable(drawable);
        }
        listBinding.recyclerview.addItemDecoration(decoration);
        listBinding.recyclerview.setLayoutManager(new LinearLayoutManager2(getContext()));
        listBinding.recyclerview.setAdapter(orderAllAdapter);
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
        listBinding.ndv.setNoDataMsg("暂无订单，赶紧去下单吧");
        listBinding.ndv.setOnClickListener(v -> presenter.doRefresh());
        orderAllAdapter.setOnItemClickListener((adapter, view, position) -> presenter.doItemClick(position));
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


    //自动加载
    @Override
    public void autoRefresh() {
        listBinding.recyclerview.scrollToPosition(0);
        listBinding.refreshLayout.autoRefresh();
    }


    @Override
    public void notifyItemRangeChanged(int positionStart, int itemCount) {
        if (positionStart >= 0) {
            orderAllAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }
    }


    @Override
    public void setNoDataView(int type) {
        listBinding.refreshLayout.finishRefresh();
        listBinding.refreshLayout.finishLoadMore();
        listBinding.ndv.setType(type);
    }

    @Subscribe(tags = {@Tag(AppConstants.RxBusAction.TAG_ORDER_LIST)})
    public void refreshList(Boolean aBoolean) {
        if (aBoolean) {
            autoRefresh();
        }
    }

}

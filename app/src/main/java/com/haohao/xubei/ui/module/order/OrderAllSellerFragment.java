package com.haohao.xubei.ui.module.order;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.databinding.ActOrderAllFagBinding;
import com.haohao.xubei.databinding.CommonListLayoutBinding;
import com.haohao.xubei.ui.module.base.ABaseFragment;
import com.haohao.xubei.ui.module.order.adapter.OrderAllSellerAdapter;
import com.haohao.xubei.ui.module.order.contract.OrderAllSellerContract;
import com.haohao.xubei.ui.module.order.model.OutOrderBean;
import com.haohao.xubei.ui.module.order.presenter.OrderAllSellerPresenter;
import com.haohao.xubei.utlis.LinearLayoutManager2;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;


/**
 * 我的出租订单
 * date：2017/11/20 18:00
 * author：Seraph
 **/
public class OrderAllSellerFragment extends ABaseFragment<OrderAllSellerContract.Presenter> implements OrderAllSellerContract.View {

    private ActOrderAllFagBinding binding;

    private CommonListLayoutBinding listBinding;

    @Inject
    public OrderAllSellerFragment() {
    }


    @Override
    protected View initDataBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.act_order_all_fag, container, false);
        return binding.getRoot();
    }

    @Inject
    OrderAllSellerPresenter presenter;


    private OrderAllSellerAdapter orderAllAdapter;


    @Override
    protected OrderAllSellerPresenter getMVPPresenter() {
        return presenter;
    }

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
        orderAllAdapter = new OrderAllSellerAdapter(list);
        listBinding.ndv.setNoDataMsg("暂无订单");
        listBinding.ndv.setOnClickListener(v -> presenter.doRefresh());
        listBinding.recyclerview.setLayoutManager(new LinearLayoutManager2(getContext()));
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.act_acc_list_divider);
        if (drawable != null) {
            decoration.setDrawable(drawable);
        }
        listBinding.recyclerview.addItemDecoration(decoration);
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

        orderAllAdapter.setOnItemClickListener((adapter, view, position) -> presenter.doItemClick(position));
        orderAllAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.tv_rights_apply:    //申请维权
                    presenter.doRightsApply(position);
                    break;
                case R.id.tv_rights_handling:    //处理维权
                    presenter.doRightsHandling(position);
                    break;
                case R.id.tv_rights_look://查看维权记录
                    presenter.doRightsLook(position);
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

    @Subscribe(tags = {@Tag(AppConstants.RxBusAction.TAG_ORDER_SELLER_LIST)})
    public void autoRefresh(Boolean isRefresh) {
        autoRefresh();
    }

}

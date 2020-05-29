package com.xubei.shop.ui.module.order;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.databinding.ActOrderAllSellerBinding;
import com.xubei.shop.di.QualifierType;
import com.xubei.shop.ui.module.base.ABaseActivity;
import com.xubei.shop.ui.module.base.IABaseContract;
import com.xubei.shop.ui.module.order.adapter.OrderSellerVPAdapter;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 我发布的全部订单
 * date：2017/12/4 15:09
 * author：Seraph
 *
 **/
@Route(path = AppConstants.PagePath.ORDER_SELLER_ALL,extras = AppConstants.ARAction.LOGIN)
public class OrderAllSellerActivity extends ABaseActivity<IABaseContract.ABasePresenter> {

    private ActOrderAllSellerBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_order_all_seller);
    }

    @Override
    protected IABaseContract.ABasePresenter getMVPPresenter() {
        return null;
    }

    @Inject
    OrderSellerVPAdapter vpAdapter;

    @Inject
    @QualifierType("type")
    Integer type;

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("我的出租订单");
        initLayout();
    }

    private void initLayout() {
        binding.vpOrder.setAdapter(vpAdapter);
        binding.xtlOrder.setupWithViewPager(binding.vpOrder);
        binding.vpOrder.setCurrentItem(type);
    }

}
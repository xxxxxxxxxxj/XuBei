package com.haohao.xubei.ui.module.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.TimeUtils;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.databinding.ActOrderRDetailBinding;
import com.haohao.xubei.ui.module.account.model.OutGoodsDetailBean;
import com.haohao.xubei.ui.module.base.ABaseActivity;
import com.haohao.xubei.ui.module.order.contract.OrderRDetailContract;
import com.haohao.xubei.ui.module.order.model.OutOrderBean;
import com.haohao.xubei.ui.module.order.presenter.OrderRDetailPresenter;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;


/**
 * 出租订单详情
 * date：2017/12/6 16:33
 * author：Seraph
 **/
@Route(path = AppConstants.PagePath.ORDER_R_DETAIL)
public class OrderRDetailActivity extends ABaseActivity<OrderRDetailContract.Presenter> implements OrderRDetailContract.View {


    private ActOrderRDetailBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_order_r_detail);
    }

    @Inject
    OrderRDetailPresenter presenter;

    @Override
    protected OrderRDetailContract.Presenter getMVPPresenter() {
        return presenter;
    }


    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("出租订单详情");
        binding.ndv.setOnClickListener(v -> presenter.start());
        presenter.start();
    }


    @Override
    public void setOrderBean(OutOrderBean orderBean) {
        if (orderBean == null) {
            return;
        }
        //标题
        binding.tvTitle.setText(orderBean.goodTitle);
        //租赁开始时间
        binding.tvStartTime.setText(String.format(Locale.getDefault(), "租赁开始时间：%s", orderBean.beginTime != null ? TimeUtils.millis2String(orderBean.beginTime) : "--"));
        //租赁结束时间
        binding.tvEndTime.setText(String.format(Locale.getDefault(), "租赁结束时间：%s", orderBean.endTime != null ? TimeUtils.millis2String(orderBean.endTime) : "--"));
        //订单编号
        binding.tvOrderNumber.setText(String.format(Locale.getDefault(), "订单编号：%s", orderBean.gameNo));
        //商品编号
        binding.tvProductNumber.setText(String.format(Locale.getDefault(), "商品编号：%s", orderBean.goodCode));
        //下单时间
        binding.tvOrderTime.setText(String.format(Locale.getDefault(), "下单时间：%s", orderBean.createTime != null ? TimeUtils.millis2String(orderBean.createTime) : "--"));
        //实付金额
        binding.tvActualAmount.setText(String.format(Locale.getDefault(), "¥%s", orderBean.orderAllAmount));
        //订单押金
        binding.tvOrderDeposit.setText(String.format(Locale.getDefault(), "¥%s", orderBean.orderForegiftAmount));
        //订单金额
        binding.tvOrderAmount.setText(String.format(Locale.getDefault(), "¥%s", orderBean.orderAllAmount));
        //展示商品的属性
        //动态渲染字段
        List<OutGoodsDetailBean.NewGoodsWzDtoBean> protoTypeBeanList = orderBean.outGoodsDetail.prototypelist;
        if (protoTypeBeanList != null && protoTypeBeanList.size() > 0) {
            binding.llGoodsAttr.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(this);
            for (OutGoodsDetailBean.NewGoodsWzDtoBean protoTypeBean : protoTypeBeanList) {
                View view = inflater.inflate(R.layout.act_acc_detail_goods_attr, binding.llGoodsAttr, false);
                TextView key = view.findViewById(R.id.tv_key);
                TextView value = view.findViewById(R.id.tv_value);
                key.setText(protoTypeBean.keyName);
                value.setText(protoTypeBean.getStrValue());
                binding.llGoodsAttr.addView(view);
            }
        }

    }

    @Override
    public void setNoDataView(int type) {
        binding.ndv.setType(type);
    }

}

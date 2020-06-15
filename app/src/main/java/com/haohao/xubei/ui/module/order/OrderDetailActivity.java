package com.haohao.xubei.ui.module.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.haohao.xubei.AppConfig;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.data.network.glide.GlideApp;
import com.haohao.xubei.databinding.ActOrderDetailBinding;
import com.haohao.xubei.ui.module.base.ABaseActivity;
import com.haohao.xubei.ui.module.order.contract.OrderDetailContract;
import com.haohao.xubei.ui.module.order.model.OutOrderBean;
import com.haohao.xubei.ui.module.order.presenter.OrderDetailPresenter;
import com.haohao.xubei.utlis.Tools;

import java.math.BigDecimal;
import java.util.Locale;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;


/**
 * 订单详情
 * date：2017/12/6 16:33
 * author：Seraph
 **/
@Route(path = AppConstants.PagePath.ORDER_DETAIL)
public class OrderDetailActivity extends ABaseActivity<OrderDetailContract.Presenter> implements OrderDetailContract.View {


    private ActOrderDetailBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_order_detail);
        binding.setActivity(this);
    }

    @Inject
    OrderDetailPresenter presenter;

    @Override
    protected OrderDetailContract.Presenter getMVPPresenter() {
        return presenter;
    }


    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("订单详情");
        RxToolbar.itemClicks(binding.appbar.toolbar).as(bindLifecycle()).subscribe(menuItem ->
                ARouter.getInstance().build(AppConstants.PagePath.COMM_AGENTWEB)
                        .withString("title", "查看上号步骤")
                        .withString("webUrl", AppConstants.AgreementAction.DEVICE_STEPS)
                        .navigation()
        );
        binding.ndv.setOnClickListener(v -> presenter.start());
        presenter.start();
    }

    private final int SCAN_QC = 100;

    //点击事件
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_copy_login_code://复制订单到剪切板
                presenter.doCopyOrderNo();
                break;
            case R.id.tv_copy_game_user://复制用户名
                presenter.doCopyUser();
                break;
            case R.id.tv_copy_game_parssword://复制密码
                presenter.doCopyParssword();
                break;
            case R.id.tv_scan://扫一扫
                ARouter.getInstance().build(AppConstants.PagePath.COMM_CAPTURE).navigation(this, SCAN_QC);
                break;
            case R.id.cl_game://跳转商品详情
                presenter.doAccClick();
                break;
            case R.id.ll_kf://联系客服
                Tools.startQQCustomerService(this, AppConfig.SERVICE_QQ);
                break;
        }

    }


    @Override
    public void setOrderBean(OutOrderBean orderBean) {
        if (orderBean == null) {
            return;
        }
        binding.tvStatus.setText(orderBean.getOrderStatusText());//订单状态
        //订单编号
        binding.tvOrderNo.setText(orderBean.gameNo);
        //订单租金
        double rent = BigDecimal.valueOf(Double.valueOf(orderBean.orderAllAmount)).subtract(BigDecimal.valueOf(Double.valueOf(orderBean.orderForegiftAmount))).doubleValue();
        binding.tvForegiftAmount.setText(String.format(Locale.getDefault(), "¥%.2f", rent));
        //订单押金
        binding.tvForegift.setText(String.format(Locale.getDefault(), "¥%s", orderBean.orderForegiftAmount));
        //订单合计金额
        binding.tvAmount.setText(String.format(Locale.getDefault(), "¥%s", orderBean.orderAllAmount));
        //租赁时间开始
        binding.tvBeginTime.setText(TimeUtils.millis2String(orderBean.beginTime));
        //结束
        binding.tvEndTime.setText(TimeUtils.millis2String(orderBean.endTime));

        //上号方式
        if ("1".equals(orderBean.outGoodsDetail.isShow)) {
            binding.tvGameLoginMode.setText("账号密码登录");
            //显示账号密码登录
            binding.llGameAcc.setVisibility(View.VISIBLE);
            //判断订单状态，如果不是租赁中，则使用****代替
            if (orderBean.orderStatus == 2) {
                binding.tvGameUser.setText(orderBean.outGoodsDetail.gameAccount);//游戏账号
                binding.tvGameParssword.setText(orderBean.outGoodsDetail.gamePwd);//游戏密码
            } else {
                binding.tvGameUser.setText("******");//游戏账号
                binding.tvGameParssword.setText("******");//游戏密码
            }
        } else {
            binding.tvGameLoginMode.setText("上号器登录");
            //显示登录码
            binding.llLoginCode.setVisibility(View.VISIBLE);
            if (orderBean.orderStatus == 2) {
                binding.tvLoginCode.setText(orderBean.gameNo);//设置登录码
                //显示扫一扫
                binding.tvScan.setVisibility(View.VISIBLE);
            } else {
                binding.tvLoginCode.setText("******");//设置登录码
            }

//            if (orderBean.orderStatus == 2 && "王者荣耀".equals(orderBean.bigGameName)) {
//                binding.btnAutoLogin.setVisibility(View.VISIBLE);
//            }
        }
        //商品名称
        binding.tvGoodsTitle.setText(orderBean.outGoodsDetail.goodsTitle);
        //游戏区服
        binding.tvGameName.setText(orderBean.outGoodsDetail.gameAllName);
        //图片
        if (ObjectUtils.isEmpty(orderBean.imagePath) || !orderBean.imagePath.contains("http")) {
            orderBean.imagePath = "http:" + orderBean.imagePath;
        }
        GlideApp.with(this).load(orderBean.imagePath).override(140).circleCrop().transform(new RoundedCorners(8)).into(binding.ivGameImage);
    }

    @Override
    public void setNoDataView(int type) {
        binding.ndv.setType(type);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_order_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Subscribe(tags = {@Tag(AppConstants.RxBusAction.TAG_ORDER_DETAIL)})
    public void updateOrderDetail(Boolean isUpdate) {
        presenter.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCAN_QC && resultCode == Activity.RESULT_OK) {
            int ResultType = data.getIntExtra(CodeUtils.RESULT_TYPE, -1);
            String resultStr = data.getStringExtra(CodeUtils.RESULT_STRING);
            //调用登录确认弹框
            presenter.onPCLogin(ResultType, resultStr);
        }
    }
}

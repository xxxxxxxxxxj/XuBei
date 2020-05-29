package com.xubei.shop.ui.module.user;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.databinding.ActRcPopBinding;
import com.xubei.shop.databinding.ActRedemptionCenterBinding;
import com.xubei.shop.ui.module.base.ABaseActivity;
import com.xubei.shop.ui.module.user.adapter.ActRcPopAdapter;
import com.xubei.shop.ui.module.user.adapter.RedemptionCenterAdapter;
import com.xubei.shop.ui.module.user.contract.RedemptionCenterContract;
import com.xubei.shop.ui.module.user.model.RedemptionCenterBean;
import com.xubei.shop.ui.module.user.model.RedemptionGameBean;
import com.xubei.shop.ui.module.user.presenter.RedemptionCenterPresenter;
import com.xubei.shop.utlis.LinearLayoutManager2;
import com.xubei.shop.utlis.Tools;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 兑换中心
 * date：2018/12/5 17:25
 * author：xiongj
 **/
@Route(path = AppConstants.PagePath.USER_REDEMPTIONCENTER, extras = AppConstants.ARAction.LOGIN)
public class RedemptionCenterActivity extends ABaseActivity<RedemptionCenterContract.Presenter> implements RedemptionCenterContract.View {

    private ActRedemptionCenterBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_redemption_center);
    }

    @Inject
    RedemptionCenterPresenter presenter;

    @Override
    protected RedemptionCenterContract.Presenter getMVPPresenter() {
        return presenter;
    }

    @Inject
    LinearLayoutManager2 layoutManager2;

    private RedemptionCenterAdapter adapter;

    @Override
    protected void initCreate(@Nullable Bundle savedInstanceState) {
        binding.ctl.toolbar.setTitle("兑换中心");
        RxToolbar.itemClicks(binding.ctl.toolbar).as(bindLifecycle()).subscribe(menuItem ->
                ARouter.getInstance().build(AppConstants.PagePath.USER_REDEMPTIONRECORD).navigation());
        presenter.start();
    }


    @Override
    public void initLayout(List<RedemptionCenterBean> list) {
        adapter = new RedemptionCenterAdapter(list);
        binding.cll.recyclerview.setLayoutManager(layoutManager2);
        binding.cll.recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> presenter.onItemClick(position));
        binding.cll.refreshLayout.setOnRefreshListener(refreshLayout -> presenter.onRefresh());
        binding.cll.ndv.setNoDataMsg(R.mipmap.icon_no_info_dh, "暂无优惠券信息哦~");
        binding.cll.ndv.setOnClickListener(v -> presenter.onRefresh());
    }


    @Override
    public void setNoDataView(int type) {
        binding.cll.refreshLayout.finishRefresh();
        binding.cll.ndv.setType(type);
    }


    @Override
    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dhjl, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private PopupWindow popWin;

    //显示游列表戏兑换
    public void showExchangePop(List<RedemptionGameBean> list) {
        ActRcPopAdapter actRcPopAdapter = new ActRcPopAdapter(list);
        ActRcPopBinding popBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.act_rc_pop, null, false);
        popBinding.rvAccType.setLayoutManager(new LinearLayoutManager2(this));
        popBinding.rvAccType.setAdapter(actRcPopAdapter);
        actRcPopAdapter.setOnItemClickListener((adapter, view, position) -> {
            actRcPopAdapter.setSelectP(position);
            actRcPopAdapter.notifyDataSetChanged();
        });
        popBinding.btnOk.setOnClickListener(v -> presenter.doDHPosition(actRcPopAdapter.getSelectP()));
        popWin = new PopupWindow(popBinding.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popWin.setAnimationStyle(R.style.common_pop_head_selected);
        popWin.setFocusable(true);
        popWin.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popWin.setOutsideTouchable(true);
        popWin.setOnDismissListener(() -> Tools.setWindowAlpha(this, 1f));
        popBinding.ivClose.setOnClickListener(v -> popWin.dismiss());
        if (!popWin.isShowing()) {
            popWin.showAtLocation(binding.ctl.appbar, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            Tools.setWindowAlpha(this, 0.5f);
        }
    }

    @Override
    public void onAutoRefresh() {
        //隐藏pop
        if (popWin != null && popWin.isShowing()) {
            popWin.dismiss();
        }
        binding.cll.recyclerview.scrollToPosition(0);
        presenter.onRefresh();
    }

}

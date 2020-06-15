package com.haohao.xubei.ui.module.user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.databinding.ActRedemptionRecordBinding;
import com.haohao.xubei.ui.module.base.ABaseActivity;
import com.haohao.xubei.ui.module.user.adapter.RedemptionRecordAdapter;
import com.haohao.xubei.ui.module.user.contract.RedemptionRecordContract;
import com.haohao.xubei.ui.module.user.model.RedemptionRecordBean;
import com.haohao.xubei.ui.module.user.presenter.RedemptionRecordPresenter;
import com.haohao.xubei.utlis.LinearLayoutManager2;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;

/**
 * 兑换记录
 * date：2018/12/5 17:25
 * author：xiongj
 **/
@Route(path = AppConstants.PagePath.USER_REDEMPTIONRECORD)
public class RedemptionRecordActivity extends ABaseActivity<RedemptionRecordContract.Presenter> implements RedemptionRecordContract.View {

    private static final int SCAN_QC = 100;

    private ActRedemptionRecordBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_redemption_record);
    }

    @Inject
    RedemptionRecordPresenter presenter;

    @Override
    protected RedemptionRecordContract.Presenter getMVPPresenter() {
        return presenter;
    }

    @Inject
    LinearLayoutManager2 layoutManager2;

    private RedemptionRecordAdapter adapter;

    @Override
    protected void initCreate(@Nullable Bundle savedInstanceState) {
        binding.ctl.toolbar.setTitle("兑换记录");
        RxToolbar.itemClicks(binding.ctl.toolbar).as(bindLifecycle()).subscribe(menuItem -> {
            ARouter.getInstance().build(AppConstants.PagePath.COMM_AGENTWEB)
                    .withString("title", "查看上号步骤")
                    .withString("webUrl", AppConstants.AgreementAction.DEVICE_STEPS)
                    .navigation();
        });
        presenter.start();
    }

    @Override
    public void initLayout(List<RedemptionRecordBean> list) {
        adapter = new RedemptionRecordAdapter(list);
        binding.cll.recyclerview.setLayoutManager(layoutManager2);
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.act_divider_redemption_record);
        decoration.setDrawable(drawable);
        binding.cll.recyclerview.addItemDecoration(decoration);
        binding.cll.recyclerview.setAdapter(adapter);
        binding.cll.refreshLayout.setOnRefreshListener(refreshLayout -> presenter.onRefresh());
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.tv_login_code_copy:
                    presenter.doCopyCode(position);
                    break;
                case R.id.tv_acc_copy:
                    presenter.doCopyAcc(position);
                    break;
                case R.id.tv_pw_copy:
                    presenter.doCopyPw(position);
                    break;
                case R.id.tv_scan:
                    presenter.doScan(position);
                    break;
                case R.id.tv_ty:
                    presenter.doTy(position);
                    break;
            }
        });
        binding.cll.ndv.setNoDataMsg(R.mipmap.icon_no_info_dh, "暂无兑换信息哦~");
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
        getMenuInflater().inflate(R.menu.menu_act_order_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //扫一扫
    public void onScan() {
        ARouter.getInstance().build(AppConstants.PagePath.COMM_CAPTURE).navigation(this, SCAN_QC);
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

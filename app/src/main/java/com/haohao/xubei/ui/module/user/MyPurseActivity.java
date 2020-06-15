package com.haohao.xubei.ui.module.user;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.databinding.ActUserPurseBinding;
import com.haohao.xubei.ui.module.base.ABaseActivity;
import com.haohao.xubei.ui.module.user.contract.PurseContract;
import com.haohao.xubei.ui.module.user.model.AcctManageBean;
import com.haohao.xubei.ui.module.user.presenter.PursePresenter;

import java.util.Locale;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 我的钱包
 * date：2017/12/4 15:08
 * author：Seraph
 **/
@Route(path = AppConstants.PagePath.USER_MYPURSE, extras = AppConstants.ARAction.LOGIN)
public class MyPurseActivity extends ABaseActivity<PurseContract.Presenter> implements PurseContract.View {

    private ActUserPurseBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_user_purse);
        binding.setActivity(this);
    }

    @Inject
    PursePresenter presenter;

    @Override
    protected PurseContract.Presenter getMVPPresenter() {
        return presenter;
    }

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("我的钱包");
        RxToolbar.itemClicks(binding.appbar.toolbar)
                .as(bindLifecycle())
                .subscribe(menuItem -> ARouter.getInstance().build(AppConstants.PagePath.USER_FUNDDETAILS).navigation());
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void setAccountMoney(AcctManageBean acctManageBean) {
        //可用
        binding.tvAvailableaMount.setText(String.format(Locale.getDefault(), "%,.2f", acctManageBean.aviableAmt));
        //冻结
        binding.tvFreezeaMount.setText(String.format(Locale.getDefault(), "%,.2f", acctManageBean.freezeAmt));
        //全部余额
        binding.textView118.setText(String.format(Locale.getDefault(), "%,.2f", acctManageBean.acctAmt));
    }


    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_recharge://账号充值
                presenter.doAccountInfo(true, 1);
                break;
            case R.id.btn_withdraw://提现
                presenter.doAccountInfo(true, 2);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mx, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
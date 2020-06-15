package com.haohao.xubei.ui.module.account;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.databinding.ActAccRSuccessBinding;
import com.haohao.xubei.ui.module.account.contract.AccRSuccessContract;
import com.haohao.xubei.ui.module.base.ABaseActivity;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 账号发布成功界面
 * date：2018/8/8 14:02
 * author：Seraph
 *
 **/
@Route(path = AppConstants.PagePath.ACC_R_SUCCESS)
public class AccRSuccessActivity extends ABaseActivity<AccRSuccessContract.Presenter> {


    private ActAccRSuccessBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_acc_r_success);
    }

    @Override
    protected AccRSuccessContract.Presenter getMVPPresenter() {
        return null;
    }

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("发布完成");
        binding.tvContinueRelease.setOnClickListener(v -> finish());
        binding.tvLookProgress.setOnClickListener(v -> {
            ARouter.getInstance().build(AppConstants.PagePath.ACC_R_LIST).navigation();
            finish();
        });
    }
}

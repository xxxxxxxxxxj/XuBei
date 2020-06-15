package com.haohao.xubei.ui.module.account;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.databinding.ActAccRAgreementBinding;
import com.haohao.xubei.ui.module.account.contract.AccRAgreementContract;
import com.haohao.xubei.ui.module.base.ABaseActivity;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 账号发布协议
 * date：2018/8/8 14:02
 * author：Seraph
 *
 **/
@Route(path = AppConstants.PagePath.ACC_R_AGREEMENT)
public class AccRAgreementActivity extends ABaseActivity<AccRAgreementContract.Presenter> {


    private ActAccRAgreementBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_acc_r_agreement);
    }

    @Override
    protected AccRAgreementContract.Presenter getMVPPresenter() {
        return null;
    }

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
            binding.appbar.toolbar.setTitle("虚拟资产发布协议");
            binding.tvStr.setText(R.string.str_release_agreement);
    }
}

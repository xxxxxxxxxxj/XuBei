package com.xubei.shop.ui.module.account;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.databinding.ActAccLeaseNoticeBinding;
import com.xubei.shop.ui.module.base.ABaseActivity;
import com.xubei.shop.ui.module.base.IABaseContract;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 租赁须知
 * date：2018/10/25 10:00
 * author：xiongj
 **/
@Route(path = AppConstants.PagePath.ACC_LEASENOTICE)
public class AccLeaseNoticeActivity extends ABaseActivity<IABaseContract.ABasePresenter> implements IABaseContract.IBaseView {

    private ActAccLeaseNoticeBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_acc_lease_notice);
    }

    @Override
    protected IABaseContract.ABasePresenter getMVPPresenter() {
        return null;
    }

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("租赁须知");
    }
}

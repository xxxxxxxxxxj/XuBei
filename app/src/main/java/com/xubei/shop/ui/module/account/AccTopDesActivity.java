package com.xubei.shop.ui.module.account;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.databinding.ActAccTopDesBinding;
import com.xubei.shop.ui.module.base.ABaseActivity;
import com.xubei.shop.ui.module.base.IABaseContract;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 置顶说明
 * date：2018/12/5 15:59
 * author：xiongj
 **/
@Route(path = AppConstants.PagePath.ACC_TOPDES)
public class AccTopDesActivity extends ABaseActivity<IABaseContract.ABasePresenter> implements IABaseContract.IBaseView {

    private ActAccTopDesBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_acc_top_des);
    }

    @Override
    protected IABaseContract.ABasePresenter getMVPPresenter() {
        return null;
    }

    @Override
    protected void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("置顶说明");
    }
}

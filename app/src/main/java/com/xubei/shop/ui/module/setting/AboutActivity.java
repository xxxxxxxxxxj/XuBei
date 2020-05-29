package com.xubei.shop.ui.module.setting;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.databinding.ActSettingAboutBinding;
import com.xubei.shop.ui.module.base.ABaseActivity;
import com.xubei.shop.ui.module.base.IABaseContract;
import com.xubei.shop.ui.module.setting.contract.SettingContract;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;


/**
 * 关于
 * date：2017/12/4 14:25
 * author：Seraph
 **/
@Route(path = AppConstants.PagePath.SETTING_ABOUT)
public class AboutActivity extends ABaseActivity<IABaseContract.ABasePresenter> implements IABaseContract.IBaseView {

    private ActSettingAboutBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_setting_about);
    }

    @Override
    protected SettingContract.Presenter getMVPPresenter() {
        return null;
    }

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("关于虚贝租号");
    }

}

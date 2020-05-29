package com.xubei.shop.ui.module.welcome;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.data.network.glide.GlideApp;
import com.xubei.shop.databinding.ActWelcomeBinding;
import com.xubei.shop.ui.module.base.ABaseActivity;

import java.util.Locale;

import javax.inject.Inject;


/**
 * 欢迎页
 * date：2017/5/3 09:50
 * author：Seraph
 **/
@Route(path = AppConstants.PagePath.WELCOME_HOME)
public class WelcomeActivity extends ABaseActivity<WelcomeActivityContract.Presenter> implements WelcomeActivityContract.View {

    private ActWelcomeBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_welcome);
        binding.setActivity(this);
    }

    @Inject
    WelcomeActivityPresenter presenter;

    @Override
    protected WelcomeActivityPresenter getMVPPresenter() {
        return presenter;
    }

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        presenter.start();
    }

    @Override
    public void setADImageUrl(String adUrl) {
        //显示跳过
        binding.btnJump.setVisibility(View.VISIBLE);
        GlideApp.with(this).load(adUrl).placeholder(R.drawable.welcome_bg).error(R.drawable.welcome_bg).into(binding.ivAd);
    }

    @Override
    public void setADShowCount(long tempCount) {
        binding.btnJump.setText(String.format(Locale.getDefault(), "跳过广告 %ds", tempCount));
    }

    @Override
    public void jumpNextActivity() {
        //如果已经不是第一次则跳转主界面。如果是第一次，跳转引导页
        if (presenter.isFirstOpenApp()) {
            ARouter.getInstance().build(AppConstants.PagePath.WELCOME_GUIDEPAGES).navigation();
        } else {
            ARouter.getInstance().build(AppConstants.PagePath.APP_MAIN).navigation();
        }
    }

    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_ad://跳转广告
                presenter.doJumpAD();
                break;
            case R.id.btn_jump://跳过
                jumpNextActivity();
                break;
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        jumpNextActivity();
    }
}

package com.haohao.xubei.ui.module.setting;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.databinding.ActSettingBinding;
import com.haohao.xubei.ui.module.base.ABaseActivity;
import com.haohao.xubei.ui.module.setting.contract.SettingContract;
import com.haohao.xubei.ui.module.setting.presenter.SettingPresenter;

import java.util.Locale;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;


/**
 * 设置界面
 * date：2017/12/4 14:25
 * author：Seraph
 **/
@Route(path = AppConstants.PagePath.SETTING_HOME, extras = AppConstants.ARAction.LOGIN)
public class SettingActivity extends ABaseActivity<SettingContract.Presenter> implements SettingContract.View {

    private ActSettingBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_setting);
        binding.setActivity(this);
    }

    @Inject
    SettingPresenter presenter;

    @Override
    protected SettingContract.Presenter getMVPPresenter() {
        return presenter;
    }

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("设置");
        presenter.start();
    }

    @Override
    public void setAppCode(String appVersionName, String businessNo) {
        binding.tvCode.setText(String.format(Locale.getDefault(), "v%s.%s", appVersionName, businessNo));
    }


    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_item1://修改账号密码
                ARouter.getInstance().build(AppConstants.PagePath.LOGIN_RESETPASSWORD).navigation();
                break;
            case R.id.ll_item2://修改支付密码
                ARouter.getInstance().build(AppConstants.PagePath.LOGIN_RESETPAYPW).navigation();
                break;
            case R.id.ll_item3://检查更新
                presenter.checkUpgrade();
                break;
            case R.id.ll_item4://关于我们
                ARouter.getInstance().build(AppConstants.PagePath.SETTING_ABOUT).navigation();
                break;
            case R.id.ll_item5://新消息通知开关
                ARouter.getInstance().build(AppConstants.PagePath.SETTING_NEWMSG).navigation();
                break;
            case R.id.btn_log_out://注销
                presenter.doLogOut();
                break;
        }
    }

}

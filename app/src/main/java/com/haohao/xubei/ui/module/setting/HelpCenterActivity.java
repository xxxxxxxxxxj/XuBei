package com.haohao.xubei.ui.module.setting;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;
import com.haohao.xubei.AppConfig;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.databinding.ActSettingHelpCenterBinding;
import com.haohao.xubei.ui.module.base.ABaseActivity;
import com.haohao.xubei.ui.module.setting.adapter.HelpCenterAdapter;
import com.haohao.xubei.ui.module.setting.contract.HelpCenterContract;
import com.haohao.xubei.ui.module.setting.model.ProblemBean;
import com.haohao.xubei.ui.module.setting.presenter.HelpCenterPresenter;
import com.haohao.xubei.utlis.Tools;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

/**
 * 帮助中心
 * date：2018/7/31 14:17
 * author：Seraph
 **/
@Route(path = AppConstants.PagePath.SETTING_HELPCENTER)
public class HelpCenterActivity extends ABaseActivity<HelpCenterContract.Presenter> implements HelpCenterContract.View {


    private ActSettingHelpCenterBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_setting_help_center);
        binding.setActivity(this);
    }

    @Inject
    HelpCenterPresenter presenter;

    @Override
    protected HelpCenterContract.Presenter getMVPPresenter() {
        return presenter;
    }

    @Inject
    GridLayoutManager layoutManager;

    @Inject
    HelpCenterAdapter adapter;

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("帮助中心");
        binding.recyclerview.setLayoutManager(layoutManager);
        binding.recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            ProblemBean bean = (ProblemBean) adapter.getItem(position);
            ARouter.getInstance()
                    .build(AppConstants.PagePath.SETTING_HELPLIST)
                    .withSerializable("bean", bean)
                    .navigation();
        });
        RxToolbar.itemClicks(binding.appbar.toolbar).as(bindLifecycle()).subscribe(menuItem -> Tools.startQQCustomerService(this, AppConfig.SERVICE_QQ));
    }

    public void onViewClicked(View view) {
        presenter.doCopyAddress();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_help_center, menu);
        return super.onCreateOptionsMenu(menu);
    }

}

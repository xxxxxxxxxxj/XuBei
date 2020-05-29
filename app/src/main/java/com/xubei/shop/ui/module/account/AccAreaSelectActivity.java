package com.xubei.shop.ui.module.account;

import android.os.Bundle;
import android.view.Menu;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.databinding.ActAccAsBinding;
import com.xubei.shop.ui.module.account.adapter.AccASAdapter;
import com.xubei.shop.ui.module.account.contract.AccASContract;
import com.xubei.shop.ui.module.account.model.GameAreaBean;
import com.xubei.shop.ui.module.account.presenter.AccASPresenter;
import com.xubei.shop.ui.module.base.ABaseActivity;
import com.xubei.shop.utlis.LinearLayoutManager2;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 账号大区选择
 * date：2017/12/18 15:10
 * author：Seraph
 *
 **/
@Route(path = AppConstants.PagePath.ACC_AREASELECT)
public class AccAreaSelectActivity extends ABaseActivity<AccASPresenter> implements AccASContract.View {

    private ActAccAsBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_acc_as);
    }

    @Inject
    AccASPresenter presenter;

    @Override
    protected AccASPresenter getMVPPresenter() {
        return presenter;
    }

    @Inject
    AccASAdapter mAdapter;

    @Inject
    LinearLayoutManager2 linearLayoutManager;

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("选择区服");
        initLayout();
        presenter.start();
    }

    private void initLayout() {
        binding.rv.setLayoutManager(linearLayoutManager);
        binding.rv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            GameAreaBean gameAreaBean = (GameAreaBean) adapter.getItem(position);
            presenter.onItemClick(gameAreaBean);
        });
        RxToolbar.itemClicks(binding.appbar.toolbar).as(bindLifecycle()).subscribe(menuItem -> presenter.doSave());
    }

    @Override
    public void setListDate(List<GameAreaBean> gameBeans) {
        mAdapter.replaceData(gameBeans);
    }

    @Override
    public void setSelectItem(GameAreaBean selectBean) {
        mAdapter.setSelectItem(selectBean);
    }


    @Override
    public void onBackPressed() {
        presenter.doBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }
}

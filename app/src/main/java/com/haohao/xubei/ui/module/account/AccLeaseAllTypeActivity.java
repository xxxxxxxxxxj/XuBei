package com.haohao.xubei.ui.module.account;

import android.os.Bundle;
import android.view.Menu;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.androidkun.xtablayout.XTabLayout;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.databinding.ActAccLeaseAllTypeBinding;
import com.haohao.xubei.ui.module.account.adapter.AccLeaseAdapter;
import com.haohao.xubei.ui.module.account.adapter.AccLeaseLetterAdapter;
import com.haohao.xubei.ui.module.account.contract.AccLeaseAllTypeContract;
import com.haohao.xubei.ui.module.account.model.GameBean;
import com.haohao.xubei.ui.module.account.presenter.AccLeaseAllTypePresenter;
import com.haohao.xubei.ui.module.base.ABaseActivity;
import com.haohao.xubei.ui.module.main.model.GameTypeBean;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

/**
 * 账号租赁所有类型
 * date：2018/10/9 16:54
 * author：xiongj
 **/
@Route(path = AppConstants.PagePath.ACC_LEASEALLTYPE)
public class AccLeaseAllTypeActivity extends ABaseActivity<AccLeaseAllTypeContract.Presenter> implements AccLeaseAllTypeContract.View {

    private ActAccLeaseAllTypeBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_acc_lease_all_type);
    }

    @Inject
    AccLeaseAllTypePresenter presenter;

    @Override
    protected AccLeaseAllTypeContract.Presenter getMVPPresenter() {
        return presenter;
    }

    private AccLeaseLetterAdapter letterAdapter;

    private AccLeaseAdapter adapter;

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("租号");
        //搜索
        RxToolbar.itemClicks(binding.appbar.toolbar).as(bindLifecycle())
                .subscribe(menuItem ->
                                ARouter.getInstance().build(AppConstants.PagePath.ACC_SEARCH).navigation());
        presenter.start();
    }

    @Override
    public void setUITypeDate(List<GameTypeBean> typeList, int type) {
        //类型
        for (GameTypeBean gameTypeBean : typeList) {
            XTabLayout.Tab tab = binding.xtlType.newTab();
            tab.setText(gameTypeBean.name);
            binding.xtlType.addTab(tab);
        }
        //选中
        try {
            binding.xtlType.getTabAt(type).select();
        } catch (Exception e) {
            e.printStackTrace();
        }
        binding.xtlType.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                presenter.onTypeClick(tab.getPosition());
            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void setNoData(int type) {
        binding.ndv.setType(type);
    }

    @Override
    public void initLayout(List<String> letterList, List<GameBean> gameList) {
        binding.rvLetter.setLayoutManager(new GridLayoutManager(this, 8));
        binding.rvAccType.setLayoutManager(new GridLayoutManager(this, 3));
        //字母筛选
        letterAdapter = new AccLeaseLetterAdapter(letterList);
        letterAdapter.setOnItemClickListener((adapter, view, position) -> presenter.onLetterClick(position));
        //游戏类型
        adapter = new AccLeaseAdapter(gameList);
        adapter.setOnItemClickListener((adapter, view, position) -> presenter.onItemClick(position));
        binding.rvLetter.setAdapter(letterAdapter);
        binding.rvAccType.setAdapter(adapter);
        binding.ndv.setOnClickListener(v -> presenter.doRentPage());
    }

    @Override
    public void updateView(int selectLetterPosition) {
        //刷新数据源
        letterAdapter.selectLetterPosition(selectLetterPosition);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_search_icon, menu);
        return super.onCreateOptionsMenu(menu);
    }
}

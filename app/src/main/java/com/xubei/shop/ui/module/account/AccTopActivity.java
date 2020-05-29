package com.xubei.shop.ui.module.account;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.databinding.ActAccTopBinding;
import com.xubei.shop.databinding.PopupGameListSelectLayoutBinding;
import com.xubei.shop.ui.module.account.adapter.AccLeaseLetterAdapter;
import com.xubei.shop.ui.module.account.adapter.AccTopAdapter;
import com.xubei.shop.ui.module.account.adapter.PopupGameListAdapter;
import com.xubei.shop.ui.module.account.contract.AccTopContract;
import com.xubei.shop.ui.module.account.model.GameBean;
import com.xubei.shop.ui.module.account.model.OutGoodsBean;
import com.xubei.shop.ui.module.account.presenter.AccTopPresenter;
import com.xubei.shop.ui.module.base.ABaseActivity;
import com.xubei.shop.ui.views.NoDataView;
import com.xubei.shop.utlis.LinearLayoutManager2;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

/**
 * 商品置顶
 * date：2018/12/5 15:21
 * author：xiongj
 **/
@Route(path = AppConstants.PagePath.ACC_TOP)
public class AccTopActivity extends ABaseActivity<AccTopContract.Presenter> implements AccTopContract.View {

    private ActAccTopBinding binding;

    @Inject
    AccTopPresenter presenter;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_acc_top);
        binding.setP(presenter);
    }

    @Override
    protected AccTopContract.Presenter getMVPPresenter() {
        return presenter;
    }

    @Inject
    LinearLayoutManager2 layoutManager2;

    private AccTopAdapter adapter;

    @Override
    protected void initCreate(@Nullable Bundle savedInstanceState) {
        binding.ctl.tvTitle.setOnClickListener(v -> presenter.onShowGameSelect());
        binding.ctl.ivBack.setOnClickListener(v -> onBackPressed());
        binding.ctl.tvZdsm.setOnClickListener(v -> ARouter.getInstance().build(AppConstants.PagePath.ACC_TOPDES).navigation());
        presenter.start();
    }

    @Override
    public void initView(List<OutGoodsBean> list, NoDataView noDataView) {
        adapter = new AccTopAdapter(list);
        binding.itemList.rv.setLayoutManager(layoutManager2);
        binding.itemList.rv.setAdapter(adapter);
        adapter.setEmptyView(noDataView);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            presenter.onItemClick(position);
        });
        binding.itemList.srl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                presenter.onLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                presenter.onRefresh();
            }
        });
    }


    @Override
    public void notifyItemRangeChanged(int startP, int size, Object payloads) {
        if (startP == 0 && size == 0) {
            adapter.notifyDataSetChanged();
        } else {
            adapter.notifyItemRangeChanged(startP, size, payloads);
        }
    }

    @Override
    public void setAllSelectType(Boolean allSelectType) {
        if (allSelectType != null) {
            //设置全选按钮状态
            binding.ivIcAll.setImageResource(allSelectType ? R.mipmap.ic_acc_top_item_ok : R.drawable.act_acc_top_not_all);
        }
    }

    @Override
    public void setAllSelectPrice(double allPrice) {
        binding.tvAllPrice.setText(String.format(Locale.getDefault(), "¥ %.2f", allPrice));
    }

    @Override
    public void onAutoRefresh() {
        binding.itemList.rv.scrollToPosition(0);
        binding.itemList.srl.autoRefresh();
    }

    private PopupWindow popupWindow;

    //平台和排序用适配器
    private PopupGameListAdapter popGameAdapter;

    private AccLeaseLetterAdapter letterAdapter;

    @Override
    public void setGameList(List<String> letterList, List<GameBean> gameList) {
        //显示游戏筛选类目弹框
        PopupGameListSelectLayoutBinding popBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.popup_game_list_select_layout, null, false);
        popupWindow = new PopupWindow(popBinding.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0xffffffff));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(() -> binding.vLayout.setVisibility(View.GONE));
        popBinding.rvLetter.setLayoutManager(new GridLayoutManager(this, 8));
        popBinding.rvAccType.setLayoutManager(new GridLayoutManager(this, 4));
        letterAdapter = new AccLeaseLetterAdapter(letterList);
        letterAdapter.setOnItemClickListener((adapter, view, position) -> presenter.onLetterClick(position));
        popGameAdapter = new PopupGameListAdapter(gameList);
        popGameAdapter.setOnItemClickListener((adapter, view, position) -> {
            //筛选点击。
            presenter.onGameSelect(position);
            popupWindow.dismiss();
        });
        popBinding.rvLetter.setAdapter(letterAdapter);
        popBinding.rvAccType.setAdapter(popGameAdapter);
    }

    @Override
    public void showSelectGamePop() {
        if (popupWindow != null && !popupWindow.isShowing()) {
            popupWindow.showAsDropDown(binding.ctl.getRoot());
            binding.vLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setGameSelect(GameBean selectGameBean) {
        //设置选择的游戏
        popGameAdapter.setSelectGame(selectGameBean);
    }

    @Override
    public SmartRefreshLayout getSrl() {
        return binding.itemList.srl;
    }


    @Override
    public void updateSelectView(int selectLetterPosition) {
        //刷新选择
        letterAdapter.selectLetterPosition(selectLetterPosition);
        //刷新游戏
        popGameAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_acc_top, menu);
        return super.onCreateOptionsMenu(menu);
    }

}

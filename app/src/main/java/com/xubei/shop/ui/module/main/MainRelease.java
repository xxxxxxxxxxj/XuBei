package com.xubei.shop.ui.module.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.data.network.rx.RxSchedulers;
import com.xubei.shop.databinding.ActMainGameBinding;
import com.xubei.shop.ui.module.account.model.GameBean;
import com.xubei.shop.ui.module.base.ABaseFragment;
import com.xubei.shop.ui.module.main.adapter.GameShowAdapter;
import com.xubei.shop.ui.module.main.adapter.GameTypeAdapter;
import com.xubei.shop.ui.module.main.contract.MainGameTypeContract;
import com.xubei.shop.ui.module.main.model.GameTypeBean;
import com.xubei.shop.ui.module.main.presenter.MainGameTypePresenter;
import com.xubei.shop.ui.views.NoDataView;
import com.xubei.shop.utlis.LinearLayoutManager2;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * 发布(游戏分类)
 * date：2017/2/21 17:06
 **/
public class MainRelease extends ABaseFragment<MainGameTypeContract.Presenter> implements MainGameTypeContract.View {

    private final String mPageName = "MainReleaseFragment";

    private ActMainGameBinding binding;

    @Override
    protected View initDataBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.act_main_game, container, false);
        return binding.getRoot();
    }

    @Inject
    MainGameTypePresenter presenter;

    @Override
    protected MainGameTypePresenter getMVPPresenter() {
        return presenter;
    }

    @Inject
    GameTypeAdapter typeAdapter;

    @Inject
    GameShowAdapter showAdapter;

    @Inject
    public MainRelease() {
    }


    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        initLayout();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        presenter.start();
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }


    private void initLayout() {
        binding.rvShowType.setLayoutManager(new LinearLayoutManager2(getContext()));
        binding.rvShowUi.setLayoutManager(new GridLayoutManager(getContext(), 3));
        binding.rvShowType.setAdapter(typeAdapter);
        binding.rvShowUi.setAdapter(showAdapter);
        typeAdapter.setOnItemClickListener((adapter, view, position) -> presenter.onTypeClick(position));
        showAdapter.setOnItemClickListener((adapter, view, position) -> presenter.onGameReleaseClick(position));
        binding.ndv.setOnClickListener(v -> presenter.start());
        //账号发布搜索
        binding.appbar.tvSearch.setOnClickListener(v ->
                ARouter.getInstance().build(AppConstants.PagePath.ACC_R_SEARCH).navigation());
    }


    @Override
    public void setUIDate(List<GameTypeBean> gameTypeBean, int selectPosition) {
        if (gameTypeBean.size() > 0) {
            //适配类型
            typeAdapter.onSelectPosition(selectPosition);
            typeAdapter.replaceData(gameTypeBean);
            //适配对应的子元素
            Flowable.intervalRange(0, 1, 0, 500, TimeUnit.MICROSECONDS)
                    .compose(RxSchedulers.io_main())
                    .as(bindLifecycle())
                    .subscribe(aLong -> {
                        //适配对应的子元素
                        List<GameBean> gameList = gameTypeBean.get(selectPosition).content;
                        if (gameList != null) {
                            showAdapter.replaceData(gameList);
                        } else {
                            showAdapter.replaceData(new ArrayList<>());
                        }
                    });
            //隐藏对应的站位界面
            setNoData(NoDataView.LOADING_OK);
        } else {
            setNoData(NoDataView.NO_DATA);
        }
    }

    @Override
    public void setNoData(int type) {
        binding.ndv.setType(type);
    }

}

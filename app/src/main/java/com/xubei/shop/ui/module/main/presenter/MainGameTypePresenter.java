package com.xubei.shop.ui.module.main.presenter;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.xubei.shop.AppConfig;
import com.xubei.shop.AppConstants;
import com.xubei.shop.data.network.rx.RxSchedulers;
import com.xubei.shop.data.network.service.ApiPHPService;
import com.xubei.shop.ui.module.account.model.GameBean;
import com.xubei.shop.ui.module.base.ABaseSubscriber;
import com.xubei.shop.ui.module.main.contract.MainGameTypeContract;
import com.xubei.shop.ui.module.main.model.GameTypeBean;
import com.xubei.shop.ui.views.NoDataView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 分类
 * date：2017/2/21 17:10
 * author：Seraph
 **/
public class MainGameTypePresenter extends MainGameTypeContract.Presenter {


    private ApiPHPService apiPHPService;

    @Inject
    MainGameTypePresenter(ApiPHPService apiPHPService) {
        this.apiPHPService = apiPHPService;
    }

    private List<GameTypeBean> gameTypes = new ArrayList<>();

    //记录选中的位置(默认第一个)
    private int selectPosition = 0;

    @Override
    public void start() {
        //请求网络，为了用户体验，而且总数据不多的情况，使用一次性请求全部
        //如果没有数据 则进行请求
        if (gameTypes.size() == 0) {
            doRentPage();
        }
    }


    public void onTypeClick(int position) {
        if (position == selectPosition) {
            return;
        }
        this.selectPosition = position;
        if (gameTypes.size() == 0) {
            doRentPage();
        } else {
            mView.setUIDate(gameTypes, selectPosition);
        }

    }

    //跳转对应发布游戏账号界面
    public void onGameReleaseClick(int position) {
        //获取当前点击类型的数据源
        List<GameBean> gameList = gameTypes.get(selectPosition).content;
        if (gameList != null) {
            ARouter.getInstance().build(AppConstants.PagePath.ACC_RELEASE).withSerializable("bean", gameList.get(position)).navigation();
        }
    }


    //新的请求租号列表接口
    private void doRentPage() {
        apiPHPService.appconfigRentPage(AppConfig.APP_KEY_PHP)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.setNoData(NoDataView.LOADING))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<List<GameTypeBean>>() {
                    @Override
                    public void onSuccess(List<GameTypeBean> typeBeans) {
                        gameTypes.clear();
                        gameTypes.addAll(typeBeans);
                        if (mView == null) {
                            return;
                        }
                        //进行数据过滤，如果是免费试玩的则去掉对应的数据
                        dataFilter();
                        mView.setUIDate(gameTypes, selectPosition);
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                        mView.setNoData(NoDataView.NET_ERR);
                    }
                });
    }

    //多余数据过滤
    private void dataFilter() {
        for (int i = 0; i < gameTypes.size(); i++) {
            List<GameBean> list = gameTypes.get(i).content;
            //过滤空数据和免费试玩数据（检查第一条是否免费试玩数据）
            if (list == null || list.size() == 0 || "mianfei".equals(list.get(0).goto_link)) {
                gameTypes.remove(i);
                --i;
            }
        }
    }
}

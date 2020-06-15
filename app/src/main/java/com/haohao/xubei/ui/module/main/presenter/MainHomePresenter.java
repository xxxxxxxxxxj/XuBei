package com.haohao.xubei.ui.module.main.presenter;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.haohao.xubei.AppConfig;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.data.network.rx.RxSchedulers;
import com.haohao.xubei.data.network.service.Api8Service;
import com.haohao.xubei.data.network.service.ApiPHPService;
import com.haohao.xubei.ui.module.account.model.AccBean;
import com.haohao.xubei.ui.module.account.model.GameBean;
import com.haohao.xubei.ui.module.base.ABaseSubscriber;
import com.haohao.xubei.ui.module.base.BaseData;
import com.haohao.xubei.ui.module.base.BaseDataCms;
import com.haohao.xubei.ui.module.main.contract.MainHomeContract;
import com.haohao.xubei.ui.module.main.model.BannerBean;
import com.haohao.xubei.ui.module.main.model.GameTypeBean;
import com.haohao.xubei.ui.module.main.model.HomeMultipleItem;
import com.haohao.xubei.ui.module.main.model.WelfareBean;
import com.haohao.xubei.ui.views.NoDataView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * 首页逻辑处理层
 * date：2017/2/15 11:27
 * author：Seraph
 **/
public class MainHomePresenter extends MainHomeContract.Presenter {


    private ApiPHPService mApiPHPService;

    private Api8Service api8Service;

    @Inject
    MainHomePresenter(ApiPHPService apiPHPService, Api8Service api8Service) {
        this.mApiPHPService = apiPHPService;
        this.api8Service = api8Service;
    }

    private List<HomeMultipleItem> allList;

    private List<BaseDataCms<BannerBean>> bannerList;
    private List<BaseDataCms<AccBean>> hotList;
    private List<BaseDataCms<WelfareBean>> welfareList;

    @Override
    public void start() {
        if (allList == null || allList.size() == 0) {
            doGamesList();
        }
    }


    //元素点击
    public void onItemClick(int position) {
        HomeMultipleItem item = allList.get(position);
        switch (item.getItemType()) {
            case HomeMultipleItem.TYPE_ACCOUNT_TYPE://类型
                //游戏类型
                ARouter.getInstance().build(AppConstants.PagePath.ACC_LEASEALLTYPE).withInt("type", item.pos).navigation();
                break;
            case HomeMultipleItem.TYPE_ACCOUNT_LIST://账号
                ARouter.getInstance().build(AppConstants.PagePath.ACC_LIST).withSerializable("bean", (GameBean) item.date).navigation();
                break;
            case HomeMultipleItem.TYPE_ACCOUNT_MORE://所有游戏
                ARouter.getInstance().build(AppConstants.PagePath.ACC_LEASEALLTYPE).navigation();
                break;
        }
    }

    //热销商品
    public void onItemHotClick(int position) {
        ARouter.getInstance().build(AppConstants.PagePath.ACC_DETAIL).withString("id", hotList.get(position).properties.id).navigation();
    }

    //福利中心点击
    public void onItemWelfareClick(int position) {
        BaseDataCms<WelfareBean> item = welfareList.get(position);
        //判断跳转类型
        if ("1".equals(item.properties.goto_type)) {
            //游戏列表
            ARouter.getInstance().build(AppConstants.PagePath.ACC_LIST).withSerializable("bean", item.properties).navigation();
        } else {
            //网页链接
            ARouter.getInstance().build(AppConstants.PagePath.COMM_AGENTWEB)
                    .withString("title", "详情")
                    .withString("webUrl", item.properties.goto_link)
                    .navigation();
        }
    }


    //首页banner点击
    public void doBannerClick(int position) {
        BaseDataCms<BannerBean> bannerBean = bannerList.get(position);
        switch (bannerBean.properties.goto_type) {
            case "1"://游戏列表
                ARouter.getInstance().build(AppConstants.PagePath.ACC_LIST).withSerializable("bean", bannerBean.properties).navigation();
                break;
            case "2"://自定义url
                ARouter.getInstance().build(AppConstants.PagePath.COMM_AGENTWEB)
                        .withString("title", "详情")
                        .withString("webUrl", bannerBean.properties.goto_link)
                        .navigation();
                break;
        }

    }


    //数据重构
    private List<HomeMultipleItem> refactorData(List<BaseDataCms<BannerBean>> cmsBannerList, List<GameTypeBean> gameTypeList, List<BaseDataCms<AccBean>> cmsAccList, List<BaseDataCms<WelfareBean>> cmsWelfareList) {
        //banner
        bannerList = cmsBannerList;
        //热销
        hotList = cmsAccList;
        //福利中心
        welfareList = cmsWelfareList;
        // 进行转换（游戏）
        List<HomeMultipleItem> list = new ArrayList<>();
        //添加游戏类型
        for (int j = 0; j < gameTypeList.size(); j++) {
            GameTypeBean gameTypeBean = gameTypeList.get(j);
            //类型
            list.add(new HomeMultipleItem<>(HomeMultipleItem.TYPE_ACCOUNT_TYPE, gameTypeBean.name, j));
            //判断子账号如果大于8个则取8个，如果大于4小于8则取4个，如果小于4个则，取当前
            int tempI = gameTypeBean.content.size();
            if (tempI >= 8) {
                tempI = 8;
            } else if (tempI >= 4) {
                tempI = 4;
            }
            //子账号
            for (int i = 0; i < tempI; i++) {
                list.add(new HomeMultipleItem<>(HomeMultipleItem.TYPE_ACCOUNT_LIST, gameTypeBean.content.get(i)));
            }
        }
        //查看更多游戏
        list.add(new HomeMultipleItem(HomeMultipleItem.TYPE_ACCOUNT_MORE));
        return list;
    }


    //获取banner图
    private Flowable<BaseData<BaseDataCms<BannerBean>>> doBanner() {
        return api8Service.getHomeBanner()
                .compose(RxSchedulers.io_main_business());

    }

    //所有游戏分类
    private Flowable<List<GameTypeBean>> doAllGame() {
        return mApiPHPService.appconfigRentPage(AppConfig.APP_KEY_PHP)
                .compose(RxSchedulers.io_main_business());
    }

    //热销榜单
    private Flowable<BaseData<BaseDataCms<AccBean>>> doHotList() {
        return api8Service.getHotList()
                .compose(RxSchedulers.io_main_business());
    }

    //福利中心
    private Flowable<BaseData<BaseDataCms<WelfareBean>>> doWelfare() {
        return api8Service.getWelfareCenter()
                .compose(RxSchedulers.io_main_business());
    }


    //获取游戏列表
    private void doGamesList() {
        Flowable.combineLatest(doBanner(), doAllGame(), doHotList(), doWelfare(),
                (cmsBannerList, gameTypeList, cmshotList, cmsWelfareList) ->
                        refactorData(cmsBannerList.datas, gameTypeList, cmshotList.datas, cmsWelfareList.datas)
        ).doOnSubscribe(subscription -> mView.setNoData(NoDataView.LOADING))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<List<HomeMultipleItem>>(mView) {
                    @Override
                    public void onSuccess(List<HomeMultipleItem> list) {
                        allList = list;
                        mView.setGameList(bannerList, allList, hotList, welfareList);
                        mView.setNoData(NoDataView.LOADING_OK);
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                        mView.setNoData(NoDataView.NET_ERR);
                    }

                });
    }


}

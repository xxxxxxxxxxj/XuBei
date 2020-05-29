package com.xubei.shop.ui.module.account.presenter;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.xubei.shop.AppConfig;
import com.xubei.shop.AppConstants;
import com.xubei.shop.data.db.help.UserBeanHelp;
import com.xubei.shop.data.network.rx.RxSchedulers;
import com.xubei.shop.data.network.service.Api8Service;
import com.xubei.shop.data.network.service.ApiUserNewService;
import com.xubei.shop.ui.module.account.contract.AccUserFreeContract;
import com.xubei.shop.ui.module.account.model.MianFeiBean;
import com.xubei.shop.ui.module.account.model.ShareMianFeiBean;
import com.xubei.shop.ui.module.account.model.UserAccFreeBean;
import com.xubei.shop.ui.module.base.ABaseSubscriber;
import com.xubei.shop.ui.module.base.BaseData;
import com.xubei.shop.ui.module.base.BaseDataCms;
import com.xubei.shop.ui.views.NoDataView;
import com.xubei.shop.utlis.PageUtils;
import com.xubei.shop.utlis.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * date：2017/11/28 11:23
 * author：Seraph
 **/
public class AccUserFreePresenter extends AccUserFreeContract.Presenter {


    private Api8Service api8Service;

    private UserBeanHelp userBeanHelp;

    private ApiUserNewService apiUserNewService;

    private NoDataView noDataView;

    @Inject
    AccUserFreePresenter(Api8Service api8Service, ApiUserNewService apiUserNewService, UserBeanHelp userBeanHelp, NoDataView noDataView) {
        this.api8Service = api8Service;
        this.userBeanHelp = userBeanHelp;
        this.apiUserNewService = apiUserNewService;
        this.noDataView = noDataView;
    }

    private List<UserAccFreeBean> list = new ArrayList<>();

    private int pageNo = 0;

    //当前邀请的信息
    private UserAccFreeBean userAccFreeBean;

    @Override
    public void start() {
        mView.initView(list, noDataView);
        doRefresh();
        noDataView.setOnClickListener(v -> doRefresh());
    }


    //邀请好友助力
    public void onItemChildClick(int position, String platform) {
        userAccFreeBean = list.get(position);
        doShowFreeShare(platform);
    }

    //详情
    public void onItemClick(int position) {
        ARouter.getInstance().build(AppConstants.PagePath.ACC_DETAIL_FREE).withString("id", list.get(position).id).navigation();
    }

    public void doRefresh() {
        //刷新第一页
        getUserGoodsList(1);
    }

    public void nextPage() {
        //下一页
        getUserGoodsList(pageNo + 1);
    }


    private class MianfeiAll {

        public MianFeiBean mianFeiBean;

        public ShareMianFeiBean shareMianFeiBean;

        public MianfeiAll(MianFeiBean mianFeiBean, ShareMianFeiBean shareMianFeiBean) {
            this.mianFeiBean = mianFeiBean;
            this.shareMianFeiBean = shareMianFeiBean;
        }
    }

    //显示分享(邀请助力)
    public void doShowFreeShare(String platform) {
        if (userAccFreeBean == null) {
            return;
        }
        //判断是否登录
        if (!userBeanHelp.isLogin(true)) {
            return;
        }
        //获取对应免费玩活动信息和分享文案
        Flowable.combineLatest(getMainFei(), getShareMianfeiWenan(),
                (mianFeiBean, baseDataCmsBaseData) ->
                        new MianfeiAll(mianFeiBean, baseDataCmsBaseData.datas.get(0).properties))
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<MianfeiAll>(mView) {
                    @Override
                    protected void onSuccess(MianfeiAll mianfeiAll) {
                        Tools.showShare(platform,
                                mianfeiAll.shareMianFeiBean.official,
                                String.format(Locale.getDefault(), "【%s】%s", userAccFreeBean.bigGameName, userAccFreeBean.goodsTitle),
                                userAccFreeBean.id,
                                "https://new-xubei-static.oss-cn-shenzhen.aliyuncs.com/common/img/zhuli.jpg",
                                mView.getContext(),
                                true,
                                mianfeiAll.mianFeiBean.id);
                    }

                    @Override
                    protected void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }

    //免费玩信息
    private Flowable<MianFeiBean> getMainFei() {
        return apiUserNewService.addInitiator(userBeanHelp.getAuthorization(), AppConfig.MIAN_FEI_ACTIV_NO, userAccFreeBean.bigGameId, userAccFreeBean.id).compose(RxSchedulers.io_main_business());
    }

    //免费玩文案
    private Flowable<BaseData<BaseDataCms<ShareMianFeiBean>>> getShareMianfeiWenan() {
        return api8Service.getShareMianfeiWenan().compose(RxSchedulers.io_main_business());
    }


    //获取数据
    private void getUserGoodsList(int tempPageNo) {
        apiUserNewService.getUserGoodslist(userBeanHelp.getAuthorization(), AppConfig.MIAN_FEI_ACTIV_NO, tempPageNo, 16)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> noDataView.setType(NoDataView.LOADING))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<BaseData<UserAccFreeBean>>() {
                    @Override
                    protected void onSuccess(BaseData<UserAccFreeBean> baseData) {
                        pageNo = PageUtils.doSuccess(tempPageNo, list, baseData.data,
                                (start, size) -> mView.notifyItemRangeChanged(start, size),
                                noDataView,
                                mView.getSrl(), PageUtils.RefreshMode.LOAD_ALL, 16);
                    }

                    @Override
                    protected void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                        PageUtils.doError(noDataView, mView.getSrl());
                    }
                });
    }


}
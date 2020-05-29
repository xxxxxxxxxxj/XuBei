package com.xubei.shop.ui.module.account.presenter;

import com.blankj.utilcode.util.ToastUtils;
import com.xubei.shop.data.db.help.UserBeanHelp;
import com.xubei.shop.data.network.rx.RxSchedulers;
import com.xubei.shop.data.network.service.ApiUserNewService;
import com.xubei.shop.ui.module.account.contract.AccRDetailContract;
import com.xubei.shop.ui.module.account.model.OutGoodsDetailBean;
import com.xubei.shop.ui.module.base.ABaseSubscriber;
import com.xubei.shop.ui.module.common.photopreview.PhotoPreviewActivity;
import com.xubei.shop.ui.module.common.photopreview.PhotoPreviewBean;
import com.xubei.shop.ui.views.NoDataView;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * 发布账号详情
 * date：2018/8/1 18:17
 * author：Seraph
 **/
public class AccRDetailPresenter extends AccRDetailContract.Presenter {

    private ApiUserNewService apiUserNewService;

    private UserBeanHelp userBeanHelp;

    private String id;


    @Inject
    public AccRDetailPresenter(UserBeanHelp userBeanHelp, ApiUserNewService apiUserNewService, String id) {
        this.userBeanHelp = userBeanHelp;
        this.apiUserNewService = apiUserNewService;
        this.id = id;
    }

    private OutGoodsDetailBean outGoodsDetailBean;

    @Override
    public void start() {
        doGoodsDetail();
    }


    public void doBannerClick(int position) {
        //banner图点击放大
        ArrayList<PhotoPreviewBean> list = new ArrayList<>();
        for (OutGoodsDetailBean.GoodsPicLocationBean pictureBean : outGoodsDetailBean.picture) {
            PhotoPreviewBean premViewBean = new PhotoPreviewBean();
            premViewBean.objURL = pictureBean.location;
            list.add(premViewBean);
        }
        PhotoPreviewActivity.startPhotoPreview(list, position);
    }


    //获取账号详情
    private void doGoodsDetail() {
        apiUserNewService.getGoodsDetail(userBeanHelp.getAuthorization(), id)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.setNoDataView(NoDataView.LOADING))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<OutGoodsDetailBean>(mView) {
                    @Override
                    public void onSuccess(OutGoodsDetailBean detailBean) {
                        if (detailBean != null) {
                            outGoodsDetailBean = detailBean;
                            mView.onGoodsDetail(outGoodsDetailBean);
                            mView.setNoDataView(NoDataView.LOADING_OK);
                        }
                    }

                    @Override
                    public void onError(String errStr) {
                        mView.setNoDataView(NoDataView.NET_ERR);
                        ToastUtils.showShort(errStr);
                    }
                });


    }


}

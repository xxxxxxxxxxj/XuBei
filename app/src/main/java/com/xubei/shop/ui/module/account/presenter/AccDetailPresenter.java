package com.xubei.shop.ui.module.account.presenter;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xubei.shop.AppConfig;
import com.xubei.shop.AppConstants;
import com.xubei.shop.data.db.help.UserBeanHelp;
import com.xubei.shop.data.network.rx.RxSchedulers;
import com.xubei.shop.data.network.service.Api8Service;
import com.xubei.shop.data.network.service.ApiUserNewService;
import com.xubei.shop.ui.module.account.contract.AccDetailContract;
import com.xubei.shop.ui.module.account.model.GoldBlBean;
import com.xubei.shop.ui.module.account.model.MianFeiBean;
import com.xubei.shop.ui.module.account.model.OutGoodsDetailBean;
import com.xubei.shop.ui.module.account.model.ShareMianFeiBean;
import com.xubei.shop.ui.module.base.ABaseSubscriber;
import com.xubei.shop.ui.module.base.BaseData;
import com.xubei.shop.ui.module.base.BaseDataCms;
import com.xubei.shop.ui.module.common.photopreview.PhotoPreviewBean;
import com.xubei.shop.ui.module.order.model.OrderPayBean;
import com.xubei.shop.ui.module.user.model.AcctManageBean;
import com.xubei.shop.ui.views.NoDataView;
import com.xubei.shop.utlis.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import androidx.appcompat.app.AlertDialog;
import io.reactivex.Flowable;
import okhttp3.RequestBody;

/**
 * 账号详情
 * date：2017/11/28 11:23
 * author：Seraph
 **/
public class AccDetailPresenter extends AccDetailContract.Presenter {


    private Api8Service api8Service;

    private UserBeanHelp userBeanHelp;

    private ApiUserNewService apiUserNewService;

    @Inject
    AccDetailPresenter(Api8Service api8Service, ApiUserNewService apiUserNewService, UserBeanHelp userBeanHelp, String id) {
        this.api8Service = api8Service;
        this.userBeanHelp = userBeanHelp;
        this.accountId = id;
        this.apiUserNewService = apiUserNewService;
    }

    private String accountId;


    private OutGoodsDetailBean outGoodsDetailBean;

    //支付订单信息
    private OrderPayBean mOrderBean;


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
        mView.startLookImage(list, position);
    }

    //租赁
    public void doClickRent() {
        if (isOKPay()) {
            startOrderCreateActivity();
        }
    }


    //提示去助力
    private void showGotoShareDialog(int icoinAmount, int hourLeaseCoin) {
        new AlertDialog.Builder(mView.getContext()).setTitle("提示")
                .setMessage(String.format(Locale.getDefault(), "达到%d金币就可以免费体验，您的金币为%d。（助力一次=20金币）", hourLeaseCoin, icoinAmount))
                .setNegativeButton("再看看", null)
                .setPositiveButton("邀请好友助力", (dialog, which) -> mView.showShareDialog())
                .show();
    }


    //是否可以开始支付
    private boolean isOKPay() {
        if (outGoodsDetailBean == null) {
            return false;
        }
        //判断订单是否是展示中状态
        if (outGoodsDetailBean.goodsStatus != 3 || "1".equals(outGoodsDetailBean.goodsFlag)) {
            return false;
        }
        //如果游戏在维护中
        if (outGoodsDetailBean.gameMaintain != null && outGoodsDetailBean.gameMaintain.status == 1) {
            String tempEndTime = outGoodsDetailBean.gameMaintain.endTime;
            if (tempEndTime != null && tempEndTime.length() > 16) {
                tempEndTime = tempEndTime.substring(0, 16);
            }
            String tempStr = "当前游戏正在维护";
            if (tempEndTime == null) {
                tempStr += "。";
            } else {
                tempStr += "，\n预计 " + tempEndTime + " 恢复。";
            }
            new AlertDialog.Builder(mView.getContext())
                    .setTitle("游戏维护中")
                    .setMessage(tempStr)
                    .setPositiveButton("我知道啦", null)
                    .show();
            return false;
        }
        return true;
    }

    //跳转订单创建
    private void startOrderCreateActivity() {
        ARouter.getInstance().build(AppConstants.PagePath.ORDER_CREATE)
                .withSerializable("bean", outGoodsDetailBean)
                .navigation();
    }

    //获取详情
    private void doGoodsDetail() {
        api8Service.goodsDetail(accountId, AppConfig.getChannelValue())
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.setNoDataView(NoDataView.LOADING))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<OutGoodsDetailBean>() {
                    @Override
                    public void onSuccess(OutGoodsDetailBean detailBean) {
                        outGoodsDetailBean = detailBean;
                        if (outGoodsDetailBean != null) {
                            mView.setAccountDetail(outGoodsDetailBean);
                            mView.setNoDataView(NoDataView.LOADING_OK);
                            //查询收藏
                            if (userBeanHelp.isLogin()) {
                                getCollection();
                            }
                        } else {
                            mView.setNoDataView(NoDataView.NET_ERR);
                            ToastUtils.showShort("获取详情失败");
                        }
                    }

                    @Override
                    public void onError(String errStr) {
                        mView.setNoDataView(NoDataView.NET_ERR);
                        if (ObjectUtils.isNotEmpty(errStr)) {
                            ToastUtils.showShort(errStr);
                        }
                    }
                });

    }

    //查询商品是否收藏
    private void getCollection() {
        apiUserNewService.getCollection(userBeanHelp.getAuthorization(), accountId)
                .compose(RxSchedulers.io_main_business())
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<String>(mView) {
                    @Override
                    public void onSuccess(String s) {
                        mView.onShowCollection(s);
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }


    //切换收藏
    public void doSwitchCollection() {
        //判断是否登录
        if (!userBeanHelp.isLogin(true)) {
            return;
        }
        apiUserNewService.addCollection(userBeanHelp.getAuthorization(), accountId)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<String>(mView) {
                    @Override
                    public void onSuccess(String type) {
                        mView.onShowCollection(type);
                        if ("收藏成功".equals(type)) {
                            ToastUtils.showShort("账号收藏成功");
                        } else {
                            ToastUtils.showShort("已取消账号收藏");
                        }
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }

    //显示分享
    public void doShowShare(String platform) {
        if (outGoodsDetailBean == null) {
            return;
        }
        String tempPicture = "";
        if (outGoodsDetailBean.picture != null && outGoodsDetailBean.picture.size() > 0) {
            tempPicture = outGoodsDetailBean.picture.get(0).location;
        }
        Tools.showShare(platform,
                "虚贝租号-专业便捷的租号平台！",
                outGoodsDetailBean.goodsTitle,
                outGoodsDetailBean.id,
                tempPicture,
                mView.getContext());
    }

    //开始使用金币支付
    public void startGoldPay(String password) {
        payLaunchExt(mOrderBean.orderBalanceNo, password);
    }

    //分享相关bean
    private class MianfeiAllBean {

        public MianFeiBean mianFeiBean;

        public ShareMianFeiBean shareMianFeiBean;

        public MianfeiAllBean(MianFeiBean mianFeiBean, ShareMianFeiBean shareMianFeiBean) {
            this.mianFeiBean = mianFeiBean;
            this.shareMianFeiBean = shareMianFeiBean;
        }
    }

    //显示分享(邀请助力)
    public void doShowFreeShare(String platform) {
        if (outGoodsDetailBean == null) {
            return;
        }
        //判断是否登录
        if (!userBeanHelp.isLogin(true)) {
            return;
        }
        //获取对应免费玩活动信息和分享文案
        Flowable.combineLatest(getMainFei(), getShareMianfeiWenan(),
                (mianFeiBean, baseDataCmsBaseData) ->
                        new MianfeiAllBean(mianFeiBean, baseDataCmsBaseData.datas.get(0).properties))
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<MianfeiAllBean>(mView) {
                    @Override
                    protected void onSuccess(MianfeiAllBean mianfeiAll) {
                        Tools.showShare(platform,
                                mianfeiAll.shareMianFeiBean.official,
                                String.format(Locale.getDefault(), "【%s】%s", outGoodsDetailBean.bigGameName, outGoodsDetailBean.goodsTitle),
                                outGoodsDetailBean.id,
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
        return apiUserNewService.addInitiator(userBeanHelp.getAuthorization(), AppConfig.MIAN_FEI_ACTIV_NO, outGoodsDetailBean.bigGameId, outGoodsDetailBean.id).compose(RxSchedulers.io_main_business());
    }

    //免费玩文案
    private Flowable<BaseData<BaseDataCms<ShareMianFeiBean>>> getShareMianfeiWenan() {
        return api8Service.getShareMianfeiWenan().compose(RxSchedulers.io_main_business());
    }

    private class AccGoldBean {

        public AcctManageBean acctManageBean;

        public GoldBlBean goldBlBean;

        public AccGoldBean(AcctManageBean acctManageBean, GoldBlBean goldBlBean) {
            this.acctManageBean = acctManageBean;
            this.goldBlBean = goldBlBean;
        }
    }

    //租赁(免费玩)
    public void doClickRentFree() {
        if (isOKPay()) {
            //获取账号金币数量和金币换算比例
            //判断金币是否足够支付当前的商品
            Flowable.combineLatest(getJinbipeizhi(), doAccountInfo(),
                    (baseDataCmsBaseData, acctManageBean) -> new AccGoldBean(acctManageBean, baseDataCmsBaseData.datas.get(0).properties))
                    .doOnSubscribe(s -> mView.showLoading().setOnDismissListener(dialog -> s.cancel()))
                    .as(mView.bindLifecycle())
                    .subscribe(new ABaseSubscriber<AccGoldBean>() {
                        @Override
                        protected void onSuccess(AccGoldBean accGoldBean) {
                            if (StringUtils.isEmpty(accGoldBean.goldBlBean.coin)) {
                                accGoldBean.goldBlBean.coin = "0";
                            }
                            //1元对应的金币数量
                            //计算对应需要的商品需要的金币数量
                            int hourLeaseDecimal = new BigDecimal(outGoodsDetailBean.hourLease).multiply(new BigDecimal(accGoldBean.goldBlBean.coin)).setScale(0, BigDecimal.ROUND_UP).stripTrailingZeros().intValue();
                            //如果账号金币小于需要的金币进行提示去助力
                            if (accGoldBean.acctManageBean.icoinAmount < hourLeaseDecimal) {
                                mView.hideLoading();
                                showGotoShareDialog(accGoldBean.acctManageBean.icoinAmount, hourLeaseDecimal);
                            } else {
                                //判断是否有遗留未支付的订单信息
                                if (mOrderBean != null) {
                                    mView.hideLoading();
                                    //进行支付操作
                                    mView.showPayInputPop(mOrderBean.orderGameNo, "1小时", String.valueOf(accGoldBean.acctManageBean.icoinAmount), String.valueOf(hourLeaseDecimal));
                                } else {
                                    //创建订单
                                    createOrder(String.valueOf(accGoldBean.acctManageBean.icoinAmount), String.valueOf(hourLeaseDecimal));
                                }
                            }
                        }


                        @Override
                        protected void onError(String errStr) {
                            mView.hideLoading();
                            ToastUtils.showShort(errStr);
                        }
                    });
        }

    }


    //获取账号资金信息
    private Flowable<AcctManageBean> doAccountInfo() {
        return apiUserNewService.acctManageIndex(userBeanHelp.getAuthorization())
                .compose(RxSchedulers.io_main_business());
    }

    //获取当前金币和价格的换算比例
    private Flowable<BaseData<BaseDataCms<GoldBlBean>>> getJinbipeizhi() {
        return api8Service.getJinbipeizhi()
                .compose(RxSchedulers.io_main_business());
    }


    //创建订单(1个小时)
    private void createOrder(String icoinAmount, String hourLeaseDecimal) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("businessNo", AppConfig.getChannelValue());
            jsonObject.put("goodsId", outGoodsDetailBean.id);
            jsonObject.put("count", 1);
            jsonObject.put("leaseType", 0);
            jsonObject.put("activityNo", AppConfig.MIAN_FEI_ACTIV_NO);
        } catch (JSONException e) {
            e.printStackTrace();
            ToastUtils.showShort("创建订单异常");
            return;
        }
        RequestBody jsonBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        apiUserNewService.createOrder(userBeanHelp.getAuthorization(), jsonBody)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<OrderPayBean>(mView) {
                    @Override
                    public void onSuccess(OrderPayBean orderBean) {
                        //调用对应的支付
                        mOrderBean = orderBean;
                        //显示支付信息
                        mView.showPayInputPop(mOrderBean.orderGameNo, "1小时", icoinAmount, hourLeaseDecimal);
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }


    //支付(金币支付)
    private void payLaunchExt(String balanceNo, String payPassword) {
        apiUserNewService.payLaunchExt(userBeanHelp.getAuthorization(), balanceNo, "pay_coin", "lease", payPassword)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<String>(mView) {
                    @Override
                    public void onSuccess(String result) {
                        paySuccess();
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });

    }

    //支付成功
    private void paySuccess() {
        ARouter.getInstance().build(AppConstants.PagePath.ORDER_SUCCESS)
                .withString("orderNo", mOrderBean.orderGameNo)
                .navigation();
        mView.finish();
    }


}
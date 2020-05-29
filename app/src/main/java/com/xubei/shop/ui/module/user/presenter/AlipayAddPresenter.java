package com.xubei.shop.ui.module.user.presenter;

import com.xubei.shop.data.db.help.UserBeanHelp;
import com.xubei.shop.data.network.rx.RxSchedulers;
import com.xubei.shop.ui.module.user.contract.AlipayAddContract;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * 绑定支付宝
 * date：2017/12/4 15:04
 * author：Seraph
 *
 **/
public class AlipayAddPresenter extends AlipayAddContract.Presenter {

    private UserBeanHelp userBeanHelp;


    @Inject
    public AlipayAddPresenter(UserBeanHelp userBeanHelp) {
        this.userBeanHelp = userBeanHelp;
    }



    @Override
    public void start() {
        String phone = userBeanHelp.getUserBean().getMobile();
        mView.setUserPhone(phone);
    }

    //开始倒计时
    private void startCountdown(final int count) {
        Flowable.intervalRange(1, count, 0, 1, TimeUnit.SECONDS)
                .compose(RxSchedulers.io_main())
                .as(mView.bindLifecycle())
                .subscribe(aLong -> mView.setCountdownText(count - aLong));
    }


//    public void doGetCode() {
////        if (ObjectUtils.isEmpty(phone)) {
////            ToastUtils.showShort("获取绑定手机失败");
////            return;
////        }
//        //获取验证码
////        apiUserService.sendMobileCode(phone)
////                .compose(RxSchedulers.io_main_business(mView))
////                .doOnSubscribe(subscription -> mView.showLoading("获取验证码").setOnDismissListener(dialog -> subscription.cancel()))
////                .subscribe(new ABaseSubscriber<BaseData<String>>(mView) {
////                    @Override
////                    public void onSuccess(BaseData<String> stringBaseData) {
////                        ToastUtils.showShort("发送验证码成功");
////                        //获取验证码成功，开始倒计时
////                        startCountdown(Integer.valueOf(stringBaseData.interval));
////                    }
////
////                    @Override
////                    public void onError(String errStr) {
////                        ToastUtils.showShort(errStr);
////                    }
////                });
//    }

//    //添加支付宝账号
//    public void doAdd(String code, String alipayNo) {
////        apiUserService.addAlipayAccount(userBeanHelp.getLoginToken(), code, alipayNo)
////                .compose(RxSchedulers.io_main_business(mView))
////                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
////                .subscribe(new ABaseSubscriber<String>(mView) {
////                    @Override
////                    public void onSuccess(String s) {
////                        ToastUtils.showShort("账号添加成功");
////                        RxBus.get().post(AppConstants.RxBusAction.TAG_ADD_ALIPAY, true);
////                        mView.finish();
////                    }
////
////                    @Override
////                    public void onError(String errStr) {
////                        ToastUtils.showShort(errStr);
////                    }
////                });
//    }
}

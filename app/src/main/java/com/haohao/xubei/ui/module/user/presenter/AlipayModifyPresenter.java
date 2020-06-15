package com.haohao.xubei.ui.module.user.presenter;

import com.haohao.xubei.data.db.help.UserBeanHelp;
import com.haohao.xubei.data.network.rx.RxSchedulers;
import com.haohao.xubei.ui.module.user.contract.AlipayModifyContract;
import com.haohao.xubei.ui.module.user.model.CardAccountBean;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * 修改支付宝账号
 * date：2017/12/4 15:04
 * author：Seraph
 *
 **/
public class AlipayModifyPresenter extends AlipayModifyContract.Presenter {

    private UserBeanHelp userBeanHelp;


    @Inject
    AlipayModifyPresenter(UserBeanHelp userBeanHelp) {
        this.userBeanHelp = userBeanHelp;
    }



    private CardAccountBean mCardAccount;

    @Override
    public void start() {
        String phone = userBeanHelp.getUserBean().getMobile();
        mView.setUserPhone(phone);
        //获取原先绑定的账号
       // doQueryAlipayAccount();
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
////
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

//    //修改支付宝账号
//    public void doUpdateAlipayAccount(String newBankCardNo, String code) {
////        if (mCardAccount == null) {
////            ToastUtils.showShort("获取信息失败");
////            return;
////        }
////        apiUserService.updateAlipayAccount(userBeanHelp.getLoginToken(), newBankCardNo, mCardAccount.id, code)
////                .compose(RxSchedulers.io_main_business(mView))
////                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
////                .subscribe(new ABaseSubscriber<String>(mView) {
////                    @Override
////                    public void onSuccess(String s) {
////                        ToastUtils.showShort("账号修改成功");
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
//
//    //查询支付宝卡号
//    private void doQueryAlipayAccount() {
////        apiUserService.queryAlipayAccount(userBeanHelp.getLoginToken())
////                .compose(RxSchedulers.io_main_business(mView))
////                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
////                .subscribe(new ABaseSubscriber<CardAccountBean>(mView) {
////                    @Override
////                    public void onSuccess(CardAccountBean cardAccount) {
////                        mCardAccount = cardAccount;
////                    }
////
////                    @Override
////                    public void onError(String errStr) {
////                        ToastUtils.showShort(errStr);
////                    }
////                });
//    }
}

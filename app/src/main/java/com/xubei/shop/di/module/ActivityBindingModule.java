package com.xubei.shop.di.module;


import com.xubei.shop.di.module.activity.AboutModule;
import com.xubei.shop.di.module.activity.AccAreaSelectModule;
import com.xubei.shop.di.module.activity.AccDetailModule;
import com.xubei.shop.di.module.activity.AccFreeDetailModule;
import com.xubei.shop.di.module.activity.AccLeaseAllTypeModule;
import com.xubei.shop.di.module.activity.AccLeaseNoticeModule;
import com.xubei.shop.di.module.activity.AccListModule;
import com.xubei.shop.di.module.activity.AccRAgreementModule;
import com.xubei.shop.di.module.activity.AccRDetailModule;
import com.xubei.shop.di.module.activity.AccREditModule;
import com.xubei.shop.di.module.activity.AccRListModule;
import com.xubei.shop.di.module.activity.AccRListSResultModule;
import com.xubei.shop.di.module.activity.AccRListSearchModule;
import com.xubei.shop.di.module.activity.AccRSearchModule;
import com.xubei.shop.di.module.activity.AccRSuccessModule;
import com.xubei.shop.di.module.activity.AccReleaseModule;
import com.xubei.shop.di.module.activity.AccSCModule;
import com.xubei.shop.di.module.activity.AccSResultModule;
import com.xubei.shop.di.module.activity.AccSearchModule;
import com.xubei.shop.di.module.activity.AccTopDesModule;
import com.xubei.shop.di.module.activity.AccTopModule;
import com.xubei.shop.di.module.activity.AccUserFreeModule;
import com.xubei.shop.di.module.activity.AlipayAddModule;
import com.xubei.shop.di.module.activity.AlipayModifyModule;
import com.xubei.shop.di.module.activity.CaptureModule;
import com.xubei.shop.di.module.activity.CommonAgentWebModule;
import com.xubei.shop.di.module.activity.CommonWebLocalModule;
import com.xubei.shop.di.module.activity.FreezeDetailsModule;
import com.xubei.shop.di.module.activity.FundDetailsGoldModule;
import com.xubei.shop.di.module.activity.FundDetailsModule;
import com.xubei.shop.di.module.activity.GuidePagesModule;
import com.xubei.shop.di.module.activity.HelpCenterModule;
import com.xubei.shop.di.module.activity.HelpListModule;
import com.xubei.shop.di.module.activity.LocalImageListModule;
import com.xubei.shop.di.module.activity.LoginModule;
import com.xubei.shop.di.module.activity.LoginTypeSelectModule;
import com.xubei.shop.di.module.activity.MainModule;
import com.xubei.shop.di.module.activity.MyMsgModule;
import com.xubei.shop.di.module.activity.MyPurseModule;
import com.xubei.shop.di.module.activity.NoticeListModule;
import com.xubei.shop.di.module.activity.OrderAllModule;
import com.xubei.shop.di.module.activity.OrderAllSellerModule;
import com.xubei.shop.di.module.activity.OrderCreateModule;
import com.xubei.shop.di.module.activity.OrderDetailModule;
import com.xubei.shop.di.module.activity.OrderPayModule;
import com.xubei.shop.di.module.activity.OrderRDetailModule;
import com.xubei.shop.di.module.activity.OrderRenewModule;
import com.xubei.shop.di.module.activity.OrderSearchModule;
import com.xubei.shop.di.module.activity.OrderSuccessModule;
import com.xubei.shop.di.module.activity.PhoneBindModule;
import com.xubei.shop.di.module.activity.PhotoPreviewModule;
import com.xubei.shop.di.module.activity.RedemptionCenterModule;
import com.xubei.shop.di.module.activity.RedemptionRecordModule;
import com.xubei.shop.di.module.activity.RegisteredModule;
import com.xubei.shop.di.module.activity.ResetPasswordModule;
import com.xubei.shop.di.module.activity.ResetPayPwModule;
import com.xubei.shop.di.module.activity.RightsApplySellerModule;
import com.xubei.shop.di.module.activity.RightsDetailSellerModule;
import com.xubei.shop.di.module.activity.RightsProcessSellerModule;
import com.xubei.shop.di.module.activity.SettingModule;
import com.xubei.shop.di.module.activity.SettingNewMsgModule;
import com.xubei.shop.di.module.activity.UpdateBindPhoneModule;
import com.xubei.shop.di.module.activity.UpdateNickNameModule;
import com.xubei.shop.di.module.activity.UserGetMoneyModule;
import com.xubei.shop.di.module.activity.UserInfoModule;
import com.xubei.shop.di.module.activity.UserPayModule;
import com.xubei.shop.di.module.activity.UserVerifiedModule;
import com.xubei.shop.di.module.activity.WelcomeModule;
import com.xubei.shop.di.scoped.ActivityScoped;
import com.xubei.shop.ui.module.account.AccAreaSelectActivity;
import com.xubei.shop.ui.module.account.AccDetailActivity;
import com.xubei.shop.ui.module.account.AccFreeDetailActivity;
import com.xubei.shop.ui.module.account.AccLeaseAllTypeActivity;
import com.xubei.shop.ui.module.account.AccLeaseNoticeActivity;
import com.xubei.shop.ui.module.account.AccListActivity;
import com.xubei.shop.ui.module.account.AccRAgreementActivity;
import com.xubei.shop.ui.module.account.AccRDetailActivity;
import com.xubei.shop.ui.module.account.AccREditActivity;
import com.xubei.shop.ui.module.account.AccRListActivity;
import com.xubei.shop.ui.module.account.AccRListSResultActivity;
import com.xubei.shop.ui.module.account.AccRListSearchActivity;
import com.xubei.shop.ui.module.account.AccRSearchActivity;
import com.xubei.shop.ui.module.account.AccRSuccessActivity;
import com.xubei.shop.ui.module.account.AccReleaseActivity;
import com.xubei.shop.ui.module.account.AccSCActivity;
import com.xubei.shop.ui.module.account.AccSResultActivity;
import com.xubei.shop.ui.module.account.AccSearchActivity;
import com.xubei.shop.ui.module.account.AccTopActivity;
import com.xubei.shop.ui.module.account.AccTopDesActivity;
import com.xubei.shop.ui.module.account.AccUserFreeActivity;
import com.xubei.shop.ui.module.common.photolist.LocalImageListActivity;
import com.xubei.shop.ui.module.common.photopreview.PhotoPreviewActivity;
import com.xubei.shop.ui.module.common.scan.CaptureActivity;
import com.xubei.shop.ui.module.common.web.CommonAgentWebActivity;
import com.xubei.shop.ui.module.common.web.CommonWebLocalActivity;
import com.xubei.shop.ui.module.login.LoginActivity;
import com.xubei.shop.ui.module.login.LoginTypeSelectActivity;
import com.xubei.shop.ui.module.login.PhoneBindActivity;
import com.xubei.shop.ui.module.login.RegisteredActivity;
import com.xubei.shop.ui.module.login.ResetPasswordActivity;
import com.xubei.shop.ui.module.login.ResetPayPwActivity;
import com.xubei.shop.ui.module.main.MainActivity;
import com.xubei.shop.ui.module.order.OrderAllActivity;
import com.xubei.shop.ui.module.order.OrderAllSellerActivity;
import com.xubei.shop.ui.module.order.OrderCreateActivity;
import com.xubei.shop.ui.module.order.OrderDetailActivity;
import com.xubei.shop.ui.module.order.OrderPayActivity;
import com.xubei.shop.ui.module.order.OrderRDetailActivity;
import com.xubei.shop.ui.module.order.OrderRenewActivity;
import com.xubei.shop.ui.module.order.OrderSearchActivity;
import com.xubei.shop.ui.module.order.OrderSuccessActivity;
import com.xubei.shop.ui.module.rights.RightsApplySellerActivity;
import com.xubei.shop.ui.module.rights.RightsDetailSellerActivity;
import com.xubei.shop.ui.module.rights.RightsProcessSellerActivity;
import com.xubei.shop.ui.module.setting.AboutActivity;
import com.xubei.shop.ui.module.setting.HelpCenterActivity;
import com.xubei.shop.ui.module.setting.HelpListActivity;
import com.xubei.shop.ui.module.setting.SettingActivity;
import com.xubei.shop.ui.module.setting.SettingNewMsgActivity;
import com.xubei.shop.ui.module.user.AlipayAddActivity;
import com.xubei.shop.ui.module.user.AlipayModifyActivity;
import com.xubei.shop.ui.module.user.FreezeDetailsActivity;
import com.xubei.shop.ui.module.user.FundDetailsActivity;
import com.xubei.shop.ui.module.user.FundDetailsGoldActivity;
import com.xubei.shop.ui.module.user.MyMsgActivity;
import com.xubei.shop.ui.module.user.MyPurseActivity;
import com.xubei.shop.ui.module.user.NoticeListActivity;
import com.xubei.shop.ui.module.user.RedemptionCenterActivity;
import com.xubei.shop.ui.module.user.RedemptionRecordActivity;
import com.xubei.shop.ui.module.user.UpdateBindPhoneActivity;
import com.xubei.shop.ui.module.user.UpdateNickNameActivity;
import com.xubei.shop.ui.module.user.UserGetMoneyActivity;
import com.xubei.shop.ui.module.user.UserInfoActivity;
import com.xubei.shop.ui.module.user.UserPayActivity;
import com.xubei.shop.ui.module.user.UserVerifiedActivity;
import com.xubei.shop.ui.module.welcome.GuidePagesActivity;
import com.xubei.shop.ui.module.welcome.WelcomeActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * 负责所有ActivityModule实例的管理
 * date：2018/7/20 16:00
 * author：Seraph
 **/
@Module
public abstract class ActivityBindingModule {

    //欢迎模块
    @ActivityScoped
    @ContributesAndroidInjector(modules = {WelcomeModule.class})
    abstract WelcomeActivity contributeWelcomeActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {GuidePagesModule.class})
    abstract GuidePagesActivity contributeGuidePagesActivityInjector();

    //通用模块
    @ActivityScoped
    @ContributesAndroidInjector(modules = {LocalImageListModule.class})
    abstract LocalImageListActivity contributeLocalImageListActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {PhotoPreviewModule.class})
    abstract PhotoPreviewActivity contributePhotoPreviewActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {CaptureModule.class})
    abstract CaptureActivity contributeCaptureActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {CommonAgentWebModule.class})
    abstract CommonAgentWebActivity contributeCommonAgentWebActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {CommonWebLocalModule.class})
    abstract CommonWebLocalActivity contributeCommonWebLocalActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {HelpListModule.class})
    abstract HelpListActivity contributeHelpListActivityInjector();

    //登录模块
    @ActivityScoped
    @ContributesAndroidInjector(modules = {LoginTypeSelectModule.class})
    abstract LoginTypeSelectActivity contributeLoginTypeSelectActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {PhoneBindModule.class})
    abstract PhoneBindActivity contributePhoneBindActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {LoginModule.class})
    abstract LoginActivity contributeLoginActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {RegisteredModule.class})
    abstract RegisteredActivity contributeRegisteredActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {ResetPasswordModule.class})
    abstract ResetPasswordActivity contributeResetPasswordActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {ResetPayPwModule.class})
    abstract ResetPayPwActivity contributeResetPayPwActivityInjector();

    //主页
    @ActivityScoped
    @ContributesAndroidInjector(modules = {MainModule.class})
    abstract MainActivity contributeMainActivityInjector();

    //消息
    @ActivityScoped
    @ContributesAndroidInjector(modules = {NoticeListModule.class})
    abstract NoticeListActivity contributeNoticeListActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {MyMsgModule.class})
    abstract MyMsgActivity contributeMyMsgActivityInjector();

    //维权模块
    @ActivityScoped
    @ContributesAndroidInjector(modules = {RightsApplySellerModule.class})
    abstract RightsApplySellerActivity contributeApplyRightsSellerActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {RightsDetailSellerModule.class})
    abstract RightsDetailSellerActivity contributeRightsDetailSellerActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {RightsProcessSellerModule.class})
    abstract RightsProcessSellerActivity contributeRightsProcessSellerActivityInjector();

    //设置
    @ActivityScoped
    @ContributesAndroidInjector(modules = {SettingModule.class})
    abstract SettingActivity contributeSettingActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {SettingNewMsgModule.class})
    abstract SettingNewMsgActivity contributeSettingNewMsgActivityInjector();


    @ActivityScoped
    @ContributesAndroidInjector(modules = {AboutModule.class})
    abstract AboutActivity contributeAboutActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {HelpCenterModule.class})
    abstract HelpCenterActivity contributeHelpCenterActivityInjector();

    //用户相关 UserModule
    @ActivityScoped
    @ContributesAndroidInjector(modules = {UserInfoModule.class})
    abstract UserInfoActivity contributeUserInfoActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {MyPurseModule.class})
    abstract MyPurseActivity contributeMyPurseActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {FundDetailsModule.class})
    abstract FundDetailsActivity contributeFundDetailsActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {FundDetailsGoldModule.class})
    abstract FundDetailsGoldActivity contributeFundDetailsGoldActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {RedemptionCenterModule.class})
    abstract RedemptionCenterActivity contributeRedemptionCenterActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {RedemptionRecordModule.class})
    abstract RedemptionRecordActivity contributeRedemptionRecordActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {FreezeDetailsModule.class})
    abstract FreezeDetailsActivity contributeFreezeDetailsActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {UserPayModule.class})
    abstract UserPayActivity contributeUserPayActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {UserGetMoneyModule.class})
    abstract UserGetMoneyActivity contributeUserGetMoneyActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {UserVerifiedModule.class})
    abstract UserVerifiedActivity contributeUserVerifiedActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {UpdateNickNameModule.class})
    abstract UpdateNickNameActivity contributeUpdateNickNameActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {UpdateBindPhoneModule.class})
    abstract UpdateBindPhoneActivity contributeUpdateBindPhoneActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AlipayAddModule.class})
    abstract AlipayAddActivity contributeAlipayAddActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AlipayModifyModule.class})
    abstract AlipayModifyActivity contributeAlipayModifyActivityInjector();

    //账号相关
    @ActivityScoped
    @ContributesAndroidInjector(modules = {AccLeaseAllTypeModule.class})
    abstract AccLeaseAllTypeActivity contributeAccLeaseAllTypeActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AccSearchModule.class})
    abstract AccSearchActivity contributeAccSearchActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AccSResultModule.class})
    abstract AccSResultActivity contributeAccSResultActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AccRListSearchModule.class})
    abstract AccRListSearchActivity contributeAccRListSearchActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AccRListSResultModule.class})
    abstract AccRListSResultActivity contributeAccRListSResultActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AccTopModule.class})
    abstract AccTopActivity contributeAccTopActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AccTopDesModule.class})
    abstract AccTopDesActivity contributeAccTopDesActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AccRSearchModule.class})
    abstract AccRSearchActivity contributeAccRSearchActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AccListModule.class})
    abstract AccListActivity contributeAccListActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AccDetailModule.class})
    abstract AccDetailActivity contributeAccDetailActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AccFreeDetailModule.class})
    abstract AccFreeDetailActivity contributeAccFreeDetailActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AccLeaseNoticeModule.class})
    abstract AccLeaseNoticeActivity contributeAccLeaseNoticeActivityActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AccRDetailModule.class})
    abstract AccRDetailActivity contributeAccRDetailActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AccReleaseModule.class})
    abstract AccReleaseActivity contributeAccReleaseActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AccAreaSelectModule.class})
    abstract AccAreaSelectActivity contributeAccAreaSelectActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AccRListModule.class})
    abstract AccRListActivity contributeAccRListActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AccRAgreementModule.class})
    abstract AccRAgreementActivity contributeAccRAgreementActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AccREditModule.class})
    abstract AccREditActivity contributeAccREditActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AccSCModule.class})
    abstract AccSCActivity contributeAccSCActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AccRSuccessModule.class})
    abstract AccRSuccessActivity contributeAccRSuccessActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AccUserFreeModule.class})
    abstract AccUserFreeActivity contributeAccUserFreeActivityInjector();

    //订单相关 OrderModule
    @ActivityScoped
    @ContributesAndroidInjector(modules = {OrderCreateModule.class})
    abstract OrderCreateActivity contributeOrderCreateActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {OrderSuccessModule.class})
    abstract OrderSuccessActivity contributeOrderSuccessActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {OrderDetailModule.class})
    abstract OrderDetailActivity contributeOrderDetailActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {OrderRDetailModule.class})
    abstract OrderRDetailActivity contributeOrderRDetailActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {OrderRenewModule.class})
    abstract OrderRenewActivity contributeOrderRenewActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {OrderSearchModule.class})
    abstract OrderSearchActivity contributeOrderSearchActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {OrderPayModule.class})
    abstract OrderPayActivity contributeOrderPayActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {OrderAllModule.class})
    abstract OrderAllActivity contributeOrderAllActivityInjector();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {OrderAllSellerModule.class})
    abstract OrderAllSellerActivity contributeOrderAllSellerActivityInjector();


}

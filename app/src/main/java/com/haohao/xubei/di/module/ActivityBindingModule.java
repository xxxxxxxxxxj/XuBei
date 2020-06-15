package com.haohao.xubei.di.module;


import com.haohao.xubei.di.module.activity.AboutModule;
import com.haohao.xubei.di.module.activity.AccAreaSelectModule;
import com.haohao.xubei.di.module.activity.AccDetailModule;
import com.haohao.xubei.di.module.activity.AccFreeDetailModule;
import com.haohao.xubei.di.module.activity.AccLeaseAllTypeModule;
import com.haohao.xubei.di.module.activity.AccLeaseNoticeModule;
import com.haohao.xubei.di.module.activity.AccListModule;
import com.haohao.xubei.di.module.activity.AccRAgreementModule;
import com.haohao.xubei.di.module.activity.AccRDetailModule;
import com.haohao.xubei.di.module.activity.AccREditModule;
import com.haohao.xubei.di.module.activity.AccRListModule;
import com.haohao.xubei.di.module.activity.AccRListSResultModule;
import com.haohao.xubei.di.module.activity.AccRListSearchModule;
import com.haohao.xubei.di.module.activity.AccRSearchModule;
import com.haohao.xubei.di.module.activity.AccRSuccessModule;
import com.haohao.xubei.di.module.activity.AccReleaseModule;
import com.haohao.xubei.di.module.activity.AccSCModule;
import com.haohao.xubei.di.module.activity.AccSResultModule;
import com.haohao.xubei.di.module.activity.AccSearchModule;
import com.haohao.xubei.di.module.activity.AccTopDesModule;
import com.haohao.xubei.di.module.activity.AccTopModule;
import com.haohao.xubei.di.module.activity.AccUserFreeModule;
import com.haohao.xubei.di.module.activity.AlipayAddModule;
import com.haohao.xubei.di.module.activity.AlipayModifyModule;
import com.haohao.xubei.di.module.activity.CaptureModule;
import com.haohao.xubei.di.module.activity.CommonAgentWebModule;
import com.haohao.xubei.di.module.activity.CommonWebLocalModule;
import com.haohao.xubei.di.module.activity.FreezeDetailsModule;
import com.haohao.xubei.di.module.activity.FundDetailsGoldModule;
import com.haohao.xubei.di.module.activity.FundDetailsModule;
import com.haohao.xubei.di.module.activity.GuidePagesModule;
import com.haohao.xubei.di.module.activity.HelpCenterModule;
import com.haohao.xubei.di.module.activity.HelpListModule;
import com.haohao.xubei.di.module.activity.LocalImageListModule;
import com.haohao.xubei.di.module.activity.LoginModule;
import com.haohao.xubei.di.module.activity.LoginTypeSelectModule;
import com.haohao.xubei.di.module.activity.MainModule;
import com.haohao.xubei.di.module.activity.MyMsgModule;
import com.haohao.xubei.di.module.activity.MyPurseModule;
import com.haohao.xubei.di.module.activity.NoticeListModule;
import com.haohao.xubei.di.module.activity.OrderAllModule;
import com.haohao.xubei.di.module.activity.OrderAllSellerModule;
import com.haohao.xubei.di.module.activity.OrderCreateModule;
import com.haohao.xubei.di.module.activity.OrderDetailModule;
import com.haohao.xubei.di.module.activity.OrderPayModule;
import com.haohao.xubei.di.module.activity.OrderRDetailModule;
import com.haohao.xubei.di.module.activity.OrderRenewModule;
import com.haohao.xubei.di.module.activity.OrderSearchModule;
import com.haohao.xubei.di.module.activity.OrderSuccessModule;
import com.haohao.xubei.di.module.activity.PhoneBindModule;
import com.haohao.xubei.di.module.activity.PhotoPreviewModule;
import com.haohao.xubei.di.module.activity.RedemptionCenterModule;
import com.haohao.xubei.di.module.activity.RedemptionRecordModule;
import com.haohao.xubei.di.module.activity.RegisteredModule;
import com.haohao.xubei.di.module.activity.ResetPasswordModule;
import com.haohao.xubei.di.module.activity.ResetPayPwModule;
import com.haohao.xubei.di.module.activity.RightsApplySellerModule;
import com.haohao.xubei.di.module.activity.RightsDetailSellerModule;
import com.haohao.xubei.di.module.activity.RightsProcessSellerModule;
import com.haohao.xubei.di.module.activity.SettingModule;
import com.haohao.xubei.di.module.activity.SettingNewMsgModule;
import com.haohao.xubei.di.module.activity.UpdateBindPhoneModule;
import com.haohao.xubei.di.module.activity.UpdateNickNameModule;
import com.haohao.xubei.di.module.activity.UserGetMoneyModule;
import com.haohao.xubei.di.module.activity.UserInfoModule;
import com.haohao.xubei.di.module.activity.UserPayModule;
import com.haohao.xubei.di.module.activity.UserVerifiedModule;
import com.haohao.xubei.di.module.activity.WelcomeModule;
import com.haohao.xubei.di.scoped.ActivityScoped;
import com.haohao.xubei.ui.module.account.AccAreaSelectActivity;
import com.haohao.xubei.ui.module.account.AccDetailActivity;
import com.haohao.xubei.ui.module.account.AccFreeDetailActivity;
import com.haohao.xubei.ui.module.account.AccLeaseAllTypeActivity;
import com.haohao.xubei.ui.module.account.AccLeaseNoticeActivity;
import com.haohao.xubei.ui.module.account.AccListActivity;
import com.haohao.xubei.ui.module.account.AccRAgreementActivity;
import com.haohao.xubei.ui.module.account.AccRDetailActivity;
import com.haohao.xubei.ui.module.account.AccREditActivity;
import com.haohao.xubei.ui.module.account.AccRListActivity;
import com.haohao.xubei.ui.module.account.AccRListSResultActivity;
import com.haohao.xubei.ui.module.account.AccRListSearchActivity;
import com.haohao.xubei.ui.module.account.AccRSearchActivity;
import com.haohao.xubei.ui.module.account.AccRSuccessActivity;
import com.haohao.xubei.ui.module.account.AccReleaseActivity;
import com.haohao.xubei.ui.module.account.AccSCActivity;
import com.haohao.xubei.ui.module.account.AccSResultActivity;
import com.haohao.xubei.ui.module.account.AccSearchActivity;
import com.haohao.xubei.ui.module.account.AccTopActivity;
import com.haohao.xubei.ui.module.account.AccTopDesActivity;
import com.haohao.xubei.ui.module.account.AccUserFreeActivity;
import com.haohao.xubei.ui.module.common.photolist.LocalImageListActivity;
import com.haohao.xubei.ui.module.common.photopreview.PhotoPreviewActivity;
import com.haohao.xubei.ui.module.common.scan.CaptureActivity;
import com.haohao.xubei.ui.module.common.web.CommonAgentWebActivity;
import com.haohao.xubei.ui.module.common.web.CommonWebLocalActivity;
import com.haohao.xubei.ui.module.login.LoginActivity;
import com.haohao.xubei.ui.module.login.LoginTypeSelectActivity;
import com.haohao.xubei.ui.module.login.PhoneBindActivity;
import com.haohao.xubei.ui.module.login.RegisteredActivity;
import com.haohao.xubei.ui.module.login.ResetPasswordActivity;
import com.haohao.xubei.ui.module.login.ResetPayPwActivity;
import com.haohao.xubei.ui.module.main.MainActivity;
import com.haohao.xubei.ui.module.order.OrderAllActivity;
import com.haohao.xubei.ui.module.order.OrderAllSellerActivity;
import com.haohao.xubei.ui.module.order.OrderCreateActivity;
import com.haohao.xubei.ui.module.order.OrderDetailActivity;
import com.haohao.xubei.ui.module.order.OrderPayActivity;
import com.haohao.xubei.ui.module.order.OrderRDetailActivity;
import com.haohao.xubei.ui.module.order.OrderRenewActivity;
import com.haohao.xubei.ui.module.order.OrderSearchActivity;
import com.haohao.xubei.ui.module.order.OrderSuccessActivity;
import com.haohao.xubei.ui.module.rights.RightsApplySellerActivity;
import com.haohao.xubei.ui.module.rights.RightsDetailSellerActivity;
import com.haohao.xubei.ui.module.rights.RightsProcessSellerActivity;
import com.haohao.xubei.ui.module.setting.AboutActivity;
import com.haohao.xubei.ui.module.setting.HelpCenterActivity;
import com.haohao.xubei.ui.module.setting.HelpListActivity;
import com.haohao.xubei.ui.module.setting.SettingActivity;
import com.haohao.xubei.ui.module.setting.SettingNewMsgActivity;
import com.haohao.xubei.ui.module.user.AlipayAddActivity;
import com.haohao.xubei.ui.module.user.AlipayModifyActivity;
import com.haohao.xubei.ui.module.user.FreezeDetailsActivity;
import com.haohao.xubei.ui.module.user.FundDetailsActivity;
import com.haohao.xubei.ui.module.user.FundDetailsGoldActivity;
import com.haohao.xubei.ui.module.user.MyMsgActivity;
import com.haohao.xubei.ui.module.user.MyPurseActivity;
import com.haohao.xubei.ui.module.user.NoticeListActivity;
import com.haohao.xubei.ui.module.user.RedemptionCenterActivity;
import com.haohao.xubei.ui.module.user.RedemptionRecordActivity;
import com.haohao.xubei.ui.module.user.UpdateBindPhoneActivity;
import com.haohao.xubei.ui.module.user.UpdateNickNameActivity;
import com.haohao.xubei.ui.module.user.UserGetMoneyActivity;
import com.haohao.xubei.ui.module.user.UserInfoActivity;
import com.haohao.xubei.ui.module.user.UserPayActivity;
import com.haohao.xubei.ui.module.user.UserVerifiedActivity;
import com.haohao.xubei.ui.module.welcome.GuidePagesActivity;
import com.haohao.xubei.ui.module.welcome.WelcomeActivity;

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

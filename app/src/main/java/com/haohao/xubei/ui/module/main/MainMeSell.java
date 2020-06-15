package com.haohao.xubei.ui.module.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ObjectUtils;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.umeng.analytics.MobclickAgent;
import com.haohao.xubei.AppConfig;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.data.db.table.UserTable;
import com.haohao.xubei.data.network.glide.GlideApp;
import com.haohao.xubei.databinding.ActMainMeSellBinding;
import com.haohao.xubei.ui.module.base.ABaseFragment;
import com.haohao.xubei.ui.module.main.contract.MainMeBuySellContract;
import com.haohao.xubei.ui.module.main.presenter.MainMeBuySellPresenter;
import com.haohao.xubei.ui.module.user.model.AcctManageBean;
import com.haohao.xubei.utlis.Tools;

import java.util.Locale;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;


/**
 * 我的(卖家)
 * date：2017/2/20 16:38
 * author：Seraph
 **/
public class MainMeSell extends ABaseFragment<MainMeBuySellContract.Presenter> implements MainMeBuySellContract.View {

    private final String mPageName = "MainMeSellFragment";

    private ActMainMeSellBinding binding;

    @Override
    protected View initDataBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.act_main_me_sell, container, false);
        binding.setContext(this);
        return binding.getRoot();
    }

    @Inject
    MainMeBuySellPresenter presenter;

    @Override
    protected MainMeBuySellContract.Presenter getMVPPresenter() {
        return presenter;
    }


    @Inject
    public MainMeSell() {
    }

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.title.ivAvatar.setOnClickListener(this::onViewTitleClicked);
        binding.title.tvName.setOnClickListener(this::onViewTitleClicked);
        binding.title.ivUserInfo.setOnClickListener(this::onViewTitleClicked);
        binding.title.ivKyIsopen.setOnClickListener(this::onViewTitleClicked);
        binding.title.ivDjIsopen.setOnClickListener(this::onViewTitleClicked);
        binding.title.ivKyJbIsopen.setOnClickListener(this::onViewTitleClicked);
        binding.title.tvRecharge.setOnClickListener(this::onViewTitleClicked);
        binding.title.tvFundDetails.setOnClickListener(this::onViewTitleClicked);
        binding.title.tvFundJbDetails.setOnClickListener(this::onViewTitleClicked);
        binding.tool.tvTool1.setOnClickListener(this::onViewToolClicked);
        binding.tool.tvTool2.setOnClickListener(this::onViewToolClicked);
        binding.tool.tvTool3.setOnClickListener(this::onViewToolClicked);
        binding.tool.tvTool4.setOnClickListener(this::onViewToolClicked);
        binding.tool.tvTool5.setOnClickListener(this::onViewToolClicked);
        binding.tool.tvTool6.setOnClickListener(this::onViewToolClicked);
        binding.tool.tvTool7.setOnClickListener(this::onViewToolClicked);
        //通知加载子界面完成
        RxBus.get().post(AppConstants.RxBusAction.TAG_MAIN_ME3, true);
    }


    //页面title部分的点击监听
    public void onViewTitleClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_avatar://头像
                presenter.onSeeBigAvatar();
                break;
            case R.id.tv_name://昵称
            case R.id.iv_user_info://详细信息
                ARouter.getInstance().build(AppConstants.PagePath.USER_INFO).navigation();
                break;
            case R.id.iv_ky_isopen://可用是否打开
                presenter.isLogin();
                if (acctManageBean == null) {
                    return;
                }
                MainMe.isShowKyAcct = !MainMe.isShowKyAcct;
                RxBus.get().post(AppConstants.RxBusAction.TAG_MAIN_ME, acctManageBean);
                break;
            case R.id.iv_ky_jb_isopen://可用是否打开
                presenter.isLogin();
                if (acctManageBean == null) {
                    return;
                }
                MainMe.isShowKyJbAcct = !MainMe.isShowKyJbAcct;
                RxBus.get().post(AppConstants.RxBusAction.TAG_MAIN_ME, acctManageBean);
                break;
            case R.id.iv_dj_isopen://冻结是否打开
                presenter.isLogin();
                if (acctManageBean == null) {
                    return;
                }
                MainMe.isShowDjAcct = !MainMe.isShowDjAcct;
                RxBus.get().post(AppConstants.RxBusAction.TAG_MAIN_ME, acctManageBean);
                break;
            case R.id.tv_recharge://充值
                ARouter.getInstance().build(AppConstants.PagePath.USER_PAY).navigation();
                break;
            case R.id.tv_fund_details://资金明细
                ARouter.getInstance().build(AppConstants.PagePath.USER_FUNDDETAILS).navigation();
                break;
            case R.id.tv_fund_jb_details://金币说明
                ARouter.getInstance().build(AppConstants.PagePath.USER_FUNDDETAILS_JB).navigation();
                break;
        }
    }

    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_1://我出租订单
                ARouter.getInstance().build(AppConstants.PagePath.ORDER_SELLER_ALL).withInt("type", 0).navigation();
                break;
            case R.id.tv_order_sell1://待付款
                ARouter.getInstance().build(AppConstants.PagePath.ORDER_SELLER_ALL).withInt("type", 2).navigation();
                break;
            case R.id.tv_order_sell2://租赁中
                ARouter.getInstance().build(AppConstants.PagePath.ORDER_SELLER_ALL).withInt("type", 3).navigation();
                break;
            case R.id.tv_order_sell3://已完成
                ARouter.getInstance().build(AppConstants.PagePath.ORDER_SELLER_ALL).withInt("type", 4).navigation();
                break;
            case R.id.tv_order_sell4://售后维权
                ARouter.getInstance().build(AppConstants.PagePath.ORDER_SELLER_ALL).withInt("type", 5).navigation();
                break;
            case R.id.ll_2://我的账号管理
                ARouter.getInstance().build(AppConstants.PagePath.ACC_R_LIST).withInt("type", 0).navigation();
                break;
            case R.id.tv_account_sell1://审核中
                ARouter.getInstance().build(AppConstants.PagePath.ACC_R_LIST).withInt("type", 1).navigation();
                break;
            case R.id.tv_account_sell2://展示中
                ARouter.getInstance().build(AppConstants.PagePath.ACC_R_LIST).withInt("type", 2).navigation();
                break;
            case R.id.tv_account_sell3://仓库中
                ARouter.getInstance().build(AppConstants.PagePath.ACC_R_LIST).withInt("type", 3).navigation();
                break;
            case R.id.tv_account_sell4://出租中
                ARouter.getInstance().build(AppConstants.PagePath.ACC_R_LIST).withInt("type", 4).navigation();
                break;
        }
    }


    //页面常用工具点击
    public void onViewToolClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_tool1://我的钱包
                //如果没有登录，则先进行登录
                ARouter.getInstance().build(AppConstants.PagePath.USER_MYPURSE).navigation();
                break;
            case R.id.tv_tool2://我的收藏
                ARouter.getInstance().build(AppConstants.PagePath.ACC_SC).navigation();
                break;
            case R.id.tv_tool3://帮助中心
                ARouter.getInstance().build(AppConstants.PagePath.SETTING_HELPCENTER).navigation();
                break;
            case R.id.tv_tool4://联系客服
                Tools.startQQCustomerService(getContext(), AppConfig.SERVICE_QQ);
                break;
            case R.id.tv_tool5://我的消息
                ARouter.getInstance().build(AppConstants.PagePath.USER_MYMSG).navigation();
                break;
            case R.id.tv_tool6://兑换中心
                ARouter.getInstance().build(AppConstants.PagePath.USER_REDEMPTIONCENTER).navigation();
                break;
            case R.id.tv_tool7://免费租号
                ARouter.getInstance().build(AppConstants.PagePath.ACC_USER_FREE).navigation();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }


    @Override
    public void setUserInfo(UserTable userBean) {
        if (userBean != null) {
            GlideApp.with(this).load(userBean.getAvatar()).placeholder(R.mipmap.common_avatar_default).error(R.mipmap.common_avatar_default).into(binding.title.ivAvatar);
            String tempMobile = "";
            if (ObjectUtils.isNotEmpty(userBean.getMobile()) && userBean.getMobile().length() == 11) {
                tempMobile = userBean.getMobile().substring(0, 3) + "***" + userBean.getMobile().substring(7, 11);
            }
            binding.title.tvName.setText(ObjectUtils.isNotEmpty(userBean.getUsernick()) ? userBean.getUsernick() : tempMobile);
        } else {
            binding.title.ivAvatar.setImageResource(R.mipmap.common_avatar_default);
            binding.title.tvName.setText("登录/注册");
            setAcctManageBean(null);
        }
    }


    @Subscribe(tags = {@Tag(AppConstants.RxBusAction.TAG_MAIN_ME2)})
    public void setUpdateUserInfo(Boolean aBoolean) {
        presenter.updateUserBean();
    }


    private AcctManageBean acctManageBean;

    @Subscribe(tags = {@Tag(AppConstants.RxBusAction.TAG_MAIN_ME)})
    public void setAcctManageBean(AcctManageBean manageBean) {
        this.acctManageBean = manageBean;
        String tempKyBalance;
        String tempDjBalance;
        String tempKyJbBalance;
        if (acctManageBean != null) {
            if (MainMe.isShowKyAcct) {
                tempKyBalance = String.format(Locale.getDefault(), "%.2f", acctManageBean.aviableAmt);
            } else {
                tempKyBalance = "******";
            }
            if (MainMe.isShowDjAcct) {
                tempDjBalance = String.format(Locale.getDefault(), "%.2f", acctManageBean.freezeAmt);
            } else {
                tempDjBalance = "******";
            }
            if (MainMe.isShowKyJbAcct) {
                tempKyJbBalance = String.format(Locale.getDefault(), "%d", acctManageBean.icoinAmount);
            } else {
                tempKyJbBalance = "******";
            }
        } else {
            if (MainMe.isShowKyAcct) {
                tempKyBalance = "0.00";
            } else {
                tempKyBalance = "******";
            }
            if (MainMe.isShowDjAcct) {
                tempDjBalance = "0.00";
            } else {
                tempDjBalance = "******";
            }
            if (MainMe.isShowKyJbAcct) {
                tempKyJbBalance = "0.00";
            } else {
                tempKyJbBalance = "******";
            }
        }
        //可用余额
        binding.title.tvKyBalance.setText(tempKyBalance);
        //冻结金额
        binding.title.tvDjBalance.setText(tempDjBalance);
        //可用金币
        binding.title.tvKyJbBalance.setText(tempKyJbBalance);
        //切换眼睛打开或者关闭图标
        if (MainMe.isShowKyAcct) {
            binding.title.ivKyIsopen.setImageResource(R.mipmap.icon_eyes_open);
            binding.title.textView135.setVisibility(View.VISIBLE);
        } else {
            binding.title.ivKyIsopen.setImageResource(R.mipmap.icon_eyes_close);
            binding.title.textView135.setVisibility(View.GONE);
        }
        if (MainMe.isShowDjAcct) {
            binding.title.ivDjIsopen.setImageResource(R.mipmap.icon_eyes_open);
            binding.title.textView136.setVisibility(View.VISIBLE);
        } else {
            binding.title.ivDjIsopen.setImageResource(R.mipmap.icon_eyes_close);
            binding.title.textView136.setVisibility(View.GONE);
        }
        if (MainMe.isShowKyJbAcct) {
            binding.title.ivKyJbIsopen.setImageResource(R.mipmap.icon_eyes_open);
        } else {
            binding.title.ivKyJbIsopen.setImageResource(R.mipmap.icon_eyes_close);
        }
        //刷新一下熊的眼睛动画
        if (MainMe.isShowKyJbAcct && MainMe.isShowDjAcct) {
            binding.title.ivXiong.setImageResource(R.mipmap.act_mian_me_xiong1);
        } else if (MainMe.isShowDjAcct) {
            binding.title.ivXiong.setImageResource(R.mipmap.act_mian_me_xiong2);
        } else if (MainMe.isShowKyJbAcct) {
            binding.title.ivXiong.setImageResource(R.mipmap.act_mian_me_xiong3);
        } else {
            binding.title.ivXiong.setImageResource(R.mipmap.act_mian_me_xiong4);
        }
    }
}

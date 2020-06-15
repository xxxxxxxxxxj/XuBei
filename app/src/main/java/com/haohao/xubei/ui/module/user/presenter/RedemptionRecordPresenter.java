package com.haohao.xubei.ui.module.user.presenter;

import android.content.ClipData;
import android.content.ClipboardManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.haohao.xubei.AppConfig;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.data.db.help.UserBeanHelp;
import com.haohao.xubei.data.network.rx.RxSchedulers;
import com.haohao.xubei.data.network.service.ApiCommonService;
import com.haohao.xubei.data.network.service.ApiPCService;
import com.haohao.xubei.ui.module.account.model.GameBean;
import com.haohao.xubei.ui.module.base.ABaseSubscriber;
import com.haohao.xubei.ui.module.user.contract.RedemptionRecordContract;
import com.haohao.xubei.ui.module.user.model.RedemptionRecordBean;
import com.haohao.xubei.ui.views.NoDataView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.appcompat.app.AlertDialog;

/**
 * 兑换记录
 * date：2017/12/4 15:04
 * author：Seraph
 **/
public class RedemptionRecordPresenter extends RedemptionRecordContract.Presenter {

    private UserBeanHelp userBeanHelp;

    private ApiCommonService apiCommonService;

    private ClipboardManager clipboardManager;

    private ApiPCService apiPCService;

    @Inject
    RedemptionRecordPresenter(UserBeanHelp userBeanHelp, ApiCommonService apiCommonService, ApiPCService apiPCService, ClipboardManager clipboardManager) {
        this.userBeanHelp = userBeanHelp;
        this.apiCommonService = apiCommonService;
        this.clipboardManager = clipboardManager;
        this.apiPCService = apiPCService;
    }


    private List<RedemptionRecordBean> list = new ArrayList<>();

    private int position;

    @Override
    public void start() {
        mView.initLayout(list);
        onRefresh();
    }

    //复制登录码
    public void doCopyCode(int position) {
        RedemptionRecordBean recordBean = list.get(position);
        if (clipboardManager != null) {
            if (StringUtils.isEmpty(recordBean.exchangeOrderNo)) {
                ToastUtils.showShort("登录码已过期");
            } else {
                clipboardManager.setPrimaryClip(ClipData.newPlainText(null, recordBean.exchangeOrderNo));
                ToastUtils.showShort("登录码复制成功");
            }
        }
    }

    public void doCopyAcc(int position) {
        RedemptionRecordBean recordBean = list.get(position);
        if (clipboardManager != null) {
            if (StringUtils.isEmpty(recordBean.gameAccount)) {
                ToastUtils.showShort("账号密码已过期");
            } else {
                clipboardManager.setPrimaryClip(ClipData.newPlainText(null, recordBean.gameAccount));
                ToastUtils.showShort("游戏账号复制成功");
            }
        }
    }

    public void doCopyPw(int position) {
        RedemptionRecordBean recordBean = list.get(position);
        if (clipboardManager != null) {
            if (StringUtils.isEmpty(recordBean.gamePwd)) {
                ToastUtils.showShort("账号密码已过期");
            } else {
                clipboardManager.setPrimaryClip(ClipData.newPlainText(null, recordBean.gamePwd));
                ToastUtils.showShort("游戏密码复制成功");
            }
        }
    }

    //扫一扫
    public void doScan(int position) {
        this.position = position;
        mView.onScan();
    }


    //pc的上号器登录
    public void onPCLogin(int resultType, final String resultStr) {
        //判断是否上号器的二维码
        if (resultStr.contains(AppConfig.SCAN_QC_PC_STR)) {
            new AlertDialog.Builder(mView.getContext())
                    .setTitle("上号器登录")
                    .setMessage("是否确认登录？")
                    .setPositiveButton("确认", (dialog, which) -> {
                        String SCAN_QC_PC_STR = resultStr.replace(AppConfig.SCAN_QC_PC_STR, "");
                        onPcOrderLogin(SCAN_QC_PC_STR);
                    })
                    .setNegativeButton("取消", null).show();

        } else {
            ToastUtils.showShort("请扫描正确的二维码");
        }
    }

    //再次体验
    public void doTy(int position) {
        RedemptionRecordBean recordBean = list.get(position);
        //跳转对应的游戏列表
        GameBean gameBean = new GameBean();
        gameBean.setGameName(recordBean.gameName);
        gameBean.game_id = recordBean.exchangeGameId;
        //1手游，0端游
        if ("1".equals(recordBean.isPhone)) {
            gameBean.game_type = "2";
        } else {
            gameBean.game_type = "1";
        }
        ARouter.getInstance().build(AppConstants.PagePath.ACC_LIST).withSerializable("bean", gameBean).navigation();
    }

    public void onRefresh() {
        getExchangeByMobile();
    }

    //获取兑换信息
    private void getExchangeByMobile() {
        apiCommonService.getExchangeByMobile(userBeanHelp.getAuthorization())
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.setNoDataView(NoDataView.LOADING))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<List<RedemptionRecordBean>>() {
                    @Override
                    protected void onSuccess(List<RedemptionRecordBean> centerBeanList) {
                        list.clear();
                        if (centerBeanList.size() == 0) {
                            mView.setNoDataView(NoDataView.NO_DATA);
                        } else {
                            list.addAll(centerBeanList);
                            mView.setNoDataView(NoDataView.LOADING_OK);
                        }
                        mView.notifyDataSetChanged();
                    }

                    @Override
                    protected void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                        mView.setNoDataView(NoDataView.NET_ERR);
                    }
                });
    }


    //pc上号器扫码登录
    private void onPcOrderLogin(String authStr) {
        RedemptionRecordBean recordBean = list.get(position);
        String tempEndTime = recordBean.gameEndTime;
        if (tempEndTime != null && tempEndTime.length() > 19) {
            tempEndTime = tempEndTime.substring(0, 19);
        }
        apiPCService.mobileLogin(authStr, recordBean.exchangeOrderNo, tempEndTime)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<String>(mView) {
                    @Override
                    public void onSuccess(String s) {
                        ToastUtils.showShort("请求登录成功");
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }
}

package com.xubei.shop.ui.module.setting.presenter;

import com.blankj.utilcode.util.ToastUtils;
import com.xubei.shop.data.db.help.UserBeanHelp;
import com.xubei.shop.data.network.rx.RxSchedulers;
import com.xubei.shop.data.network.service.ApiCommonService;
import com.xubei.shop.ui.module.base.ABaseSubscriber;
import com.xubei.shop.ui.module.setting.contract.SettingNewMsgContract;
import com.xubei.shop.ui.module.user.model.MessageConfigBean;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 设置新消息
 * date：2017/12/4 14:38
 * author：Seraph
 **/
public class SettingNewMsgPresenter extends SettingNewMsgContract.Presenter {

    private UserBeanHelp userBeanHelp;

    private ApiCommonService apiCommonService;

    @Inject
    SettingNewMsgPresenter(UserBeanHelp userBeanHelp, ApiCommonService apiCommonService) {
        this.userBeanHelp = userBeanHelp;
        this.apiCommonService = apiCommonService;
    }

    private List<MessageConfigBean> list = new ArrayList<>();

    @Override
    public void start() {
        getMessageConfig();
    }


    //下架通知
    public void onMsgObained(boolean isChecked) {
        saveMessageConfig("1", isChecked ? "1" : "2");
    }

    //维权通知
    public void onMsgRightsProtection(boolean isChecked) {
        saveMessageConfig("2", isChecked ? "1" : "2");
    }

    //置顶通知
    public void onMsgTopping(boolean isChecked) {
        saveMessageConfig("3", isChecked ? "1" : "2");
    }

    //置顶结束通知
    public void onMsgToppingEnd(boolean isChecked) {
        saveMessageConfig("4", isChecked ? "1" : "2");
    }

    //系统通知
    public void onMsgSys(boolean isChecked) {
        saveMessageConfig("5", isChecked ? "1" : "2");
    }

    //商品租赁通知
    public void onMsgZl(boolean isChecked) {
        saveMessageConfig("6", isChecked ? "1" : "2");
    }

    //获取消息配置
    private void getMessageConfig() {
        apiCommonService.getMessageConfig(userBeanHelp.getAuthorization())
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<List<MessageConfigBean>>(mView) {
                    @Override
                    protected void onSuccess(List<MessageConfigBean> messageConfigBeans) {
                        list.clear();
                        list.addAll(messageConfigBeans);
                        mView.showMessageConfig(list);
                    }

                    @Override
                    protected void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });

    }


    //保存消息配置
    private void saveMessageConfig(String type, String status) {
        apiCommonService.saveMessageConfig(userBeanHelp.getAuthorization(), type, status)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<String>(mView) {
                    @Override
                    protected void onSuccess(String s) {
                        //更新设置
                        for (int i = 0; i < list.size(); i++) {
                            if (type.equals(list.get(i).type)) {
                                list.get(i).status = status;
                            }
                        }
                    }

                    @Override
                    protected void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                        //恢复当前的设置
                        mView.showMessageConfig(list);
                    }
                });

    }


//    //刷新按钮状态
//    private void upDateIsOpenMsg() {
//        Boolean isChecked = spUtils.getBoolean(AppConstants.SPAction.IS_OPEN_PUSH, true);
//        mView.setIsOpenMsg(isChecked);
//        //进行推送开关操作
//        if (isChecked && JPushInterface.isPushStopped(mView.getContext().getApplicationContext())) {
//            //打开推送
//            JPushInterface.resumePush(mView.getContext().getApplicationContext());
//        } else if (!isChecked && !JPushInterface.isPushStopped(mView.getContext().getApplicationContext())) {
//            //停止推送
//            JPushInterface.stopPush(mView.getContext().getApplicationContext());
//        }
//    }
//
//    //点击消息开关
//    public void checkSwitch(boolean isChecked) {
//        if (isChecked) {
//            spUtils.put(AppConstants.SPAction.IS_OPEN_PUSH, true);
//            //刷新按钮状态
//            upDateIsOpenMsg();
//        } else {
//            new AlertDialog.Builder(mView.getContext())
//                    .setTitle("提示")
//                    .setMessage("关闭消息通知，您将无法及时收到相关推送信息！")
//                    .setNegativeButton("取消", (dialog, which) -> {
//                        //刷新按钮状态
//                        upDateIsOpenMsg();
//                    })
//                    .setPositiveButton("确定", (dialog, which) -> {
//                        spUtils.put(AppConstants.SPAction.IS_OPEN_PUSH, false);
//                        //刷新按钮状态
//                        upDateIsOpenMsg();
//                    }).show();
//        }
//
//    }


}

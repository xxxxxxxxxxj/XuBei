package com.haohao.xubei.ui.module.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.haohao.xubei.AppConfig;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.data.db.help.UserBeanHelp;
import com.haohao.xubei.data.network.rx.RxSchedulers;
import com.haohao.xubei.data.network.service.Api8Service;
import com.haohao.xubei.ui.module.base.ABaseSubscriber;
import com.haohao.xubei.ui.module.main.model.NoticeBean;

import org.json.JSONObject;

import javax.inject.Inject;

import cn.jpush.android.api.JPushInterface;
import dagger.android.DaggerBroadcastReceiver;


/**
 * 极光推送接收广播
 * 消息类型文档：https://docs.jiguang.cn/jpush/client/Android/android_api/
 * date：2018/11/9 15:27
 * author：xiongj
 **/
public class JPushReceiver extends DaggerBroadcastReceiver {


    @Inject
    UserBeanHelp userBeanHelp;

    @Inject
    Api8Service api8Service;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Bundle bundle = intent.getExtras();
        if (intent.getAction() == null || bundle == null) {
            return;
        }
        switch (intent.getAction()) {
            case JPushInterface.ACTION_MESSAGE_RECEIVED: // 收到了自定义消息。消息内容是
                break;
            case JPushInterface.ACTION_NOTIFICATION_RECEIVED:         //收到了通知
                if (AppConfig.DEBUG) {
                    String tempTitle = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
                    String tempContent = bundle.getString(JPushInterface.EXTRA_ALERT);
                    String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
                    LogUtils.i("收到了通知:" + tempTitle + " " + tempContent + " " + extras);
                }
                break;
            case JPushInterface.ACTION_NOTIFICATION_OPENED://用户点击打开了通知
                //判断app是否不在前台 并且 没有活动界面
                if (!AppUtils.isAppForeground() && ActivityUtils.getActivityList().size() == 0) {
                    AppUtils.launchApp(context.getPackageName());
                }
//                String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
//                String content = bundle.getString(JPushInterface.EXTRA_ALERT);
//                String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//                int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);

                openNotification(bundle);
                break;

        }
    }


    //打开通知
    private void openNotification(Bundle bundle) {
        NoticeBean noticeBean;
        try {
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            String extend = new JSONObject(extras).optString("extend");
            noticeBean = new Gson().fromJson(extend, NoticeBean.class);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (noticeBean == null) {
            //直接跳转消息本身的信息
            return;
        }
        //调用设置已读接口
        if (userBeanHelp.isLogin() && api8Service != null && noticeBean.id != null) {
            api8Service.updateStatus(userBeanHelp.getAuthorization(), noticeBean.id)
                    .compose(RxSchedulers.io_main_business())
                    .subscribe(new ABaseSubscriber<String>() {
                        @Override
                        protected void onSuccess(String s) {

                        }

                        @Override
                        protected void onError(String errStr) {

                        }
                    });
        }

        startTypeActivity(noticeBean);
    }

    //根据消息类型，跳转不同的界面 1.商品下架通知2.维权通知3置顶商品租赁通知.4.置顶结束通知 5.系统消息(文字和url)
    private void startTypeActivity(NoticeBean noticeBean) {
        switch (noticeBean.msgType) {
            case 1://我的账号管理页面（仓库中）
                ARouter.getInstance()
                        .build(AppConstants.PagePath.ACC_R_LIST)
                        .withInt("type", 3)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).navigation();
                break;
            case 2://我的出租订单（维权中）
                ARouter.getInstance()
                        .build(AppConstants.PagePath.ORDER_SELLER_ALL)
                        .withInt("type", 5)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).navigation();
                break;
            case 3://我的账号管理
            case 4:
                ARouter.getInstance()
                        .build(AppConstants.PagePath.ACC_R_LIST)
                        .withInt("type", 0)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).navigation();
                break;
            case 5://系统消息
                if ("1".equals(noticeBean.dataType)) {
                    //url
                    ARouter.getInstance()
                            .build(AppConstants.PagePath.COMM_AGENTWEB)
                            .withString("title", noticeBean.title)
                            .withString("webUrl", noticeBean.dataValue)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).navigation();
                } else {
                    String webContent;
                    if (noticeBean.secondTitle.equals(noticeBean.dataValue)) {
                        webContent = noticeBean.secondTitle;
                    } else {
                        webContent = noticeBean.secondTitle + "<br/>" + noticeBean.dataValue;
                    }
                    ARouter.getInstance()
                            .build(AppConstants.PagePath.COMM_WEBLOCAL)
                            .withString("title", noticeBean.title)
                            .withString("webContent", webContent)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).navigation();
                }
                break;
            default://其它的跳转app首页
                //判断程序是否在前台，如果在后台则切换成前台
                if (!AppUtils.isAppForeground()) {
                    //app切换到前台
                    ARouter.getInstance().build(AppConstants.PagePath.APP_MAIN).navigation();
                }
                break;

        }
    }
}

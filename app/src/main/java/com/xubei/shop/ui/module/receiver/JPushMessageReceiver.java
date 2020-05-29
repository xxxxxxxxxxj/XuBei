package com.xubei.shop.ui.module.receiver;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.blankj.utilcode.util.LogUtils;
import com.xubei.shop.AppConfig;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;

/**
 * 极光推送接收广播
 * 消息类型文档：https://docs.jiguang.cn/jpush/client/Android/android_api/
 * date：2018/11/9 15:27
 * author：xiongj
 **/
public class JPushMessageReceiver extends cn.jpush.android.service.JPushMessageReceiver {

    private Context mContext;


    private final Handler mHandler = new Handler(msg -> {
        if (mContext == null) {
            return false;
        }
        JPushMessage jPushMessage = (JPushMessage) msg.obj;
        switch (jPushMessage.getSequence()) {
            case 1: //设置别名
                JPushInterface.setAlias(mContext, jPushMessage.getSequence(), jPushMessage.getAlias());
                break;
            case 2://设置标签
                JPushInterface.setTags(mContext, jPushMessage.getSequence(), jPushMessage.getTags());
                break;
            case 3://删除别名
                JPushInterface.deleteAlias(mContext, jPushMessage.getSequence());
                break;
            case 4://移除标签
                JPushInterface.cleanTags(mContext, jPushMessage.getSequence());
                break;
        }
        return false;
    });

    //tag 增删查改的操作会在此方法中回调结果。
    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        if (AppConfig.DEBUG) {
            LogUtils.i(jPushMessage.toString());
        }
        if (context != null) {
            this.mContext = context.getApplicationContext();
        }
        //如果错误为6002或者6024，则60秒后重试
        //判断设置标签是否成功
        switch (jPushMessage.getErrorCode()) {
            case 6002:
            case 6024:
                Message message = mHandler.obtainMessage();
                message.obj = jPushMessage;
                mHandler.sendMessageDelayed(message, 60 * 1000);
                break;
        }

    }


    //alias 相关的操作会在此方法中回调结果。
    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        if (AppConfig.DEBUG) {
            LogUtils.i(jPushMessage.toString());
        }
        if (context != null) {
            this.mContext = context.getApplicationContext();
        }
        switch (jPushMessage.getErrorCode()) {
            case 6002:
            case 6024:
                Message message = mHandler.obtainMessage();
                message.obj = jPushMessage;
                mHandler.sendMessageDelayed(message, 60 * 1000);
                break;
        }
    }


}

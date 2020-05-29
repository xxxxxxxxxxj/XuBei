package com.xubei.shop.ui.module.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.xubei.shop.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * 自动输入服务
 */
@TargetApi(16)
public class AccessibilityAutoInputService extends AccessibilityService {


    //private ClipboardManager clipboardManager = null;

//    @Override
//    protected void onServiceConnected() {
//        super.onServiceConnected();
//        // 通过代码可以动态配置，但是可配置项少一点
////        AccessibilityServiceInfo accessibilityServiceInfo = new AccessibilityServiceInfo();
////        accessibilityServiceInfo.eventTypes = AccessibilityEvent.TYPE_WINDOWS_CHANGED
////                | AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
////                | AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
////                | AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
////        accessibilityServiceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
////        accessibilityServiceInfo.notificationTimeout = 0;
////        accessibilityServiceInfo.flags = AccessibilityServiceInfo.DEFAULT;
////        setServiceInfo(accessibilityServiceInfo);
//    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // 此方法是在主线程中回调过来的，所以消息是阻塞执行的
        AccessibilityOperator.getInstance().updateEvent(this, event);
        // int eventType = event.getEventType();
        String pkgName = event.getPackageName().toString();
        String pkgUi = event.getClassName().toString();
//        switch (eventType) {
//            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:  //收到通知栏消息
//                break;
//            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:    //界面状态改变
//                break;
//            case AccessibilityEvent.TYPE_VIEW_CLICKED:   //点击事件
//                //如果是自己的app则在此处获取对于的账号和密码
//                if ("com.haohao.zuhaohao".equals(pkgName) && "android.widget.Button".equals(pkgUi)) {
//                    Flowable.intervalRange(1, 1, 1, 1, TimeUnit.SECONDS)
//                            .compose(RxSchedulers.<Long>io_main()).subscribe(new Consumer<Long>() {
//                        @Override
//                        public void accept(Long aLong) throws Exception {
//                            //在点击的时候获取对应隐藏账号密码的控件
////                            List<AccessibilityNodeInfo> list1 = AccessibilityOperator.getInstance().findNodesById("com.haohao.zuhaohao:id/tv_loading_msg");
////                            if (list1 != null && list1.size() >= 1) {
////                                listInputUser = list1.get(0).getText().toString().split("~!@#%&");
////                            }
//                            //获取剪切板内容
//                            initInputUserInfo();
//                        }
//                    });
//
//                }
//                break;
//            case AccessibilityEvent.CONTENT_CHANGE_TYPE_TEXT: //文本改变
//                break;
//            //省略其他的一堆可以监听的事件
//        }

        //  LogUtils.i(pkgUi + ":" + eventType);
        if ("com.tencent.mobileqq".equals(pkgName) && "com.tencent.qqconnect.wtlogin.Login".equals(pkgUi)) {
            //获取界面所有的控件
            List<AccessibilityNodeInfo> list = AccessibilityOperator.getInstance().findAllNodesByText();
            //获取界面第一个和第二个输入控件和登录按钮
            List<AccessibilityNodeInfo> listInput = new ArrayList<>();
            AccessibilityNodeInfo loginButton = null;
            for (AccessibilityNodeInfo nodeInfo : list) {
                if ("android.widget.EditText".equals(nodeInfo.getClassName().toString())) {
                    listInput.add(nodeInfo);
                } else if ("android.widget.Button".equals(nodeInfo.getClassName().toString())) {//登录
                    loginButton = nodeInfo;
                }
            }
            Context context = null;
            try {
                context = createPackageContext("com.haohao.zuhaohao", Context.CONTEXT_IGNORE_SECURITY);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if (context != null) {
                SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.SPAction.SP_NAME, Context.MODE_MULTI_PROCESS);
                if (sharedPreferences != null) {
                    String username = sharedPreferences.getString(AppConstants.SPAction.TEMP_USER_NAME, "");
                    String password = sharedPreferences.getString(AppConstants.SPAction.TEMP_PASSWORD, "");
//            String username = SPUtils.getInstance().getString(AppConstants.SPAction.TEMP_USER_NAME);
//            String password = SPUtils.getInstance(AppConstants.SPAction.SP_NAME).getString(AppConstants.SPAction.TEMP_PASSWORD);
                    //LogUtils.i(username + " " + password);

                    if (listInput.size() >= 2) {
                        AccessibilityNodeInfo userName = listInput.get(0);
                        AccessibilityNodeInfo passWord = listInput.get(1);
                        Bundle arguments = new Bundle();
                        if (!"".equals(username)) {
                            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, username);
                            userName.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                        }
                        //设置对于的值
                        if (!"".equals(password)) {
                            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, password);
                            passWord.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                        }
                        sharedPreferences.edit().putString(AppConstants.SPAction.TEMP_PASSWORD, "").putString(AppConstants.SPAction.TEMP_USER_NAME, "").apply();
                    }
                }
            }
        }
    }

//    //剪切板内容操作
//    private void initInputUserInfo() {
////        if (clipboardManager == null) {
////            clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
////        }
////        if (clipboardManager != null) {
////            String tempStr = clipboardManager.getText().toString();
//////                                //判断是否是
//////                                if (tempStr.length() == 48) {
//////                                    //获取密文
//////                                    tempStr = new String(EncryptUtils.decryptHexString3DES(tempStr, new byte[]{0x00, 0x00, 0x00, 0x00,
//////                                            (byte) 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0x00,
//////                                            (byte) 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0x00, 0x00, 0x00,
//////                                            0x00, (byte) 0x00}),"UTF-8");
//////                                }
////            LogUtils.i(tempStr);
////            if (tempStr.contains("~!@#%&")) {
////                listInputUser = tempStr.split("~!@#%&");
////            }
////        }
//    }

    @Override
    public void onInterrupt() {

    }


}

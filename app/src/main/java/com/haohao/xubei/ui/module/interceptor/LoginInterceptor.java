package com.haohao.xubei.ui.module.interceptor;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.data.db.DBManager;
import com.haohao.xubei.data.db.help.UserBeanHelp;

/**
 * 登录拦截器
 * date：2019/1/8 10:19
 * author：xiongj
 **/
@Interceptor(priority = 1)
public class LoginInterceptor implements IInterceptor {

    private UserBeanHelp userBeanHelp;


    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        //如果跳转的界面需要登录则进行判断
        if (postcard.getExtra() == AppConstants.ARAction.LOGIN) {
            //自动跳转登录界面（判断是否登录）
            if (userBeanHelp.isLogin(true)) {
                callback.onContinue(postcard);
            } else {
                callback.onInterrupt(null);
            }
        } else {
            callback.onContinue(postcard);
        }

    }

    @Override
    public void init(Context context) {
        userBeanHelp = new UserBeanHelp(new DBManager(context).getDaoSession());
    }
}

package com.haohao.xubei.data.db.help;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.data.db.gen.DaoSession;
import com.haohao.xubei.data.db.gen.UserTableDao;
import com.haohao.xubei.data.db.table.UserTable;

import javax.inject.Inject;

/**
 * 专门对于当前登录的用户设计的一个帮助类
 * date：2017/7/26 17:01
 * author：Seraph
 **/
public class UserBeanHelp {

    private UserTableDao mUserTableDao;

    @Inject
    public UserBeanHelp(DaoSession daoSession) {
        mUserTableDao = daoSession.getUserTableDao();
    }


    //获取令牌
    public String getAuthorization() {
        return getAuthorization(false);
    }

    /**
     * 获取当前用户的用户id
     */
    public String getAuthorization(boolean isAutoJumpLogin) {
        if (getUserBean(isAutoJumpLogin) != null) {
            return getUserBean(isAutoJumpLogin).getAuthorization();
        }
        return null;
    }


    /**
     * 获取用户表
     */
    public UserTable getUserBean() {
        return getUserBean(false);
    }

    /**
     * 获取用户表
     *
     * @param isAutoJumpLogin 没有用户是否自动跳转登录界面
     */
    public UserTable getUserBean(boolean isAutoJumpLogin) {
        if (mUserTableDao != null && mUserTableDao.loadAll().size() > 0) {
            return mUserTableDao.loadAll().get(0);
        }
        if (isAutoJumpLogin) {
            startLoginActivity();
        }
        return null;
    }


    /**
     * 保存唯一用户
     */
    public void saveUserBean(UserTable userTable) {
        mUserTableDao.deleteAll();
        mUserTableDao.save(userTable);
    }

    /**
     * 更新或者保存用户信息
     */
    public void save(UserTable userTable) {
        mUserTableDao.save(userTable);
    }

    /**
     * 清除登录信息
     */
    public void cleanUserBean() {
        mUserTableDao.deleteAll();
    }


    public boolean isLogin() {
        return isLogin(false);
    }

    /**
     * 是否登录
     *
     * @param isAutoJumpLogin 在没有登录的情况下是否跳转
     */
    public boolean isLogin(boolean isAutoJumpLogin) {
        if (mUserTableDao != null && mUserTableDao.loadAll().size() > 0) {
            return true;
        }
        if (isAutoJumpLogin) {
            startLoginActivity();
        }
        return false;
    }

    private void startLoginActivity() {
        ToastUtils.showShort("请先登录");
        ARouter.getInstance().build(AppConstants.PagePath.LOGIN_TYPESELECT).navigation();
    }
}

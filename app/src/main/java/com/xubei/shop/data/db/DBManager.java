package com.xubei.shop.data.db;

import android.app.Application;
import android.content.Context;

import com.xubei.shop.AppConfig;
import com.xubei.shop.data.db.gen.DaoMaster;
import com.xubei.shop.data.db.gen.DaoSession;

import javax.inject.Inject;

/**
 * greenDao数据库初始化
 * date：2017/2/14 17:40
 * author：Seraph
 **/
public class DBManager {

    private DBOpenHelper mHelper;

    @Inject
    DBManager(Application a) {
        this(a.getApplicationContext());
    }

    public DBManager(Context context) {
        mHelper = new DBOpenHelper(context.getApplicationContext(), AppConfig.DB_NAME);
    }

    /**
     * 获取操作数据库的Session
     */
    public DaoSession getDaoSession() {
        //该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        DaoMaster mDaoMaster = new DaoMaster(mHelper.getWritableDatabase());
        return mDaoMaster.newSession();
    }

}

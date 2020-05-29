package com.xubei.shop.di.module;

import com.xubei.shop.data.db.DBManager;
import com.xubei.shop.data.db.gen.DaoSession;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * 全局Module
 * date：2017/4/5 15:37
 * author：Seraph
 **/
@Module
public abstract class AppModule {

    @Provides
    @Singleton
    static DaoSession provideDaoSession(DBManager dbGreenDaoHelp) {
        return dbGreenDaoHelp.getDaoSession();
    }


}

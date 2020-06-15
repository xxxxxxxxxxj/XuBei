package com.haohao.xubei.di.module;

import com.haohao.xubei.data.db.DBManager;
import com.haohao.xubei.data.db.gen.DaoSession;

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

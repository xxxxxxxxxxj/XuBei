package com.xubei.shop.data.db;

import android.content.Context;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.xubei.shop.data.db.gen.DaoMaster;
import com.xubei.shop.data.db.gen.SearchHistoryTableDao;

import org.greenrobot.greendao.database.Database;

import com.xubei.shop.data.db.gen.UserTableDao;

/**
 * 管理表的升级
 * date：2017/9/18 11:24
 * author：Seraph
 *
 **/
public class DBOpenHelper extends DaoMaster.OpenHelper {

    DBOpenHelper(Context context, String name) {
        super(context, name);
    }


    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        //数据库升级
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db,ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db,ifExists);
            }
        }, UserTableDao.class, SearchHistoryTableDao.class);
    }
}

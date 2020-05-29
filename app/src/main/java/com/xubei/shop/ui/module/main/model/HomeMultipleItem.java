package com.xubei.shop.ui.module.main.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 首页多布局Item
 * date：2018/1/30 11:41
 * author：Seraph
 **/
public class HomeMultipleItem<T> implements MultiItemEntity {

    //账号类型
    public static final int TYPE_ACCOUNT_TYPE = 1;
    //账号列表
    public static final int TYPE_ACCOUNT_LIST = 2;
    //更多账号
    public static final int TYPE_ACCOUNT_MORE = 3;

    private int itemType;

    public T date;

    public int pos;

    public HomeMultipleItem(int itemType) {
        this.itemType = itemType;
    }

    public HomeMultipleItem(int itemType, T date) {
        this.itemType = itemType;
        this.date = date;
    }

    public HomeMultipleItem(int itemType, T date, int pos) {
        this.itemType = itemType;
        this.date = date;
        this.pos = pos;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}

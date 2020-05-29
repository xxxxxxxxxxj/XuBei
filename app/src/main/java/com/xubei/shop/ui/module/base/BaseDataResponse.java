package com.xubei.shop.ui.module.base;

/**
 * 网络返回数据结构最外层
 * date：2017/2/15 10:38
 * author：Seraph
 *
 **/
public class BaseDataResponse<T> {

    /**
     * 业务逻辑标识
     */
    public int code = -1;
    /**
     * 提示信息
     */
    public String message;


    public T result;

}

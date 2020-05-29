package com.xubei.shop.ui.module.user.model;

import java.io.Serializable;

/**
 * 消息配置bean
 * date：2018/12/25 11:02
 * author：xiongj
 **/
public class MessageConfigBean implements Serializable {

    public int id;
    //1启用。2禁用
    public String status;
    //类型:1.商品下架通知2.维权通知3置顶商品租赁通知.4.置顶结束通知5.系统消息
    public String type;

    public int userId;

}

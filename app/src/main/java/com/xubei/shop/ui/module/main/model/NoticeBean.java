package com.xubei.shop.ui.module.main.model;

import java.io.Serializable;

/**
 * 活动公告bean
 * date：2017/5/4 15:57
 * author：Seraph
 **/
public class NoticeBean implements Serializable {

    public Long id;             //推送消息使用

    public Long messageId;      //消息列表使用
    public String title;       //标题
    public String secondTitle; //二级标题
    public Integer status;    //是否已读,0:未读 1:已读 ,
    public Integer type;        //消息类型 0 公告 1系统 2个人
    public String dataType;  //数据类型   1是落地页
    public String dataValue=""; //数据值
    public String sendTime;  //发送消息时间

    //类型:1.商品下架通知2.维权通知3置顶商品租赁通知.4.置顶结束通知 5.系统消息(文字和url)
    public int msgType = 5;

}

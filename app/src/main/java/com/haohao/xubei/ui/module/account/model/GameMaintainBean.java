package com.haohao.xubei.ui.module.account.model;

import java.io.Serializable;

/**
 * 游戏停服配置
 * date：2018/11/22 10:50
 * author：xiongj
 **/
public class GameMaintainBean implements Serializable {

    public int id;// 来源主键 ,

    public String beginTime;// 维护开始时间 ,

    public String bigGameId;// 游戏ID ,

    public String createTime;// 创建时间 ,

    public String endTime;//维护结束时间 ,

    public String reason;// 原因描述 ,

    public int status;// 状态 , 1是在维护

    public String tipContent;// 提示内容 ,

    public String updateTime;// 更新时间


}

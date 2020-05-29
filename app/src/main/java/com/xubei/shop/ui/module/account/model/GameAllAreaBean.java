package com.xubei.shop.ui.module.account.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 所有服务器模型
 * date：2018/3/5 10:26
 * author：Seraph
 * mail：417753393@qq.com
 **/
public class GameAllAreaBean implements Serializable {

    public String gameType;// 1,
    public String id;// 245,
    public String gameName;// 英雄联盟",
    public String parentId;// 0,
    public String state;// 0,
    public String createUserId;// 131,
    public String grade;// 1,
    public String allName;//英雄联盟",
    public String sort;// 1,
    public ArrayList<GameAllAreaBean> children;//包含的数据
}

package com.xubei.shop.ui.module.account.model;

import java.io.Serializable;

/**
 * 游戏类目搜索
 * date：2018/11/22 18:00
 * author：xiongj
 **/
public class AccRSResultBean implements Serializable {

    public String game_id;    //游戏id
    public String game; //游戏名称
    public String url; //游戏图片
    public String fz_url;//分站游戏图片
    public int is_show; //是否显示   0不显示 1显示
    public int type; //1普通游戏 2热门推荐 ,
    public int game_type; //1端游，2手游
    public String logoUrl; //游戏logo

}

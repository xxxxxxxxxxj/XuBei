package com.haohao.xubei.ui.module.main.model;

import com.haohao.xubei.ui.module.account.model.GameBean;

import java.io.Serializable;
import java.util.List;

/**
 * 游戏分类Bean
 * date：2017/11/29 09:35
 * author：Seraph
 *
 **/
public class GameTypeBean implements Serializable {


    public String id;

    //分类类型名称
    public String name;

    //类型下面包含的游戏
    public List<GameBean> content;


}

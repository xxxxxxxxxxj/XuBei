package com.xubei.shop.ui.module.account.model;

import com.blankj.utilcode.util.ObjectUtils;

import java.io.Serializable;

/**
 * 游戏Bean
 * date：2017/11/28 13:59
 * author：Seraph
 **/
public class GameBean implements Serializable {

    public String game_id;//371                *
    public String game_type;//1 端游  2手游     *
    protected String game_name;//游戏名称         *
    public String img_url = "";//图片      (热门游戏，租号类型包含的游戏)
    public String goto_link; // 跳转的超连接    *

    //租号游戏类型列表使用
    private String name; //游戏名称

    public String getGameName() {
        if (ObjectUtils.isNotEmpty(game_name)) {
            return game_name;
        } else if (ObjectUtils.isNotEmpty(name)) {
            return name;
        } else {
            return "";
        }
    }

    public void setGameName(String gameName) {
        this.game_name = gameName;
    }

    //游戏名称首字母（自己转换获取）
    public String firstLetter;

    //服务器返回的游戏首字母
    public String firstWord;
    //筛选游戏名称
    public String game;
}

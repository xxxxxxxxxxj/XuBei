package com.haohao.xubei.ui.module.welcome.model;

import com.haohao.xubei.ui.module.account.model.GameBean;

/**
 * 广告Bean
 * date：2017/5/4 15:57
 * author：Seraph
 **/
public class ADBean extends GameBean {

    public String dataId;

    public String ad_image; // 广告图片

    //1游戏列表，2网页链接
    public String ad_type;

    public String ad_url;//广告跳转的url

    //设置游戏名称
    public void setGameName(String gameName) {
        this.game_name = gameName;
    }

}

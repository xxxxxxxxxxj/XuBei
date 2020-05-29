package com.xubei.shop.ui.module.setting.model;

import java.io.Serializable;

/**
 * 问题分类bean
 * date：2018/8/7 18:21
 * author：Seraph
 *
 **/
public class ProblemBean implements Serializable {

    public String menuId;

    public String menuName;

    public Integer logo;

    public ProblemBean(String menuName, Integer logo, String menuId) {
        this.menuName = menuName;
        this.logo = logo;
        this.menuId = menuId;
    }
}

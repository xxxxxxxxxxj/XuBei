package com.xubei.shop.ui.module.account.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 布局部分数据提交bean(界面填写数据)
 * date：2017/12/19 10:46
 * author：Seraph
 *
 **/
public class SaveGoodsBean implements Serializable {
    //手机号
    public String phone;
    //qq号
    public String qq;
    //动态的值
    public List<HashMap<String,String>> configValues = new ArrayList<>();
}

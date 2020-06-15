package com.haohao.xubei.ui.module.account.model;

import java.io.Serializable;
import java.util.List;

/**
 * 我的发布账号详情回填数据bean
 * date：2017/12/1 11:10
 * author：Seraph
 *
 **/
public class OutGoodsDetailValuesBean implements Serializable {

    //动态的回填字段
    public List<FieldsBean> fields;

    //图片
    public List<ImgBean> imgList;


    public static class FieldsBean implements Serializable {

        public String key;

        public List<Object> value;       //属性值

    }

    public static class ImgBean implements Serializable {

        public String location;

    }


}

package com.haohao.xubei.ui.module.account.model;

import java.io.Serializable;
import java.util.List;

/**
 * 游戏高级筛选bean
 * date：2017/11/28 13:59
 * author：Seraph
 * mail：417753393@qq.com
 **/
public class GameSearchRelationBean implements Serializable {


    public String fieldName; //高级选项名
    public String showName;    //显示名称
    public String paramName;  //请求参数名
    public int isCheckbox;  //是否可多选值域，多选值用逗号隔开
    public String linkType; //多选值用用什么隔开，比如一般用逗号
    public List<OgssBean> ogss; // 高级选项值域列表


    public static class OgssBean implements Serializable {
        /**
         * id : 34
         * keyName : 一元租号
         * val : 1-1
         * valType : 2
         * isEnter : 0
         * remark : 通过价格范围检索
         * isEnable : 1
         * createTime : 2018-10-26 11:52:36:000
         * ord : 0
         */

        public int id;
        public String keyName; //名称
        public String val; //值
        public int valType;//值类型 1:字符串,2:范围值
        public int isEnter; // 是否需要输入值去检索，1：是，0：否 ,
        public String remark;
        public int isEnable; //是否有效 1:有效,0:无效 ,
        public int ord; //排序号

    }

    //需要提交到服务器的选项值
    private OgssBean selectValue;

    public OgssBean getSelectValue() {
        return selectValue;
    }

    public void setSelectValue(OgssBean selectValue) {
        this.selectValue = selectValue;
    }
}

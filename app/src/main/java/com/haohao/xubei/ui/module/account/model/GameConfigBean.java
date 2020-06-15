package com.haohao.xubei.ui.module.account.model;

import java.io.Serializable;

/**
 * 游戏发布配置bean
 * date：2017/11/28 13:59
 * author：Seraph
 * mail：417753393@qq.com
 **/
public class GameConfigBean implements Serializable {

    public String id;
    public String fieldName;//标题
    public String type;//(type: 1：文本;2：时间;3：下拉框;4：单选框;5：文件上传;6：密码;7：文本域;8：身份证图片;9：复选框;)
    public String sqlName;//goodsTitle
    public String errorMsg;//标题长度请在8到50个字之间,
    public String moduleType;//(moduleType:1:基础信息，2：扩展信息，3：交易信息
    public String defaultVal;//"radio":0是 1否，"text"//输入类型
    public String required;//0必填 1非必填

    public Integer maxLength;//最大字符
    public Integer minLength;//最小字符

    public String placeHolder; //输入提示

    public String tips; //提示语

    public String unit;//单位

    public String reg;//正则表达式验证值
}

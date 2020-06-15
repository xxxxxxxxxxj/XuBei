package com.haohao.xubei.ui.module.account.model;

import java.io.Serializable;
import java.util.List;

/**
 * 我的发布账号详情bean
 * date：2017/12/1 11:10
 * author：Seraph
 * mail：417753393@qq.com
 **/
public class OutGoodsDetailBean implements Serializable {
    public String createTime;//创建时间
    public String actity;//活动描述
    public String actityStr;//活动数字描述
    public String allNightPlay;//十元包夜
    public String bigGameId;// 游戏ID
    public String bigGameName;// 游戏名
    public String businessNo;// 商户号
    public String buyCount;// 购买次数
    public String compensate;// 0 赔付 1和空位不赔付
    public String coupon;// 点券
    public String cutisNum;// 皮肤数量
    public String dayHours;// 天租
    public String dayLease;// 天租
    public String ddaylease;// 天租-n
    public String foregift;// 押金
    public String gameAccount;// 账号
    public String gameAllName;// 游戏全称
    public String gameId;// 区服ID
    public String gameName;// 区服名称
    public String gamePwd;// 账号密码
    public String goldCoins;// 金币数
    public String goodsCode;// 商品编号
    public String goodsFlag;// 是否删除
    public int goodsStatus;// 商品状态
    public String goodsTitle;// 商品标题
    public String gradeId;// 等级
    public String gradingFrameId;// 段位框
    public String gradingId;// 段位id
    public String gradingName;// 段位名
    public String heroNames;// 英雄信息
    public String heroNum;// 英雄数
    public String hotValue;// 热度值
    public String hourLease;// 时租-n
    public String id;// id
    public String isPhone;// 是否是手游 0是端游1是手游
    public String isPlayQualifying;// 是否排位
    public String isShow;// 是否明文显示密码。0-否，1-是
    public String isShowText;// 是否明文显示密码。0-否，1-是-描述
    public String isme;// 无用
    public String leasePrice;// 时租
    public String linghuozupaiId;// 无用
    public String phone;// 手机号
    public String phoneType;// 0是安卓 1是苹果
    public String phoneTypeText;// 0是安卓 1是苹果-描述
    public String qq;// QQ信息
    public String remark;// 描述
    public String roleName;// 角色名称
    public String runeStones;// 符文
    public String searchTitle;// 搜索标题
    public String serverId;// 业务Id
    public String serverName;// 业务名称
    public String shortLease;// 最短租赁时间
    public int signseller;// 是否签约卖家
    public String skinNames;// 皮肤信息
    public String tenHours;// 十元包夜
    public String text;// 文本显示
    public String userId;// 用户UserID
    public String weekLease;// 周租
    public String wweeklease;// 周租-n

    public boolean isDeadlineOnline;//是否到时不下线

    public List<GoodsPicLocationBean> picture;

    public List<NewGoodsWzDtoBean> prototypelist;

    public GameMaintainBean gameMaintain;//游戏停服配置


    public class GoodsPicLocationBean implements Serializable {

        public String location;

    }

    public static class NewGoodsWzDtoBean implements Serializable {

        public String ctime;
        public String gameId; //游戏id
        public String goodId;  //商品id
        public String id;
        public String key;
        public String keyName; //属性名字
        public String value;    //属性值

        //获取翻译正确的值
        public String getStrValue() {
            //排位兼容web
            if (keyName != null && keyName.contains("排位")) {
                switch (value) {
                    case "0":
                        return "允许";
                    case "1":
                        return "不允许";
                    default:
                        return value;
                }
            }
            //兼容段位
            if (keyName != null && keyName.contains("段位")) {
                switch (value) {
                    case "yyht0":
                        return "英勇黄铜Ⅴ";
                    case "yyht1":
                        return "英勇黄铜Ⅳ";
                    case "yyht2":
                        return "英勇黄铜Ⅲ";
                    case "yyht3":
                        return "英勇黄铜Ⅱ";
                    case "yyht4":
                        return "英勇黄铜Ⅰ";
                    case "bqby0":
                        return "不屈白银Ⅴ";
                    case "bqby1":
                        return "不屈白银Ⅳ";
                    case "bqby2":
                        return "不屈白银Ⅲ";
                    case "bqby3":
                        return "不屈白银Ⅱ";
                    case "bqby4":
                        return "不屈白银Ⅰ";
                    case "ryhj0":
                        return "荣耀黄金Ⅴ";
                    case "ryhj1":
                        return "荣耀黄金Ⅳ";
                    case "ryhj2":
                        return "荣耀黄金Ⅲ";
                    case "ryhj3":
                        return "荣耀黄金Ⅱ";
                    case "ryhj4":
                        return "荣耀黄金Ⅰ";
                    case "hgbj0":
                        return "华贵铂金Ⅴ";
                    case "hgbj1":
                        return "华贵铂金Ⅳ";
                    case "hgbj2":
                        return "华贵铂金Ⅲ";
                    case "hgbj3":
                        return "华贵铂金Ⅱ";
                    case "hgbj4":
                        return "华贵铂金Ⅰ";
                    case "cczs0":
                        return "璀璨钻石Ⅴ";
                    case "cczs1":
                        return "璀璨钻石Ⅳ";
                    case "cczs2":
                        return "璀璨钻石Ⅲ";
                    case "cczs3":
                        return "璀璨钻石Ⅱ";
                    case "cczs4":
                        return "璀璨钻石Ⅰ";
                    case "cfds0":
                        return "超凡大师";
                    case "zqwz0":
                        return "最强王者";
                    case "0":
                        return "无段位框";
                    case "1":
                        return "黄铜框";
                    case "2":
                        return "白银框";
                    case "3":
                        return "黄金框";
                    case "4":
                        return "铂金框";
                    case "5":
                        return "钻石框";
                    case "6":
                        return "大师框";
                    case "7":
                        return "王者框";
                    case "wdw":
                        return "无段位";
                    default:
                        return value;
                }
            }
            return value == null ? "" : value;
        }
    }

    //商品文字描述状态
    public String getGoodsStatusStr() {
        switch (goodsStatus) {
            case 1:
                return "仓库中";
            case 2:
                return "待审核";
            case 3:
                return "立即租用";
            case 4:
                return "出租中";
            default:
                return "未上架商品";
        }
    }
}

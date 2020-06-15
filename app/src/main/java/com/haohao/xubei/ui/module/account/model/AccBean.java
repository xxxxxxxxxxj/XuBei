package com.haohao.xubei.ui.module.account.model;

import java.io.Serializable;

/**
 * 账号列表bean
 * date：2017/11/30 14:01
 * author：Seraph
 **/
public class AccBean implements Serializable {

    public String big_game_name; // 游戏名称
    public int compensate = 1;// 0 赔付 1和空位不赔付
    public int signseller; //签约 0否，1是
    public String cutis_num; // 皮肤数量
    public double foregift;// 押金
    public String game_all_name;//":"英雄联盟-电信-水晶之痕", // 游戏区服
    public String game_name; //水晶之痕// 游戏所在大区
    public Integer goods_status; // 商品状态，1 仓库中，2  待审核，3  展示中，4 出租中，5 已出售，6 出售分销展示
    public String goods_title;//":"花木兰 寒冰源计划  蛇年蛇女  猴年猴王 电玩全套  源计划剑圣  情人节琴女 SKT滑板鞋等", // 商品描述
    public String grading_id;
    public String grading_name;//"不屈白银III",
    public String hero_num; // 英雄数量
    public String id;// 商品ID
    public String if_play_qualifying;//支持, // 是否支持打排位  0 1
    public double lease_price;//2.75, // 租赁价格
    public String role_name;//":"Anti丨Fate丶京", // 角色名称
    public String search_title;//"花木兰 寒冰源计划  蛇年蛇女  猴年猴王 电玩全套  源计划剑圣  情人节琴女 SKT滑板鞋等", // 商品描述
    public int short_lease; // 最短租赁时间
    public String status_name;//展示中", // 商品状态描述
    public int buy_count;//350, // 成功出租/出售次数
    public String imageurl = "";//http://xubei-files.oss-cn-hangzhou.aliyuncs.com/goodsImg/2d637044c958462e9ac8f14e68b86db9.gif // 首页图片地址

    //活动
    public String actity = "";

    public String remark;  //备注
    //   "leasescore": 158,
    //   "cutis_num": 501,
    public long game_id; //游戏id
    //   "phone_type": 3,
    //     "is_phone": 0,
    //   "success_rent_num": null,
    //   "goods_id": "1850044",
    //    "is_show": 3,
    public String stickorder; //1热推
    public long hot_value;
    //   "is_show_text": "上号器登录",

    public String deadLineOnLine;   //1 到时不下线

}

package com.xubei.shop.data.db.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 用户表
 * date：2017/2/14 17:06
 * author：Seraph
 *
 * <p>
 * 一般用户表保存一条当前的登录用户的信息，此为测试数据库示例。
 * 在实际开发中，直接用服务端返回的用户id会更加方便使用。
 * 在注销的时候，清除数据库表。
 * 更新的实体判断以主键为基础。
 **/
@Entity
public class UserTable {

    /**
     * id 自增长
     */
    @Id(autoincrement = true)
    private Long _id;

    /**
     * jwt认证
     */
    @NotNull
    private String authorization;

    /**
     * 用户id
     */
    private String userid;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别
     */
    private String sex;

    /**
     * 用户手机
     */
    private String mobile;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String usernick;

    /**
     * QQ号
     */
    private String qqnumber;

    @Generated(hash = 2015368422)
    public UserTable(Long _id, @NotNull String authorization, String userid,
            String avatar, String sex, String mobile, String username,
            String usernick, String qqnumber) {
        this._id = _id;
        this.authorization = authorization;
        this.userid = userid;
        this.avatar = avatar;
        this.sex = sex;
        this.mobile = mobile;
        this.username = username;
        this.usernick = usernick;
        this.qqnumber = qqnumber;
    }

    @Generated(hash = 726134616)
    public UserTable() {
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getAuthorization() {
        return this.authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getUserid() {
        return this.userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsernick() {
        return this.usernick;
    }

    public void setUsernick(String usernick) {
        this.usernick = usernick;
    }

    public String getQqnumber() {
        return this.qqnumber;
    }

    public void setQqnumber(String qqnumber) {
        this.qqnumber = qqnumber;
    }
    


}

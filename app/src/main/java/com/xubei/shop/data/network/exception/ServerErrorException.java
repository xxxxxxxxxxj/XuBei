package com.xubei.shop.data.network.exception;

/**
 * 业务逻辑失败以及部分自定义异常
 * date：2017/2/17 09:39
 * author：Seraph
 **/
public class ServerErrorException extends Exception {

    /**
     * 业务失败
     */
    public final static int CODE_STATUS_ERR = 0;

    /**
     * 网络失败
     */
    public final static int CODE_NET_ERR = 3;

    /**
     * 关闭图形验证码
     */
    public final static int CODE_CLOSE_IMAGE_CODE = 601;

    /**
     * 新接口的token失效
     */
    public final static int CODE_TOKEN_ERR_401 = 401;


    /**
     * 登录频繁
     */
    public final static String CODE_TOKEN_ERR_401_STR = "用户信息失效，请重新登录";


    /**
     * 错误码{@link #CODE_STATUS_ERR,#CODE_NET_ERR}
     */
    private int code = -1;


    public ServerErrorException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}

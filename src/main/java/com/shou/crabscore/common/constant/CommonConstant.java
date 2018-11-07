package com.shou.crabscore.common.constant;

/**
 * @author spencercjh
 */
public interface CommonConstant {
    byte[] AES_KEY = {34, 70, 37, -19, -41, -44, -114, -103, 91, -13, -115, -57, 94, 17, 67, 3};

    /**
     * 成功
     */
    Integer SUCCESS = 200;
    /**
     * JWT中自定义校验值的变量名
     */
    String MYKEY = "MYKEY";
    /**
     * JWT中自定义校验值
     */
    String MYKEY_VALUE = "mykeymCPuT5IHaZ628q5f91Ok5Sv13f1bfh5z";
    /**
     * 全部大赛 0
     */
    Integer USER_COMPETITION_ALL = 0;
    /**
     * JWT过期时间 30分钟
     */
    Long TTLMILLIS = 1800000L;
    /**
     * 用户默认头像
     */
    String USER_DEFAULT_AVATAR = "";

    /**
     * 用户正常状态
     */
    Integer USER_STATUS_NORMAL = 1;

    /**
     * 用户禁用状态
     */
    Integer USER_STATUS_LOCK = 0;

    /**
     * 普通用户
     */
    Integer USER_TYPE_NORMAL = 0;

    /**
     * 公共权限
     */
    Integer USER_TYPE_COMMON = 999;
    /**
     * 管理员
     */
    Integer USER_TYPE_ADMIN = 1;

    String ADMIN = "admin";
    /**
     * 评委
     */
    Integer USER_TYPE_JUDGE = 2;

    String JUDGE = "judge";
    /**
     * 工作人员
     */
    Integer USER_TYPE_STAFF = 3;

    String STAFF = "staff";
    /**
     * 参选单位
     */
    Integer USER_TYPE_COMPANY = 4;

    String COMPANY = "company";
}

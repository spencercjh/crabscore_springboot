package com.shou.crabscore.common.constant;

/**
 * @author spencercjh
 */
public interface CommonConstant {

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
     * 管理员
     */
    Integer USER_TYPE_ADMIN = 1;

    /**
     * 评委
     */
    Integer USER_TYPE_JUDGE = 2;

    /**
     * 工作人员
     */
    Integer USER_TYPE_STAFF = 3;

    /**
     * 参选单位
     */
    Integer USER_TYPE_COMPANY = 4;
}

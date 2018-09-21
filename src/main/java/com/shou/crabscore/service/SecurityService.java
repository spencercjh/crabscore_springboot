package com.shou.crabscore.service;

import javax.servlet.http.HttpServletRequest;

/**
 * JWT校验接口
 *
 * @author spencercjh
 */
public interface SecurityService {
    /**
     * JWT校验
     *
     * @param jwt     包含用户信息的JWT串
     * @param roleId  当前Api允许的用户组
     * @param request 记录IP地址
     * @return 是否符合接口访问要求
     */
    boolean verify(String jwt, Integer roleId, HttpServletRequest request);
}
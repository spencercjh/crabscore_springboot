package com.shou.crabscore.service;

import com.shou.crabscore.common.vo.Result;

import javax.servlet.http.HttpServletRequest;

/**
 * JWT校验接口
 *
 * @author spencercjh
 */
public interface SecurityService {
    /**
     * JWT校验:将JWT串中的用户组信息与利用用户信息查询得到的用户组信息和当前访问的Api允许的用户组信息进行三重比对
     *
     * @param jwt              包含用户信息的JWT串
     * @param requestApiRoleId 当前访问的Api允许的用户组信息
     * @param request          记录IP地址
     * @return 是否符合接口访问要求
     */
    Result<Object> verify(String jwt, Integer requestApiRoleId, HttpServletRequest request);
}
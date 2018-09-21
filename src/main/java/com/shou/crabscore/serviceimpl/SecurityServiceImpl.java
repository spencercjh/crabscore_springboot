package com.shou.crabscore.serviceimpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shou.crabscore.common.constant.CommonConstant;
import com.shou.crabscore.common.util.ResultUtil;
import com.shou.crabscore.common.vo.Result;
import com.shou.crabscore.dao.UserMapper;
import com.shou.crabscore.entity.User;
import com.shou.crabscore.service.SecurityService;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static com.shou.crabscore.common.util.JwtUtil.*;

/**
 * @author spencercjh
 */
@Log4j2
@Service
public class SecurityServiceImpl implements SecurityService {
    private final UserMapper userMapper;

    @Autowired
    public SecurityServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public Result<Object> verify(String jwt, Integer roleId, HttpServletRequest request) {
        try {
            Claims claims = parseJWT(jwt);
            String subjectJson = claims.getSubject();
            JSONObject jsonObject = JSON.parseObject(subjectJson);
            String jwtUserName = jsonObject.getString("username");
            Integer jwtRoleId = jsonObject.getInteger("roleId");
            User searchResult = this.userMapper.selectByUserName(jwtUserName);
            if (searchResult == null) {
                String errorMessage = request.getRemoteAddr() + "试图越界访问（仿冒用户）" + jwtUserName + " IP:" + request.getRequestURI();
                log.error(errorMessage);
                return new ResultUtil<>().setData(false, errorMessage, 401, false);
            } else if (!jwtRoleId.equals(searchResult.getRoleId())) {
                String errorMessage = request.getRemoteAddr() + "试图越界访问（仿冒用户组）" + roleId + " IP:" + request.getRequestURI();
                log.error(errorMessage);
                return new ResultUtil<>().setData(false, errorMessage, 401, false);
            } else if (!claims.get(CommonConstant.MYKEY, String.class).equals(CommonConstant.MYKEY_VALUE)) {
                String errorMessage = request.getRemoteAddr() + "试图越界访问（MYKEY不正确）" + claims.get(CommonConstant.MYKEY, String.class) + " IP:" + request.getRequestURI();
                log.error(errorMessage);
                return new ResultUtil<>().setData(false, errorMessage, 401, false);
            } else if (!jwtRoleId.equals(roleId) && !roleId.equals(CommonConstant.USER_TYPE_COMMON)) {
                String errorMessage = request.getRemoteAddr() + "试图越界访问（数据正常）" + request.getRequestURI();
                log.error(errorMessage);
                return new ResultUtil<>().setData(false, errorMessage, 401, false);
            } else {
                return new ResultUtil<>().setData(true, "访问成功", 200, true);
            }
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            String errorMessage = request.getRemoteAddr() + "试图越界访问（JWT过期）" + request.getRequestURI();
            log.error(errorMessage);
            return new ResultUtil<>().setData(false, errorMessage, 401, false);
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            e.printStackTrace();
            String errorMessage = request.getRemoteAddr() + "试图越界访问（JWT本身有其他问题）" + request.getRequestURI();
            log.error(errorMessage);
            return new ResultUtil<>().setData(false, errorMessage, 401, false);
        }
    }
}

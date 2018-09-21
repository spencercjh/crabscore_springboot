package com.shou.crabscore.serviceimpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shou.crabscore.common.constant.CommonConstant;
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
    public boolean verify(String jwt, Integer roleId, HttpServletRequest request) {
        try {
            Claims claims = parseJWT(jwt);
            String subjectJson = claims.getSubject();
            JSONObject jsonObject = JSON.parseObject(subjectJson);
            String jwtUserName = jsonObject.getString("username");
            Integer jwtRoleId = jsonObject.getInteger("roleId");
            User searchResult = this.userMapper.selectByUserName(jwtUserName);
            if (searchResult == null) {
                log.error(request.getRemoteAddr() + "试图越界访问（仿冒用户）" + request.getRequestURI());
                return false;
            } else if (!jwtRoleId.equals(searchResult.getRoleId())) {
                log.error(request.getRemoteAddr() + "试图越界访问（仿冒用户组）" + request.getRequestURI());
                return false;
            } else if (!claims.get(CommonConstant.MYKEY, String.class).equals(CommonConstant.MYKEY_VALUE)) {
                log.error(request.getRemoteAddr() + "试图越界访问（MYKEY不正确）" + request.getRequestURI());
                return false;
            } else if (!jwtRoleId.equals(roleId) && !roleId.equals(CommonConstant.USER_TYPE_COMMON)) {
                log.error(request.getRemoteAddr() + "试图越界访问（数据正常）" + request.getRequestURI());
                return false;
            } else {
                return true;
            }
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            log.error(request.getRemoteAddr() + "试图越界访问（JWT过期）" + request.getRequestURI());
            return false;
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            e.printStackTrace();
            log.error(request.getRemoteAddr() + "试图越界访问（JWT本身有其他问题）" + request.getRequestURI());
            return false;
        }
    }
}

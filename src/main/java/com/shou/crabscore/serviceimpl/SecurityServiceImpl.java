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
    public boolean verify(String jwt, HttpServletRequest request) {
        try {
            Claims claims = parseJWT(jwt);
            String subjectJson = claims.getSubject();
            JSONObject jsonObject = JSON.parseObject(subjectJson);
            String username = jsonObject.getString("username");
            Integer roleId = jsonObject.getInteger("roleId");
            User searchResult = this.userMapper.selectByUserName(username);
            if (searchResult == null) {
                log.error(request.getRemoteAddr() + "试图越界访问" + request.getRequestURI());
                return false;
            } else if (!roleId.equals(searchResult.getRoleId())) {
                log.error(request.getRemoteAddr() + "试图越界访问" + request.getRequestURI());
                return false;
            } else if (claims.get(CommonConstant.MYKEY, String.class).equals(CommonConstant.MYKEY_VALUE)) {
                log.error(request.getRemoteAddr() + "试图越界访问" + request.getRequestURI());
                return false;
            } else {
                return true;
            }
        } catch (ExpiredJwtException | UnsupportedJwtException |
                MalformedJwtException | SignatureException | IllegalArgumentException e) {
            /*parseClaimsJws抛出的种种异常*/
            log.error(request.getRemoteAddr() + "试图越界访问" + request.getRequestURI());
            return false;
        }
    }
}

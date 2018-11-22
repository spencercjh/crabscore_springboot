package com.shou.crabscore.serviceimpl;

import com.github.pagehelper.PageHelper;
import com.shou.crabscore.dao.UserMapper;
import com.shou.crabscore.entity.User;
import com.shou.crabscore.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 用户接口实现
 *
 * @author spencercjh
 */
@Log4j2
@Service
@CacheConfig(cacheNames = "UserServiceCache")
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Cacheable(key = "#status")
    @Override
    public List<User> selectAllUserSelective(Integer status, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return userMapper.selectAllUserSelective(status);
    }

    @Cacheable
    @Override
    public List<User> selectAllUser(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return userMapper.selectAllUser();
    }

    @Cacheable(key = "username")
    @Override
    public User selectByUserName(String username) {
        return userMapper.selectByUserName(username);
    }

    @CacheEvict(key = "userId")
    @Override
    public int deleteByPrimaryKey(Integer userId) {
        return userMapper.deleteByPrimaryKey(userId);
    }

    @CachePut(key = "#record.userId")
    @Override
    public int insert(User record) {
        return userMapper.insert(record);
    }

    @CachePut(key = "#record.userId")
    @Override
    public int insertSelective(User record) {
        return userMapper.insertSelective(record);
    }

    @Cacheable(key = "#userId")
    @Override
    public User selectByPrimaryKey(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @CacheEvict(key = "#record.userId")
    @Override
    public int updateByPrimaryKeySelective(User record) {
        return userMapper.updateByPrimaryKeySelective(record);
    }

    @CacheEvict(key = "#record.userId")
    @Override
    public int updateByPrimaryKey(User record) {
        return userMapper.updateByPrimaryKey(record);
    }
}

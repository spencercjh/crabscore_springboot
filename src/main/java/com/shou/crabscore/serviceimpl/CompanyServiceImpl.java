package com.shou.crabscore.serviceimpl;

import com.github.pagehelper.PageHelper;
import com.shou.crabscore.dao.CompanyMapper;
import com.shou.crabscore.entity.Company;
import com.shou.crabscore.service.CompanyService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 参选单位接口实现
 *
 * @author spencercjh
 */
@Log4j2
@Service
@CacheConfig(cacheNames = "CompanyServiceCache")
public class CompanyServiceImpl implements CompanyService {

    private final CompanyMapper companyMapper;

    @Autowired
    public CompanyServiceImpl(CompanyMapper companyMapper) {
        this.companyMapper = companyMapper;
    }

    @Cacheable
    @Override
    public List<Company> selectAllCompany() {
        return companyMapper.selectAllCompany();
    }

    @Cacheable
    @Override
    public List<Company> selectAllCompany(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return companyMapper.selectAllCompany();
    }

    @CacheEvict(key = "#companyId")
    @Override
    public int deleteByPrimaryKey(Integer companyId) {
        return companyMapper.deleteByPrimaryKey(companyId);
    }

    @CachePut(key = "#record.companyId")
    @Override
    public int insert(Company record) {
        return companyMapper.insert(record);
    }

    @CachePut(key = "#record.companyId")
    @Override
    public int insertSelective(Company record) {
        return companyMapper.insertSelective(record);
    }

    @Cacheable(key = "#companyId")
    @Override
    public Company selectByPrimaryKey(Integer companyId) {
        return companyMapper.selectByPrimaryKey(companyId);
    }

    @CacheEvict(key = "#record.companyId")
    @Override
    public int updateByPrimaryKeySelective(Company record) {
        return companyMapper.updateByPrimaryKeySelective(record);
    }

    @CacheEvict(key = "#record.companyId")
    @Override
    public int updateByPrimaryKey(Company record) {
        return companyMapper.updateByPrimaryKey(record);
    }
}

package top.spencercjh.crabscore.refactory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.spencercjh.crabscore.refactory.mapper.CrabMapper;
import top.spencercjh.crabscore.refactory.model.Crab;
import top.spencercjh.crabscore.refactory.service.CrabService;

/**
 * @author MyBatisCodeHelperPro
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class CrabServiceImpl extends ServiceImpl<CrabMapper, Crab> implements CrabService {

}

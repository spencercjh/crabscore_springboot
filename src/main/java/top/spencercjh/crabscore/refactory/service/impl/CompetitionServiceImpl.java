package top.spencercjh.crabscore.refactory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.spencercjh.crabscore.refactory.mapper.CompetitionInfoMapper;
import top.spencercjh.crabscore.refactory.model.Competition;
import top.spencercjh.crabscore.refactory.service.CompetitionService;

/**
 * @author MyBatisCodeHelperPro
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class CompetitionServiceImpl extends ServiceImpl<CompetitionInfoMapper, Competition> implements CompetitionService {

}

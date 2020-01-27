package top.spencercjh.crabscore.refactory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.spencercjh.crabscore.refactory.mapper.ScoreQualityMapper;
import top.spencercjh.crabscore.refactory.model.ScoreQuality;
import top.spencercjh.crabscore.refactory.service.ScoreQualityService;

/**
 * @author MyBatisCodeHelperPro
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class ScoreQualityServiceImpl extends ServiceImpl<ScoreQualityMapper, ScoreQuality> implements ScoreQualityService {

}

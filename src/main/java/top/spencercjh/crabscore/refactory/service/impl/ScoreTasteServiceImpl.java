package top.spencercjh.crabscore.refactory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.spencercjh.crabscore.refactory.mapper.ScoreTasteMapper;
import top.spencercjh.crabscore.refactory.model.ScoreTaste;
import top.spencercjh.crabscore.refactory.service.ScoreTasteService;

/**
 * @author MyBatisCodeHelperPro
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class ScoreTasteServiceImpl extends ServiceImpl<ScoreTasteMapper, ScoreTaste> implements ScoreTasteService {

}

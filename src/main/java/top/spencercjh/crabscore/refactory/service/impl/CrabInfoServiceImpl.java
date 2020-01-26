package top.spencercjh.crabscore.refactory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.spencercjh.crabscore.refactory.mapper.CrabInfoMapper;
import top.spencercjh.crabscore.refactory.model.CrabInfo;
import top.spencercjh.crabscore.refactory.service.CrabInfoService;

@Service
public class CrabInfoServiceImpl extends ServiceImpl<CrabInfoMapper, CrabInfo> implements CrabInfoService {

}

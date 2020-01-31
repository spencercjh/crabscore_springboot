package top.spencercjh.crabscore.refactory.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.spencercjh.crabscore.refactory.model.Crab;
import top.spencercjh.crabscore.refactory.service.AsyncScoreService;
import top.spencercjh.crabscore.refactory.service.ScoreQualityService;
import top.spencercjh.crabscore.refactory.service.ScoreTasteService;

import java.io.Serializable;

/**
 * @author Spencer
 * @date 2020/1/31
 */
@Transactional(rollbackFor = Exception.class)
@Service
@Slf4j
public class AsyncScoreServiceImpl implements AsyncScoreService {
    private final ScoreQualityService scoreQualityService;
    private final ScoreTasteService scoreTasteService;

    public AsyncScoreServiceImpl(ScoreQualityService scoreQualityService, ScoreTasteService scoreTasteService) {
        this.scoreQualityService = scoreQualityService;
        this.scoreTasteService = scoreTasteService;
    }

    @Async("asyncThreadPool")
    @Override
    public void asyncSaveScoresByCrab(@NotNull Crab crab) {
        scoreQualityService.saveScoreQualityByCrab(crab);
        scoreTasteService.saveScoreTasteByCrab(crab);
    }

    @Async("asyncThreadPool")
    @Override
    public void asyncDeleteScoresByCrab(@NotNull Serializable crabId) {
        scoreQualityService.deleteScoreQualityByCrabId(crabId);
        scoreTasteService.deleteScoreTasteByCrabId(crabId);
    }
}

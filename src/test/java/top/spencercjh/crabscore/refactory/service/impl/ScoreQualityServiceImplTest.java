package top.spencercjh.crabscore.refactory.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import top.spencercjh.crabscore.refactory.model.ScoreQuality;
import top.spencercjh.crabscore.refactory.service.CrabService;
import top.spencercjh.crabscore.refactory.service.ScoreQualityService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Spencer
 * @date 2020/1/30
 */
@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class ScoreQualityServiceImplTest {

    @Autowired
    private CrabService crabService;
    @Autowired
    private ScoreQualityService scoreQualityService;

    //    @Test
    void mockData() {
        crabService.list().forEach(crab -> scoreQualityService.save(new ScoreQuality()
                .setCompetitionId(crab.getCompetitionId())
                .setCrabId(crab.getId())
                .setGroupId(crab.getGroupId())
                .setJudgesId(crab.getGroupId())));
    }

    @Test
    void pageQuery() throws Exception {
        // page search
        final long page = 2L;
        final long size = 15L;
        final IPage<ScoreQuality> pageQuery = scoreQualityService.pageQuery(null, null,
                null, null, null, null, page, size);
        assertEquals(page, pageQuery.getCurrent());
        assertEquals(size, pageQuery.getSize());
        // groupId search
        final int groupId = 99;
        IPage<ScoreQuality> searchResult = scoreQualityService.pageQuery(groupId, null,
                null, null, null, null, 1L, 100L);
        searchResult.getRecords().forEach(scoreQuality -> {
            assertNotNull(scoreQuality);
            log.debug(scoreQuality.toString());
            assertNotNull(scoreQuality.getGroupId());
            assertEquals(groupId, scoreQuality.getGroupId());
        });
        // competitionId search
        final int competitionId = 1;
        searchResult = scoreQualityService.pageQuery(null, competitionId,
                null, null, null, null, 1L, 100L);
        searchResult.getRecords().forEach(scoreQuality -> {
            assertNotNull(scoreQuality);
            log.debug(scoreQuality.toString());
            assertNotNull(scoreQuality.getCompetitionId());
            assertEquals(competitionId, scoreQuality.getCompetitionId());
        });
        // judgeId search
        final int judgeId = 48;
        searchResult = scoreQualityService.pageQuery(null, null,
                judgeId, null, null, null, 1L, 100L);
        searchResult.getRecords().forEach(scoreQuality -> {
            assertNotNull(scoreQuality);
            log.debug(scoreQuality.toString());
            assertNotNull(scoreQuality.getJudgesId());
            assertEquals(judgeId, scoreQuality.getJudgesId());
        });
        // crabId search
        final int crabId = 520;
        searchResult = scoreQualityService.pageQuery(null, null,
                null, crabId, null, null, 1L, 100L);
        searchResult.getRecords().forEach(scoreQuality -> {
            assertNotNull(scoreQuality);
            log.debug(scoreQuality.toString());
            assertNotNull(scoreQuality.getCrabId());
            assertEquals(crabId, scoreQuality.getCrabId());
        });
    }
}
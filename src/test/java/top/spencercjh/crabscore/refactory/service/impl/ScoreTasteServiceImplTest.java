package top.spencercjh.crabscore.refactory.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import top.spencercjh.crabscore.refactory.model.ScoreTaste;
import top.spencercjh.crabscore.refactory.service.CrabService;
import top.spencercjh.crabscore.refactory.service.ScoreTasteService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Spencer
 * @date 2020/1/30
 */
@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class ScoreTasteServiceImplTest {

    @Autowired
    private CrabService crabService;
    @Autowired
    private ScoreTasteService scoreTasteService;

    @Test
    void mockData() {
        crabService.list().forEach(crab -> scoreTasteService.save(new ScoreTaste()
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
        final IPage<ScoreTaste> pageQuery = scoreTasteService.pageQuery(null, null,
                null, null, null, null, page, size);
        assertEquals(page, pageQuery.getCurrent());
        assertEquals(size, pageQuery.getSize());
        // groupId search
        final int groupId = 99;
        IPage<ScoreTaste> searchResult = scoreTasteService.pageQuery(groupId, null,
                null, null, null, null, 1L, 100L);
        searchResult.getRecords().forEach(ScoreTaste -> {
            assertNotNull(ScoreTaste);
            log.debug(ScoreTaste.toString());
            assertNotNull(ScoreTaste.getGroupId());
            assertEquals(groupId, ScoreTaste.getGroupId());
        });
        // competitionId search
        final int competitionId = 1;
        searchResult = scoreTasteService.pageQuery(null, competitionId,
                null, null, null, null, 1L, 100L);
        searchResult.getRecords().forEach(ScoreTaste -> {
            assertNotNull(ScoreTaste);
            log.debug(ScoreTaste.toString());
            assertNotNull(ScoreTaste.getCompetitionId());
            assertEquals(competitionId, ScoreTaste.getCompetitionId());
        });
        // judgeId search
        final int judgeId = 48;
        searchResult = scoreTasteService.pageQuery(null, null,
                judgeId, null, null, null, 1L, 100L);
        searchResult.getRecords().forEach(ScoreTaste -> {
            assertNotNull(ScoreTaste);
            log.debug(ScoreTaste.toString());
            assertNotNull(ScoreTaste.getJudgesId());
            assertEquals(judgeId, ScoreTaste.getJudgesId());
        });
        // crabId search
        final int crabId = 520;
        searchResult = scoreTasteService.pageQuery(null, null,
                null, crabId, null, null, 1L, 100L);
        searchResult.getRecords().forEach(ScoreTaste -> {
            assertNotNull(ScoreTaste);
            log.debug(ScoreTaste.toString());
            assertNotNull(ScoreTaste.getCrabId());
            assertEquals(crabId, ScoreTaste.getCrabId());
        });
    }
}
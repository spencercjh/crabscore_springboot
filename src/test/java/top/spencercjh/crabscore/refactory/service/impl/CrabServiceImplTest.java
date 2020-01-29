package top.spencercjh.crabscore.refactory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import top.spencercjh.crabscore.refactory.model.Crab;
import top.spencercjh.crabscore.refactory.model.Group;
import top.spencercjh.crabscore.refactory.model.ScoreQuality;
import top.spencercjh.crabscore.refactory.model.ScoreTaste;
import top.spencercjh.crabscore.refactory.model.enums.SexEnum;
import top.spencercjh.crabscore.refactory.model.vo.CrabVo;
import top.spencercjh.crabscore.refactory.service.CrabService;
import top.spencercjh.crabscore.refactory.service.GroupService;
import top.spencercjh.crabscore.refactory.service.ScoreQualityService;
import top.spencercjh.crabscore.refactory.service.ScoreTasteService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Spencer
 * @date 2020/1/25
 */
@ActiveProfiles("local")
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CrabServiceImplTest {
    @Autowired
    private CrabService crabService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private ScoreTasteService scoreTasteService;
    @Autowired
    private ScoreQualityService scoreQualityService;

    //    @Test
    void mockData() {
        final List<Group> allGroup = groupService.list();
        for (Group group : allGroup) {
            IntStream.range(0, 10).forEach(i -> crabService.save(new Crab()
                    .setCrabLabel("Mock Data")
                    .setCrabSex(i % 2 == 0 ? SexEnum.FEMALE : SexEnum.MALE)
                    .setGroupId(group.getId())
                    .setCompetitionId(1)));
        }
    }

    @Transactional
    @Test
    void removeById() {
        final int toDelete = 210;
        assertTrue(crabService.removeById(toDelete));
        assertFalse(crabService.removeById(toDelete));
    }

    @Test
    void pageQuery() {
        // page
        final long page = 2;
        final long size = 10;
        final IPage<CrabVo> pageResult = crabService.pageQuery(null, null, null,
                null, null, page, size);
        assertEquals(size, pageResult.getRecords().size());
        assertEquals(page, pageResult.getCurrent());
        pageResult.getRecords().forEach(crab -> {
            assertNotNull(crab);
            log.debug(crab.toString());
            assertTrue(crab.getId() > size);
        });
        // search competitionId
        final int competitionId = 99;
        IPage<CrabVo> searchResult = crabService.pageQuery(competitionId, null, null,
                null, null, 1L, 100L);
        searchResult.getRecords().forEach(crab -> {
            assertNotNull(crab);
            log.debug(crab.toString());
            assertNotNull(crab.getCompetitionId());
            assertEquals(competitionId, crab.getCompetitionId());
        });
        // search groupId
        final int groupId = 10;
        searchResult = crabService.pageQuery(null, groupId, null,
                null, null, 1L, 100L);
        searchResult.getRecords().forEach(crab -> {
            assertNotNull(crab);
            log.debug(crab.toString());
            assertNotNull(crab.getGroupId());
            assertEquals(groupId, crab.getGroupId());
        });
        // search sex
        final SexEnum sex = SexEnum.MALE;
        searchResult = crabService.pageQuery(null, null, sex,
                null, null, 1L, 100L);
        searchResult.getRecords().forEach(crab -> {
            assertNotNull(crab);
            log.debug(crab.toString());
            assertNotNull(crab.getCrabSex());
            assertEquals(sex, crab.getCrabSex());
        });
    }

    @Test
    void pageQueryNewFeature() {
        final long size = 1000L;
        final long page = 1L;
        final IPage<CrabVo> crabIPage = crabService.pageQuery(null, null, null,
                null, null, page, size);
        assertNotNull(crabIPage);
        log.debug(crabIPage.toString());
        assertEquals(page, crabIPage.getCurrent());
        assertEquals(size, crabIPage.getSize());
        crabIPage.getRecords().forEach(crabVo -> {
            assertNotNull(crabVo);
            log.debug(crabVo.toString());
            assertNotNull(crabVo.getScoreQuality());
            assertNotNull(crabVo.getScoreTaste());
            assertEquals(crabVo.getId(), crabVo.getScoreTaste().getCrabId());
            assertEquals(crabVo.getId(), crabVo.getScoreQuality().getCrabId());
        });

    }

    @Transactional
    @Test
    void commitAndUpdate() throws IOException {
        final int toUpdateId = 209;
        final String updatedLabel = "Update Test";
        final Crab toUpdate = new Crab().setId(toUpdateId).setCrabLabel(updatedLabel);
        assertTrue(crabService.commitAndUpdate(toUpdate,
                new MockMultipartFile("image", "QQ图片20171115233745.jpg", null,
                        Files.readAllBytes(Paths.get("src", "test", "resources", "QQ图片20171115233745.jpg")))));
        final Crab actual = crabService.getById(toUpdateId);
        assertEquals(updatedLabel, toUpdate.getCrabLabel());
        assertEquals(updatedLabel, actual.getCrabLabel());
        assertTrue(actual.getAvatarUrl().contains("20171115233745"));
        // no file
        assertTrue(crabService.commitAndUpdate(toUpdate, null));
    }

    @Transactional
    @Test
    void commitAndInsert() throws IOException {
        final String insertLabel = "Insert Test";
        final Crab toInsert = new Crab().setGroupId(99).setCompetitionId(99).setCrabLabel(insertLabel);
        assertTrue(crabService.commitAndInsert(toInsert,
                new MockMultipartFile("image", "QQ图片20171115233745.jpg", null,
                        Files.readAllBytes(Paths.get("src", "test", "resources", "QQ图片20171115233745.jpg")))));
        final Crab actual = crabService.getOne(
                new QueryWrapper<Crab>().eq(Crab.COL_CRAB_LABEL, insertLabel));
        assertEquals(actual.getId(), toInsert.getId());
        assertEquals(insertLabel, toInsert.getCrabLabel());
        assertEquals(insertLabel, actual.getCrabLabel());
        assertTrue(actual.getAvatarUrl().contains("20171115233745"));
        // check two score
        assertNotNull(scoreQualityService.getOne(new QueryWrapper<ScoreQuality>().eq(ScoreQuality.COL_CRAB_ID, actual.getId())));
        assertNotNull(scoreTasteService.getOne(new QueryWrapper<ScoreTaste>().eq(ScoreTaste.COL_CRAB_ID, actual.getId())));
        // no file
        assertTrue(crabService.commitAndInsert(new Crab().setCompetitionId(99).setGroupId(99).setCrabLabel("NEW"), null));
    }

    @Transactional
    @Test
    void saveCrabAndScoreBatch() {
        int repeat = 10;
        final List<Crab> crabs = new ArrayList<>(repeat + 1);
        final int groupId = 1000;
        final Crab toInsert = new Crab().setGroupId(groupId).setCrabSex(SexEnum.MALE).setCompetitionId(groupId);
        for (int i = 0; i < repeat; i++) {
            crabs.add(toInsert);
        }
        assertTrue(crabService.saveCrabAndScoreBatch(crabs));
        // check crab
        assertEquals(repeat, crabService.list(
                new QueryWrapper<Crab>().eq(Crab.COL_GROUP_ID, groupId).eq(Crab.COL_COMPETITION_ID, groupId)).size());
        // check score
        assertEquals(repeat, scoreQualityService.list(
                new QueryWrapper<ScoreQuality>().eq(Crab.COL_GROUP_ID, groupId).eq(Crab.COL_COMPETITION_ID, groupId)).size());
        assertEquals(repeat, scoreTasteService.list(
                new QueryWrapper<ScoreTaste>().eq(Crab.COL_GROUP_ID, groupId).eq(Crab.COL_COMPETITION_ID, groupId)).size());
    }
}
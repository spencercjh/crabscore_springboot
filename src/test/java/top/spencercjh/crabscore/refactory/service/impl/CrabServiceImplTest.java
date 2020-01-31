package top.spencercjh.crabscore.refactory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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
import top.spencercjh.crabscore.refactory.service.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
    @Autowired
    private AsyncScoreService asyncScoreService;

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

    /**
     * The deletion of scores runs in multiple threads and don't be rolled back!
     */
    @Transactional
    @Test
    void removeById() {
        final int toDelete = 210;
        assertTrue(crabService.removeById(toDelete));
        // check two score
        assertNull(scoreQualityService.getOne(new QueryWrapper<ScoreQuality>().eq(ScoreQuality.COL_CRAB_ID, toDelete)));
        assertNull(scoreTasteService.getOne(new QueryWrapper<ScoreTaste>().eq(ScoreTaste.COL_CRAB_ID, toDelete)));
        // try again, return false
        assertFalse(crabService.removeById(toDelete));
    }

    @Transactional
    @Test
    void removeByIds() throws InterruptedException {
        final int repeat = 20;
        final int groupId = 2000;
        final List<Integer> crabIdList = mockSaveBatch(repeat, groupId).stream().map(Crab::getId).collect(Collectors.toList());
        assertTrue(crabService.removeByIds(crabIdList));
        Thread.sleep(5000);
        // check two score
        assertNull(scoreQualityService.getOne(new QueryWrapper<ScoreQuality>().eq(ScoreQuality.COL_GROUP_ID, groupId)));
        assertNull(scoreTasteService.getOne(new QueryWrapper<ScoreTaste>().eq(ScoreTaste.COL_GROUP_ID, groupId)));
        assertFalse(crabService.removeByIds(crabIdList));
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

    /**
     * The insertion of scores runs in multiple threads and don't be rolled back!
     */
    @Transactional
    @Test
    void save() throws InterruptedException {
        final int competitionId = 90010;
        final Crab entity = new Crab()
                .setCompetitionId(competitionId)
                .setGroupId(competitionId)
                .setCrabSex(SexEnum.MALE)
                .setCrabLabel("异步测试");
        assertTrue(crabService.save(entity));
        Thread.sleep(5000);
        // check two score
        assertNotNull(scoreQualityService.getOne(new QueryWrapper<ScoreQuality>().eq(ScoreQuality.COL_CRAB_ID, entity.getId())));
        assertNotNull(scoreTasteService.getOne(new QueryWrapper<ScoreTaste>().eq(ScoreTaste.COL_CRAB_ID, entity.getId())));
        // rollback manually
        asyncScoreService.asyncDeleteScoresByCrab(entity.getId());
        Thread.sleep(5000);
    }

    @Transactional
    @Test
    void commitAndInsert() throws IOException, InterruptedException {
        final String insertLabel = "commitAndInsert";
        final int groupId = 90020;
        final Crab toInsert = new Crab().setGroupId(groupId).setCompetitionId(groupId).setCrabLabel(insertLabel);
        assertTrue(crabService.commitAndInsert(toInsert,
                new MockMultipartFile("image", "QQ图片20171115233745.jpg", null,
                        Files.readAllBytes(Paths.get("src", "test", "resources", "QQ图片20171115233745.jpg")))));
        final Crab actual = crabService.getOne(
                new QueryWrapper<Crab>().eq(Crab.COL_CRAB_LABEL, insertLabel));
        assertEquals(actual.getId(), toInsert.getId());
        assertEquals(insertLabel, toInsert.getCrabLabel());
        assertEquals(insertLabel, actual.getCrabLabel());
        assertTrue(actual.getAvatarUrl().contains("20171115233745"));
        // rollback manually
        Thread.sleep(5000);
        asyncScoreService.asyncDeleteScoresByCrab(actual.getId());
        Thread.sleep(5000);
        // no file
        final Crab newCrab = new Crab().setCompetitionId(99).setGroupId(99).setCrabLabel("NEW");
        assertTrue(crabService.commitAndInsert(newCrab, null));
        // rollback manually
        Thread.sleep(5000);
        asyncScoreService.asyncDeleteScoresByCrab(newCrab.getId());
        Thread.sleep(5000);
    }

    @NotNull
    private List<Crab> mockSaveBatch(int repeat, int groupId) {
        final List<Crab> crabs = new ArrayList<>(repeat + 1);
        for (int i = 0; i < repeat; i++) {
            crabs.add(new Crab().setGroupId(groupId).setCrabSex(SexEnum.MALE).setCompetitionId(groupId));
        }
        assertTrue(crabService.saveCrabAndScoreBatch(crabs));
        // wait threads to insert
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignore) {
            ignore.printStackTrace();
        }
        return crabs;
    }

    @Transactional
    @Test
    void saveCrabAndScoreBatch() {
        final int repeat = 10;
        final int groupId = 1000;
        final List<Integer> crabIdList = mockSaveBatch(repeat, groupId).stream().map(Crab::getId).collect(Collectors.toList());
        // check crab
        assertEquals(repeat, crabService.list(
                new QueryWrapper<Crab>().eq(Crab.COL_GROUP_ID, groupId).eq(Crab.COL_COMPETITION_ID, groupId)).size());
        // check score
        assertEquals(repeat, scoreQualityService.list(
                new QueryWrapper<ScoreQuality>().eq(Crab.COL_GROUP_ID, groupId).eq(Crab.COL_COMPETITION_ID, groupId)).size());
        assertEquals(repeat, scoreTasteService.list(
                new QueryWrapper<ScoreTaste>().eq(Crab.COL_GROUP_ID, groupId).eq(Crab.COL_COMPETITION_ID, groupId)).size());
        // Manually delete the added Scores
        crabIdList.forEach(crabId -> asyncScoreService.asyncDeleteScoresByCrab(crabId));
    }
}
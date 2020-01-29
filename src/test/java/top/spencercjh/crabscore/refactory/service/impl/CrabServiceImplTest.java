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
import top.spencercjh.crabscore.refactory.model.enums.SexEnum;
import top.spencercjh.crabscore.refactory.service.CrabService;
import top.spencercjh.crabscore.refactory.service.GroupService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        final IPage<Crab> pageResult = crabService.pageQuery(null, null, null,
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
        IPage<Crab> searchResult = crabService.pageQuery(competitionId, null, null,
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
        assertEquals(insertLabel, toInsert.getCrabLabel());
        assertEquals(insertLabel, actual.getCrabLabel());
        assertTrue(actual.getAvatarUrl().contains("20171115233745"));
        // no file
        assertTrue(crabService.commitAndInsert(new Crab().setCompetitionId(99).setGroupId(99).setCrabLabel("NEW"), null));
    }
}
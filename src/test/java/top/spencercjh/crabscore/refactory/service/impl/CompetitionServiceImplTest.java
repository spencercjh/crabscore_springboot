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
import top.spencercjh.crabscore.refactory.model.Competition;
import top.spencercjh.crabscore.refactory.service.CompetitionService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Spencer
 * @date 2020/1/27
 */
@ActiveProfiles("local")
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CompetitionServiceImplTest {

    @Autowired
    private CompetitionService competitionService;

    @Transactional
    @Test
    void removeById() {
        final int toDelete = 10;
        assertTrue(competitionService.removeById(toDelete));
        assertFalse(competitionService.removeById(toDelete));
    }

    @Test
    void pageQuery() {
        // page
        final long page = 2;
        final long size = 10;
        final IPage<Competition> pageResult = competitionService.pageQuery(null, null, page, size);
        pageResult.getRecords().forEach(competition -> {
            assertNotNull(competition);
            log.debug(competition.toString());
            assertNotNull(competition.getCompetitionYear());
            assertTrue(competition.getId() > size);
        });
        // search status
        final byte status = 1;
        IPage<Competition> searchResult = competitionService.pageQuery(null, status, 1L, 100L);
        searchResult.getRecords().forEach(competition -> {
            assertNotNull(competition);
            log.debug(competition.toString());
            assertNotNull(competition.getCompetitionYear());
            assertEquals(status, competition.getStatus().byteValue());
        });
        // search year
        final String yearPrefix = "20";
        searchResult = competitionService.pageQuery(yearPrefix, null, 1L, 100L);
        searchResult.getRecords().forEach(competition -> {
            assertNotNull(competition);
            log.debug(competition.toString());
            assertNotNull(competition.getCompetitionYear());
            assertTrue(competition.getCompetitionYear().startsWith(yearPrefix));
        });
    }

    @Transactional
    @Test
    void commitAndUpdate() throws IOException {
        final int toUpdateId = 12;
        final String updatedYear = "Update Test";
        final Competition toUpdate = new Competition().setId(toUpdateId).setCompetitionYear(updatedYear);
        assertTrue(competitionService.commitAndUpdate(toUpdate,
                new MockMultipartFile("image", "QQ图片20171115233745.jpg", null,
                        Files.readAllBytes(Paths.get("src", "test", "resources", "QQ图片20171115233745.jpg")))));
        final Competition actual = competitionService.getById(toUpdateId);
        assertEquals(updatedYear, toUpdate.getCompetitionYear());
        assertEquals(updatedYear, actual.getCompetitionYear());
        assertTrue(actual.getAvatarUrl().contains("20171115233745"));
        // no file
        assertTrue(competitionService.commitAndUpdate(toUpdate, null));
    }

    @Transactional
    @Test
    void commitAndInsert() throws IOException {
        final String insertYear = "Insert Test";
        final Competition toInsert = new Competition().setCompetitionYear(insertYear);
        assertTrue(competitionService.commitAndInsert(toInsert,
                new MockMultipartFile("image", "QQ图片20171115233745.jpg", null,
                        Files.readAllBytes(Paths.get("src", "test", "resources", "QQ图片20171115233745.jpg")))));
        final Competition actual = competitionService.getOne(
                new QueryWrapper<Competition>().eq(Competition.COL_COMPETITION_YEAR, insertYear));
        assertEquals(insertYear, toInsert.getCompetitionYear());
        assertEquals(insertYear, actual.getCompetitionYear());
        assertTrue(actual.getAvatarUrl().contains("20171115233745"));
        // no file
        assertTrue(competitionService.commitAndInsert(new Competition().setCompetitionYear("NEW"), null));
    }
}
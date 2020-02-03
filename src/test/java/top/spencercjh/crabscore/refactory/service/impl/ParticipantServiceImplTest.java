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
import top.spencercjh.crabscore.refactory.MockDataManager;
import top.spencercjh.crabscore.refactory.model.Company;
import top.spencercjh.crabscore.refactory.model.Participant;
import top.spencercjh.crabscore.refactory.model.vo.ParticipantVo;
import top.spencercjh.crabscore.refactory.service.CompanyService;
import top.spencercjh.crabscore.refactory.service.ParticipantService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Spencer
 * @date 2020/2/1
 */
@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class ParticipantServiceImplTest {
    public static final int MOCK_DATA_COMPETITION = 1;
    public static final int COMPANY_ROLE_ID = 4;
    @Autowired
    private ParticipantService participantService;

    @Autowired
    private CompanyService companyService;

    //    @Test
    void modifyPassword() {
        participantService.list().forEach(user -> {
            user.setPassword("123456");
            participantService.updateById(user);
        });
    }

    //    @Test
    void mockData() {
        // mock company users data
        companyService.list(new QueryWrapper<Company>().eq(Company.COL_COMPETITION_ID, MOCK_DATA_COMPETITION))
                .forEach(company -> assertTrue(participantService.save(new Participant().setCompanyId(company.getId())
                        .setPassword("123456")
                        .setEmail(MockDataManager.getRandomEmail())
                        .setRoleId(COMPANY_ROLE_ID)
                        .setCompetitionId(MOCK_DATA_COMPETITION)
                        .setDisplayName(MockDataManager.getRandomDisplayName())
                        .setUsername(MockDataManager.getRandomUsername()))));
        // mock other user data
        IntStream.range(1, 4).mapToObj(roleId -> new Participant()
                .setRoleId(roleId)
                .setPassword("123456")
                .setEmail(MockDataManager.getRandomEmail())
                .setCompetitionId(MOCK_DATA_COMPETITION)
                .setDisplayName(MockDataManager.getRandomDisplayName())
                .setUsername(MockDataManager.getRandomUsername()))
                .forEach(user -> assertTrue(participantService.save(user)));
    }

    @Transactional
    @Test
    void removeById() {
        final int toDelete = 111;
        assertTrue(participantService.removeById(toDelete));
        assertFalse(participantService.removeById(toDelete));
    }

    @Test
    void pageQuery() {
        // page search
        final long page = 2;
        final long size = 15;
        final IPage<ParticipantVo> userVoIPage = participantService.pageQuery(null, null, null,
                null, null, page, size);
        assertEquals(page, userVoIPage.getCurrent());
        assertEquals(size, userVoIPage.getSize());
        assertEquals(participantService.count(), userVoIPage.getTotal());
        // companyId search
        final int companyId = 1;
        IPage<ParticipantVo> searchResult = participantService.pageQuery(companyId, null, null, null,
                null, 1L, 1000L);
        searchResult.getRecords().forEach(userVo -> {
            assertNotNull(userVo);
            log.debug(userVo.toString());
            assertEquals(companyId, userVo.getCompanyId());
            assertEquals(companyId, userVo.getCompany().getId());
        });
        // competitionId search
        final int competitionId = 1;
        searchResult = participantService.pageQuery(null, competitionId, null, null,
                null, 1L, 1000L);
        searchResult.getRecords().forEach(userVo -> {
            assertNotNull(userVo);
            log.debug(userVo.toString());
            assertEquals(competitionId, userVo.getCompetitionId());
        });
        // roleId search
        searchResult = participantService.pageQuery(null, null, COMPANY_ROLE_ID, null, null, 1L, 1000L);
        searchResult.getRecords().forEach(userVo -> {
            assertNotNull(userVo);
            log.debug(userVo.toString());
            assertEquals(COMPANY_ROLE_ID, userVo.getRoleId());
        });
        // username search
        final String username = "删除测试";
        searchResult = participantService.pageQuery(null, null, null, username, null, 1L, 1000L);
        searchResult.getRecords().forEach(userVo -> {
            assertNotNull(userVo);
            log.debug(userVo.toString());
            assertEquals(username, userVo.getUsername());
        });
        // display name search
        final String displayName = "删除测试显示名";
        searchResult = participantService.pageQuery(null, null, null, null, displayName, 1L, 1000L);
        searchResult.getRecords().forEach(userVo -> {
            assertNotNull(userVo);
            log.debug(userVo.toString());
            assertEquals(displayName, userVo.getDisplayName());
        });
    }

    @Transactional
    @Test
    void commitAndUpdate() throws IOException {
        final int toUpdateId = 112;
        final int toUpdateCompetitionId = 0;
        final Participant toUpdate = new Participant().setId(toUpdateId)
                .setCompetitionId(toUpdateCompetitionId)
                .setCompanyId(toUpdateCompetitionId)
                .setUsername("commitAndUpdateUser");
        assertTrue(participantService.commitAndUpdate(toUpdate,
                new MockMultipartFile("image", "QQ图片20171115233745.jpg", null,
                        Files.readAllBytes(Paths.get("src", "test", "resources", "QQ图片20171115233745.jpg")))));
        final Participant actual = participantService.getById(toUpdateId);
        assertEquals(toUpdateCompetitionId, toUpdate.getCompanyId());
        assertEquals(toUpdateCompetitionId, actual.getCompanyId());
        assertTrue(actual.getAvatarUrl().contains("20171115233745"));
        // no file
        assertTrue(participantService.commitAndUpdate(toUpdate, null));
    }

    @Transactional
    @Test
    void commitAndInsert() throws IOException {
        final int toInsertCompanyId = 0;
        final Participant toInsert = new Participant()
                .setCompanyId(toInsertCompanyId)
                .setCompetitionId(toInsertCompanyId)
                .setRoleId(0)
                .setUsername("CommitAndInsertUser")
                .setPassword("CommitAndInsertUser");
        assertTrue(participantService.commitAndInsert(toInsert,
                new MockMultipartFile("image", "QQ图片20171115233745.jpg", null,
                        Files.readAllBytes(Paths.get("src", "test", "resources", "QQ图片20171115233745.jpg")))));
        final Participant actual = participantService.getById(toInsert.getId());
        assertEquals(toInsertCompanyId, toInsert.getCompanyId());
        assertEquals(toInsertCompanyId, actual.getCompanyId());
        assertTrue(actual.getAvatarUrl().contains("20171115233745"));
        // no file
        assertTrue(participantService.commitAndInsert(new Participant()
                .setCompetitionId(999)
                .setCompanyId(999)
                .setRoleId(0)
                .setUsername("CommitAndInsertUserNoFile")
                .setPassword("CommitAndInsertUserNoFile"), null));
    }
}
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
import top.spencercjh.crabscore.refactory.model.Company;
import top.spencercjh.crabscore.refactory.model.Group;
import top.spencercjh.crabscore.refactory.service.CompanyService;
import top.spencercjh.crabscore.refactory.service.GroupService;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Spencer
 * @date 2020/1/28
 */
@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class GroupServiceImplTest {
    @Autowired
    private GroupService groupService;
    @Autowired
    private CompanyService companyService;


    //    @Test
    void mockData() {
        final List<Company> allCompany = companyService.list();
        allCompany.forEach(company -> groupService.save(new Group().setCompanyId(company.getId()).setCompetitionId(1)));
    }

    @Transactional
    @Test
    void removeById() {
        final int toDelete = 55;
        assertTrue(groupService.removeById(toDelete));
        assertFalse(groupService.removeById(toDelete));
    }

    @Test
    void pageQuery() {
        // page search
        final long page = 2L;
        final long size = 15L;
        final IPage<Group> groupIPage = groupService.pageQuery(null, null, page, size);
        assertEquals(size, groupIPage.getRecords().size());
        assertEquals(page, groupIPage.getCurrent());
        groupIPage.getRecords().forEach(group -> {
            assertNotNull(group);
            log.debug(group.toString());
            assertTrue(group.getId() > size);
        });
        // companyId search
        final int companyId = 1;
        IPage<Group> searchResult = groupService.pageQuery(companyId, null, 1L, 100L);
        searchResult.getRecords().forEach(group -> {
            assertNotNull(group);
            log.debug(group.toString());
            assertNotNull(group.getCompanyId());
            assertEquals(companyId, group.getCompanyId());
        });
        // competitionId search
        final int competitionId = 99;
        searchResult = groupService.pageQuery(null, competitionId, 1L, 100L);
        searchResult.getRecords().forEach(group -> {
            assertNotNull(group);
            log.debug(group.toString());
            assertNotNull(group.getCompetitionId());
            assertEquals(competitionId, group.getCompetitionId());
        });
    }

    @Transactional
    @Test
    void commitAndUpdate() throws Exception {
        final int toUpdateId = 56;
        final int toUpdateCompanyId = 100;
        final Group toUpdate = new Group().setId(toUpdateId).setCompanyId(toUpdateCompanyId).setCompetitionId(toUpdateCompanyId);
        assertTrue(groupService.commitAndUpdate(toUpdate,
                new MockMultipartFile("image", "QQ图片20171115233745.jpg", null,
                        Files.readAllBytes(Paths.get("src", "test", "resources", "QQ图片20171115233745.jpg")))));
        final Group actual = groupService.getById(toUpdateId);
        assertEquals(toUpdateCompanyId, toUpdate.getCompanyId());
        assertEquals(toUpdateCompanyId, actual.getCompanyId());
        assertTrue(actual.getAvatarUrl().contains("20171115233745"));
        // no file
        assertTrue(groupService.commitAndUpdate(toUpdate, null));
    }

    @Transactional
    @Test
    void commitAndInsert() throws Exception {
        final int toInsertCompanyId = 101;
        final Group toInsert = new Group().setCompanyId(toInsertCompanyId).setCompetitionId(toInsertCompanyId);
        assertTrue(groupService.commitAndInsert(toInsert,
                new MockMultipartFile("image", "QQ图片20171115233745.jpg", null,
                        Files.readAllBytes(Paths.get("src", "test", "resources", "QQ图片20171115233745.jpg")))));
        final Group actual = groupService.getOne(
                new QueryWrapper<Group>().eq(Group.COL_COMPANY_ID, toInsertCompanyId));
        assertEquals(toInsertCompanyId, toInsert.getCompanyId());
        assertEquals(toInsertCompanyId, actual.getCompanyId());
        assertTrue(actual.getAvatarUrl().contains("20171115233745"));
        // no file
        assertTrue(groupService.commitAndInsert(new Group().setCompetitionId(999).setCompanyId(999), null));
    }

    @Test
    void getCurrent() {
        String username = "update";
        assertNotNull(groupService.getCurrent(username));
        username = "delete";
        assertNull(groupService.getCurrent(username));
    }
}
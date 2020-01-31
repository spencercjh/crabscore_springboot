package top.spencercjh.crabscore.refactory.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import top.spencercjh.crabscore.refactory.model.Crab;
import top.spencercjh.crabscore.refactory.model.ScoreQuality;
import top.spencercjh.crabscore.refactory.model.ScoreTaste;
import top.spencercjh.crabscore.refactory.model.enums.SexEnum;
import top.spencercjh.crabscore.refactory.service.CrabService;
import top.spencercjh.crabscore.refactory.service.ScoreQualityService;
import top.spencercjh.crabscore.refactory.service.ScoreTasteService;
import top.spencercjh.crabscore.refactory.util.JacksonUtil;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Spencer
 * @date 2020/1/27
 */
@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CrabControllerTest {
    public static final String URL_TEMPLATE = "/crabs";
    public static final int GROUP_ID = 90000;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CrabService crabService;
    @Autowired
    private ScoreQualityService scoreQualityService;
    @Autowired
    private ScoreTasteService scoreTasteService;

    @AfterEach
    void rollback() {
        // Rollback manually
        scoreQualityService.remove(new QueryWrapper<ScoreQuality>().eq(ScoreQuality.COL_GROUP_ID, GROUP_ID));
        scoreTasteService.remove(new QueryWrapper<ScoreTaste>().eq(ScoreTaste.COL_GROUP_ID, GROUP_ID));
        crabService.remove(new QueryWrapper<Crab>().eq(Crab.COL_GROUP_ID, GROUP_ID));
    }

    @Transactional
    @Test
    void successBatchInsert() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_TEMPLATE)
                .param("repeat", "10")
                .param("crab", JacksonUtil.serialize(new Crab()
                        .setCrabLabel("successBatchInsert")
                        .setGroupId(GROUP_ID)
                        .setCompetitionId(GROUP_ID)
                        .setCrabSex(SexEnum.MALE))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").isArray())
                .andDo(print());
    }

    @Transactional
    @Test
    void successSingleInsert() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_TEMPLATE)
                .file(new MockMultipartFile("image", "QQ图片20171115233745.jpg", null,
                        Files.readAllBytes(Paths.get("src", "test", "resources", "QQ图片20171115233745.jpg"))))
                .param("crab", JacksonUtil.serialize(new Crab()
                        .setCrabLabel("successSingleInsert")
                        .setGroupId(GROUP_ID)
                        .setCompetitionId(GROUP_ID)
                        .setCrabSex(SexEnum.MALE))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").isMap())
                .andDo(print())
                .andReturn();
    }

    @Transactional
    @Test
    void badRequestInsert() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_TEMPLATE)
                .param("crab", "{}"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Transactional
    @Test
    void successDeleteById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_TEMPLATE + "/210"))
                .andExpect(status().isOk())
                .andDo(print());
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_TEMPLATE + "/210"))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }

    @Transactional
    @Test
    void successDeleteByIds() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_TEMPLATE)
                .param("ids", "210", "211"))
                .andExpect(status().isOk())
                .andDo(print());
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_TEMPLATE)
                .param("ids", "210", "211"))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }

    @Transactional
    @Test
    void successDeleteByGroupId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_TEMPLATE)
                .param("groupId", "0"))
                .andExpect(status().isOk())
                .andDo(print());
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_TEMPLATE)
                .param("groupId", "0"))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }

    @Transactional
    @Test
    void successDeleteByGroupIdAndSex() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_TEMPLATE)
                .param("groupId", "0")
                .param("sex", SexEnum.MALE.getDescription()))
                .andExpect(status().isOk())
                .andDo(print());
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_TEMPLATE)
                .param("groupId", "0")
                .param("sex", SexEnum.MALE.getDescription()))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }

    @Transactional
    @Test
    void badRequestDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_TEMPLATE))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Transactional
    @Test
    void successUpdate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_TEMPLATE)
                .file(new MockMultipartFile("image", "QQ图片20171115233745.jpg", null,
                        Files.readAllBytes(Paths.get("src", "test", "resources", "QQ图片20171115233745.jpg"))))
                .param("crab", JacksonUtil.serialize(new Crab()
                        .setId(209)
                        .setGroupId(99)
                        .setCompetitionId(99)
                        .setCrabSex(SexEnum.MALE)
                        .setCrabLabel("TEST")))
                .with((MockHttpServletRequest request) -> {
                    request.setMethod(HttpMethod.PUT.name());
                    return request;
                }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isMap())
                .andDo(print());
    }

    @Transactional
    @Test
    void failedUpdate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_TEMPLATE)
                .param("crab", JacksonUtil.serialize(new Crab()
                        // a non-existent id
                        .setId(1)
                        .setGroupId(99)
                        .setCompetitionId(99)
                        .setCrabSex(SexEnum.MALE)
                        .setCrabLabel("TEST")))
                .with((MockHttpServletRequest request) -> {
                    request.setMethod(HttpMethod.PUT.name());
                    return request;
                }))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }

    @Transactional
    @Test
    void badRequestUpdate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_TEMPLATE)
                .param("crab", JacksonUtil.serialize(new Crab()
                        .setCrabSex(SexEnum.MALE)
                        .setCrabLabel("TEST")))
                .with((MockHttpServletRequest request) -> {
                    request.setMethod(HttpMethod.PUT.name());
                    return request;
                }))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    /**
     * For detailed search parameters tests, please see the service layer test;
     *
     * @throws Exception ignore
     */
    @Test
    void listSearch() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isMap())
                .andDo(print());
    }

    /**
     * For detailed search parameters tests, please see the service layer test;
     *
     * @throws Exception ignore
     */
    @Test
    void notFoundListSearch() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE)
                .param("groupId", "1000"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void getDetail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "/209"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isMap())
                .andDo(print());
    }

    @Test
    void notFoundGetDetail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "/1"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}
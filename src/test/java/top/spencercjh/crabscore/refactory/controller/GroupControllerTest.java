package top.spencercjh.crabscore.refactory.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import top.spencercjh.crabscore.refactory.model.Group;
import top.spencercjh.crabscore.refactory.util.JacksonUtil;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Spencer
 * @date 2020/1/29
 */
@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class GroupControllerTest {
    public static final String URL_TEMPLATE = "/api/groups";
    @Autowired
    private MockMvc mockMvc;
    @Value("${testOnly.token.admin}")
    private String adminToken;

    @Value("${testOnly.token.company}")
    private String companyToken;

    @Test
    void getCurrent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "/current")
                .header("Authorization", companyToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isMap())
                .andDo(print());
    }

    @Test
    void successGetDetail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "/1")
                .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isMap())
                .andDo(print());
    }

    @Test
    void notFoundGetDetail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "/999")
                .header("Authorization", adminToken))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void listSearch() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE)
                .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isMap())
                .andDo(print());
    }

    @Test
    void notFoundListSearch() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE)
                .param("companyId", "1000")
                .header("Authorization", adminToken))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Transactional
    @Test
    void successUpdate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_TEMPLATE)
                .file(new MockMultipartFile("image", "QQ图片20171115233745.jpg", null,
                        Files.readAllBytes(Paths.get("src", "test", "resources", "QQ图片20171115233745.jpg"))))
                .param("group", JacksonUtil.serialize(new Group()
                        .setId(56)
                        .setCompanyId(99)
                        .setCompetitionId(99)))
                .with((MockHttpServletRequest request) -> {
                    request.setMethod(HttpMethod.PUT.name());
                    return request;
                })
                .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isMap())
                .andDo(print());
    }

    @Transactional
    @Test
    void failedUpdate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_TEMPLATE)
                .param("group", JacksonUtil.serialize(new Group()
                        // non existing id
                        .setId(999)
                        .setCompanyId(99)
                        .setCompetitionId(99)))
                .with((MockHttpServletRequest request) -> {
                    request.setMethod(HttpMethod.PUT.name());
                    return request;
                })
                .header("Authorization", adminToken))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }

    @Transactional
    @Test
    void badRequestUpdate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_TEMPLATE)
                .param("group", JacksonUtil.serialize(new Group()
                        .setCompanyId(99)
                        .setCompetitionId(99)))
                .with((MockHttpServletRequest request) -> {
                    request.setMethod(HttpMethod.PUT.name());
                    return request;
                })
                .header("Authorization", adminToken))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Transactional
    @Test
    void successDeleteGroup() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_TEMPLATE + "/55")
                .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Transactional
    @Test
    void failedDeleteGroup() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_TEMPLATE + "/999")
                .header("Authorization", adminToken))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }

    @Transactional
    @Test
    void successInsertGroup() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_TEMPLATE)
                .file(new MockMultipartFile("image", "QQ图片20171115233745.jpg", null,
                        Files.readAllBytes(Paths.get("src", "test", "resources", "QQ图片20171115233745.jpg"))))
                .param("group", JacksonUtil.serialize(new Group()
                        .setCompanyId(99)
                        .setCompetitionId(99)))
                .header("Authorization", adminToken))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").isMap())
                .andDo(print());
    }

    @Transactional
    @Test
    void badRequestInsertGroup() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_TEMPLATE)
                .file(new MockMultipartFile("image", "QQ图片20171115233745.jpg", null,
                        Files.readAllBytes(Paths.get("src", "test", "resources", "QQ图片20171115233745.jpg"))))
                .param("group", JacksonUtil.serialize(new Group()
                        .setCompetitionId(99)))
                .header("Authorization", adminToken))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
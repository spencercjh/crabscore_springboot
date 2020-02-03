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
import top.spencercjh.crabscore.refactory.model.Participant;
import top.spencercjh.crabscore.refactory.util.JacksonUtil;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Spencer
 * @date 2020/2/1
 */
@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ParticipantControllerTest {
    public static final String URL_TEMPLATE = "/api/users";
    @Autowired
    private MockMvc mockMvc;

    @Value("${testOnly.token.admin}")
    private String adminToken;

    @Test
    void successGetCurrent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "/current")
                .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isMap())
                .andDo(print());
    }

    @Transactional
    @Test
    void successUpdateCurrent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_TEMPLATE + "/current")
                .file(new MockMultipartFile("image", "QQ图片20171115233745.jpg", null,
                        Files.readAllBytes(Paths.get("src", "test", "resources", "QQ图片20171115233745.jpg"))))
                .param("user", JacksonUtil.serialize(new Participant()
                        .setCompetitionId(0)
                        .setCompanyId(0)
                        .setUsername("MOCK_ADMIN")
                        .setDisplayName("MOCK_ADMIN")))
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
    void forbiddenUpdateCurrent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_TEMPLATE + "/current")
                .file(new MockMultipartFile("image", "QQ图片20171115233745.jpg", null,
                        Files.readAllBytes(Paths.get("src", "test", "resources", "QQ图片20171115233745.jpg"))))
                .param("user", JacksonUtil.serialize(new Participant()
                        .setCompetitionId(0)
                        .setCompanyId(0)
                        .setUsername("MOCK_ADMIN")
                        .setDisplayName("MOCK_ADMIN")))
                .with((MockHttpServletRequest request) -> {
                    request.setMethod(HttpMethod.PUT.name());
                    return request;
                }))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Transactional
    @Test
    void badRequestUpdateCurrent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_TEMPLATE + "/current")
                .file(new MockMultipartFile("image", "QQ图片20171115233745.jpg", null,
                        Files.readAllBytes(Paths.get("src", "test", "resources", "QQ图片20171115233745.jpg"))))
                .param("user", "{")
                .with((MockHttpServletRequest request) -> {
                    request.setMethod(HttpMethod.PUT.name());
                    return request;
                })
                .header("Authorization", adminToken))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void successGetDetail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "/56")
                .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isMap())
                .andDo(print());
    }

    @Test
    void notFoundGetDetail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "/10000")
                .header("Authorization", adminToken))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void successListSearch() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE)
                .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isMap())
                .andDo(print());
    }

    @Test
    void notFoundListSearch() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE)
                .param("companyId", "9999")
                .header("Authorization", adminToken))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Transactional
    @Test
    void successUpdateUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_TEMPLATE)
                .file(new MockMultipartFile("image", "QQ图片20171115233745.jpg", null,
                        Files.readAllBytes(Paths.get("src", "test", "resources", "QQ图片20171115233745.jpg"))))
                .param("user", JacksonUtil.serialize(new Participant()
                        .setId(112)
                        .setCompetitionId(0)
                        .setCompanyId(0)
                        .setUsername("commitAndUpdateUser")))
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
    void badRequestUpdateUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_TEMPLATE)
                .file(new MockMultipartFile("image", "QQ图片20171115233745.jpg", null,
                        Files.readAllBytes(Paths.get("src", "test", "resources", "QQ图片20171115233745.jpg"))))
                .param("user", JacksonUtil.serialize(new Participant()
                        .setCompetitionId(0)
                        .setCompanyId(0)
                        .setUsername("commitAndUpdateUser")))
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
    void successDeleteUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_TEMPLATE + "/111")
                .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Transactional
    @Test
    void failedDeleteUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_TEMPLATE + "/10000")
                .header("Authorization", adminToken))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }

    @Transactional
    @Test
    void successInsertUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_TEMPLATE)
                .file(new MockMultipartFile("image", "QQ图片20171115233745.jpg", null,
                        Files.readAllBytes(Paths.get("src", "test", "resources", "QQ图片20171115233745.jpg"))))
                .param("user", JacksonUtil.serialize(new Participant()
                        .setCompanyId(0)
                        .setCompetitionId(0)
                        .setRoleId(0)
                        .setUsername("CommitAndInsertUser")
                        .setPassword("CommitAndInsertUser")))
                .header("Authorization", adminToken))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").isMap())
                .andDo(print());
    }

    @Transactional
    @Test
    void badRequestInsertUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_TEMPLATE)
                .file(new MockMultipartFile("image", "QQ图片20171115233745.jpg", null,
                        Files.readAllBytes(Paths.get("src", "test", "resources", "QQ图片20171115233745.jpg"))))
                .param("user", JacksonUtil.serialize(new Participant()
                        .setCompanyId(0)
                        .setCompetitionId(0)
                        .setUsername("CommitAndInsertUser")
                        .setPassword("CommitAndInsertUser")))
                .header("Authorization", adminToken))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
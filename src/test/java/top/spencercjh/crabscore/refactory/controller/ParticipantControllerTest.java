package top.spencercjh.crabscore.refactory.controller;

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
    public static final String URL_TEMPLATE = "/users";
    @Autowired
    private MockMvc mockMvc;

    @Test
    void successGetDetail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "/56"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isMap())
                .andDo(print());
    }

    @Test
    void notFoundGetDetail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "/10000"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void successListSearch() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isMap())
                .andDo(print());
    }

    @Test
    void notFoundListSearch() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE)
                .param("companyId", "9999"))
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
                }))
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
                }))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Transactional
    @Test
    void successDeleteUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_TEMPLATE + "/111"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Transactional
    @Test
    void failedDeleteUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_TEMPLATE + "/10000"))
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
                        .setPassword("CommitAndInsertUser"))))
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
                        .setPassword("CommitAndInsertUser"))))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
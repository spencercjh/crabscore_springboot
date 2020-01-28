package top.spencercjh.crabscore.refactory.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import top.spencercjh.crabscore.refactory.model.Competition;
import top.spencercjh.crabscore.refactory.util.JacksonUtil;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Spencer
 * @date 2020/1/28
 */
@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CompetitionControllerTest {

    public static final String URL_TEMPLATE = "/competitions";
    @Autowired
    private MockMvc mockMvc;

    @Test
    void successGetDetail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "/1"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void notFoundGetDetail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "/999"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void successListSearch() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void notFoundListSearch() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE)
                .param("year", "123456"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Transactional
    @Test
    void successUpdateCompetition() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_TEMPLATE)
                .file(new MockMultipartFile("image", "QQ图片20171115233745.jpg", null,
                        Files.readAllBytes(Paths.get("src", "test", "resources", "QQ图片20171115233745.jpg"))))
                .param("competition", JacksonUtil.serialize(new Competition()
                        .setId(12)
                        .setCompetitionYear("Update Test")))
                .with(request -> {
                    request.setMethod(HttpMethod.PUT.name());
                    return request;
                }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isMap())
                .andDo(print());
    }

    @Transactional
    @Test
    void badRequestUpdateCompetition() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_TEMPLATE)
                .param("competition", JacksonUtil.serialize(new Competition()
                        // no id
                        .setCompetitionYear("Update Test")))
                .with(request -> {
                    request.setMethod(HttpMethod.PUT.name());
                    return request;
                }))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Transactional
    @Test
    void failedUpdateCompetition() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_TEMPLATE)
                .param("competition", JacksonUtil.serialize(new Competition()
                        // non existing id
                        .setId(999)
                        .setCompetitionYear("Update Test")))
                .with(request -> {
                    request.setMethod(HttpMethod.PUT.name());
                    return request;
                }))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }

    @Transactional
    @Test
    void successDeleteCompetition() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_TEMPLATE + "/13"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void failedDeleteCompetition() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_TEMPLATE + "/999"))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }

    @Transactional
    @Test
    void successInsertCompetition() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_TEMPLATE)
                .file(new MockMultipartFile("image", "QQ图片20171115233745.jpg", null,
                        Files.readAllBytes(Paths.get("src", "test", "resources", "QQ图片20171115233745.jpg"))))
                .param("competition", JacksonUtil.serialize(new Competition()
                        .setCompetitionYear("Update Test"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").isMap())
                .andDo(print());
    }

    @Test
    void badRequestInsertCompetition() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_TEMPLATE)
                .param("competition", JacksonUtil.serialize(new Competition()
                        // blank year
                        .setCompetitionYear(""))))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
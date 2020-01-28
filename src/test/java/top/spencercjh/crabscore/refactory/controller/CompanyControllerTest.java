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
import top.spencercjh.crabscore.refactory.model.Company;
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
class CompanyControllerTest {
    public static final String URL_TEMPLATE = "/companies";
    @Autowired
    private MockMvc mockMvc;

    @Test
    void successGetDetail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isMap())
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
                .andExpect(jsonPath("$.data").isMap())
                .andDo(print());
    }

    @Test
    void notFoundListSearch() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE)
                .param("companyName", "123"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Transactional
    @Test
    void successUpdateCompanyInfo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_TEMPLATE)
                .file(new MockMultipartFile("image", "QQ图片20171115233745.jpg", null,
                        Files.readAllBytes(Paths.get("src", "test", "resources", "QQ图片20171115233745.jpg"))))
                .param("company", JacksonUtil.serialize(new Company().setId(54).setCompanyName("UPDATE TEST")))
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
    void badRequestUpdateCompanyInfo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_TEMPLATE)
                .param("company", JacksonUtil.serialize(new Company().setCompanyName("UPDATE TEST")))
                .with((MockHttpServletRequest request) -> {
                    request.setMethod(HttpMethod.PUT.name());
                    return request;
                }))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Transactional
    @Test
    void failedUpdateCompanyInfo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_TEMPLATE)
                .param("company", JacksonUtil.serialize(new Company().setId(999).setCompanyName("UPDATE TEST")))
                .with((MockHttpServletRequest request) -> {
                    request.setMethod(HttpMethod.PUT.name());
                    return request;
                }))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }

    @Transactional
    @Test
    void successDeleteCompanyInfo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_TEMPLATE + "/53"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Transactional
    @Test
    void failedDeleteCompanyInfo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_TEMPLATE + "/999"))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }

    @Transactional
    @Test
    void successInsertCompanyInfo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_TEMPLATE)
                .file(new MockMultipartFile("image", "QQ图片20171115233745.jpg", null,
                        Files.readAllBytes(Paths.get("src", "test", "resources", "QQ图片20171115233745.jpg"))))
                .param("company", JacksonUtil.serialize(new Company().setCompanyName("INSERT TEST"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").isMap())
                .andDo(print());
    }

    @Transactional
    @Test
    void badRequestInsertCompanyInfo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(URL_TEMPLATE)
                // blank name
                .param("company", JacksonUtil.serialize(new Company().setCompanyName(""))))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
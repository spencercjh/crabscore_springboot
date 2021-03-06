package top.spencercjh.crabscore.refactory.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import top.spencercjh.crabscore.refactory.model.ScoreTaste;
import top.spencercjh.crabscore.refactory.util.JacksonUtil;

import java.util.Objects;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Spencer
 * @date 2020/1/31
 */
@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ScoreTasteControllerTest {
    public static final String URL_TEMPLATE = "/api/tasteScores";
    @Autowired
    private MockMvc mockMvc;
    @Value("${testOnly.token.admin}")
    private String adminToken;

    @Test
    void successGetDetail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "/1")
                .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isMap())
                .andDo(print());
    }

    @Test
    void failedGetDetail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_TEMPLATE + "/9999")
                .header("Authorization", adminToken))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Transactional
    @Test
    void successUpdateScoreTaste() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URL_TEMPLATE)
                .content(Objects.requireNonNull(JacksonUtil.serialize(new ScoreTaste()
                        .setId(1)
                        .setGroupId(999)
                        .setCompetitionId(999)
                        .setCrabId(999)
                        .setJudgeUsername("TEST"))))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isMap())
                .andDo(print());
    }

    @Transactional
    @Test
    void badRequestUpdateScoreTaste() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(URL_TEMPLATE)
                .content(Objects.requireNonNull(JacksonUtil.serialize(new ScoreTaste()
                        .setGroupId(999)
                        .setCompetitionId(999)
                        .setCrabId(999)
                        .setJudgeUsername("TEST"))))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", adminToken))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
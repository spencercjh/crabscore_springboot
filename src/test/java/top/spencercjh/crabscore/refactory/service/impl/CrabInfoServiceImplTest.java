package top.spencercjh.crabscore.refactory.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.spencercjh.crabscore.refactory.model.CrabInfo;
import top.spencercjh.crabscore.refactory.service.CrabInfoService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Spencer
 * @date 2020/1/25
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CrabInfoServiceImplTest {
    @Autowired
    private CrabInfoService crabInfoService;

    @Test
    void list() {
        final List<CrabInfo> pageResult = crabInfoService.list();
        for (CrabInfo crabInfo : pageResult) {
            log.debug(crabInfo.toString());
            assertNotNull(crabInfo);
        }
    }
}
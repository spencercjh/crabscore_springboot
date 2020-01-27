package top.spencercjh.crabscore.refactory.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.spencercjh.crabscore.refactory.model.Crab;
import top.spencercjh.crabscore.refactory.service.CrabService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Spencer
 * @date 2020/1/25
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CrabServiceImplTest {
    @Autowired
    private CrabService crabService;

    @Test
    void list() {
        final List<Crab> pageResult = crabService.list();
        for (Crab crab : pageResult) {
            log.debug(crab.toString());
            assertNotNull(crab);
        }
    }
}
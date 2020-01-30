package top.spencercjh.crabscore.refactory.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author Spencer
 * @date 2020/1/31
 */
@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AsyncScoreServiceImplTest {

    /**
     * @see CrabServiceImplTest#save()
     * @see CrabServiceImplTest#saveCrabAndScoreBatch()
     */
    @Test
    void asyncSaveScoresByCrab() {
    }

    /**
     * @see CrabServiceImplTest#removeById()
     * @see CrabServiceImplTest#removeByIds()
     */
    @Test
    void asyncDeleteScoresByCrab() {
    }
}
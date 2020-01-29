package top.spencercjh.crabscore.refactory.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import top.spencercjh.crabscore.refactory.model.vo.CrabVo;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Spencer
 * @date 2020/1/30
 */
@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class CrabMapperTest {
    @Resource
    private CrabMapper crabMapper;

    @Test
    void selectCrabVo() {
        final long size = 1000L;
        final long page = 1L;
        final IPage<CrabVo> crabVoIPage = crabMapper.selectCrabVo(new Page<>(page, size), null,
                null, null, null, null);
        assertNotNull(crabVoIPage);
        log.debug(crabVoIPage.toString());
        assertEquals(page, crabVoIPage.getCurrent());
        assertEquals(size, crabVoIPage.getSize());
        crabVoIPage.getRecords().forEach(crabVo -> {
            assertNotNull(crabVo);
            log.debug(crabVo.toString());
            assertNotNull(crabVo.getScoreQuality());
            assertNotNull(crabVo.getScoreTaste());
            assertEquals(crabVo.getId(), crabVo.getScoreTaste().getCrabId());
            assertEquals(crabVo.getId(), crabVo.getScoreQuality().getCrabId());
        });
    }
}
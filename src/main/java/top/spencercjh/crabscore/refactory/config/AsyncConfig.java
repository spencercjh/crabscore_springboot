package top.spencercjh.crabscore.refactory.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

/**
 * @author Spencer
 * @date 2020/1/25
 */
@EnableAsync
@Component
public class AsyncConfig {
    @Value("${async.corePoolSize}")
    private Integer corePoolSize;
    @Value("${async.maxPoolSize}")
    private Integer maxPoolSize;
    @Value("${async.queueCapacity}")
    private Integer queueCapacity;

    @Bean(name = "asyncThreadPool")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }
}

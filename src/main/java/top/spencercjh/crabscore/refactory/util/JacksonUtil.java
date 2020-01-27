package top.spencercjh.crabscore.refactory.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Spencer
 */
public class JacksonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JacksonUtil.class);

    private JacksonUtil() {
    }

    /**
     * https://stackoverflow.com/questions/42947213/how-to-completely-deserialize-json-into-a-generic-list
     *
     * @param body
     * @param typeRef
     * @param <T>
     * @return
     */
    @Nullable
    public static <T> T deserialize(String body, TypeReference<T> typeRef) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(body, typeRef);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @Nullable
    public static <T> String serialize(T instance) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(instance);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            return null;
        }
    }
}

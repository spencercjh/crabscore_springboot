package top.spencercjh.crabscore.refactory.util;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Spencer
 * @date 2020/2/1
 */
class JacksonUtilTest {

    @Test
    void successTest() {
        // object
        final String bodyJson = "{\"username\":\"123\",\"id\":1,\"timestamp\":1572512169,\"map\":{\"123\":456,\"456\":789},\"list\":[\"1\",\"2\",\"3\"]}";
        TestObject testObject = new TestObject()
                .setId(1)
                .setList(Stream.of(1, 2, 3, 4, 5).map(Object::toString).collect(Collectors.toList()))
                .setMap(JacksonUtil.deserialize(bodyJson, new TypeReference<>() {
                }))
                .setTimestamp(new Timestamp(System.currentTimeMillis()))
                .setUsername("TEST");
        final String actual = JacksonUtil.serialize(testObject);
        assertNotNull(actual);
        System.out.println(actual);
        // list
        final List<TestObject> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(testObject);
        }
        final String actualList = JacksonUtil.serialize(list);
        assertNotNull(actualList);
        System.out.println(actualList);
    }

    @Test
    void failedTest() {
        assertEquals("null", JacksonUtil.serialize(null));
        assertNull(JacksonUtil.deserialize("{", new TypeReference<>() {
        }));
    }

    /**
     * Jackson只能反序列化静态内部类，不能持有外部类的引用
     */
    @Data
    @Accessors(chain = true)
    static class TestObject {
        private Timestamp timestamp;
        private Map<String, Object> map = new HashMap<>();
        private List<String> list = new ArrayList<>();
        private String username;
        private Integer id;

        public TestObject() {
        }
    }
}
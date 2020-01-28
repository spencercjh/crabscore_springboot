package top.spencercjh.crabscore.refactory.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 螃蟹性别
 *
 * @author Spencer
 * @date 2020/1/25
 */
@Getter
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public enum SexEnum {
    /**
     * 雌性 0
     */
    FEMALE(0, "FEMALE"),
    /**
     * 雄性 1
     */
    MALE(1, "MALE"),
    /**
     * 未知 -1
     */
    UNKNOWN(-1, "UNKNOWN");

    @EnumValue
    private final int code;
    @JsonValue
    private final String description;

    SexEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
package top.spencercjh.crabscore.refactory.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Spencer
 * @date 2020/1/25
 */
public enum SexEnum {
    /**
     * 雌性 0
     */
    FEMALE(0, "雌性"),
    /**
     * 雄性 1
     */
    MALE(1, "雄性");

    @EnumValue
    @JsonValue
    private final int code;
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
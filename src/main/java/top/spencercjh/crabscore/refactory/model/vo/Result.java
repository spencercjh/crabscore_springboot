package top.spencercjh.crabscore.refactory.model.vo;

/**
 * @author Spencer
 * @date 2020/1/25
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * API结果包装类
 * <p>
 * 这个类不能被继承，只能用建造者模式创建，构造函数全部关闭
 *
 * @author Spencer
 * @date 12/26/2019
 */
@ApiModel(value = "Result")
@Data
@Accessors(chain = true)
final public class Result<T> {
    @ApiModelProperty(value = "业务码", notes = "请务必与Http状态码区分开")
    private int code;
    @ApiModelProperty(value = "业务消息")
    private String message;
    @ApiModelProperty(value = "状态", notes = "success为True，Fail为False")
    private boolean status;
    @ApiModelProperty(value = "数据")
    private T data;
    @ApiModelProperty(value = "请求返回包装时的时间戳", example = "2020-01-03 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;

    private Result(int code, String message, boolean status, T data, Date timestamp) {
        this.code = code;
        this.message = message;
        this.status = status;
        this.data = data;
        this.timestamp = timestamp;
    }

    @Data
    final public static class ResultBuilder<T> {
        private int code;
        private String message;
        private boolean status;
        private T data;

        public Result<T> build() {
            return new Result<>(code, message, status, data, new Date());
        }
    }
}


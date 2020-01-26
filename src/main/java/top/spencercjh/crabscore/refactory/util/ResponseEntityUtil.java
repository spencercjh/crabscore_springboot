package top.spencercjh.crabscore.refactory.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import top.spencercjh.crabscore.refactory.model.vo.Result;

/**
 * 封装对{@link org.springframework.http.ResponseEntity}的使用，这个类全是工厂方法，不能被继承和实例化
 * <p>
 * REST规范参考：
 * <p>http://www.ruanyifeng.com/blog/2014/05/restful_api.html</p>
 * <p>http://www.ruanyifeng.com/blog/2018/10/restful-api-best-practices.html</p>
 * <p></p>
 * <b>HTTP STATUS CODE规约：（修改过）</b>
 * <p></p>
 * <b>200状态码表示操作成功，但是不同的方法可以返回更精确的状态码。</b>
 * <p></p>
 * GET: 200 OK
 * <p>
 * POST: 201 Created
 * <p>
 * PUT: 200 OK
 * <p>
 * PATCH: 200 OK
 * <p>
 * DELETE: 204 No Content
 * <p>
 * <p></p>
 * <b>4xx状态码表示客户端错误，主要有下面几种。</b>
 * <p></p>
 * 400 Bad Request：服务器不理解客户端的请求，未做任何处理。
 * <p>
 * 401 Unauthorized：用户未提供身份验证凭据，或者没有通过身份验证。
 * <p>
 * 403 Forbidden：用户通过了身份验证，但是不具有访问资源所需的权限。
 * <p>
 * 404 Not Found：所请求的资源不存在，或不可用。
 * <p>
 * 405 Method Not Allowed：用户已经通过身份验证，但是所用的 HTTP 方法不在他的权限之内。
 * <p>
 * 415 Unsupported Media Type：客户端要求的返回格式不支持。比如，API 只能返回 JSON 格式，但是客户端要求返回 XML 格式。
 * <p>
 * 422 Unprocessable Entity ：客户端上传的附件无法处理，导致请求失败。
 * <p>
 * 429 Too Many Requests：客户端的请求次数超过限额。
 * <p>
 * <p></p>
 * <b>5xx状态码表示服务端错误。一般来说，API 不会向用户透露服务器的详细信息，所以只要两个状态码就够了。</b>
 * <p></p>
 * <p>
 * 500 Internal Server Error：客户端请求有效，服务器处理时发生了意外。
 * <p>
 * 503 Service Unavailable：服务器无法处理请求，一般用于网站维护状态。
 *
 * @author Spencer
 * @date 12/26/2019
 */
final public class ResponseEntityUtil {
    /**
     * 传参失败，参数非法统一状态码 410
     */
    public static final int ILLEGAL_ARGUMENTS_FAIL_CODE = 410;
    /**
     * 内部预料之外的非受检错误 510
     */
    public static final int INTERNAL_EXCEPTION_FAIL_CODE = 510;
    /**
     * 业务状态码 0
     */
    public static final int SUCCESS_COMMON_CODE = 0;
    /**
     * 业务状态码 -1
     */
    public static final int FAIL_COMMON_CODE = -1;
    /**
     * HTTP状态码
     */
    private static final HttpStatus SUCCESS_COMMON_HTTP_CODE = HttpStatus.OK;
    private static final String SUCCESS_COMMON_MESSAGE = "Request succeeded";
    private static final boolean SUCCESS_STATUS = true;
    private static final String FAIL_COMMON_MESSAGE = "Request failed";
    /**
     * HTTP状态码
     */
    private static final HttpStatus FAIL_COMMON_HTTP_CODE = HttpStatus.INTERNAL_SERVER_ERROR;
    private static final boolean FAIL_STATUS = false;

    private ResponseEntityUtil() {
    }

    @NotNull
    @Contract(" -> new")
    public static <T> ResponseEntity<Result<T>> success() {
        return new ResponseEntity<>(new Result.ResultBuilder<T>()
                .setCode(HttpStatus.OK.value())
                .setStatus(SUCCESS_STATUS)
                .setMessage(SUCCESS_COMMON_MESSAGE)
                .build(), SUCCESS_COMMON_HTTP_CODE);
    }

    @NotNull
    @Contract("_ -> new")
    public static <T> ResponseEntity<Result<T>> success(HttpStatus status) {
        return new ResponseEntity<>(new Result.ResultBuilder<T>()
                .setCode(status.value())
                .setStatus(SUCCESS_STATUS)
                .setMessage(SUCCESS_COMMON_MESSAGE)
                .build(), status);
    }

    @NotNull
    @Contract("_, _ -> new")
    public static <T> ResponseEntity<Result<T>> result(Result<T> result, HttpStatus httpStatus) {
        return new ResponseEntity<>(result, httpStatus);
    }

    @NotNull
    @Contract("_ -> new")
    public static <T> ResponseEntity<Result<T>> success(T data) {
        return new ResponseEntity<>(new Result.ResultBuilder<T>()
                .setCode(SUCCESS_COMMON_CODE)
                .setData(data)
                .setStatus(SUCCESS_STATUS)
                .setMessage(SUCCESS_COMMON_MESSAGE)
                .build(), HttpStatus.OK);
    }

    @NotNull
    @Contract("_, _ -> new")
    public static <T> ResponseEntity<Result<T>> success(int code, HttpStatus httpStatus) {
        return new ResponseEntity<>(new Result.ResultBuilder<T>()
                .setCode(code)
                .setStatus(SUCCESS_STATUS)
                .setMessage(SUCCESS_COMMON_MESSAGE)
                .build(), httpStatus);
    }

    @NotNull
    @Contract("_, _, _ -> new")
    public static <T> ResponseEntity<Result<T>> success(int code, String message, HttpStatus httpStatus) {
        return new ResponseEntity<>(new Result.ResultBuilder<T>()
                .setCode(code)
                .setStatus(SUCCESS_STATUS)
                .setMessage(message)
                .build(), httpStatus);
    }

    @NotNull
    @Contract("_, _, _, _ -> new")
    public static <T> ResponseEntity<Result<T>> success(int code, String message, T data, HttpStatus httpStatus) {
        return new ResponseEntity<>(new Result.ResultBuilder<T>()
                .setCode(code)
                .setStatus(SUCCESS_STATUS)
                .setMessage(message)
                .setData(data)
                .build(), httpStatus);
    }

    @NotNull
    @Contract(" -> new")
    public static <T> ResponseEntity<Result<T>> fail() {
        return new ResponseEntity<>(new Result.ResultBuilder<T>()
                .setCode(FAIL_COMMON_CODE)
                .setStatus(FAIL_STATUS)
                .setMessage(FAIL_COMMON_MESSAGE)
                .build(), FAIL_COMMON_HTTP_CODE);
    }

    @NotNull
    @Contract("_ -> new")
    public static <T> ResponseEntity<Result<T>> fail(HttpStatus status) {
        return new ResponseEntity<>(new Result.ResultBuilder<T>()
                .setCode(status.value())
                .setStatus(FAIL_STATUS)
                .setMessage(FAIL_COMMON_MESSAGE)
                .build(), status);
    }

    @NotNull
    @Contract("_, _ -> new")
    public static <T> ResponseEntity<Result<T>> fail(T data, HttpStatus httpStatus) {
        return new ResponseEntity<>(new Result.ResultBuilder<T>()
                .setCode(FAIL_COMMON_CODE)
                .setData(data)
                .setStatus(FAIL_STATUS)
                .setMessage(FAIL_COMMON_MESSAGE)
                .build(), httpStatus);
    }

    @NotNull
    @Contract("_, _ -> new")
    public static <T> ResponseEntity<Result<T>> fail(int code, HttpStatus httpStatus) {
        return new ResponseEntity<>(new Result.ResultBuilder<T>()
                .setCode(code)
                .setStatus(FAIL_STATUS)
                .setMessage(FAIL_COMMON_MESSAGE)
                .build(), httpStatus);
    }

    public static <T> ResponseEntity<Result<T>> fail(int code, String message, HttpStatus httpStatus) {
        return new ResponseEntity<>(new Result.ResultBuilder<T>()
                .setCode(code)
                .setStatus(FAIL_STATUS)
                .setMessage(message)
                .build(), httpStatus);
    }

    @NotNull
    @Contract("_, _, _, _ -> new")
    public static <T> ResponseEntity<Result<T>> fail(int code, String message, T data, HttpStatus httpStatus) {
        return new ResponseEntity<>(new Result.ResultBuilder<T>()
                .setCode(code)
                .setStatus(FAIL_STATUS)
                .setMessage(message)
                .setData(data)
                .build(), httpStatus);
    }
}

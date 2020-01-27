package top.spencercjh.crabscore.refactory.config;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import top.spencercjh.crabscore.refactory.model.vo.Result;
import top.spencercjh.crabscore.refactory.util.ResponseEntityUtil;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义异常处理handler，以避免堆栈信息暴露给外部
 *
 * @author Spencer
 * @date 12 /27/2019
 */
@Slf4j
@ControllerAdvice
public class CommonCustomExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * JSR-303 @Valid 校验不通过
     *
     * @param ex      exception;
     * @param headers header;
     * @param status  status;
     * @param request request;
     * @return Response Entity;
     */
    @NotNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NotNull MethodArgumentNotValidException ex,
                                                                  @NotNull HttpHeaders headers,
                                                                  @NotNull HttpStatus status,
                                                                  @NotNull WebRequest request) {
        log.error("handler MethodArgumentNotValidException!:" + getRequestUrl((ServletWebRequest) request));
        log.error(ex.getMessage(), ex.getCause());
        return new ResponseEntity<>(
                new Result.ResultBuilder<>()
                        .setCode(ResponseEntityUtil.ILLEGAL_ARGUMENTS_FAIL_CODE)
                        .setMessage("参数非法或者字段缺失，请联系管理员")
                        .setData(setupResponseData(ex, request))
                        .setStatus(false)
                        .build(),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }

    @NotNull
    private String getRequestUrl(@NotNull ServletWebRequest request) {
        return "[" + request.getRequest().getMethod() + "] " + request.getRequest().getRequestURI();
    }

    /**
     * 服务端不可读参数错误
     *
     * @param ex      exception;
     * @param headers header;
     * @param status  status;
     * @param request request;
     * @return Response Entity;
     */
    @NotNull
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NotNull HttpMessageNotReadableException ex,
                                                                  @NotNull HttpHeaders headers,
                                                                  @NotNull HttpStatus status,
                                                                  @NotNull WebRequest request) {
        log.error("handler HttpMessageNotReadableException!:" + getRequestUrl((ServletWebRequest) request));
        log.error(ex.getMessage(), ex.getCause());
        return new ResponseEntity<>(
                new Result.ResultBuilder<>()
                        .setCode(ResponseEntityUtil.ILLEGAL_ARGUMENTS_FAIL_CODE)
                        .setMessage("参数没有抵达服务器，请联系管理员")
                        .setData(setupResponseData(ex, request))
                        .setStatus(false)
                        .build(),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * JSR-303 路径参数校验不通过
     *
     * @param ex      exception;
     * @param headers header;
     * @param status  status;
     * @param request request;
     * @return Response Entity;
     */
    @NotNull
    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(@NotNull MissingPathVariableException ex,
                                                               @NotNull HttpHeaders headers,
                                                               @NotNull HttpStatus status,
                                                               @NotNull WebRequest request) {
        log.error("handler MissingPathVariableException!:" + getRequestUrl((ServletWebRequest) request));
        log.error(ex.getMessage(), ex.getCause());
        return new ResponseEntity<>(
                new Result.ResultBuilder<>()
                        .setCode(ResponseEntityUtil.ILLEGAL_ARGUMENTS_FAIL_CODE)
                        .setMessage("参数非法或者字段缺失，请联系管理员")
                        .setData(setupResponseData(ex, request))
                        .setStatus(false)
                        .build(),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * 参数格式转换不正确
     *
     * @param ex      exception;
     * @param headers header;
     * @param status  status;
     * @param request request;
     * @return Response Entity;
     */
    @NotNull
    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(@NotNull ConversionNotSupportedException ex,
                                                                  @NotNull HttpHeaders headers,
                                                                  @NotNull HttpStatus status,
                                                                  @NotNull WebRequest request) {
        log.error("handler ConversionNotSupportedException!:" + getRequestUrl((ServletWebRequest) request));
        log.error(ex.getMessage(), ex.getCause());
        return new ResponseEntity<>(
                new Result.ResultBuilder<>()
                        .setCode(ResponseEntityUtil.ILLEGAL_ARGUMENTS_FAIL_CODE)
                        .setMessage("参数格式转换不正确，请联系管理员")
                        .setData(setupResponseData(ex, request))
                        .setStatus(false)
                        .build(),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * 405 method not supported
     *
     * @param ex      exception;
     * @param headers header;
     * @param status  status;
     * @param request request;
     * @return Response Entity;
     */
    @NotNull
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(@NotNull HttpRequestMethodNotSupportedException ex,
                                                                         @NotNull HttpHeaders headers,
                                                                         @NotNull HttpStatus status,
                                                                         @NotNull WebRequest request) {
        log.error("handler HttpRequestMethodNotSupportedException!:" + getRequestUrl((ServletWebRequest) request));
        log.error(ex.getMessage(), ex.getCause());
        return new ResponseEntity<>(
                new Result.ResultBuilder<>()
                        .setCode(ResponseEntityUtil.INTERNAL_EXCEPTION_FAIL_CODE)
                        .setMessage("内部错误，请联系管理员")
                        .setData(setupResponseData(ex, request))
                        .setStatus(false)
                        .build(),
                new HttpHeaders(),
                HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * 415 content type 不对
     *
     * @param ex      exception;
     * @param headers header
     * @param status  status;
     * @param request request;
     * @return Response Entity;
     */
    @NotNull
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(@NotNull HttpMediaTypeNotAcceptableException ex,
                                                                      @NotNull HttpHeaders headers,
                                                                      @NotNull HttpStatus status,
                                                                      @NotNull WebRequest request) {
        log.error("handler HttpMediaTypeNotAcceptableException!:" + getRequestUrl((ServletWebRequest) request));
        log.error(ex.getMessage(), ex.getCause());
        return new ResponseEntity<>(
                new Result.ResultBuilder<>()
                        .setCode(ResponseEntityUtil.INTERNAL_EXCEPTION_FAIL_CODE)
                        .setMessage("内部错误，请联系管理员")
                        .setData(setupResponseData(ex, request))
                        .setStatus(false)
                        .build(),
                new HttpHeaders(),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * JSR-303 异常处理
     *
     * @param ex      exception;
     * @param request request;
     * @return response entity;
     */
    @NotNull
    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<Result<Object>> handleValidationException(@NotNull Exception ex, @NotNull WebRequest request) {
        log.error("handler ValidationException!:" + getRequestUrl((ServletWebRequest) request));
        log.error(ex.getMessage(), ex.getCause());
        return new ResponseEntity<>(
                new Result.ResultBuilder<>()
                        .setCode(ResponseEntityUtil.ILLEGAL_ARGUMENTS_FAIL_CODE)
                        .setMessage("参数格式转换不正确，请联系管理员:" + ex.getMessage())
                        .setData(setupResponseData(ex, request))
                        .setStatus(false)
                        .build(),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * 其他各种预料之外的非受检异常处理
     *
     * @param ex      exception;
     * @param request request;
     * @return response entity;
     */
    @NotNull
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Result<Object>> handleUnexpectedUncheckedException(@NotNull Exception ex,
                                                                             @NotNull WebRequest request) {
        log.error("handler Exception!:" + getRequestUrl((ServletWebRequest) request));
        log.error(ex.getMessage(), ex.getCause());
        return new ResponseEntity<>(
                new Result.ResultBuilder<>()
                        .setCode(ResponseEntityUtil.INTERNAL_EXCEPTION_FAIL_CODE)
                        .setMessage("内部错误，请联系管理员")
                        .setData(setupResponseData(ex, request))
                        .setStatus(false)
                        .build(),
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @NotNull
    public Map<String, String> setupResponseData(@NotNull Exception ex, @NotNull WebRequest request) {
        Map<String, String> data = new HashMap<>(8);
        data.put("url", ((ServletWebRequest) request).getRequest().getRequestURI());
        data.put("method", ((ServletWebRequest) request).getRequest().getMethod());
        data.put("exception", ex.getMessage());
        return data;
    }
}

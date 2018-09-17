package com.shou.crabscore.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 我自己的异常
 *
 * @author spencercjh
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CrabScoreException extends RuntimeException {

    private String msg;

    public CrabScoreException(String msg) {
        super(msg);
        this.msg = msg;
    }
}

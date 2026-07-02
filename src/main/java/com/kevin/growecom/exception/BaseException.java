package com.kevin.growecom.exception;

import com.kevin.growecom.util.enums.ErrorCode;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException{
        private final ErrorCode errorCode;

        public BaseException(ErrorCode errorCode) {
            super(errorCode.getMessage());
            this.errorCode = errorCode;
        }
}

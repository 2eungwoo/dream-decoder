package sideprojects.dreamdecoder.application.web.dream.util.exception;

import sideprojects.dreamdecoder.global.shared.exception.CustomException;
import sideprojects.dreamdecoder.global.shared.response.ErrorCode;

public class NoCacheDataException extends CustomException {
    public NoCacheDataException(ErrorCode errorCode) {
        super(errorCode);
    }
}

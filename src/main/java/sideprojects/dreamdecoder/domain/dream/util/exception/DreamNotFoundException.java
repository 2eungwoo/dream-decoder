package sideprojects.dreamdecoder.domain.dream.util.exception;

import sideprojects.dreamdecoder.domain.dream.util.response.DreamErrorCode;
import sideprojects.dreamdecoder.global.shared.exception.CustomException;
import sideprojects.dreamdecoder.global.shared.response.ErrorCode;

public class DreamNotFoundException extends CustomException {
    public DreamNotFoundException() {
        super(DreamErrorCode.DREAM_NOT_FOUND);
    }
}

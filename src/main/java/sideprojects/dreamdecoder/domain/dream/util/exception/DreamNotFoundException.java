package sideprojects.dreamdecoder.domain.dream.util.exception;

import sideprojects.dreamdecoder.domain.dream.util.enums.DreamErrorCode;
import sideprojects.dreamdecoder.global.shared.exception.CustomException;

public class DreamNotFoundException extends CustomException {
    public DreamNotFoundException() {
        super(DreamErrorCode.DREAM_NOT_FOUND);
    }
}

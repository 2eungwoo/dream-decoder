package sideprojects.dreamdecoder.infrastructure.security.exception;

import sideprojects.dreamdecoder.global.shared.exception.CustomException;
import sideprojects.dreamdecoder.infrastructure.security.response.TokenErrorCode;

public class TokenException extends CustomException {
    public TokenException(TokenErrorCode errorCode) {
        super(errorCode);
    }
}

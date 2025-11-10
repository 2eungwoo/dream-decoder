package sideprojects.dreamdecoder.application.auth.cli.service.validator;

import sideprojects.dreamdecoder.global.shared.exception.CustomException;
import sideprojects.dreamdecoder.global.shared.response.ErrorCode;

public class PasswordMissMatchException extends CustomException {

    public PasswordMissMatchException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
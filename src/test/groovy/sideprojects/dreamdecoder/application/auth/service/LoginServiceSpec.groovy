package sideprojects.dreamdecoder.application.auth.service

import spock.lang.Specification
import sideprojects.dreamdecoder.domain.auth.persistence.User
import sideprojects.dreamdecoder.domain.auth.persistence.UserRepository
import sideprojects.dreamdecoder.application.auth.service.PasswordEncryptService
import sideprojects.dreamdecoder.application.auth.service.TokenGenerateService
import sideprojects.dreamdecoder.domain.auth.util.exception.AuthException
import sideprojects.dreamdecoder.domain.auth.util.response.AuthErrorCode

class LoginServiceSpec extends Specification {

    UserRepository userRepository = Mock()
    PasswordEncryptService passwordEncryptService = Mock()
    TokenGenerateService tokenGenerateService = Mock()

    LoginService loginService

    def setup() {
        loginService = new LoginService(userRepository, passwordEncryptService, tokenGenerateService)
    }

    def "사용자가 유효한 자격 증명으로 성공적으로 로그인해야 한다"() {
        given: "유효한 사용자 정보와 암호화된 비밀번호"
        String username = "testuser"
        String rawPassword = "password123"
        String encryptedPassword = "encryptedPassword123"
        String token = "jwt.token.here"
        User foundUser = User.builder().username(username).password(encryptedPassword).build()

        userRepository.findByUsername(username) >> Optional.of(foundUser)
        passwordEncryptService.matches(rawPassword, encryptedPassword) >> true
        tokenGenerateService.generateToken(foundUser) >> token

        when: "로그인 서비스를 호출하면"
        String resultToken = loginService.login(username, rawPassword)

        then: "토큰이 반환되어야 한다"
        resultToken == token

        1 * userRepository.findByUsername(username)
        1 * passwordEncryptService.matches(rawPassword, encryptedPassword)
        1 * tokenGenerateService.generateToken(foundUser)
    }

    def "존재하지 않는 사용자 이름으로 로그인 시 예외가 발생해야 한다"() {
        given: "존재하지 않는 사용자 이름"
        String username = "nonexistentuser"
        String rawPassword = "password123"

        userRepository.findByUsername(username) >> Optional.empty()

        when: "로그인 서비스를 호출하면"
        loginService.login(username, rawPassword)

        then: "AuthException이 발생하고 에러 코드가 USER_NOT_FOUND여야 한다"
        AuthException e = thrown()
        e.errorCode == AuthErrorCode.USER_NOT_FOUND

        1 * userRepository.findByUsername(username)
        0 * passwordEncryptService.matches(_, _)
        0 * tokenGenerateService.generateToken(_)
    }

    def "잘못된 비밀번호로 로그인 시 예외가 발생해야 한다"() {
        given: "잘못된 비밀번호"
        String username = "testuser"
        String rawPassword = "wrongpassword"
        String encryptedPassword = "encryptedPassword123"
        User foundUser = User.builder().username(username).password(encryptedPassword).build()

        userRepository.findByUsername(username) >> Optional.of(foundUser)
        passwordEncryptService.matches(rawPassword, encryptedPassword) >> false

        when: "로그인 서비스를 호출하면"
        loginService.login(username, rawPassword)

        then: "AuthException이 발생하고 에러 코드가 INVALID_PASSWORD여야 한다"
        AuthException e = thrown()
        e.errorCode == AuthErrorCode.INVALID_PASSWORD

        1 * userRepository.findByUsername(username)
        1 * passwordEncryptService.matches(rawPassword, encryptedPassword)
        0 * tokenGenerateService.generateToken(_)
    }
}

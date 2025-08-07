package sideprojects.dreamdecoder.application.auth.service

import spock.lang.Specification
import sideprojects.dreamdecoder.application.auth.usecase.TokenGenerateUseCase
import sideprojects.dreamdecoder.application.auth.validator.LoginValidator
import sideprojects.dreamdecoder.domain.auth.model.UserModel
import sideprojects.dreamdecoder.domain.auth.persistence.User
import sideprojects.dreamdecoder.presentation.auth.dto.AuthResponse
import sideprojects.dreamdecoder.presentation.auth.dto.LoginRequest

class LoginServiceSpec extends Specification {

    LoginValidator loginValidator = Mock()
    TokenGenerateUseCase tokenGenerateUseCase = Mock()

    LoginService loginService

    def setup() {
        loginService = new LoginService(loginValidator, tokenGenerateUseCase)
    }

    def "사용자가 유효한 자격 증명으로 성공적으로 로그인해야 한다"() {
        given: "유효한 로그인 요청"
        def request = new LoginRequest("testuser", "password123")
        def user = User.builder().username("testuser").email("test@test.com").password("encryptedPassword").build()
        def token = "jwt.token.here"

        // Mock LoginValidator to return the user
        loginValidator.validateAndGetUser(request) >> user

        // Mock TokenGenerateUseCase to return the token, expecting the username from the user object
        tokenGenerateUseCase.generateToken(user.getUsername()) >> token

        when: "로그인 서비스를 호출하면"
        AuthResponse response = loginService.execute(request)

        then: "토큰과 사용자 정보가 포함된 응답이 반환되어야 한다"
        response.token() == token
        response.username() == user.getUsername()
        response.email() == user.getEmail()

        // Verify interactions
        1 * loginValidator.validateAndGetUser(request)
        1 * tokenGenerateUseCase.generateToken(user.getUsername())
    }
}

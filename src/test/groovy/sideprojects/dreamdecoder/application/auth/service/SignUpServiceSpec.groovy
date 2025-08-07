package sideprojects.dreamdecoder.application.auth.service


import spock.lang.Specification
import sideprojects.dreamdecoder.domain.auth.persistence.User
import sideprojects.dreamdecoder.domain.auth.persistence.UserRepository
import sideprojects.dreamdecoder.application.auth.service.PasswordEncryptService
import sideprojects.dreamdecoder.domain.auth.util.exception.AuthException
import sideprojects.dreamdecoder.domain.auth.util.response.AuthErrorCode

class SignUpServiceSpec extends Specification {

    // Mock 객체 선언
    UserRepository userRepository = Mock()
    PasswordEncryptService passwordEncryptService = Mock()

    // 테스트 대상 객체
    SignUpService signUpService

    def setup() {
        signUpService = new SignUpService(userRepository, passwordEncryptService)
    }

    def "새로운 사용자가 성공적으로 회원가입되어야 한다"() {
        given: "회원가입 요청 정보와 암호화된 비밀번호"
        String username = "testuser"
        String email = "test@example.com"
        String rawPassword = "password123"
        String encryptedPassword = "encryptedPassword123"

        // Mocking: 사용자 이름으로 기존 사용자 조회 시 null 반환
        userRepository.findByUsername(username) >> Optional.empty()
        // Mocking: 이메일로 기존 사용자 조회 시 null 반환
        userRepository.findByEmail(email) >> Optional.empty()
        // Mocking: 비밀번호 암호화 서비스 호출 시 암호화된 비밀번호 반환
        passwordEncryptService.encrypt(rawPassword) >> encryptedPassword
        // Mocking: 사용자 저장 시 저장된 사용자 객체 반환
        userRepository.save(_) >> { User user -> user.builder().id(1L).build() }

        when: "회원가입 서비스를 호출하면"
        User registeredUser = signUpService.signUp(username, email, rawPassword)

        then: "사용자가 성공적으로 등록되고 ID가 할당되어야 한다"
        registeredUser != null
        registeredUser.id == 1L
        registeredUser.username == username
        registeredUser.email == email
        registeredUser.password == encryptedPassword

        // Mocking 검증: 각 메서드가 예상대로 호출되었는지 확인
        1 * userRepository.findByUsername(username)
        1 * userRepository.findByEmail(email)
        1 * passwordEncryptService.encrypt(rawPassword)
        1 * userRepository.save(_)
    }

    def "이미 존재하는 사용자 이름으로 회원가입 시 예외가 발생해야 한다"() {
        given: "이미 존재하는 사용자 이름"
        String username = "existinguser"
        String email = "new@example.com"
        String rawPassword = "password123"

        // Mocking: 사용자 이름으로 기존 사용자 조회 시 Optional.of(User) 반환
        userRepository.findByUsername(username) >> Optional.of(User.builder().username(username).build())

        when: "회원가입 서비스를 호출하면"
        signUpService.signUp(username, email, rawPassword)

        then: "AuthException이 발생하고 에러 코드가 USERNAME_ALREADY_EXISTS여야 한다"
        AuthException e = thrown()
        e.errorCode == AuthErrorCode.USERNAME_ALREADY_EXISTS

        // Mocking 검증: findByUsername만 호출되고 다른 메서드는 호출되지 않음
        1 * userRepository.findByUsername(username)
        0 * userRepository.findByEmail(_)
        0 * passwordEncryptService.encrypt(_)
        0 * userRepository.save(_)
    }

    def "이미 존재하는 이메일로 회원가입 시 예외가 발생해야 한다"() {
        given: "이미 존재하는 이메일"
        String username = "newuser"
        String email = "existing@example.com"
        String rawPassword = "password123"

        // Mocking: 사용자 이름은 존재하지 않고, 이메일은 존재함
        userRepository.findByUsername(username) >> Optional.empty()
        userRepository.findByEmail(email) >> Optional.of(User.builder().email(email).build())

        when: "회원가입 서비스를 호출하면"
        signUpService.signUp(username, email, rawPassword)

        then: "AuthException이 발생하고 에러 코드가 EMAIL_ALREADY_EXISTS여야 한다"
        AuthException e = thrown()
        e.errorCode == AuthErrorCode.EMAIL_ALREADY_EXISTS

        // Mocking 검증
        1 * userRepository.findByUsername(username)
        1 * userRepository.findByEmail(email)
        0 * passwordEncryptService.encrypt(_)
        0 * userRepository.save(_)
    }
}

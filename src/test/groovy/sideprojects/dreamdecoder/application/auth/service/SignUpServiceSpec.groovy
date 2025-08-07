package sideprojects.dreamdecoder.application.auth.service

import spock.lang.Specification
import sideprojects.dreamdecoder.application.auth.usecase.PasswordEncryptUseCase
import sideprojects.dreamdecoder.application.auth.validator.SignUpValidator
import sideprojects.dreamdecoder.domain.auth.model.UserModel
import sideprojects.dreamdecoder.domain.auth.persistence.User
import sideprojects.dreamdecoder.domain.auth.persistence.UserRepository
import sideprojects.dreamdecoder.domain.auth.util.mapper.UserMapper
import sideprojects.dreamdecoder.presentation.auth.dto.AuthResponse
import sideprojects.dreamdecoder.presentation.auth.dto.SignUpRequest

class SignUpServiceSpec extends Specification {

    UserRepository userRepository = Mock()
    SignUpValidator signUpValidator = Mock()
    PasswordEncryptUseCase passwordEncryptUseCase = Mock()
    UserMapper userMapper = Mock()

    SignUpService signUpService

    def setup() {
        signUpService = new SignUpService(userRepository, signUpValidator, passwordEncryptUseCase, userMapper)
    }

    def "새로운 사용자가 성공적으로 회원가입되어야 한다"() {
        given: "새로운 사용자 회원가입 요청"
        def request = new SignUpRequest("newuser", "new@example.com", "password123", "password123")
        def encryptedPassword = "encryptedPassword"
        def userEntity = User.builder().username(request.getUsername()).email(request.getEmail()).password(encryptedPassword).build()
        def savedUser = User.builder().id(1L).username(request.getUsername()).email(request.getEmail()).password(encryptedPassword).build()

        passwordEncryptUseCase.encrypt(request.getPassword()) >> encryptedPassword
        // Mock userMapper.toEntity to accept any UserModel and return userEntity
        userMapper.toEntity(_ as UserModel) >> userEntity
        // Mock userRepository.save to accept any User and return savedUser
        userRepository.save(_ as User) >> savedUser

        when: "회원가입 서비스를 호출하면"
        AuthResponse response = signUpService.execute(request)

        then: "사용자 정보가 포함된 응답이 반환되어야 한다"
        1 * signUpValidator.validate(request)
        1 * passwordEncryptUseCase.encrypt(request.getPassword())
        // Verify userMapper.toEntity was called with a UserModel
        1 * userMapper.toEntity(_ as UserModel)
        // Verify userRepository.save was called with a User
        1 * userRepository.save(_ as User)

        response.username() == savedUser.getUsername()
        response.email() == savedUser.getEmail()
    }
}

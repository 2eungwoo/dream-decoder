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
        def request = new SignUpRequest("newuser", "new@example.com", "password123")
        def encryptedPassword = "encryptedPassword"
        def userModel = UserModel.of(request.getUsername(), request.getEmail(), encryptedPassword)
        def userEntity = User.builder().username(request.getUsername()).email(request.getEmail()).password(encryptedPassword).build()
        def savedUser = User.builder().id(1L).username(request.getUsername()).email(request.getEmail()).password(encryptedPassword).build()

        passwordEncryptUseCase.encrypt(request.getPassword()) >> encryptedPassword
        userMapper.toEntity(userModel) >> userEntity
        userRepository.save(userEntity) >> savedUser

        when: "회원가입 서비스를 호출하면"
        AuthResponse response = signUpService.execute(request)

        then: "사용자 정보가 포함된 응답이 반환되어야 한다"
        1 * signUpValidator.validate(request)
        1 * passwordEncryptUseCase.encrypt(request.getPassword())
        1 * userMapper.toEntity(_ as UserModel)
        1 * userRepository.save(userEntity)

        response.username() == savedUser.getUsername()
        response.email() == savedUser.getEmail()
    }
}
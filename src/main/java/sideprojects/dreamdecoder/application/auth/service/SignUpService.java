package sideprojects.dreamdecoder.application.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sideprojects.dreamdecoder.domain.auth.model.UserModel;
import sideprojects.dreamdecoder.domain.auth.util.mapper.UserMapper;
import sideprojects.dreamdecoder.presentation.auth.dto.AuthResponse;
import sideprojects.dreamdecoder.domain.auth.persistence.User;
import sideprojects.dreamdecoder.domain.auth.persistence.UserRepository;
import sideprojects.dreamdecoder.application.auth.usecase.PasswordEncryptUseCase;
import sideprojects.dreamdecoder.application.auth.usecase.SignUpUseCase;
import sideprojects.dreamdecoder.application.auth.validator.SignUpValidator;
import sideprojects.dreamdecoder.presentation.auth.dto.SignUpRequest;

@Service
@RequiredArgsConstructor
public class SignUpService implements SignUpUseCase {

    private final UserRepository userRepository;
    private final SignUpValidator signUpValidator;
    private final PasswordEncryptUseCase passwordEncryptUseCase;
    private final UserMapper userMapper;

    @Override
    public AuthResponse execute(SignUpRequest request) {
        signUpValidator.validate(request);

        String encryptedPassword = passwordEncryptUseCase.encrypt(request.getPassword());

        UserModel model = UserModel.of(request.getUsername(), request.getEmail(), encryptedPassword);
        User saved = userRepository.save(userMapper.toEntity(model));

        return AuthResponse.builder()
            .username(saved.getUsername())
            .email(saved.getEmail())
            .build();
    }
}



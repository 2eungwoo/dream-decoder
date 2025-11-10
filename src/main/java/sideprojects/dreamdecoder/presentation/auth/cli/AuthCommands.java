package sideprojects.dreamdecoder.presentation.auth.cli;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.component.flow.ComponentFlow;
import org.springframework.shell.component.flow.ComponentFlow.ComponentFlowResult;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import sideprojects.dreamdecoder.application.auth.cli.service.CliAuthService;
import sideprojects.dreamdecoder.global.shared.exception.CustomException; // Catch CustomException for service errors

@ShellComponent
@RequiredArgsConstructor
public class AuthCommands {

    private final CliAuthService cliAuthService;
    private final ComponentFlow.Builder componentFlowBuilder;

    @ShellMethod(key = "signup", value = "새로운 계정을 등록합니다.")
    public String signUp(
            @ShellOption(help = "사용자 이름", defaultValue = ShellOption.NULL) String usernameArg,
            @ShellOption(help = "이메일 주소", defaultValue = ShellOption.NULL) String emailArg,
            @ShellOption(help = "비밀번호", defaultValue = ShellOption.NULL) String passwordArg,
            @ShellOption(help = "비밀번호 확인", defaultValue = ShellOption.NULL) String confirmPasswordArg
    ) {
        String username = usernameArg;
        String email = emailArg;
        String password = passwordArg;
        String confirmPassword = confirmPasswordArg;

        // Check if any argument is missing, if so, enter interactive mode
        if (username == null || email == null || password == null || confirmPassword == null) {
            // User Info Flow
            ComponentFlow userInfoFlow = componentFlowBuilder.clone().reset()
                    .withStringInput("username")
                        .name("사용자 이름을 입력하세요:")
                        .and()
                    .withStringInput("email")
                        .name("이메일 주소를 입력하세요:")
                        .and()
                    .build();
            
            ComponentFlowResult userInfoResult;
            try {
                userInfoResult = userInfoFlow.run();
            } catch (Exception e) {
                return "회원가입이 취소되었습니다.";
            }

            if (!userInfoResult.getContext().containsKey("username") || !userInfoResult.getContext().containsKey("email")) {
                return "회원가입이 취소되었습니다.";
            }
            username = userInfoResult.getContext().get("username");
            email = userInfoResult.getContext().get("email");

            // Password Flow (always interactive if user info was interactive)
            while (true) {
                ComponentFlow passwordFlow = componentFlowBuilder.clone().reset()
                        .withStringInput("password")
                            .name("비밀번호를 입력하세요:")
                            .maskCharacter('*')
                            .and()
                        .withStringInput("confirmPassword")
                            .name("비밀번호를 다시 입력하세요:")
                            .maskCharacter('*')
                            .and()
                        .build();
                
                ComponentFlowResult passwordResult;
                try {
                    passwordResult = passwordFlow.run();
                } catch (Exception e) {
                    return "회원가입이 취소되었습니다.";
                }

                if (!passwordResult.getContext().containsKey("password") || !passwordResult.getContext().containsKey("confirmPassword")) {
                    return "회원가입이 취소되었습니다.";
                }
                password = passwordResult.getContext().get("password");
                confirmPassword = passwordResult.getContext().get("confirmPassword");

                try {
                    cliAuthService.signUp(username, email, password, confirmPassword);
                    break; // 성공 시 루프 탈출
                } catch (CustomException e) { // Catch CustomException for specific business errors
                    System.out.println(e.getMessage() + " 다시 시도해주세요.");
                } catch (Exception e) { // Catch other unexpected exceptions
                    return "회원가입 실패: " + e.getMessage();
                }
            }
        } else {
            // Non-interactive mode: arguments were provided
            try {
                cliAuthService.signUp(username, email, password, confirmPassword);
            } catch (CustomException e) {
                return "회원가입 실패: " + e.getMessage();
            } catch (Exception e) {
                return "회원가입 실패: " + e.getMessage();
            }
        }

        return "회원가입이 완료되었습니다. 'login' 명령어를 사용하여 로그인해주세요.";
    }

    @ShellMethod(key = "login", value = "계정에 로그인합니다.")
    public String login(
            @ShellOption(help = "사용자 이름", defaultValue = ShellOption.NULL) String usernameArg,
            @ShellOption(help = "비밀번호", defaultValue = ShellOption.NULL) String passwordArg
    ) {
        String username = usernameArg;
        String password = passwordArg;

        if (username == null || password == null) {
            // 대화형 모드
            ComponentFlow flow = componentFlowBuilder.clone().reset()
                    .withStringInput("username")
                        .name("사용자 이름을 입력하세요:")
                        .and()
                    .withStringInput("password")
                        .name("비밀번호를 입력하세요:")
                        .maskCharacter('*')
                        .and()
                    .build();

            ComponentFlowResult result;
            try {
                result = flow.run();
            } catch (Exception e) {
                return "로그인이 취소되었습니다.";
            }
            
            if (!result.getContext().containsKey("username") || !result.getContext().containsKey("password")) {
                return "로그인이 취소되었습니다.";
            }
            username = result.getContext().get("username");
            password = result.getContext().get("password");
        }

        try {
            cliAuthService.login(username, password);
            return "로그인에 성공했습니다. 이제 해몽 서비스를 이용할 수 있습니다.";
        } catch (CustomException e) { // Catch CustomException for specific business errors
            return "로그인 실패: " + e.getMessage();
        } catch (Exception e) { // Catch other unexpected exceptions
            return "로그인 실패: " + e.getMessage();
        }
    }

    @ShellMethod(key = "logout", value = "계정에서 로그아웃합니다.")
    public String logout() {
        cliAuthService.logout();
        return "로그아웃 되었습니다.";
    }
}

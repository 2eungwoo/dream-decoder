package sideprojects.dreamdecoder.presentation.auth.cli;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import sideprojects.dreamdecoder.application.auth.cli.service.CliAuthService;
import sideprojects.dreamdecoder.global.shared.exception.CustomException; // Catch CustomException for service errors

@ShellComponent
@RequiredArgsConstructor
public class AuthCommands {

    private final CliAuthService cliAuthService;

    @ShellMethod(key = "signup", value = "새로운 계정을 등록합니다.")
    public String signUp(
            @ShellOption(help = "사용자 이름") String username,
            @ShellOption(help = "이메일 주소") String email,
            @ShellOption(help = "비밀번호") String password,
            @ShellOption(help = "비밀번호 확인") String confirmPassword
    ) {
        try {
            cliAuthService.signUp(username, email, password, confirmPassword);
            return "회원가입이 완료되었습니다. 'login' 명령어를 사용하여 로그인해주세요.";
        } catch (CustomException e) { // Catch CustomException for specific business errors
            return "회원가입 실패: " + e.getMessage();
        } catch (Exception e) { // Catch other unexpected exceptions
            return "회원가입 실패: " + e.getMessage();
        }
    }

    @ShellMethod(key = "login", value = "계정에 로그인합니다.")
    public String login(
            @ShellOption(help = "사용자 이름") String username,
            @ShellOption(help = "비밀번호") String password
    ) {
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
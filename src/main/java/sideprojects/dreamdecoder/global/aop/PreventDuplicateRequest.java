package sideprojects.dreamdecoder.global.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 중복 요청 방지 기능을 적용할 메소드에 사용
 * SpEL(Spring Expression Language) 사용으로 요청 키의 일부로 사용될 파라미터 값을 지정할 수 있.
 * 
 * ex) @PreventDuplicateRequest(key = "#userId")
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PreventDuplicateRequest {
    String key();
}

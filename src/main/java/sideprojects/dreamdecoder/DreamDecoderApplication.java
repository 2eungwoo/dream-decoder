package sideprojects.dreamdecoder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.WebApplicationType;

@EnableAsync
@EnableJpaAuditing
@SpringBootApplication
public class DreamDecoderApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DreamDecoderApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

}

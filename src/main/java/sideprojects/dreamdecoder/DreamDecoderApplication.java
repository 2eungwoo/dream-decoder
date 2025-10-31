package sideprojects.dreamdecoder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableAsync
@EnableJpaAuditing
@SpringBootApplication
public class DreamDecoderApplication {

    public static void main(String[] args) {
        SpringApplication.run(DreamDecoderApplication.class, args);
    }

}

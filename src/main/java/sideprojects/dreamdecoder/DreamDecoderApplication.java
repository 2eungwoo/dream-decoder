package sideprojects.dreamdecoder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class DreamDecoderApplication {

    public static void main(String[] args) {
        SpringApplication.run(DreamDecoderApplication.class, args);
    }

}

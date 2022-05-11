package fr.dev.leaguacyapi;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile("test")
public class AppTest {

    public static void main(String[] args) {
        new SpringApplicationBuilder(AppTest.class).profiles("test").run(args);
    }
}

package kea.guessr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
//@EntityScan(basePackages = "com.example.guessr.model") // Make sure to include your entity package here

public class GuessrApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuessrApplication.class, args);
    }

}

package kea.guessr.Config;

import kea.guessr.Model.User;
import kea.guessr.Service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
/*
@Component
public class InitData implements CommandLineRunner {

    private final UserService userService;

    public InitData(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        userService.createUser(new User("John Doe", "johndoe@example.com", "Active", LocalDateTime.now()));
        userService.createUser(new User("Jane Smith", "janesmith@example.com", "Inactive", LocalDateTime.now()));
        userService.createUser(new User("Bob Johnson", "bobjohnson@example.com", "Active", LocalDateTime.now()));
    }
}*/
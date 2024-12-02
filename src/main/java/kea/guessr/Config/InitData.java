package kea.guessr.Config;

import kea.guessr.Model.User;
import kea.guessr.Service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class InitData implements CommandLineRunner {

    private final UserService userService;

    public InitData(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {

        userService.createUser(new User(0,"user","test","Bobby", "adm@adm.dk","Active", User.Role.USER, LocalDateTime.now()));
        userService.createUser(new User(0,"admin","test","Bobby", "adm@adm.dk","Active", User.Role.ADMIN, LocalDateTime.now()));

    }
}
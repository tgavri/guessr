package kea.guessr.Config;

import kea.guessr.Model.User;
import kea.guessr.Service.UserService;
import org.mindrot.jbcrypt.BCrypt;
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

        userService.createUser(new User(0,"user", BCrypt.hashpw("test",BCrypt.gensalt()),"Bobby", "adm@adm.dk","Active", User.Role.USER, LocalDateTime.now()));
        userService.createUser(new User(0, "admin", BCrypt.hashpw("test", BCrypt.gensalt()), "Bobby", "adm@adm.dk", "Active", User.Role.ADMIN, LocalDateTime.now()));

    }
}
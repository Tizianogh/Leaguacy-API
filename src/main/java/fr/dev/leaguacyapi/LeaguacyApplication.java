package fr.dev.leaguacyapi;

import fr.dev.leaguacyapi.domain.model.Role;
import fr.dev.leaguacyapi.domain.model.User;
import fr.dev.leaguacyapi.domain.service.interfaces.RoleService;
import fr.dev.leaguacyapi.domain.service.interfaces.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

import static fr.dev.leaguacyapi.domain.enums.Roles.*;

@SpringBootApplication
public class LeaguacyApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeaguacyApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner run(UserService userService, RoleService roleService) {
        return args -> {
            roleService.createRole(new Role(null, JOUEUR.getValue()));
            roleService.createRole(new Role(null, ADMIN.getValue()));
            roleService.createRole(new Role(null, ARBITRE.getValue()));

            userService.createUser(
                    new User(null, "Tiziano Ghisotti", "tiziano.ghisotti@gmail.com", "password", new ArrayList<>()));
            userService.createUser(new User(null, "Aida Oukrif", "aida.oukrif@gmail.com", "password", new ArrayList<>()));
            userService.createUser(new User(null, "Johan Laforge", "johan.laforge@gmail.com", "password", new ArrayList<>()));
            userService.createUser(new User(null, "Benjamin Krief", "benjamin.krief@gmail.com", "password", new ArrayList<>()));
            userService.createUser(new User(null, "Illiona Bernard", "illiona.bernard@gmail.com", "password", new ArrayList<>()));

            userService.addRoleToUser("tiziano.ghisotti@gmail.com", JOUEUR.toString());
            userService.addRoleToUser("aida.oukrif@gmail.com", ADMIN.toString());
            userService.addRoleToUser("johan.laforge@gmail.com", ARBITRE.toString());
            userService.addRoleToUser("benjamin.krief@gmail.com", JOUEUR.toString());
            userService.addRoleToUser("illiona.bernard@gmail.com", ADMIN.toString());
        };
    }
}

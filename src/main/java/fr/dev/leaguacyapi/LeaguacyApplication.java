package fr.dev.leaguacyapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class LeaguacyApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeaguacyApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

/*    @Bean
    CommandLineRunner run(PlayerService playerService, RoleService roleService) {
        return args -> {
            roleService.createRole(new Role(null, JOUEUR.getValue()));
            roleService.createRole(new Role(null, ADMIN.getValue()));
            roleService.createRole(new Role(null, ARBITRE.getValue()));

            playerService.createPlayer(
                    new Player(null, "Tiziano Ghisotti", "tiziano.ghisotti@gmail.com", "password", new ArrayList<>()));
            playerService.createPlayer(new Player(null, "Aida Oukrif", "aida.oukrif@gmail.com", "password", new ArrayList<>()));
            playerService.createPlayer(new Player(null, "Johan Laforge", "johan.laforge@gmail.com", "password", new ArrayList<>()));
            playerService.createPlayer(
                    new Player(null, "Benjamin Krief", "benjamin.krief@gmail.com", "password", new ArrayList<>()));
            playerService.createPlayer(
                    new Player(null, "Illiona Bernard", "illiona.bernard@gmail.com", "password", new ArrayList<>()));

            playerService.addRoleToPlayer("tiziano.ghisotti@gmail.com", JOUEUR.toString());
            playerService.addRoleToPlayer("aida.oukrif@gmail.com", ADMIN.toString());
            playerService.addRoleToPlayer("johan.laforge@gmail.com", ARBITRE.toString());
            playerService.addRoleToPlayer("illiona.bernard@gmail.com", ADMIN.toString());
            playerService.addRoleToPlayer("benjamin.krief@gmail.com", JOUEUR.toString());
        };
    }*/
}

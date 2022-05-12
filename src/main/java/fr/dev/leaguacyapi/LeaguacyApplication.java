package fr.dev.leaguacyapi;

import fr.dev.leaguacyapi.domain.model.Player;
import fr.dev.leaguacyapi.domain.model.Role;
import fr.dev.leaguacyapi.domain.service.interfaces.LeagueService;
import fr.dev.leaguacyapi.domain.service.interfaces.PlayerService;
import fr.dev.leaguacyapi.domain.service.interfaces.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static fr.dev.leaguacyapi.domain.enums.Roles.ADMIN;
import static fr.dev.leaguacyapi.domain.enums.Roles.JOUEUR;

@SpringBootApplication
public class LeaguacyApplication {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(LeaguacyApplication.class).profiles("dev").run(args);
    }
}

@Component
@Profile(value = "!test")
class Runner implements CommandLineRunner {
    private final LeagueService leagueService;
    private final PlayerService playerService;

    private final RoleService roleService;

    Runner(LeagueService leagueService, PlayerService playerService, RoleService roleService) {
        this.leagueService = leagueService;
        this.playerService = playerService;
        this.roleService = roleService;
    }

    @Override
    public void run(String... args) throws Exception {
        roleService.createRole(new Role(null, JOUEUR.getValue()));
        roleService.createRole(new Role(null, ADMIN.getValue()));

        playerService.createPlayer(
                new Player(null, "Tiziano Ghisotti", "tiziano.ghisotti@gmail.com", "password", new ArrayList<>(), null));
        playerService.createPlayer(new Player(null, "Aida Oukrif", "aida.oukrif@gmail.com", "password", new ArrayList<>(),null));
        playerService.createPlayer(
                new Player(null, "Johan Laforge", "johan.laforge@gmail.com", "password", new ArrayList<>(),null));
        playerService.createPlayer(
                new Player(null, "Benjamin Krief", "benjamin.krief@gmail.com", "password", new ArrayList<>(),null));
        playerService.createPlayer(
                new Player(null, "Illiona Bernard", "illiona.bernard@gmail.com", "password", new ArrayList<>(),null));

        playerService.addRoleToPlayer("tiziano.ghisotti@gmail.com", JOUEUR.toString());
        playerService.addRoleToPlayer("aida.oukrif@gmail.com", JOUEUR.toString());
        playerService.addRoleToPlayer("johan.laforge@gmail.com", JOUEUR.toString());
        playerService.addRoleToPlayer("illiona.bernard@gmail.com", JOUEUR.toString());
        playerService.addRoleToPlayer("benjamin.krief@gmail.com", JOUEUR.toString());

    }
}

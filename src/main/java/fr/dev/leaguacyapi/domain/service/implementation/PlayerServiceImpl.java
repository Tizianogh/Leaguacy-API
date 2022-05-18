package fr.dev.leaguacyapi.domain.service.implementation;

import fr.dev.leaguacyapi.domain.model.Player;
import fr.dev.leaguacyapi.domain.model.Role;
import fr.dev.leaguacyapi.domain.repository.PlayerRepository;
import fr.dev.leaguacyapi.domain.service.interfaces.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PlayerServiceImpl implements PlayerService, UserDetailsService {
    private final PlayerRepository userRepository;
    private final RoleServiceImpl roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<Player> userByName = this.getPlayerByName(userName);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        userByName.get().getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        });

        return new org.springframework.security.core.userdetails.User(userByName.get().getUsername(),
                userByName.get().getPassword(), authorities);
    }

    @Override
    public Optional<Player> createPlayer(Player player) {
        Optional<Player> userByName = this.getPlayerByName(player.getName());

        userByName.ifPresentOrElse(retrieveUser -> {
            log.info("[{}] - Un utilisateur avec pour nom '{}', existe déjà en base de données.", new Date(),
                    retrieveUser.getUsername());
        }, () -> {
            player.setPassword(passwordEncoder.encode(player.getPassword()));
            this.userRepository.save(player);
            log.info("[{}] - L'utilisateur '{}', '{}' a été créé.", new Date(), player.getUuidPlayer(), player.getName());
        });

        return userByName;
    }

    @Override
    public Optional<Map<Player, Role>> addRoleToPlayer(String userName, String roleName) {
        Optional<Player> userByName = this.getPlayerByName(userName);
        Optional<Role> roleByName = this.roleService.getRoleByRoleName(roleName);

        Map<Player, Role> retrieveUserAndRole = new HashMap<>();
        retrieveUserAndRole.put(userByName.get(), roleByName.get());

        userByName.get().getRoles().add(roleByName.get());

        return Optional.ofNullable(retrieveUserAndRole);
    }

    @Override
    public List<Player> getPlayers() {
        List<Player> players = this.userRepository.findAll();

        if (players.isEmpty())
            log.info("[{}] - Aucun utilisateur en base de données.", new Date());

        return players;
    }

    @Override
    public Optional<Player> getPlayerByUUID(UUID uuidUser) {
        Optional<Player> userByUuidUser = Optional.ofNullable(this.userRepository.findPlayerByUuidPlayer(uuidUser));

        userByUuidUser.ifPresentOrElse(user -> {
            log.info("[{}] - L'utilisateur '{}', '{}' a été trouvé en base de données.", new Date(), user.getUuidPlayer(),
                    user.getName());
        }, () -> {
            log.info("[{}] - L'utilisateur n'a pas été trouvé en base de données.", new Date(), uuidUser);
        });

        return userByUuidUser;
    }

    @Override
    public Optional<Player> getPlayerByName(String userName) {
        Optional<Player> userByUsername = Optional.ofNullable(this.userRepository.findPlayerByUsername(userName));

        userByUsername.ifPresentOrElse(user -> {
            log.info("[{}] - L'utilisateur '{}', '{}' a été trouvé en base de données.", new Date(), user.getUuidPlayer(),
                    user.getUsername());
        }, () -> {
            log.info("[{}] - L'utilisateur '{}', n'a pas été trouvé en base de données.", new Date(), userName);
        });

        return userByUsername;
    }

    @Override
    public Optional<Player> getPlayerByUsernameAndPassword(String username, String password) {
        Optional<Player> playerByUsernameAndPassword = Optional.ofNullable(
                this.userRepository.findPlayerByUsernameAndPassword(username, password));

        playerByUsernameAndPassword.ifPresentOrElse(user -> {
            log.info("[{}] - L'utilisateur '{}', '{}' a été trouvé en base de données.", new Date(), user.getUuidPlayer(),
                    user.getUsername());
        }, () -> {
            log.info("[{}] - L'utilisateur '{}', n'a pas été trouvé en base de données.", new Date(), username);
        });

        return playerByUsernameAndPassword;
    }

    @Override
    public Optional<String> getPasswordEncodedByUsername(String username) {
        Optional<String> passwordByUsername = Optional.ofNullable(this.userRepository.findPasswordByUsername(username));

        passwordByUsername.ifPresentOrElse(user -> {
            log.info("[{}] - Mot de passe trouvé.", new Date());
        }, () -> {
            log.info("[{}] - Mot de passe non trouvé.", new Date());
        });

        return passwordByUsername;
    }

}

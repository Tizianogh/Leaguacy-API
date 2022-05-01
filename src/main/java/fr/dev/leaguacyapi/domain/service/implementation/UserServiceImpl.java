package fr.dev.leaguacyapi.domain.service.implementation;

import fr.dev.leaguacyapi.domain.model.Role;
import fr.dev.leaguacyapi.domain.model.User;
import fr.dev.leaguacyapi.domain.repository.UserRepository;
import fr.dev.leaguacyapi.domain.service.interfaces.UserService;
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
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleServiceImpl roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> userByName = this.getUserByName(userName);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        userByName.get().getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        });

        return new org.springframework.security.core.userdetails.User(userByName.get().getUsername(),
                userByName.get().getPassword(), authorities);
    }

    @Override
    public Optional<User> createUser(User user) {
        Optional<User> userByName = this.getUserByName(user.getName());

        userByName.ifPresentOrElse(retrieveUser -> {
            log.info("[{}] - Un utilisateur avec pour nom '{}', existe déjà en base de données.", new Date(),
                    retrieveUser.getUsername());
        }, () -> {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            this.userRepository.save(user);
            log.info("[{}] - L'utilisateur '{}', '{}' a été créé.", new Date(), user.getUuidUser(), user.getName());
        });

        return userByName;
    }

    @Override
    public Optional<Map<User, Role>> addRoleToUser(String userName, String roleName) {
        Optional<User> userByName = this.getUserByName(userName);
        Optional<Role> roleByName = this.roleService.getRoleByRoleName(roleName);

        Map<User, Role> retrieveUserAndRole = new HashMap<>();
        retrieveUserAndRole.put(userByName.get(), roleByName.get());

        userByName.get().getRoles().add(roleByName.get());

        return Optional.ofNullable(retrieveUserAndRole);
    }

    @Override
    public List<User> getUsers() {
        List<User> users = this.userRepository.findAll();

        if (users.isEmpty())
            log.info("[{}] - Aucun utilisateur en base de données.", new Date());

        return users;
    }

    @Override
    public Optional<User> getUserByName(String userName) {
        Optional<User> userByUsername = Optional.ofNullable(this.userRepository.findUserByUsername(userName));

        userByUsername.ifPresentOrElse(user -> {
            log.info("[{}] - L'utilisateur '{}', '{}' a été trouvé en base de données.", new Date(), user.getUuidUser(),
                    user.getUsername());
        }, () -> {
            log.info("[{}] - L'utilisateur '{}', n'a pas été trouvé en base de données.", new Date(), userName);
        });

        return userByUsername;
    }
}

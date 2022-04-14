package fr.dev.leaguacyapi.domain.service.implementation;

import fr.dev.leaguacyapi.domain.model.Role;
import fr.dev.leaguacyapi.domain.repository.RoleRepository;
import fr.dev.leaguacyapi.domain.service.interfaces.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Optional<Role> createRole(Role role) {
        Optional<Role> roleByRoleName = this.getRoleByRoleName(role.getRoleName());

        roleByRoleName.ifPresentOrElse(retrieveRole -> {
            log.info("[{}] - Un rôle avec pour nom '{}', existe déjà en base de données.", new Date(),
                    retrieveRole.getRoleName());
        }, () -> {
            this.roleRepository.save(role);
            log.info("[{}] - Un rôle '{}', '{}' a été créé.", new Date(), role.getRoleName(), role.getRoleName());
        });

        return Optional.empty();
    }

    @Override
    public Optional<Role> getRoleByRoleName(String roleName) {
        Optional<Role> roleByName = Optional.ofNullable(this.roleRepository.findRoleByRoleName(roleName));

        roleByName.ifPresentOrElse(role -> {
            log.info("[{}] - Le rôle '{}', '{}' a été trouvé en base de données.", new Date(), role.getUuidRole(),
                    role.getRoleName());
        }, () -> {
            log.info("[{}] - Le rôle '{}', n'a pas été trouvé en base de données.", new Date(), roleName);
        });

        return roleByName;
    }
}


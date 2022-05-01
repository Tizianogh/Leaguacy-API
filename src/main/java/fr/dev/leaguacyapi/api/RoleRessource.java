package fr.dev.leaguacyapi.api;

import fr.dev.leaguacyapi.domain.model.Response;
import fr.dev.leaguacyapi.domain.model.Role;
import fr.dev.leaguacyapi.domain.service.interfaces.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class RoleRessource {
    private final RoleService roleService;

    @PostMapping("/role/new")
    public ResponseEntity<Response> newRole(@RequestBody @Valid Role role) throws IOException {
        if (roleService.createRole(role).isPresent()) {
            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(now())
                            .message(String.format("[%s] - Un rôle avec pour nom '%s', existe déjà en base de données.",
                                    new Date(),
                                    role.getRoleName()))
                            .status(BAD_REQUEST)
                            .statusCode(BAD_REQUEST.value())
                            .build()
            );
        }

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("result", roleService.createRole(role)))
                        .message(String.format("[%s] - Le rôle '%s', '%s' a été créé.", new Date(), role.getUuidRole(),
                                role.getRoleName()))
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }
}

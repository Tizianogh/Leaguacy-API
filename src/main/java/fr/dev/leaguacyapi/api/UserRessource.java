package fr.dev.leaguacyapi.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.dev.leaguacyapi.domain.model.Response;
import fr.dev.leaguacyapi.domain.model.Role;
import fr.dev.leaguacyapi.domain.model.User;
import fr.dev.leaguacyapi.domain.service.interfaces.RoleService;
import fr.dev.leaguacyapi.domain.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

import static fr.dev.leaguacyapi.privates.Secrets.SECRET;
import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class UserRessource {
    private final UserService userService;
    private final RoleService roleService;

    @GetMapping("/users")
    public ResponseEntity<Response> getUsers() {
        List<User> users = this.userService.getUsers();

        if (users.isEmpty()) {
            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(now())
                            .message(String.format("[%s] - Aucun utilisateur en base de données.", new Date()))
                            .status(NOT_FOUND)
                            .statusCode(NOT_FOUND.value())
                            .build()
            );
        }

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("results", this.userService.getUsers()))
                        .message(String.format("[%s] - '%s' utilisateur(s) ont été trouvée(s).", new Date(), users.size()))
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Response> newUser(@RequestBody @Valid User user) throws IOException {
        if (userService.createUser(user).isPresent()) {
            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(now())
                            .message(String.format("[%s] - Un utilisateur avec pour nom '%s', existe déjà en base de données.",
                                    new Date(),
                                    user.getUsername()))
                            .status(BAD_REQUEST)
                            .statusCode(BAD_REQUEST.value())
                            .build()
            );
        }

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(Map.of("result", userService.createUser(user)))
                        .message(String.format("[%s] - L'utilisateur '%s', '%s' a été créé.", new Date(), user.getUuidUser(),
                                user.getUuidUser()))
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }

    public ResponseEntity<Response> addRoleToUser(@RequestBody @Valid User user, Role role) throws IOException {
        if (userService.getUserByName(user.getName()).isPresent() && roleService.getRoleByRoleName(role.getRoleName())
                .isPresent()) {
            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(now())
                            .data(Map.of("result", userService.addRoleToUser(user.getName(), role.getRoleName())))
                            .message(String.format("[%s] - Le rôle '%s', a été attribué a '%s'.", new Date(), role.getRoleName(),
                                    user.getUsername()))
                            .status(CREATED)
                            .statusCode(CREATED.value())
                            .build()
            );

        } else if (!userService.getUserByName(user.getName()).isPresent()) {
            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(now())
                            .message(String.format("[%s] - Aucun utilisateur avec pour nom '%s', existe en base de données.",
                                    new Date(),
                                    user.getUsername()))
                            .status(BAD_REQUEST)
                            .statusCode(BAD_REQUEST.value())
                            .build());
        }

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .message(String.format("[%s] - Le rôle '%s', n'existe pas en base de données.",
                                new Date(),
                                role.getRoleName()))
                        .status(BAD_REQUEST)
                        .statusCode(BAD_REQUEST.value())
                        .build());
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256(SECRET.getValue().getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String userName = decodedJWT.getSubject();
                Optional<User> userByName = userService.getUserByName(userName);

                String accessToken = JWT.create().withSubject(userName)
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 600 * 1000))
                        .withIssuer(request.getRequestURI().toString())
                        .withClaim("roles", userByName.get().getRoles()
                                .stream()
                                .map(Role::getRoleName)
                                .collect((toList())))
                        .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", accessToken);
                tokens.put("refresh_token", refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception) {
                response.setHeader("erreur", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Le refresh token est manquant.");
        }
    }
}

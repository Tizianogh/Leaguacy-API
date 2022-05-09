package fr.dev.leaguacyapi.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Roles {
    ADMIN("ADMIN"),
    JOUEUR("JOUEUR"),
    ARBITRE("ARBITRE");

    private final String value;
};
